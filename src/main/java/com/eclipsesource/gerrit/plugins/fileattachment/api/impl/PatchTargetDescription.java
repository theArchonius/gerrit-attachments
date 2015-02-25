/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

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
            + ((patchSetTargetDescription == null) ? 0
                : patchSetTargetDescription.hashCode());
    result = prime * result + ((patchUrl == null) ? 0 : patchUrl.hashCode());
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
    PatchTargetDescription other = (PatchTargetDescription) obj;
    if (patchSetTargetDescription == null) {
      if (other.patchSetTargetDescription != null) return false;
    } else if (!patchSetTargetDescription
        .equals(other.patchSetTargetDescription)) return false;
    if (patchUrl == null) {
      if (other.patchUrl != null) return false;
    } else if (!patchUrl.equals(other.patchUrl)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PatchTargetDescription [patchSetTargetDescription="
        + patchSetTargetDescription + ", patchUrl=" + patchUrl + "]";
  }

}
