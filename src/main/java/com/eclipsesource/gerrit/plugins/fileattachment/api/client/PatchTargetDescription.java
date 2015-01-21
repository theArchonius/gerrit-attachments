/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;

/**
 * The {@link AttachmentTargetDescription} for a patch
 * 
 * @author Florian Zoubek
 *
 */
public class PatchTargetDescription implements AttachmentTargetDescription {

  private PatchSetTargetDescription patchSetTargetDescription;

  public PatchTargetDescription(
      PatchSetTargetDescription patchSetTargetDescription, String patchUrl) {
    super();
    this.patchSetTargetDescription = patchSetTargetDescription;
    this.patchUrl = patchUrl;
  }

  /**
   * @return the patchSetTargetDescription
   */
  public PatchSetTargetDescription getPatchSetTargetDescription() {
    return patchSetTargetDescription;
  }

  /**
   * @param patchSetTargetDescription the patchSetTargetDescription to set
   */
  public void setPatchSetTargetDescription(
      PatchSetTargetDescription patchSetTargetDescription) {
    this.patchSetTargetDescription = patchSetTargetDescription;
  }

  private String patchUrl;

  /**
   * @return the patchUrl
   */
  public String getPatchUrl() {
    return patchUrl;
  }

  /**
   * @param patchUrl the patchUrl to set
   */
  public void setPatchUrl(String patchUrl) {
    this.patchUrl = patchUrl;
  }


}
