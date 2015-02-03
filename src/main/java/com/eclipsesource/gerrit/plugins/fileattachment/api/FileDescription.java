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
   * @return the file path of the attached file on the server, the path uses '/'
   *         as separator for parts of the path and if the path is not empty,
   *         the last character must be '/'
   */
  public String getServerFilePath();

  /**
   * @return the file name of the attached file on the server
   */
  public String getServerFileName();
}
