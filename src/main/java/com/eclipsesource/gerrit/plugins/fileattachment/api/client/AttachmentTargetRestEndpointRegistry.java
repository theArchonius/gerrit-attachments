/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;

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
   * @return the rest endpoint for the given target
   */
  public RestEndpoint getRestEndpoint(AttachmentTarget target);
  
  /**
   * creates a {@link RestEndpoint} for the given {@link AttachmentTargetDescription}
   * 
   * @param target
   * @return the rest endpoint for the given target
   */
  public RestEndpoint getRestEndpoint(AttachmentTargetDescription target);

}
