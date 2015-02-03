/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.InvalidAttachmentTargetException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.InvalidFileException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.OperationFailedException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.RequestException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.ResponseException;

/**
 * The base class for all services that add and delete file attachments or
 * provide additional informations of file attachments.
 *
 * @author Florian Zoubek
 *
 */
public interface FileAttachmentClientService {

  /**
   * identifier for the {@link #attachFile(File, AttachmentTargetDescription)}
   * operation
   */
  public static final String OPERATION_ATTACH_FILE = "attachFile";

  /**
   * identifier for the
   * {@link #getAttachmentTarget(AttachmentTargetDescription)} operation
   */
  public static final String OPERATION_GET_TARGET = "getTarget";

  /**
   * identifier for the
   * {@link #getFile(FileDescription, AttachmentTargetDescription)} operation
   */
  public static final String OPERATION_GET_FILE = "getFile";

  /**
   * identifier for the {@link #deleteFile(File)} and
   * {@link #deleteFile(FileDescription, AttachmentTargetDescription)}
   * operations
   */
  public static final String OPERATION_DELETE_FILE = "deleteFile";

  /**
   * attaches a file to the given target
   *
   * @param file the file to attach
   * @param attachmentTargetDescription the target which gets the file attached
   * @throws InvalidAttachmentTargetException if the attachment target is
   *         invalid or not supported
   * @throws InvalidFileException if the file is invalid or not supported
   * @throws RequestException if an error occurs during the request to the
   *         server
   * @throws ResponseException if an error occurs during handling of the
   *         response from the server
   * @throws FileAttachmentClientException
   * 
   * @return the result summary of the attachment operation
   */
  public OperationResult attachFile(File file,
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException, InvalidAttachmentTargetException,
      InvalidFileException, RequestException, ResponseException;

  /**
   * creates a AttachmentTarget which can be used to get access to all of its
   * attached files
   *
   * @param attachmentTargetDescription
   * @return a AttachmentTarget containing all information of the attached files
   * @throws RequestException if an error occurs during the request to the
   *         server
   * @throws ResponseException if an error occurs during handling of the
   *         response from the server
   * @throws OperationFailedException if the operation failed on the server side
   * @throws FileAttachmentClientException
   */
  public AttachmentTarget getAttachmentTarget(
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException, RequestException,
      ResponseException, OperationFailedException;

  /**
   * retrieves an attached file
   *
   * @param fileDescription the file description used to identify the attached
   *        file
   * @param attachmentTarget the description of the attachment target used to
   *        identify the attached file
   * @throws RequestException if an error occurs during the request to the
   *         server
   * @throws ResponseException if an error occurs during handling of the
   *         response from the server
   * @throws OperationFailedException if the operation failed on the server side
   * @throws FileAttachmentClientException
   */
  public File getFile(FileDescription fileDescription,
      AttachmentTargetDescription attachmentTargetDescripton)
      throws FileAttachmentClientException, RequestException,
      ResponseException, OperationFailedException;

  /**
   * deletes the given file from the attached file list
   *
   * @param file the file to delete - this file must have a attachment target
   *        assigned. In case you do not have a target descriptor, use
   *        {@link #deleteFile(FileDescription, AttachmentTargetDescription)}
   *        instead or retrieve an {@link AttachmentTarget} using
   *        {@link #getAttachmentTarget(AttachmentTargetDescription)}.
   * @return TODO
   * @throws RequestException if an error occurs during the request to the
   *         server
   * @throws ResponseException if an error occurs during handling of the
   *         response from the server
   * @throws FileAttachmentClientException
   * @throws {@link IllegalArgumentException} if the file has no attachment
   *         target assigned
   */
  public OperationResult deleteFile(File file)
      throws FileAttachmentClientException, IllegalArgumentException,
      RequestException, ResponseException;

  /**
   * deletes the given attached file from the attached file list
   *
   * @param fileDescription the description of the file used to identify the
   *        file to delete
   * @param attachmentTargetDescription the description of the attachment target
   *        used to identify the file to delete
   * @return TODO
   * @throws RequestException if an error occurs during the request to the
   *         server
   * @throws ResponseException if an error occurs during handling of the
   *         response from the server
   * @throws FileAttachmentClientException
   */
  public OperationResult deleteFile(FileDescription fileDescription,
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException, RequestException, ResponseException;
}
