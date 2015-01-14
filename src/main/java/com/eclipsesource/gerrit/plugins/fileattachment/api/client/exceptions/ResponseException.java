/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

/**
 * This exception is thrown to indicate that an error occured during handling of
 * response from the server.
 * 
 * @author Florian Zoubek
 *
 */
public class ResponseException extends FileAttachmentClientException {

  /**
   * 
   */
  private static final long serialVersionUID = -5069439099179254917L;

  /**
   * @see {@link Exception#Exception()}
   */
  public ResponseException() {
    super();
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public ResponseException(Throwable cause) {
    super(cause);
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public ResponseException(String message) {
    super(message);
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public ResponseException(String message, Throwable cause) {
    super(message, cause);
  }
}
