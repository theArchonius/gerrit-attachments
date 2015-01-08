/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.change.FileResource;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;

import java.util.logging.Logger;

/**
 * A very basic REST API endpoint to attach a UTF-8 encoded text file to a given
 * FileResource (patch)
 *
 * @author Florian Zoubek
 */
public class AttachFileAction implements
    RestModifyView<FileResource, AttachFileAction.Input> {

  private final Logger log;

  /*
   * IMPORTANT NOTE : Gerrit uses GSON with the naming policy {@link
   * com.google.gson.FieldNamingPolicy#LOWER_CASE_WITH_UNDERSCORES} which
   * transforms from camel case to lower case words separated by underscores
   */
  static class Input {
    String filePath;
    String content;

    @Override
    public String toString() {
      return "Input [fileUri=" + filePath + ", content=" + content + "]";
    }

  }

  private final FileAttachmentService fileAttachmentService;
  private final Provider<CurrentUser> userProvider;

  @Inject
  public AttachFileAction(final FileAttachmentService fileAttachmentService,
      final Provider<CurrentUser> user, final Logger log) {
    this.fileAttachmentService = fileAttachmentService;
    this.userProvider = user;
    this.log = log;
  }

  @Override
  public Object apply(FileResource resource, Input input) throws AuthException {

    CurrentUser cUser = userProvider.get();

    if(! (cUser instanceof IdentifiedUser)){
      throw new AuthException("You must be authenticated to attach files.");
    }

    IdentifiedUser user = (IdentifiedUser) cUser;

    Patch.Key patchKey = resource.getPatchKey();

    try {
      fileAttachmentService.attachFile(input.filePath, patchKey, input.content,
          user);
    } catch (FileAttachmentException e) {
      // TODO add better exception handling
      e.printStackTrace();
      return "FAILED";
    }

    return "OK";
  }

}
