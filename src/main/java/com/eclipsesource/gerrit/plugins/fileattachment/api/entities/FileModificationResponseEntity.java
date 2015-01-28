/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * @author Florian Zoubek
 *
 */
public class FileModificationResponseEntity implements JsonEntity {

  /**
   * the status after the file modification operation. Allowed values:
   * 
   * <ul>
   * <li>SUCCESS - the file operation was successful</li>
   * <li>FAILED - the file operation has failed</li>
   * <li>NOTPERMITTED - the file operation is not allowed</li>
   * </ul>
   */
  private String operationResult = "";

  /**
   * the error message, if an error occurs, may be empty if no error occurs
   */
  private String errorMessage = "";

  /**
   * the new state of the attached file.
   *
   * Allowed values:
   * <ul>
   * <li>NEW - the file has been attached and no previous version exists</li>
   * <li>MODIFIED - the file has been attached and no previous version exists</li>
   * <li>UNMODIFIED - the file has been has not been modified (either the same
   * name, or the operation has failed)</li>
   * <li>DELETED - the file has been deleted</li>
   * </ul>
   */
  private String newFileState = "";

  public FileModificationResponseEntity(String operationResult,
      String errorMessage, String newFileState) {
    super();
    this.operationResult = operationResult;
    this.errorMessage = errorMessage;
    this.newFileState = newFileState;
  }

  /**
   * @return the status after the attach operation. Possible values:
   * 
   * 
   *         <ul>
   *         <li>SUCCESS - the file operation was successful</li>
   *         <li>FAILED - the file operation has failed</li>
   *         <li>NOTPERMITTED - the file operation is not allowed</li>
   *         </ul>
   */
  public String getOperationResult() {
    return operationResult;
  }

  /**
   * @param operationResult the status after the attach operation. Allowed
   *        values:
   * 
   * 
   *        <ul>
   *        <li>SUCCESS - the file operation was successful</li>
   *        <li>FAILED - the file operation has failed</li>
   *        <li>NOTPERMITTED - the file operation is not allowed</li>
   *        </ul>
   */
  public void setOperationResult(String operationResult) {
    this.operationResult = operationResult;
  }

  /**
   * @return the errorMessage if it exists, empty otherwise
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * @return the new state of the attached file.
   *
   *         Allowed values:
   *         <ul>
   *         <li>NEW - the file has been attached and no previous version exists
   *         </li>
   *         <li>MODIFIED - the file has been attached and no previous version
   *         exists</li>
   *         <li>UNMODIFIED - the file has been has not been modified (either
   *         the same name, or the operation has failed)</li>
   *         <li>DELETED - the file has been deleted</li>
   *         </ul>
   */
  public String getNewFileState() {
    return newFileState;
  }

  /**
   * @param newFileState the new state of the attached file.
   *
   *        Allowed values:
   *        <ul>
   *        <li>NEW - the file has been attached and no previous version exists</li>
   *        <li>MODIFIED - the file has been attached and no previous version
   *        exists</li>
   *        <li>UNMODIFIED - the file has been has not been modified (either the
   *        same name, or the operation has failed)</li>
   *        <li>DELETED - the file has been deleted</li>
   *        </ul>
   */
  public void setNewFileState(String newFileState) {
    this.newFileState = newFileState;
  }



}
