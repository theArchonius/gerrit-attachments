/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.eclipsesource.gerrit.plugins.fileattachment.api.RestEndpoint;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;

/**
 * A simple implementation of {@link RestEndpoint} that allows direct
 * specification of its properties.
 * 
 * @author Florian Zoubek
 *
 */
public class GenericRestEndpoint implements RestEndpoint {

  private static final char PATH_DELIMITER = '/';

  /**
   * the complete path without leading or trailing slashes
   */
  private String path;

  /**
   * 
   * @param path the complete and already encoded path, without leading or
   *        trailing slashes
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
        sb.append(PATH_DELIMITER);
      } catch (EncoderException e) {
        throw new FileAttachmentClientException(
            "Error occured during URL encoding.", e);
      }
    }
    sb.deleteCharAt(sb.length() - 1);

    path = sb.toString();
  }

  /**
   * appends the given pathElement to the path
   * 
   * @param pathElement the path element to append without any leading or
   *        trailing slashes
   * @return the {@link GenericRestEndpoint} itself for convenience
   */
  public GenericRestEndpoint append(String pathElement) {
    path += PATH_DELIMITER + pathElement;
    return this;
  }

  /**
   * encodes the given pathElement and then appends it to the path
   * 
   * @param pathElement the path element to append without any leading or
   *        trailing slashes
   * @return the {@link GenericRestEndpoint} itself for convenience
   * @throws FileAttachmentClientException if an Exception occurs during URL
   *         encoding
   */
  public GenericRestEndpoint appendEncoded(String pathElement)
      throws FileAttachmentClientException {
    try {
      path += PATH_DELIMITER + new URLCodec().encode(pathElement);
    } catch (EncoderException e) {
      throw new FileAttachmentClientException(
          "Error occured during URL encoding.", e);
    }
    return this;
  }


  @Override
  public String getPath() {
    return path;
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
    result = prime * result + ((path == null) ? 0 : path.hashCode());
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
    GenericRestEndpoint other = (GenericRestEndpoint) obj;
    if (path == null) {
      if (other.path != null) return false;
    } else if (!path.equals(other.path)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "GenericRestEndpoint [path=" + path + "]";
  }

}
