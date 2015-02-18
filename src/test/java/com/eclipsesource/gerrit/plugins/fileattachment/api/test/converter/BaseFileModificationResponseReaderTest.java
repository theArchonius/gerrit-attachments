/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResultType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity.FileState;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity.ResultStatus;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter.BaseFileModificationResponseReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericOperationResult;

/**
 * @author Florian Zoubek
 *
 */
public class BaseFileModificationResponseReaderTest {

  /**
   * A map containing the expected mapping of {@link ResultStatus} values to
   * {@link OperationResultType} values
   */
  private static final Map<ResultStatus, OperationResultType> RESULTSTATUS_TO_OPERATIONRESULTTYPE_MAP =
      createOperationResultTypeMap();

  private static Map<ResultStatus, OperationResultType> createOperationResultTypeMap() {
    Map<ResultStatus, OperationResultType> operationResultTypeMap =
        new HashMap<ResultStatus, OperationResultType>();

    operationResultTypeMap.put(ResultStatus.FAILED, OperationResultType.FAILED);
    operationResultTypeMap.put(ResultStatus.NOTPERMITTED,
        OperationResultType.NOTPERMITTED);
    operationResultTypeMap.put(ResultStatus.SUCCESS,
        OperationResultType.SUCCESS);

    return Collections.unmodifiableMap(operationResultTypeMap);
  }

  /**
   * A map containing messages for specific {@link ResultStatus}
   */
  private static final Map<ResultStatus, String> MESSAGES_MAP =
      createMessagesMap();


  private static Map<ResultStatus, String> createMessagesMap() {
    Map<ResultStatus, String> messages =
        new HashMap<OperationResultEntity.ResultStatus, String>();

    messages.put(ResultStatus.FAILED, "An error occured");
    messages.put(ResultStatus.NOTPERMITTED, "Operation is not permitted");
    messages.put(ResultStatus.SUCCESS, "");

    return Collections.unmodifiableMap(messages);
  }


  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter.BaseFileModificationResponseReader#toObject(com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity, java.lang.Object)}
   * .
   */
  @Test
  public void testToObject() {
    BaseFileModificationResponseReader baseFileModificationResponseReader =
        new BaseFileModificationResponseReader();

    for (ResultStatus resultStatus : ResultStatus.values()) {

      for (FileState newFileState : FileState.values()) {

        OperationResultEntity operationResultEntity =
            new OperationResultEntity(resultStatus, "");
        FileModificationResponseEntity jsonEntity =
            new FileModificationResponseEntity(operationResultEntity,
                newFileState);

        OperationResult expectedOperationResult =
            new GenericOperationResult(
                RESULTSTATUS_TO_OPERATIONRESULTTYPE_MAP.get(resultStatus),
                MESSAGES_MAP.get(resultStatus));

        OperationResult operationResult =
            baseFileModificationResponseReader.toObject(jsonEntity, null);

        assertThat(operationResult, is(expectedOperationResult));

      }
    }

  }
}
