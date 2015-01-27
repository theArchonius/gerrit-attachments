/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.model;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.AttachmentTargetRestEndpointRegistry;

/**
 * The default {@link AttachmentTargetRestEndpointRegistry} which currently no
 * attachment targets
 * 
 * @author Florian Zoubek
 *
 */
public class DefaultRestEndpointRegistry implements
    AttachmentTargetRestEndpointRegistry {

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * AttachmentTargetRestEndpointRegistry
   * #getRestEndpoint(com.eclipsesource.gerrit
   * .plugins.fileattachment.api.AttachmentTarget)
   */
  @Override
  public RestEndpoint getRestEndpoint(AttachmentTarget target) {
    // TODO implement and update javadoc class description
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * AttachmentTargetRestEndpointRegistry
   * #getRestEndpoint(com.eclipsesource.gerrit
   * .plugins.fileattachment.api.AttachmentTargetDescription)
   */
  @Override
  public RestEndpoint getRestEndpoint(AttachmentTargetDescription target) {
    // TODO implement and update javadoc class description
    return null;
  }

}
