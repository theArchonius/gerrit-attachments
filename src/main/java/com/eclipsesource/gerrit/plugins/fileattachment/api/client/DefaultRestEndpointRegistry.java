/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.UnsupportedFileOperationException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.ChangeTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.GenericRestEndpoint;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.PatchSetTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.PatchTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.RestEndpoint;

/**
 * The default {@link AttachmentTargetRestEndpointRegistry} which currently
 * supports the following targets:
 * 
 * <ul>
 * <li>{@link PatchTargetDescription}</li>
 * </ul>
 * 
 * @author Florian Zoubek
 *
 */
public class DefaultRestEndpointRegistry implements
    AttachmentTargetRestEndpointRegistry {

  /**
   * the plugin name used to create last part of the REST endpoint name (plugin
   * endpoints in Gerrit follow the convention path/pluginname~endpointname)
   */
  private static final String PLUGIN_NAME = "fileattachment";

  /**
   * the endpoint name for operations with or on a single file attachment
   */
  private static final String FILE_ENDPOINT_NAME = "file";

  private static final Map<String, String> OPERATION_TO_ENDPOINT_MAP =
      createOperationToEndpointMap();

  private static Map<String, String> createOperationToEndpointMap() {
    Map<String, String> map = new HashMap<>();
    map.put(FileAttachmentClientService.OPERATION_ATTACH_FILE,
        FILE_ENDPOINT_NAME);
    map.put(FileAttachmentClientService.OPERATION_DELETE_FILE,
        FILE_ENDPOINT_NAME);
    map.put(FileAttachmentClientService.OPERATION_GET_FILE, FILE_ENDPOINT_NAME);
    map.put(FileAttachmentClientService.OPERATION_GET_TARGET,
        FILE_ENDPOINT_NAME);
    return map;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * AttachmentTargetRestEndpointRegistry
   * #getRestEndpoint(com.eclipsesource.gerrit
   * .plugins.fileattachment.api.AttachmentTarget)
   */
  @Override
  public RestEndpoint getRestEndpoint(AttachmentTarget target, String operation) {

    // TODO implement and update javadoc class description
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * AttachmentTargetRestEndpointRegistry
   * #getRestEndpoint(com.eclipsesource.gerrit
   * .plugins.fileattachment.api.AttachmentTargetDescription)
   */
  @Override
  public RestEndpoint getRestEndpoint(AttachmentTargetDescription target,
      String operation) throws UnsupportedFileOperationException,
      FileAttachmentClientException {

    // Don't forget to update the the class description in the javadoc after
    // adding support of a new AttachmentTargetDescripton!

    // retrieve the endpoint name
    String endpointName = OPERATION_TO_ENDPOINT_MAP.get(operation);

    if (endpointName == null) {
      // no endpoint found for the operation -> operation is not supported
      throw new UnsupportedFileOperationException(
          operation,
          MessageFormat
              .format(
                  "The operation {0} is currently not supported by the target endpoint registry",
                  operation));
    }
    // build the full endpoint name
    endpointName = PLUGIN_NAME + "~" + endpointName;

    if (target instanceof PatchTargetDescription) {
      PatchTargetDescription patchTargetDescription =
          (PatchTargetDescription) target;
      PatchSetTargetDescription patchSetTargetDescription =
          patchTargetDescription.getPatchSetTargetDescription();
      ChangeTargetDescription changeTargetDescription =
          patchSetTargetDescription.getChangeTargetDescription();

      return new GenericRestEndpoint("a", "changes",
          changeTargetDescription.getChangeId(), "revisions",
          patchSetTargetDescription.getId() + "", "files",
          patchTargetDescription.getPatchUrl(), endpointName);

    }
    return null;
  }
}
