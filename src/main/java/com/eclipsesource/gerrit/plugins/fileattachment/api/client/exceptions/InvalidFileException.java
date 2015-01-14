/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;

/**
 * This exception is thrown if a given {@link File} is invalid or that the use
 * of the given {@link File} is invalid in the context of operation that raised
 * this exception.
 * 
 * @author Florian Zoubek
 *
 */
public class InvalidFileException extends FileAttachmentClientException {

  /**
   * 
   */
  private static final long serialVersionUID = -2952210987179940591L;
  /**
   * the {@link File} that caused this exception
   */
  private File file;

  /**
   * @see {@link Exception#Exception()}
   */
  public InvalidFileException(File file) {
    super();
    this.file = file;
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public InvalidFileException(File file, Throwable cause) {
    super(cause);
    this.file = file;
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public InvalidFileException(File file, String message) {
    super(message);
    this.file = file;
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public InvalidFileException(File file, String message, Throwable cause) {
    super(message, cause);
    this.file = file;
  }

  /**
   * @return the instance of the {@link AttachmentTargetDescription} that caused
   *         this exception
   */
  public File getFile() {
    return file;
  }


}
