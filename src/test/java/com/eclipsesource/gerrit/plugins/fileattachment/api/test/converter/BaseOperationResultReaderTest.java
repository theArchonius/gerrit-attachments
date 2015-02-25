/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.converter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResultType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity.ResultStatus;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter.BaseOperationResultReader;

/**
 * @author Florian Zoubek
 *
 */
public class BaseOperationResultReaderTest {

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
    Map<ResultStatus, String> messages = new HashMap<ResultStatus, String>();

    messages.put(ResultStatus.FAILED, "An error occured");
    messages.put(ResultStatus.NOTPERMITTED, "Operation is not permitted");
    messages.put(ResultStatus.SUCCESS, "");

    return Collections.unmodifiableMap(messages);
  }

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter.BaseOperationResultReader#toObject(com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity, java.lang.Object)}
   * .
   */
  @Test
  public void testToObject() {

    BaseOperationResultReader baseOperationResultReader =
        new BaseOperationResultReader();

    for (ResultStatus resultStatus : ResultStatus.values()) {

      OperationResultType expectedOperationResultType =
          RESULTSTATUS_TO_OPERATIONRESULTTYPE_MAP.get(resultStatus);
      String expectedMessagePart = MESSAGES_MAP.get(resultStatus);

      OperationResultEntity jsonEntity =
          new OperationResultEntity(resultStatus,
              MESSAGES_MAP.get(resultStatus));

      OperationResult operationResult =
          baseOperationResultReader.toObject(jsonEntity, null);

      // check result type mapping
      assertThat(operationResult.getResultType(),
          is(expectedOperationResultType));
      // check if the status message contains the message of the entity
      assertThat(operationResult.getStatusMessage(),
          containsString(expectedMessagePart));
    }
  }

}
