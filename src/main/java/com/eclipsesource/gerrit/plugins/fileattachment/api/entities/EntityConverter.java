/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a converter that is able to convert an object O to a specific
 * {@link JsonEntity} and vice versa unsing the writing context CW and the
 * reading context CR.
 * 
 * @param <O> the {@link Object} type to convert to or from
 * @param <J> the {@link JsonEntity} type to convert to or from
 * @param <CW> the type of the context used during conversion from the
 *        {@link Object} type to the {@link JsonEntity} type
 * @param <CR> the type of the context used during conversion from the
 *        {@link JsonEntity} type to the {@link Object} type
 * 
 * @author Florian Zoubek
 *
 */
public interface EntityConverter<O, J extends JsonEntity, CW, CR> extends
    EntityWriter<O, J, CW>, EntityReader<O, J, CR> {
}
