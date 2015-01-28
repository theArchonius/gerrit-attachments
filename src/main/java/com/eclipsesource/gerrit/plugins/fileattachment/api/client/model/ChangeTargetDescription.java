/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.model;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;

/**
 * The {@link AttachmentTargetDescription} for a Change
 * 
 * @author Florian Zoubek
 *
 */
public class ChangeTargetDescription implements AttachmentTargetDescription {

  private String changeId;

  public ChangeTargetDescription(String changeId) {
    super();
    this.changeId = changeId;
  }

  /**
   * @return the change id
   */
  public String getChangeId() {
    return changeId;
  }

  /**
   * @param changeId the change id to set
   */
  public void setChangeId(String changeId) {
    this.changeId = changeId;
  }



}
