/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

/**
 * This exception is thrown to indicate that an error occured during creation of
 * the server request.
 * 
 * @author Florian Zoubek
 *
 */
public class RequestException extends FileAttachmentClientException {

  /**
   * 
   */
  private static final long serialVersionUID = 3906453490804012344L;

  /**
   * @see {@link Exception#Exception()}
   */
  public RequestException() {
    super();
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public RequestException(Throwable cause) {
    super(cause);
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public RequestException(String message) {
    super(message);
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public RequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
