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

}
