package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;


/**
 * Represents a class that is able to convert an object O to a specific
 * {@link JsonEntity} J using the context C.
 *
 * @param <O> the {@link Object} type to convert from
 * @param <J> the {@link JsonEntity} type to convert to
 * @param <C> the type of the context used during conversion from the
 *        {@link Object} type to the {@link JsonEntity} type
 *
 * @author Florian Zoubek
 *
 */
public interface EntityWriter<O, J extends JsonEntity, C> {

  // TODO add exceptions
  /**
   * converts the given object to a {@link JsonEntity}
   *
   * @param object the object to convert
   * @param context the context used to extract additional data during writing
   * @return the {@link JsonEntity} instance, or null if the conversion was not
   *         possible
   */
  public abstract J toEntity(O object, C context);

}
