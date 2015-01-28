/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.model;

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

}
