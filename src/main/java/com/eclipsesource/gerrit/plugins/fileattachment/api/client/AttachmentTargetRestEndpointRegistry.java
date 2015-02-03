/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.RestEndpoint;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.UnsupportedFileOperationException;

/**
 * Represents a registry that maps {@link AttachmentTarget}s to
 * {@link RestEndpoint}s
 * 
 * @author Florian Zoubek
 *
 */
public interface AttachmentTargetRestEndpointRegistry {

  /**
   * creates a {@link RestEndpoint} for the given {@link AttachmentTarget}
   * 
   * @param target
   * @param operation the operation identifier
   * @return the rest endpoint for the given target or null if the target is not
   *         supported
   * @throws UnsupportedFileOperationException if the operation is not supported
   *         by this registry
   * @throws FileAttachmentClientException if an error occurs during endpoint
   *         creation
   */
  public RestEndpoint getRestEndpoint(AttachmentTarget target, String operation)
      throws UnsupportedFileOperationException, FileAttachmentClientException;

  /**
   * creates a {@link RestEndpoint} for the given
   * {@link AttachmentTargetDescription}
   * 
   * @param target
   * @param operation the operation identifier
   * @return the rest endpoint for the given target or null if the target is not
   *         supported
   * @throws UnsupportedFileOperationException if the operation is not supported
   *         by this registry
   * @throws FileAttachmentClientException if an error occurs during endpoint
   *         creation
   */
  public RestEndpoint getRestEndpoint(AttachmentTargetDescription target,
      String operation) throws UnsupportedFileOperationException,
      FileAttachmentClientException;

}
