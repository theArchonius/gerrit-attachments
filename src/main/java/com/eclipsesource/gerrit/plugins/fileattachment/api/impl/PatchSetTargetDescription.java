/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;


/**
 * The {@link AttachmentTargetDescription} for a PatchSet.
 * 
 * @author Florian Zoubek
 *
 */
public class PatchSetTargetDescription implements AttachmentTargetDescription {

  private ChangeTargetDescription changeTargetDescription;

  /**
   * the id of the patch set
   */
  private int id;

  public PatchSetTargetDescription(
      ChangeTargetDescription changeTargetDescription, int id) {
    super();
    this.changeTargetDescription = changeTargetDescription;
    this.id = id;
  }

  /**
   * @return the patch set id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the patch set id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the changeTargetDescription
   */
  public ChangeTargetDescription getChangeTargetDescription() {
    return changeTargetDescription;
  }

  /**
   * @param changeTargetDescription the changeTargetDescription to set
   */
  public void setChangeTargetDescription(
      ChangeTargetDescription changeTargetDescription) {
    this.changeTargetDescription = changeTargetDescription;
  }

}
