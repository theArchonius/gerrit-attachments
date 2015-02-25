/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import java.util.List;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;

/**
 * The {@link AttachmentTarget} for a patch
 * 
 * @author Florian Zoubek
 *
 */
public class PatchTarget implements AttachmentTarget {

  /**
   * the corresponding PatchTargetDescription for this patch
   */
  private PatchTargetDescription patchTargetDescription;

  /**
   * the file descriptions of all attached file of this patch
   */
  private List<FileDescription> attachedFileDescriptions;

  /**
   * 
   * @param patchTargetDescription the corresponding PatchTargetDescription for
   *        this patch
   * @param attachedFileDescriptions the file descriptions of all attached file
   *        of this patch
   */
  public PatchTarget(PatchTargetDescription patchTargetDescription,
      List<FileDescription> attachedFileDescriptions) {
    super();
    this.patchTargetDescription = patchTargetDescription;
    this.attachedFileDescriptions = attachedFileDescriptions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget#
   * getTargetDescription()
   */
  @Override
  public AttachmentTargetDescription getTargetDescription() {
    return patchTargetDescription;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget#
   * getAttachedFileDescriptions()
   */
  @Override
  public List<FileDescription> getAttachedFileDescriptions() {
    return attachedFileDescriptions;
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
            + ((attachedFileDescriptions == null) ? 0
                : attachedFileDescriptions.hashCode());
    result =
        prime
            * result
            + ((patchTargetDescription == null) ? 0 : patchTargetDescription
                .hashCode());
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
    PatchTarget other = (PatchTarget) obj;
    if (attachedFileDescriptions == null) {
      if (other.attachedFileDescriptions != null) return false;
    } else if (!attachedFileDescriptions.equals(other.attachedFileDescriptions))
      return false;
    if (patchTargetDescription == null) {
      if (other.patchTargetDescription != null) return false;
    } else if (!patchTargetDescription.equals(other.patchTargetDescription))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PatchTarget [patchTargetDescription=" + patchTargetDescription
        + ", attachedFileDescriptions=" + attachedFileDescriptions + "]";
  }

}
