/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import java.util.Arrays;

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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result
            + ((attachmentTarget == null) ? 0 : attachmentTarget.hashCode());
    result = prime * result + Arrays.hashCode(content);
    result =
        prime * result + ((contentType == null) ? 0 : contentType.hashCode());
    result =
        prime * result
            + ((fileDescription == null) ? 0 : fileDescription.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BinaryFile other = (BinaryFile) obj;
    if (attachmentTarget == null) {
      if (other.attachmentTarget != null) return false;
    } else if (!attachmentTarget.equals(other.attachmentTarget)) return false;
    if (!Arrays.equals(content, other.content)) return false;
    if (contentType == null) {
      if (other.contentType != null) return false;
    } else if (!contentType.equals(other.contentType)) return false;
    if (fileDescription == null) {
      if (other.fileDescription != null) return false;
    } else if (!fileDescription.equals(other.fileDescription)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BinaryFile [attachmentTarget=" + attachmentTarget
        + ", fileDescription=" + fileDescription + ", contentType="
        + contentType + ", content=" + Arrays.toString(content) + "]";
  }

}
