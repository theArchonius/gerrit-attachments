/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api;

/**
 * This class provides a basic description of a file which can be used to
 * identify attached files.
 *
 * @author Florian Zoubek
 *
 */
public interface FileDescription {

  /**
   * @return the file path of the attached file
   */
  public String getFilePath();

  /**
   * @return the file name of the attached file
   */
  public String getFileName();
}
