/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

import com.google.gerrit.extensions.restapi.ResourceNotFoundException;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.Patch.Key;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.change.ChangeResource;
import com.google.gerrit.server.change.ChangesCollection;
import com.google.gerrit.server.git.GitRepositoryManager;
import com.google.gwtorm.server.OrmException;
import com.google.inject.Inject;

import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectStream;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * A file attachment service that stores file attachments in the git repository
 * of the corresponding patch. The file attachments are stored per patch set in
 * commits, and the last commit is referenced by the ref
 * "ref/attachments/[changeId]/[patchsetId]". The mapping of a file to a
 * particular patch is determined by an index file at the root of the revision
 * tree, the attached files are stored in the subtree "files".
 *
 * @author Florian Zoubek
 *
 */
public class GitFileAttachmentService implements FileAttachmentService {

  private final GitRepositoryManager gitRepoManager;
  private final ChangesCollection changes;
  private final Logger log;

  private static final String BASE_REF = "refs/attachments";
  private static final String FILE_PATH_PREFIX = "files/";
  private static final String INDEX_FILE_PATH = "index";



  /**
   *
   * @param gitRepoManager a repository manager instance used to obtain access
   *        to the git repository
   * @param changes a collection of changes used to determine the project and
   *        repository name
   * @param logger a logger instance to log to
   */
  @Inject
  public GitFileAttachmentService(final GitRepositoryManager gitRepoManager,
      final ChangesCollection changes, final Logger logger) {
    this.gitRepoManager = gitRepoManager;
    this.changes = changes;
    this.log = logger;
  }

