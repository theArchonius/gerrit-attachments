/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api;

/**
 * Represents a REST Endpoint
 *  
 * @author Florian Zoubek
 *
 */
public interface RestEndpoint {

  /**
   * 
   * @return the relative path of the rest endpoint for this target, without leading or trailing slashes
   */
  public String getPath();
}
