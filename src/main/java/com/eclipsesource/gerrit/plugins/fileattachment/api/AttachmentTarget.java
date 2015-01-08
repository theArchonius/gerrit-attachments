/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api;

import java.util.List;

/**
 * Represents a target on the server side which allows attachment of one or more
 * files.
 *
 * @author Florian Zoubek
 *
 */
public interface AttachmentTarget {


  /**
   * @return the target description that uniquely identifies this attachment target
   */
  public AttachmentTargetDescription getTargetDescription();

  /**
   * @return a list of all currently attached files of this target
   */
  public List<FileDescription> getAttachedFileDescriptions();
}