  @Override
  public void attachFile(String filePath, Key patchKey, String data,
      IdentifiedUser user) throws FileAttachmentException {

    String repoFilePath = toRepoFilePath(filePath);
    PatchSet.Id patchSetId = patchKey.getParentKey();
    Change.Id changeId = patchSetId.getParentKey();
    ChangeResource changeResource = null;

    FileIndex fileIndex = new FileIndex();

    try {
      changeResource = changes.parse(changeId);
      Project.NameKey projectName = changeResource.getChange().getProject();
      Repository repository = null;
      ObjectInserter inserter = null;

      try {
        repository = gitRepoManager.openRepository(projectName);
        inserter = repository.newObjectInserter();

        String refName = toRefName(changeId, patchSetId);

        Ref attachmentref = repository.getRef(refName);

        // We're working on a bare respository here, so create a temporary index
        // to build the tree for the commit afterwards
        DirCache index = DirCache.newInCore();
        DirCacheBuilder dirCacheBuilder = index.builder();

        if (attachmentref != null) {
          // The ref already exists so we have to copy the previous RevTree to
          // keep all already attached files

          log.fine("Ref exists - copying tree to maintain old attachments");

          RevWalk revWalk = new RevWalk(repository);
          RevCommit prevCommit =
              revWalk.parseCommit(attachmentref.getObjectId());
          RevTree tree = prevCommit.getTree();

          List<String> ignoredFiles = new ArrayList<>();
          // add file path of the new file to the ignored file paths, as we
          // don't want any already existing old file in our new tree
          ignoredFiles.add(repoFilePath);
          buildDirCacheFromTree(tree, repository, dirCacheBuilder,
              ignoredFiles, fileIndex);

          log.fine("finished copying tree");
        }

        // insert file as object
        byte[] content = (data).getBytes(StandardCharsets.UTF_8);
        long length = content.length;
        InputStream inputStream = new ByteArrayInputStream(content);
        ObjectId objectId =
            inserter.insert(Constants.OBJ_BLOB, length, inputStream);
        inputStream.close();

        // create tree entry
        DirCacheEntry entry = new DirCacheEntry(repoFilePath);
        entry.setFileMode(FileMode.REGULAR_FILE);
        entry.setLastModified(System.currentTimeMillis());
        entry.setLength(length);
        entry.setObjectId(objectId);
        dirCacheBuilder.add(entry);

        fileIndex.addEntry(patchKey.get(), repoFilePath);

        // insert index file to associate files to patches
        writeFileIndex(fileIndex, inserter, dirCacheBuilder);

        dirCacheBuilder.finish();

        // write new tree in database
        ObjectId indexTreeId = index.writeTree(inserter);

        // create commit
        CommitBuilder commitBuilder = new CommitBuilder();
        PersonIdent personIdent =
            user.newCommitterIdent(new Date(), TimeZone.getDefault());
        commitBuilder.setCommitter(personIdent);
        commitBuilder.setAuthor(personIdent);
        commitBuilder
        .setMessage(MessageFormat
            .format(
                "Attached file \"{0}\" to patch \"{1}\" from patch set {2}, change {3}",
                repoFilePath, patchKey.get(), patchSetId.get(),
                changeId.get()));

        if (attachmentref != null) {
          commitBuilder.setParentId(attachmentref.getObjectId());
        }
        commitBuilder.setTreeId(indexTreeId);

        // commit
        ObjectId commitId = inserter.insert(commitBuilder);
        inserter.flush();

        RefUpdate refUpdate = repository.updateRef(refName);
        refUpdate.setNewObjectId(commitId);
        if (attachmentref != null)
          refUpdate.setExpectedOldObjectId(attachmentref.getObjectId());
        else
          refUpdate.setExpectedOldObjectId(ObjectId.zeroId());

        // TODO
        // the result handling below is copied from the CommitCommand class, I
        // don't know if this is really necessary in our case
        Result result = refUpdate.forceUpdate();
        switch (result) {
          case NEW:
          case FORCED:
          case FAST_FORWARD: {
            if (repository.getRepositoryState() == RepositoryState.MERGING_RESOLVED) {
              // Commit was successful. Now delete the files
              // used for merge commits
              repository.writeMergeCommitMsg(null);
              repository.writeMergeHeads(null);
            } else if (repository.getRepositoryState() == RepositoryState.CHERRY_PICKING_RESOLVED) {
              repository.writeMergeCommitMsg(null);
              repository.writeCherryPickHead(null);
            } else if (repository.getRepositoryState() == RepositoryState.REVERTING_RESOLVED) {
              repository.writeMergeCommitMsg(null);
              repository.writeRevertHead(null);
            }
            break;
          }
          case REJECTED:
          case LOCK_FAILURE:
            throw new FileAttachmentException(new ConcurrentRefUpdateException(
                "Could not lock ref " + refUpdate.getRef().getName(),
                refUpdate.getRef(), result));
          default:
            throw new FileAttachmentException(new JGitInternalException(
                MessageFormat.format(JGitText.get().updatingRefFailed,
                    refUpdate.getRef().getName(), commitId.toString(), result)));
        }

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        if (repository != null) repository.close();
        if (inserter != null) inserter.release();
      }

    } catch (ResourceNotFoundException | OrmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * builds a dir cache by copying entries from an existing tree, skipping files
   * if specified and loads the file index on demand.
   *
   * @param tree the tree to copy from
   * @param repository the repository to work upon
   * @param dirCacheBuilder the dir builder instance used to add entries to the
   *        dir cache
   * @param ignoredFilePaths a list of file paths to ignore during copying
   * @param fileIndex if this parameters is not null, the given file index
   *        instance is loaded if the tree contains an index file - please note
   *        that the resulting file index contains all file paths specified in
   *        the index file, regardless of the existence of the actual file in
   *        the tree or the presence in the ignoredFilePaths list
   * @throws MissingObjectException
   * @throws IncorrectObjectTypeException
   * @throws CorruptObjectException
   * @throws IOException
   */
  private void buildDirCacheFromTree(RevTree tree, Repository repository,
      DirCacheBuilder dirCacheBuilder, List<String> ignoredFilePaths,
      FileIndex fileIndex) throws MissingObjectException,
      IncorrectObjectTypeException, CorruptObjectException, IOException {
    // TODO improve exception handling

    TreeWalk treeWalk = new TreeWalk(repository);
    int treeId = treeWalk.addTree(tree);
    treeWalk.setRecursive(true);

    while (treeWalk.next()) {
      String path = treeWalk.getPathString();

      CanonicalTreeParser prevTreeParser =
          treeWalk.getTree(treeId, CanonicalTreeParser.class);
      if (path.equals(INDEX_FILE_PATH) && fileIndex != null) {

        log.fine("found index file, loading index");

        // parse existing index file
        loadFileIndexFromGitObject(prevTreeParser.getEntryObjectId(),
            fileIndex, repository);

        log.fine("index loaded");

      } else if (prevTreeParser != null && !ignoredFilePaths.contains(path)) {
        // create a new DirCacheEntry with data from the previous commit

        final DirCacheEntry dcEntry = new DirCacheEntry(path);
        dcEntry.setObjectId(prevTreeParser.getEntryObjectId());
        dcEntry.setFileMode(prevTreeParser.getEntryFileMode());
        dirCacheBuilder.add(dcEntry);
      }
    }
  }

  /**
   * writes the given file index to a new object in the git repository and adds
   * a corresponding entry to the dir cache
   *
   * @param fileIndex the file index to write
   * @param inserter the object inserter used to write to the object database of
   *        the repository
   * @param dirCacheBuilder the dir cache builder used to add the entry to the
   *        dir cache
   * @throws IOException
   */
  private void writeFileIndex(FileIndex fileIndex, ObjectInserter inserter,
      DirCacheBuilder dirCacheBuilder) throws IOException {
    // TODO improve exception handling
    byte[] indexContent = fileIndex.toString().getBytes(StandardCharsets.UTF_8);
    long indexLength = indexContent.length;
    InputStream indexInputStream = new ByteArrayInputStream(indexContent);
    ObjectId indexObjectId =
        inserter.insert(Constants.OBJ_BLOB, indexLength, indexInputStream);
    indexInputStream.close();

    DirCacheEntry indexEntry = new DirCacheEntry(INDEX_FILE_PATH);
    indexEntry.setFileMode(FileMode.REGULAR_FILE);
    indexEntry.setLastModified(System.currentTimeMillis());
    indexEntry.setLength(indexLength);
    indexEntry.setObjectId(indexObjectId);
    dirCacheBuilder.add(indexEntry);
  }

  /**
   *
   * @param filePath the file path of the file as seen by the user
   * @return the file path of the given file inside the attachment rev tree
   */
  private String toRepoFilePath(String filePath) {
    return FILE_PATH_PREFIX + filePath;
  }

  /**
   * translates a file path within the attachment rev tree to the "real" file
   * path
   *
   * @param repoFilePath the file path within the attachment rev tree
   * @return the real file path
   */
  private String toRealFilePath(String repoFilePath) {
    if (repoFilePath.startsWith(FILE_PATH_PREFIX)) {
      return repoFilePath.substring(FILE_PATH_PREFIX.length());
    }
    return repoFilePath;
  }

  /**
   * translates all file paths in the given list from a path in the attachment
   * rev tree to the "real" file path
   *
   * @param repoFilePaths
   * @return
   */
  private List<String> toRealFilePaths(List<String> repoFilePaths) {
    List<String> realFilePaths = new ArrayList<String>(repoFilePaths.size());

    for (String repoFilePath : repoFilePaths) {
      realFilePaths.add(toRealFilePath(repoFilePath));
    }

    return realFilePaths;
  }

  /**
   * constructs the attachment ref name for a patch set
   *
   * @param changeId
   * @param patchSetId
   * @return
   */
  private String toRefName(Change.Id changeId, PatchSet.Id patchSetId) {
    return BASE_REF + "/" + changeId.get() + "/" + patchSetId.get(); // + "/" +
    // patchKey.get();
  }

  /**
   * loads the file index from the git object with the given object id
   *
   * @param objectId the object id of the git object
   * @param fileIndex the file index object used to store the index
   * @param repository the repository where the object resides
   * @return the file index instance for convenience
   */
  private FileIndex loadFileIndexFromGitObject(AnyObjectId objectId,
      FileIndex fileIndex, Repository repository)
      throws MissingObjectException, IOException {
    // TODO Wrap exceptions with own exception
    ObjectLoader objectLoader = repository.open(objectId);
    ObjectStream objectStream = objectLoader.openStream();
    fileIndex.read(objectStream);
    objectStream.close();
    return fileIndex;
  }


  @Override
  public List<TextFile> listAllFiles(Key patchKey) {
    PatchSet.Id patchSetId = patchKey.getParentKey();
    Change.Id changeId = patchSetId.getParentKey();
    ChangeResource changeResource = null;

    FileIndex fileIndex = new FileIndex();
    // TODO move to own class
    Map<String, TextFile> files = new HashMap<>();

    try {
      changeResource = changes.parse(changeId);
      Project.NameKey projectName = changeResource.getChange().getProject();
      Repository repository = null;
      repository = gitRepoManager.openRepository(projectName);

      String refName = toRefName(changeId, patchSetId);
      Ref attachmentRef = repository.getRef(refName);

      if (attachmentRef != null) {
        // ref exist, search for the index file in the last commit
        log.fine("Ref exists - searching for index file");

        RevWalk revWalk = new RevWalk(repository);
        RevCommit lastCommit = revWalk.parseCommit(attachmentRef.getObjectId());
        RevTree tree = lastCommit.getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        int treeId = treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        boolean indexLoaded = false;

        while (treeWalk.next() && !indexLoaded) {
          // we only depend on the index file to determine the attached files
          // for a patch - so walk through the tree until we find the file

          String path = treeWalk.getPathString();

          CanonicalTreeParser treeParser =
              treeWalk.getTree(treeId, CanonicalTreeParser.class);
          if (path.equals(INDEX_FILE_PATH)) {

            log.fine("found index file, loading index");

            // parse existing index file
            loadFileIndexFromGitObject(treeParser.getEntryObjectId(),
                fileIndex, repository);

            log.fine("index loaded");

            indexLoaded = true;
          } else if (path.startsWith(FILE_PATH_PREFIX)) {

            // read file content into temporary map (we only accept UTF-8
            // encoded text files, so we can safely treat it as a text file)

            ObjectLoader objectLoader =
                repository.open(treeParser.getEntryObjectId());
            ObjectStream objectStream = objectLoader.openStream();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(objectStream,
                    StandardCharsets.UTF_8));
            char[] cBuffer = new char[128];
            StringBuilder stringBuilder = new StringBuilder();
            int charsRead = reader.read(cBuffer);
            while (charsRead != -1) {
              stringBuilder.append(cBuffer, 0, charsRead);
              charsRead = reader.read(cBuffer);
            }
            objectStream.close();
            String filePath = toRealFilePath(treeParser.getEntryPathString());
            files.put(filePath,
                new TextFile(filePath, stringBuilder.toString()));

          }
        }

        if (!indexLoaded) {
          // Something weird happened here, as every commit on the attachment
          // ref should have an index file
          log.warning(MessageFormat
              .format(
                  "Index file not found - the content of the attachement ref {0} may be damaged",
                  refName));
        }
      } else {
        // the attachement ref does not exist -> no files exist -> do nothing
        log.fine("No attachement ref found - no files have been attached to the resource yet");
      }

    } catch (ResourceNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (OrmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (RepositoryNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    List<TextFile> attachedFiles = new ArrayList<>();

    for (String repoFilePath : fileIndex.get(patchKey.get())) {
      attachedFiles.add(files.get(toRealFilePath(repoFilePath)));
    }

    return attachedFiles;
  }

  @Override
  public void deleteFile(String filePath, Key patchKey, IdentifiedUser user)
      throws FileAttachmentException {
    String repoFilePath = toRepoFilePath(filePath);
    PatchSet.Id patchSetId = patchKey.getParentKey();
    Change.Id changeId = patchSetId.getParentKey();
    ChangeResource changeResource = null;

    FileIndex fileIndex = new FileIndex();

    try {
      changeResource = changes.parse(changeId);
      Project.NameKey projectName = changeResource.getChange().getProject();
      Repository repository = null;
      ObjectInserter inserter = null;

      repository = gitRepoManager.openRepository(projectName);
      inserter = repository.newObjectInserter();

      String refName = toRefName(changeId, patchSetId);

      Ref attachmentref = repository.getRef(refName);

      // We're working on a bare respository here, so create a temporary index
      // to build the tree for the commit afterwards
      DirCache index = DirCache.newInCore();
      DirCacheBuilder dirCacheBuilder = index.builder();

      if (attachmentref != null) {
        // The ref already exists so we have to copy the previous RevTree to
        // keep all already attached files

        log.fine("Ref exists - copying tree to maintain old attachments");

        RevWalk revWalk = new RevWalk(repository);
        RevCommit prevCommit = revWalk.parseCommit(attachmentref.getObjectId());
        RevTree tree = prevCommit.getTree();

        List<String> ignoredFiles = new ArrayList<>();
        // add file path of the file to delete to the ignored file paths
        ignoredFiles.add(repoFilePath);
        buildDirCacheFromTree(tree, repository, dirCacheBuilder, ignoredFiles,
            fileIndex);

        log.fine("finished copying tree");

        fileIndex.removeEntry(patchKey.get(), repoFilePath);

        writeFileIndex(fileIndex, inserter, dirCacheBuilder);

        // the dir cache is filled we are ready to write the tree to the object
        // database
        dirCacheBuilder.finish();

        // write new tree in database
        ObjectId indexTreeId = index.writeTree(inserter);

        // create commit
        CommitBuilder commitBuilder = new CommitBuilder();
        PersonIdent personIdent =
            user.newCommitterIdent(new Date(), TimeZone.getDefault());
        commitBuilder.setCommitter(personIdent);
        commitBuilder.setAuthor(personIdent);
        commitBuilder
            .setMessage(MessageFormat
                .format(
                    "Deleted attached file \"{0}\" of patch \"{1}\" from patch set {2}, change {3}",
                    repoFilePath, patchKey.get(), patchSetId.get(),
                    changeId.get()));

        commitBuilder.setParentId(attachmentref.getObjectId());
        commitBuilder.setTreeId(indexTreeId);

        // commit
        ObjectId commitId = inserter.insert(commitBuilder);
        inserter.flush();

        // update ref
        RefUpdate refUpdate = repository.updateRef(refName);
        refUpdate.setNewObjectId(commitId);
        refUpdate.setExpectedOldObjectId(attachmentref.getObjectId());

        // TODO
        // the result handling below is copied from the CommitCommand class, I
        // don't know if this is really necessary in our case
        Result result = refUpdate.forceUpdate();
        switch (result) {
          case NEW:
          case FORCED:
          case FAST_FORWARD: {
            if (repository.getRepositoryState() == RepositoryState.MERGING_RESOLVED) {
              // Commit was successful. Now delete the files
              // used for merge commits
              repository.writeMergeCommitMsg(null);
              repository.writeMergeHeads(null);
            } else if (repository.getRepositoryState() == RepositoryState.CHERRY_PICKING_RESOLVED) {
              repository.writeMergeCommitMsg(null);
              repository.writeCherryPickHead(null);
            } else if (repository.getRepositoryState() == RepositoryState.REVERTING_RESOLVED) {
              repository.writeMergeCommitMsg(null);
              repository.writeRevertHead(null);
            }
            break;
          }
          case REJECTED:
          case LOCK_FAILURE:
            // TODO improve exception handling
            throw new FileAttachmentException(new ConcurrentRefUpdateException(
                "Could not lock ref " + refUpdate.getRef().getName(),
                refUpdate.getRef(), result));
          default:
            // TODO improve exception handling
            throw new FileAttachmentException(new JGitInternalException(
                MessageFormat.format(JGitText.get().updatingRefFailed,
                    refUpdate.getRef().getName(), commitId.toString(), result)));
        }

      } else {
        log.fine(MessageFormat
            .format(
                "The file \"{0}\" is not attached to the patch \"{1}\" of patch set \"{2}\" in change \"{3}\"",
                filePath, patchKey, patchSetId, changeId));
      }
    } catch (ResourceNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (OrmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (RepositoryNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
