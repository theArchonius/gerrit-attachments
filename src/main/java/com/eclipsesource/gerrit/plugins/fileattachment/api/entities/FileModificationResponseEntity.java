/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * @author Florian Zoubek
 *
 */
public class FileModificationResponseEntity implements JsonEntity {

  public enum FileState {
    /**
     * the file has been attached and no previous version exists
     */
    NEW,
    /**
     * the file has been attached and a previous version exists
     */
    MODIFIED,
    /**
     * the file has been has not been modified (either the file is the same, or
     * the operation has failed)
     */
    UNMODIFIED,
    /**
     * the file has been deleted
     */
    DELETED
  }

  private OperationResultEntity operationResult;

  /**
   * the new state after the file operation
   */
  private FileState newFileState = FileState.NEW;

  public FileModificationResponseEntity(OperationResultEntity operationResult,
      FileState newFileState) {
    super();
    this.operationResult = operationResult;
    this.newFileState = newFileState;
  }

  /**
   * @return the status after the operation
   */
  public OperationResultEntity getOperationResult() {
    return operationResult;
  }

  /**
   * @param operationResult the status after the operation
   */
  public void setOperationResult(OperationResultEntity operationResult) {
    this.operationResult = operationResult;
  }

  /**
   * @return the new state of the attached file
   *
   */
  public FileState getNewFileState() {
    return newFileState;
  }

  /**
   * @param newFileState the new state of the attached file
   */
  public void setNewFileState(FileState newFileState) {
    this.newFileState = newFileState;
  }



}
