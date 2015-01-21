/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity;

/**
 * An entity reader which creates {@link OperationResult}s from
 * {@link FileModificationResponseEntity}s.
 * 
 * @author Florian Zoubek
 *
 */
public class BaseFileModificationResponseReader implements
    EntityReader<OperationResult, FileModificationResponseEntity> {

  @Override
  public OperationResult toObject(FileModificationResponseEntity jsonEntity) {

    String errorMessage = "";
    OperationResultType resultType = OperationResultType.FAILED;

    String sResult = jsonEntity.getOperationResult();

    if (sResult.equalsIgnoreCase("SUCCESS")) {
      resultType = OperationResultType.SUCCESS;
    } else if (sResult.equalsIgnoreCase("FAILED")) {
      resultType = OperationResultType.FAILED;
    } else if (sResult.equalsIgnoreCase("NOTPERMITTED")) {
      resultType = OperationResultType.NOTPERMITTED;
    }

    errorMessage = jsonEntity.getErrorMessage();

    return new GenericOperationResult(resultType, errorMessage);
  }

}
