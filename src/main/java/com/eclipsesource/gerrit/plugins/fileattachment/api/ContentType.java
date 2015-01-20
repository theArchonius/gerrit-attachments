/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api;

import java.util.Map;

/**
 * Represents the content type of a file or any other chunk of data
 *
 * @author Florian Zoubek
 *
 */
public interface ContentType {

  /**
   * Constructs a RFC2045 compliant content type value for the current content type
   *
   * @return the Content type as defined in RFC2045 Chapter 5 and RFC2046
   *
   * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC2045</a>
   * @see <a href="http://www.ietf.org/rfc/rfc2046.txt">RFC2046</a>
   */
  public String getContentTypeIdentifier();

  /**
   *
   * @return a map of parameter and value pairs of this content type
   */
  public Map<String, String> getParameters();

  /**
   *
   * @return true if the media type describes a binary encoded media, false if the media format is text encoded media
   */
  public boolean isBinary();

}
