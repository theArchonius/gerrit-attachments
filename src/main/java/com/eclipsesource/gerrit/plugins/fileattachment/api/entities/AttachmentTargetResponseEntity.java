/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a JSON entity used to pass all information of a response
 * containing an {@link AttachmentTargetEntity} from the server.
 * 
 * @author Florian Zoubek
 *
 */
public class AttachmentTargetResponseEntity implements JsonEntity {

  /**
   * the attachment target entity passed by the server
   */
  private AttachmentTargetEntity attachmentTargetEntity;

  /**
   * the operation result containing the status of the operation that issued
   * this response.
   */
  private OperationResultEntity operationResultEntity;

  /**
   * @param attachmentTargetEntity the attachment target entity passed by the
   *        server, null if the operation was not successful
   * @param operationResultEntity the operation result containing the status of
   *        the operation that issued this response.
   */
  public AttachmentTargetResponseEntity(
      AttachmentTargetEntity attachmentTargetEntity,
      OperationResultEntity operationResultEntity) {
    super();
    this.attachmentTargetEntity = attachmentTargetEntity;
    this.operationResultEntity = operationResultEntity;
  }

  /**
   * @return the attachmentTargetEntity the attachment target entity passed by
   *         the server, null if the operation was not successful
   */
  public AttachmentTargetEntity getAttachmentTargetEntity() {
    return attachmentTargetEntity;
  }

  /**
   * @param attachmentTargetEntity the attachment target entity passed by the
   *        server, null if the operation was not successful
   */
  public void setAttachmentTargetEntity(
      AttachmentTargetEntity attachmentTargetEntity) {
    this.attachmentTargetEntity = attachmentTargetEntity;
  }

  /**
   * @return the operationResultEntity the operation result containing the
   *         status of the operation that issued this response.
   */
  public OperationResultEntity getOperationResultEntity() {
    return operationResultEntity;
  }

  /**
   * @param operationResultEntity the operation result containing the status of
   *        the operation that issued this response.
   */
  public void setOperationResultEntity(
      OperationResultEntity operationResultEntity) {
    this.operationResultEntity = operationResultEntity;
  }



}
