/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.model;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;

/**
 * A simple implementation of {@link RestEndpoint} that allows direct
 * specification of its properties.
 * 
 * @author Florian Zoubek
 *
 */
public class GenericRestEndpoint implements RestEndpoint {

  private String path;

  /**
   * 
   * @param path the complete and already encoded path
   */
  public GenericRestEndpoint(String path) {
    this.path = path;
  }

  /**
   * 
   * @param pathElements the elements of the path, each element will be URL
   *        encoded
   * @throws FileAttachmentClientException if an Exception occurs during URL
   *         encoding
   */
  public GenericRestEndpoint(String... pathElements)
      throws FileAttachmentClientException {
    URLCodec urlCodec = new URLCodec();
    StringBuilder sb = new StringBuilder();

    for (String element : pathElements) {
      try {
        sb.append(urlCodec.encode(element));
      } catch (EncoderException e) {
        throw new FileAttachmentClientException(
            "Error occured during URL encoding.", e);
      }
    }

    path = sb.toString();
  }

  @Override
  public String getPath() {
    return path;
  }

}
