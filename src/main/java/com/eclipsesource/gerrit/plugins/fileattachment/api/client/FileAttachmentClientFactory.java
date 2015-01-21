/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

/**
 * Base interface for file attachment client factories
 * 
 * @author Florian Zoubek
 *
 */
public interface FileAttachmentClientFactory {

  /**
   * creates an instance of {@link FileAttachmentClientService}
   * 
   * @return
   */
  public FileAttachmentClientService createFileAttachmentClientService();

}
