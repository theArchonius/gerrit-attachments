package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a converter that is able to convert an {@link JsonEntity} J to a
 * specific object O.
 * 
 * @author Florian Zoubek
 *
 */
public interface EntityReader<O, J> {

  /**
   * converts the given {@link JsonEntity} to an object
   * 
   * @param jsonEntity the {@link JsonEntity} to convert
   * @return the object instance
   */
  public abstract O toObject(J jsonEntity);

}
