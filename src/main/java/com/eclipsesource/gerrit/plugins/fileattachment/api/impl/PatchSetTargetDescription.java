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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime
            * result
            + ((changeTargetDescription == null) ? 0 : changeTargetDescription
                .hashCode());
    result = prime * result + id;
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PatchSetTargetDescription other = (PatchSetTargetDescription) obj;
    if (changeTargetDescription == null) {
      if (other.changeTargetDescription != null) return false;
    } else if (!changeTargetDescription.equals(other.changeTargetDescription))
      return false;
    if (id != other.id) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PatchSetTargetDescription [changeTargetDescription="
        + changeTargetDescription + ", id=" + id + "]";
  }

}
