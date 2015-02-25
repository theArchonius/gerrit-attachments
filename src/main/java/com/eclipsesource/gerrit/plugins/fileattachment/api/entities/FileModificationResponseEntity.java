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
        prime * result + ((newFileState == null) ? 0 : newFileState.hashCode());
    result =
        prime * result
            + ((operationResult == null) ? 0 : operationResult.hashCode());
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
    FileModificationResponseEntity other = (FileModificationResponseEntity) obj;
    if (newFileState != other.newFileState) return false;
    if (operationResult == null) {
      if (other.operationResult != null) return false;
    } else if (!operationResult.equals(other.operationResult)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FileModificationResponseEntity [operationResult=" + operationResult
        + ", newFileState=" + newFileState + "]";
  }

}
