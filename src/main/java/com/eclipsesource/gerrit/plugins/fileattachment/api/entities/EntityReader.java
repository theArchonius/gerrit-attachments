package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a converter that is able to convert an {@link JsonEntity} J to a
 * specific object O using a context C.
 *
 * @param <O> the {@link Object} type to convert to
 * @param <J> the {@link JsonEntity} type to convert from
 * @param <C> the type of the context used during conversion from the
 *        {@link JsonEntity} type to the {@link Object} type
 *
 * @author Florian Zoubek
 *
 */
public interface EntityReader<O, J extends JsonEntity, C> {


  // TODO add exceptions
  /**
   * converts the given {@link JsonEntity} to an object
   *
   * @param jsonEntity the {@link JsonEntity} to convert
   * @param context the context used to extract additional data during reading
   * @return the object instance or null if the conversion was not possible
   */
  public abstract O toObject(J jsonEntity, C context);



}
