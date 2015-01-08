/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

/**
 * A VERY basic representation of a UTF-8 encoded text file that should only be
 * used for this proof of concept
 *
 * @author Florian Zoubek
 *
 */
public class TextFile {

  private String filePath;

  private String content;

  public TextFile(String filePath, String content) {
    super();
    this.filePath = filePath;
    this.content = content;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
