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



}
