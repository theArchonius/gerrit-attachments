/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((changeId == null) ? 0 : changeId.hashCode());
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
    ChangeTargetDescription other = (ChangeTargetDescription) obj;
    if (changeId == null) {
      if (other.changeId != null) return false;
    } else if (!changeId.equals(other.changeId)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ChangeTargetDescription [changeId=" + changeId + "]";
  }
}
