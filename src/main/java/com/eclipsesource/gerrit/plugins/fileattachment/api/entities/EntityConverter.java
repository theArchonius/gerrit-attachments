/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

/**
 * Represents a converter that is able to convert an object O to a specific
 * {@link JsonEntity} and vice versa.
 * 
 * @author Florian Zoubek
 *
 */
public interface EntityConverter<O, J> extends EntityWriter<O, J>, EntityReader<O, J> {
}
