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


}
