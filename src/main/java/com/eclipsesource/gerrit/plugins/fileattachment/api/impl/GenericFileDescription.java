/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;

/**
 * A simple {@link FileDescription} implementation that allows direct
 * specification of the properties of {@link FileDescription}
 *
 * @author Florian Zoubek
 *
 */
public class GenericFileDescription implements FileDescription {

  /**
   * the file path on the server side
   */
  private String filePath;

  /**
   * the file name on the server side
   */
  private String fileName;

  /**
   *
   * @param filePath the file path on the server side, must not be null, only
   *        '/' is allowed as separator for the individual parts of the path
   * @param fileName the file name on the server side, must not be null
   *
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public GenericFileDescription(String filePath, String fileName) {
    if (filePath == null) {
      throw new IllegalArgumentException("A file path must be specified");
    }
    if (fileName == null) {
      throw new IllegalArgumentException("A file name must be specified");
    }
    this.filePath = filePath;
    if (!filePath.isEmpty() && !filePath.endsWith("/")) {
      this.filePath += "/";
    }
    this.fileName = fileName;
  }

  @Override
  public String getServerFilePath() {
    return filePath;
  }

  @Override
  public String getServerFileName() {
    return fileName;
  }

  /* (non-Javadoc)
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

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    GenericFileDescription other = (GenericFileDescription) obj;
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
    return "GenericFileDescription [filePath=" + filePath + ", fileName="
        + fileName + "]";
  }

}
