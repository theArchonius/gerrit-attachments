package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;


/**
 * Represents a class that is able to convert an object O to a specific
 * {@link JsonEntity} J.
 * 
 * @author Florian Zoubek
 *
 */
public interface EntityWriter<O, J> {

  /**
   * converts the given object to a {@link JsonEntity}
   * 
   * @param object the object to convert
   * @return the {@link JsonEntity} instance
   */
  public abstract J toEntity(O object);

}
