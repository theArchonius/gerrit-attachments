/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;


/**
 * Represents a JSON entity used to pass all information of one file between
 * client & server
 * 
 * @author Florian Zoubek
 *
 */
public class FileEntity implements JsonEntity {

  private String fileName = "";

  private String filePath = "";

  private String contentType = "";

  private String content = "";

  private String encodingMethod = "None";

  /**
   * Default constructor - string properties set to empty strings, object
   * properties set to null, the encoding method is set to "None"
   */
  public FileEntity() {
  }

  /**
   * 
   * @param fileName the name of the file
   * @param filePath the path of the file on the server
   * @param contentType the content type of the file
   * @param content the string encoded content of the file
   * @param encodingMethod the id of the encoding method used to encode the
   *        original file content to a string
   */
  public FileEntity(String fileName, String filePath, String contentType,
      String content, String encodingMethod) {
    super();
    this.fileName = fileName;
    this.filePath = filePath;
    this.contentType = contentType;
    this.content = content;
    this.encodingMethod = encodingMethod;
  }

  /**
   * @return the file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName the file name to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * @return the file path on the server
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * @param filePath the file path on the server to set
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * @return the content type
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType the content type to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the encoding method id
   */
  public String getEncodingMethod() {
    return encodingMethod;
  }

  /**
   * @param encodingMethod the encoding method id to set
   */
  public void setEncodingMethod(String encodingMethod) {
    this.encodingMethod = encodingMethod;
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
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result =
        prime * result + ((contentType == null) ? 0 : contentType.hashCode());
    result =
        prime * result
            + ((encodingMethod == null) ? 0 : encodingMethod.hashCode());
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
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
    FileEntity other = (FileEntity) obj;
    if (content == null) {
      if (other.content != null) return false;
    } else if (!content.equals(other.content)) return false;
    if (contentType == null) {
      if (other.contentType != null) return false;
    } else if (!contentType.equals(other.contentType)) return false;
    if (encodingMethod == null) {
      if (other.encodingMethod != null) return false;
    } else if (!encodingMethod.equals(other.encodingMethod)) return false;
    if (fileName == null) {
      if (other.fileName != null) return false;
    } else if (!fileName.equals(other.fileName)) return false;
    if (filePath == null) {
      if (other.filePath != null) return false;
    } else if (!filePath.equals(other.filePath)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FileEntity [fileName=" + fileName + ", filePath=" + filePath
        + ", contentType=" + contentType + ", content=" + content.length() + " characters - hash: "
        + content.hashCode() + ", encodingMethod=" + encodingMethod + "]";
  }

}
