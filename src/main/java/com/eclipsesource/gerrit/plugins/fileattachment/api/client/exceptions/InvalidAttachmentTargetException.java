/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;

/**
 * This exception is thrown if a given {@link AttachmentTargetDescription} is
 * invalid or that the use of the given {@link AttachmentTargetDescription} is
 * invalid in the context of operation that raised this exception.
 * 
 * @author Florian Zoubek
 *
 */
public class InvalidAttachmentTargetException extends FileAttachmentClientException {

  /**
   * 
   */
  private static final long serialVersionUID = 4950514965371355402L;

  /**
   * the {@link AttachmentTargetDescription} that caused this exception
   */
  private AttachmentTargetDescription attachmentTargetDescription;

  /**
   * @see {@link Exception#Exception()}
   */
  public InvalidAttachmentTargetException(
      AttachmentTargetDescription attachmentTargetDescription) {
    super();
    this.attachmentTargetDescription = attachmentTargetDescription;
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public InvalidAttachmentTargetException(
      AttachmentTargetDescription attachmentTargetDescription, Throwable cause) {
    super(cause);
    this.attachmentTargetDescription = attachmentTargetDescription;
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public InvalidAttachmentTargetException(
      AttachmentTargetDescription attachmentTargetDescription, String message) {
    super(message);
    this.attachmentTargetDescription = attachmentTargetDescription;
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public InvalidAttachmentTargetException(
      AttachmentTargetDescription attachmentTargetDescription, String message,
      Throwable cause) {
    super(message, cause);
    this.attachmentTargetDescription = attachmentTargetDescription;
  }

  /**
   * @return the instance of the {@link AttachmentTargetDescription} that caused
   *         this exception
   */
  public AttachmentTargetDescription getAttachmentTargetDescription() {
    return attachmentTargetDescription;
  }


}
