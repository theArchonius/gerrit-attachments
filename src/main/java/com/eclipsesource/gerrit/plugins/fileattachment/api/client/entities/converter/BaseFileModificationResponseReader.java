/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.converter;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;

/**
 * An entity reader which creates {@link OperationResult}s from
 * {@link FileModificationResponseEntity}s.
 * 
 * @author Florian Zoubek
 *
 */
public class BaseFileModificationResponseReader implements
    EntityReader<OperationResult, FileModificationResponseEntity, Object> {

  private EntityReader<OperationResult, OperationResultEntity, Object> operationResultReader =
      new BaseOperationResultReader();

  @Override
  public OperationResult toObject(FileModificationResponseEntity jsonEntity,
      Object context) {
    return operationResultReader
        .toObject(jsonEntity.getOperationResult(), null);
  }

}
