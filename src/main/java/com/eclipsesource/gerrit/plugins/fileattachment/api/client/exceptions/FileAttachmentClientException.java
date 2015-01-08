package com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions;

/**
 * Generic base class for all exceptions thrown by the file attachment client services
 *
 * @author Florian Zoubek
 *
 */
public class FileAttachmentClientException extends Exception {

  private static final long serialVersionUID = -2327585755804897348L;

  /**
   * @see {@link Exception#Exception()}
   */
  public FileAttachmentClientException(){
    super();
  }

  /**
   * @see {@link Exception#Exception(Throwable)}
   */
  public FileAttachmentClientException(Throwable cause){
    super(cause);
  }

  /**
   * @see {@link Exception#Exception(String)}
   */
  public FileAttachmentClientException(String message){
    super(message);
  }

  /**
   * @see {@link Exception#Exception(String, Throwable)}
   */
  public FileAttachmentClientException(String message, Throwable cause){
    super(message, cause);
  }

}
