/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a JSON entity used to pass all information of a attachment target
 * between client & server.
 * 
 * @author Florian Zoubek
 *
 */
public class AttachmentTargetEntity implements JsonEntity {

  /**
   * Represents the type of the attachment target
   * 
   * @author Florian Zoubek
   *
   */
  public enum TargetType {
    /**
     * target is a patch
     */
    PATCH,
    /**
     * target is a patch set
     */
    PATCH_SET,
    /**
     * target is a change
     */
    CHANGE,
    /**
     * target type is unknown or the target does not exist
     */
    UNKNOWN
  }

  /**
   * the type of the target
   */
  private TargetType targetType = TargetType.UNKNOWN;

  /**
   * the file descriptions of all attached files of that target
   */
  private FileDescriptionEntity[] fileDescriptions;

  /**
   * 
   * @param targetType the type of the target
   * @param fileDescriptions the file descriptions of all attached files of that
   *        target
   */
  public AttachmentTargetEntity(TargetType targetType,
      FileDescriptionEntity[] fileDescriptions) {
    super();
    this.targetType = targetType;
    this.fileDescriptions = fileDescriptions;
  }

  /**
   * @return the type of the attachment target
   */
  public TargetType getTargetType() {
    return targetType;
  }

  /**
   * @param targetType the type of the attachment target
   */
  public void setTargetType(TargetType targetType) {
    this.targetType = targetType;
  }

  /**
   * @return a array of file description representing all attached files of this
   *         target
   */
  public FileDescriptionEntity[] getFileDescriptions() {
    return fileDescriptions;
  }

  /**
   * @param fileDescriptions the file descriptions of all attached files of that
   *        target
   */
  public void setFileDescriptions(FileDescriptionEntity[] fileDescriptions) {
    this.fileDescriptions = fileDescriptions;
  }

}
