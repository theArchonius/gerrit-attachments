/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter;

import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResultType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity.ResultStatus;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericOperationResult;

/**
 * An entity reader which creates {@link OperationResult}s from
 * {@link OperationResultEntity}s.
 * 
 * @author Florian Zoubek
 *
 */
public class BaseOperationResultReader implements
    EntityReader<OperationResult, OperationResultEntity, Object> {

  @Override
  public OperationResult toObject(OperationResultEntity jsonEntity,
      Object context) {
    
    OperationResultType resultType = OperationResultType.SUCCESS;
    
    if(jsonEntity.getResultStatus() == ResultStatus.FAILED){
      resultType = OperationResultType.FAILED;
    }else if (jsonEntity.getResultStatus() == ResultStatus.NOTPERMITTED){
      resultType = OperationResultType.NOTPERMITTED;
    }
    
    return new GenericOperationResult(resultType, jsonEntity.getStatusMessage());
  }
}
