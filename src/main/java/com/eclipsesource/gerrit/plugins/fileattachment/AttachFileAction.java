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

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity.FileState;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity.ResultStatus;

import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * A very basic REST API endpoint to attach a UTF-8 encoded text file to a given
 * FileResource (patch)
 *
 * @author Florian Zoubek
 */
public class AttachFileAction implements
    RestModifyView<FileResource, FileEntity> {

  private final Logger log;

  /*
   * IMPORTANT NOTE : Gerrit uses GSON with the naming policy {@link
   * com.google.gson.FieldNamingPolicy#LOWER_CASE_WITH_UNDERSCORES} which
   * transforms from camel case to lower case words separated by underscores
   */

  private final FileAttachmentService fileAttachmentService;
  private final Provider<CurrentUser> userProvider;
  private final EntityReader<File, FileEntity, AttachmentTarget> fileEntityReader;

  @Inject
  public AttachFileAction(final FileAttachmentService fileAttachmentService,
      final Provider<CurrentUser> user,
      final EntityReader<File, FileEntity, AttachmentTarget> fileEntityReader,
      final Logger log) {
    this.fileAttachmentService = fileAttachmentService;
    this.userProvider = user;
    this.fileEntityReader = fileEntityReader;
    this.log = log;
  }

  @Override
  public Object apply(FileResource resource, FileEntity input)
      throws AuthException {

    CurrentUser cUser = userProvider.get();

    if (!(cUser instanceof IdentifiedUser)) {
      throw new AuthException("You must be authenticated to attach files.");
    }

    IdentifiedUser user = (IdentifiedUser) cUser;

    Patch.Key patchKey = resource.getPatchKey();

    File file = fileEntityReader.toObject(input, null);

    if (file == null) {
      OperationResultEntity operationResultEntity =
          new OperationResultEntity(ResultStatus.FAILED,
              "Could not use the input file entity due to invalid entity properties");
      FileModificationResponseEntity fileModificationResponseEntity =
          new FileModificationResponseEntity(operationResultEntity,
              FileState.UNMODIFIED);
      return fileModificationResponseEntity;
    }

    try {
      // TODO return modification information
      fileAttachmentService.attachFile(file, patchKey, user);

    } catch (FileAttachmentException e) {

      OperationResultEntity operationResultEntity =
          new OperationResultEntity(
              ResultStatus.FAILED,
              MessageFormat
                  .format(
                      "The attach file operation failed due to an unexpected exception on the server: {0}",
                      e.getMessage()));
      FileModificationResponseEntity fileModificationResponseEntity =
          new FileModificationResponseEntity(operationResultEntity,
              FileState.UNMODIFIED);
      return fileModificationResponseEntity;
    }


    // TODO remove dummy data and use data returned from the service to build
    // the result
    OperationResultEntity operationResultEntity =
        new OperationResultEntity(ResultStatus.SUCCESS, MessageFormat.format(
            "Successfully attached file \"{0}\" at path \"{1}\" to patch {2}",
            input.getFileName(), input.getFilePath(), patchKey.get()));
    FileModificationResponseEntity fileModificationResponseEntity =
        new FileModificationResponseEntity(operationResultEntity, FileState.NEW);

    return fileModificationResponseEntity;
  }
}
