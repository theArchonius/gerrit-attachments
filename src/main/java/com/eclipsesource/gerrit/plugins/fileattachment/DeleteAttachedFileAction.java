/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.BadRequestException;
import com.google.gerrit.extensions.restapi.ResourceConflictException;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.change.FileResource;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * A very basic REST API endpoint to delete a attached text file from a given
 * FileResource (patch)
 *
 * @author Florian Zoubek
 *
 */
public class DeleteAttachedFileAction implements
    RestModifyView<FileResource, DeleteAttachedFileAction.Input> {


  /*
   * IMPORTANT NOTE : Gerrit uses GSON with the naming policy {@link
   * com.google.gson.FieldNamingPolicy#LOWER_CASE_WITH_UNDERSCORES} which
   * transforms from camel case to lower case words separated by underscores
   */
  static class Input {
    String filePath;
  }

  private final FileAttachmentService fileAttachmentService;
  private final Provider<CurrentUser> userProvider;

  @Inject
  public DeleteAttachedFileAction(
      final FileAttachmentService fileAttachementService,
      final Provider<CurrentUser> userProvider) {
    this.fileAttachmentService = fileAttachementService;
    this.userProvider = userProvider;
  }

  @Override
  public Object apply(FileResource resource, Input input) throws AuthException,
      BadRequestException, ResourceConflictException, Exception {

    CurrentUser cUser = userProvider.get();

    if (!(cUser instanceof IdentifiedUser)) {
      throw new AuthException("You must be authenticated to delete files.");
    }

    IdentifiedUser user = (IdentifiedUser) cUser;
    try {
      fileAttachmentService.deleteFile(input.filePath, resource.getPatchKey(),
          user);
    } catch (FileAttachmentException e) {
      return "FAILED";
    }

    return "OK";
  }

}
