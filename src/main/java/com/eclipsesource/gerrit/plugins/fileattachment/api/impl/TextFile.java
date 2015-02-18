/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import java.nio.charset.Charset;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;

/**
 * Represents a simple text file
 * 
 * @author Florian Zoubek
 *
 */
public class TextFile implements File {

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
  private String content;

  /**
   * 
   * @param fileDescription the file description, must not be null
   * @param contentType the content type, must not be null
   * @param content the content (encoded with the charset specified in the
   *        content type - if none is specified, the default system charset is assumed)
   * 
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public TextFile(FileDescription fileDescription, ContentType contentType,
      String content) {
    this(fileDescription, contentType, content, null);
  }
  
  /**
   * 
   * @param fileDescription the file description, must not be null
   * @param contentType the content type, must not be null
   * @param content the content (encoded with the charset specified in the
   *        content type - if none is specified UTF8 is assumed)
   * @param attachmentTarget the attachment target this file is attached to,
   *        null otherwise
   * 
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public TextFile(FileDescription fileDescription, ContentType contentType,
      String content, AttachmentTarget attachmentTarget) {

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
    Charset charset = Charset.defaultCharset();

    // search for specific charset in the content type, if there exists none,
    // use the system default charset

    if (contentType != null) {
      String sCharset = contentType.getParameters().get("charset");
      if (sCharset != null) {
        charset = Charset.forName(sCharset);
      }
    }

    return content.getBytes(charset);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result
            + ((attachmentTarget == null) ? 0 : attachmentTarget.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result =
        prime * result + ((contentType == null) ? 0 : contentType.hashCode());
    result =
        prime * result
            + ((fileDescription == null) ? 0 : fileDescription.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TextFile other = (TextFile) obj;
    if (attachmentTarget == null) {
      if (other.attachmentTarget != null) return false;
    } else if (!attachmentTarget.equals(other.attachmentTarget)) return false;
    if (content == null) {
      if (other.content != null) return false;
    } else if (!content.equals(other.content)) return false;
    if (contentType == null) {
      if (other.contentType != null) return false;
    } else if (!contentType.equals(other.contentType)) return false;
    if (fileDescription == null) {
      if (other.fileDescription != null) return false;
    } else if (!fileDescription.equals(other.fileDescription)) return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TextFile [attachmentTarget=" + attachmentTarget
        + ", fileDescription=" + fileDescription + ", contentType="
        + contentType + ", content=" + content + "]";
  }

}
