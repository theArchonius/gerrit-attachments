/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a JSON entity used to pass all information of a file description
 * of an attached file between client & server.
 * 
 * @author Florian Zoubek
 *
 */
public class FileDescriptionEntity implements JsonEntity {

  /**
   * the path to the file on the server
   */
  private String filePath;

  /**
   * the file name of the file on the server
   */
  private String fileName;

  /**
   * 
   * @param filePath the path to the file on the server
   * @param fileName the file name of the file on the server
   */
  public FileDescriptionEntity(String filePath, String fileName) {
    super();
    this.filePath = filePath;
    this.fileName = fileName;
  }

  /**
   * @return the path to the file on the server
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * @param filePath the path to the file on the server
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * @return the file name of the file on the server
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName the file name of the file on the server
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
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
    FileDescriptionEntity other = (FileDescriptionEntity) obj;
    if (fileName == null) {
      if (other.fileName != null) return false;
    } else if (!fileName.equals(other.fileName)) return false;
    if (filePath == null) {
      if (other.filePath != null) return false;
    } else if (!filePath.equals(other.filePath)) return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FileDescriptionEntity [filePath=" + filePath + ", fileName="
        + fileName + "]";
  }

}
