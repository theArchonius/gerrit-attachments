/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a JSON entity used to pass information about the result of an
 * operation.
 * 
 * @author Florian Zoubek
 *
 */
public class OperationResultEntity implements JsonEntity {

  /**
   * represents the status of an operation
   * 
   * @author Florian Zoubek
   *
   */
  public enum ResultStatus {
    /**
     * the operation was successful
     */
    SUCCESS,
    /**
     * the operation has failed
     */
    FAILED,
    /**
     * the operation is not allowed
     */
    NOTPERMITTED
  }

  /**
   * the status of the operation
   */
  private ResultStatus resultStatus = ResultStatus.SUCCESS;

  /**
   * the detailed status message, containing additional information, warnings
   * and error messages, may be empty but not null
   */
  private String statusMessage = "";

  /**
   * 
   * @param resultStatus the status of the operation
   * @param statusMessage the detailed status message, containing additional
   *        information, warnings and error messages, may be empty but not null
   */
  public OperationResultEntity(ResultStatus resultStatus, String statusMessage) {
    super();
    this.resultStatus = resultStatus;
    this.statusMessage = statusMessage;
  }

  /**
   * @return the status of the operation
   */
  public ResultStatus getResultStatus() {
    return resultStatus;
  }

  /**
   * @param resultStatus the status of the operation
   */
  public void setResultStatus(ResultStatus resultStatus) {
    this.resultStatus = resultStatus;
  }

  /**
   * @return the detailed status message, containing additional information,
   *         warnings and error messages, may be empty but not null
   */
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * @param statusMessage the detailed status message, containing additional
   *        information, warnings and error messages, may be empty but not null
   */
  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
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
        prime * result + ((resultStatus == null) ? 0 : resultStatus.hashCode());
    result =
        prime * result
            + ((statusMessage == null) ? 0 : statusMessage.hashCode());
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
    OperationResultEntity other = (OperationResultEntity) obj;
    if (resultStatus != other.resultStatus) return false;
    if (statusMessage == null) {
      if (other.statusMessage != null) return false;
    } else if (!statusMessage.equals(other.statusMessage)) return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "OperationResultEntity [resultStatus=" + resultStatus
        + ", statusMessage=" + statusMessage + "]";
  }

}
