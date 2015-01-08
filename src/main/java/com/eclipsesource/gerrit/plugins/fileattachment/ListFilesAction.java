/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.BadRequestException;
import com.google.gerrit.extensions.restapi.ResourceConflictException;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.server.change.FileResource;
import com.google.inject.Inject;

/**
 * A REST API endpoint which lists all attached files of the given file resource (patch)
 *
 * @author Florian Zoubek
 *
 */
public class ListFilesAction implements RestReadView<FileResource> {

  private final FileAttachmentService fileAttachmentService;

  @Inject
  public ListFilesAction(final FileAttachmentService fileAttachmentService) {
    this.fileAttachmentService = fileAttachmentService;
  }

  @Override
  public Object apply(FileResource resource) throws AuthException,
      BadRequestException, ResourceConflictException, Exception {

    Patch.Key patchKey = resource.getPatchKey();

    return fileAttachmentService.listAllFiles(patchKey);
  }

}
