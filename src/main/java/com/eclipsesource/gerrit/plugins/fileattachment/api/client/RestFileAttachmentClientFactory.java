/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import java.net.URI;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.converter.BaseAttachmentTargetResponseEntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.converter.BaseFileEntityWriter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.converter.BaseFileModificationResponseReader;

/**
 * Default implementation of the {@link FileAttachmentClientFactory}
 * 
 * @author Florian Zoubek
 *
 */
public class RestFileAttachmentClientFactory implements
    FileAttachmentClientFactory {

  private URI restRoot;

  private String username;

  private byte[] password;

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * FileAttachmentClientFactory#createFileAttachmentClientService()
   */
  @Override
  public FileAttachmentClientService createFileAttachmentClientService() {
    RestFileAttachmentClientService service =
        new RestFileAttachmentClientService(restRoot,
            new DefaultRestEndpointRegistry(),
            new BaseFileModificationResponseReader(),
            new BaseAttachmentTargetResponseEntityReader(), new BaseFileEntityWriter(),
            username, password);
    return service;
  }

}
