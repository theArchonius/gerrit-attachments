/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;

/**
 * Represents a simple file with arbitrary content
 * 
 * @author Florian Zoubek
 *
 */
public class BinaryFile implements File {

  /**
   * the target which has this file attached, <code>null</code> if this file is
   * not attached to any target
   */
  private AttachmentTarget attachmentTarget;

  /**
   * the file description
   */
  private FileDescription fileDescription;

  /**
   * the type of the file content
   */
  private ContentType contentType;

  /**
   * the content
   */
  private byte[] content;

  /**
   * 
   * @param fileDescription the file description, must not be null
   * @param contentType the content type, must not be null
   * @param content the raw content
   * 
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public BinaryFile(FileDescription fileDescription, ContentType contentType,
      byte[] content) {
    this(fileDescription, contentType, content, null);
  }

  /**
   * 
   * @param fileDescription the file description, must not be null
   * @param contentType the content type, must not be null
   * @param content the raw content
   * @param attachmentTarget the attachment target this file is attached to,
   *        null otherwise
   * 
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public BinaryFile(FileDescription fileDescription, ContentType contentType,
      byte[] content, AttachmentTarget attachmentTarget) {

    if (fileDescription == null) {
      throw new IllegalArgumentException(
          "The file description must not be null");
    }

    if (contentType == null) {
      throw new IllegalArgumentException("The content type must not be null");
    }

    this.attachmentTarget = attachmentTarget;
    this.fileDescription = fileDescription;
    this.contentType = contentType;
    this.content = content;
  }

  @Override
  public AttachmentTarget getAttachmentTarget() {
    return attachmentTarget;
  }

  @Override
  public FileDescription getFileDescription() {
    return fileDescription;
  }

  @Override
  public ContentType getContentType() {
    return contentType;
  }

  @Override
  public byte[] getContent() {
    return content;
  }

}
