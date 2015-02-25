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
            + ((attachmentTargetEntity == null) ? 0 : attachmentTargetEntity
                .hashCode());
    result =
        prime
            * result
            + ((operationResultEntity == null) ? 0 : operationResultEntity
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
    AttachmentTargetResponseEntity other = (AttachmentTargetResponseEntity) obj;
    if (attachmentTargetEntity == null) {
      if (other.attachmentTargetEntity != null) return false;
    } else if (!attachmentTargetEntity.equals(other.attachmentTargetEntity))
      return false;
    if (operationResultEntity == null) {
      if (other.operationResultEntity != null) return false;
    } else if (!operationResultEntity.equals(other.operationResultEntity))
      return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AttachmentTargetResponseEntity [attachmentTargetEntity="
        + attachmentTargetEntity + ", operationResultEntity="
        + operationResultEntity + "]";
  }



}
