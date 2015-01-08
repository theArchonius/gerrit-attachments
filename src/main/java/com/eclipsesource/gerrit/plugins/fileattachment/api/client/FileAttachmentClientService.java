/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;

/**
 * The base class for all services that add and delete file attachments or
 * provide additional informations of file attachments.
 *
 * @author Florian Zoubek
 *
 */
public interface FileAttachmentClientService {

  /**
   * attaches a file to the given target
   *
   * @param file the file to attach
   * @param attachmentTargetDescription the target which gets the file attached
   * @throws FileAttachmentClientException
   */
  public void attachFile(File file,
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException; // TODO throw more meaningful
                                            // exceptions here

  /**
   * creates a AttachmentTarget which can be used to get access to all of its
   * attached files
   *
   * @param attachmentTargetDescription
   * @return a AttachmentTarget containing all information of the attached files
   * @throws FileAttachmentClientException
   */
  public AttachmentTarget getAttachmentTarget(
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException; // TODO throw more meaningful
                                            // exceptions here

  /**
   * retrieves an attached file
   *
   * @param fileDescription the file description used to identify the attached
   *        file
   * @param attachmentTarget the description of the attachment target used to
   *        identify the attached file
   */
  public File getFile(FileDescription fileDescription,
      AttachmentTargetDescription attachmentTargetDescripton)
      throws FileAttachmentClientException;

  /**
   * deletes the given file from the attached file list
   *
   * @param file the file to delete - this file must have a attachment target
   *        assigned. In case you do not have a target descriptor, use
   *        {@link #deleteFile(FileDescription, AttachmentTargetDescription)}
   *        instead or retrieve an {@link AttachmentTarget} using
   *        {@link #getAttachmentTarget(AttachmentTargetDescription)}.
   * @throws FileAttachmentClientException
   * @throws {@link IllegalArgumentException} if the file has no attachment
   *         target assigned
   */
  public void deleteFile(File file) throws FileAttachmentClientException,
      IllegalArgumentException; // TODO throw more meaningful exceptions here

  /**
   * deletes the given attached file from the attached file list
   *
   * @param fileDescription the description of the file used to identify the
   *        file to delete
   * @param attachmentTargetDescription the description of the attachment target
   *        used to identify the file to delete
   * @throws FileAttachmentClientException
   */
  public void deleteFile(FileDescription fileDescription,
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException; // TODO throw more meaningful
                                            // exceptions here
}
