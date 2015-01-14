/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

/**
 * Represents a REST Endpoint
 *  
 * @author Florian Zoubek
 *
 */
public interface RestEndpoint {

  /**
   * 
   * @return the relative path of the rest endpoint for this target.
   */
  public String getPath();
}
