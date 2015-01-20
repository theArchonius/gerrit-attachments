/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api;

/**
 * Represents a file attached to some attachment target.
 *
 * @author Florian Zoubek
 *
 */
public interface File {

  /**
   * @return the target this file is attached to or null if this file has not
   *         been attached to a file yet
   */
  public AttachmentTarget getAttachmentTarget();

  /**
   * @return the file description containing the file name, path...
   */
  public FileDescription getFileDescription();

  /**
   * @return the content type of this file
   */
  public ContentType getContentType();

  /**
   * @return the content of the file
   */
  public byte[] getContent();

}
