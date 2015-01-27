/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;

/**
 * A simple {@link ContentType} implementation that allows direct specification
 * of the properties of {@link ContentType}. Specifies also some shortcut
 * constants for often used content types.
 * 
 * @author Florian Zoubek
 *
 */
public class GenericContentType implements ContentType {

  /*
   * Shortcut constants for common content types 
   */

  /* content types with the system's default charset */

  /**
   * plain text without with charset set to the system default charset
   */
  public static final ContentType PLAIN_TEXT = new GenericContentType("text",
      "plain", addCharsetToParameterMap(new HashMap<String, String>(),
          Charset.defaultCharset()));

  /**
   * JSON content type with charset set to the system default charset
   */
  public static final ContentType JSON = new GenericContentType("application",
      "json", false, addCharsetToParameterMap(new HashMap<String, String>(),
          Charset.defaultCharset()));

  /**
   * XML content type (base type: text) with charset set to the system default
   * charset
   */
  public static final ContentType XML_TEXT = new GenericContentType("text",
      "xml", false, addCharsetToParameterMap(new HashMap<String, String>(),
          Charset.defaultCharset()));

  /**
   * XML content type (base type: application) with charset set to the system
   * default charset
   */
  public static final ContentType XML_APPLICATION = new GenericContentType(
      "application", "xml", false, addCharsetToParameterMap(
          new HashMap<String, String>(), Charset.defaultCharset()));

  /* content types with UTF 8 charset */

  /**
   * plain text in UTF 8
   */
  public static final ContentType PLAIN_TEXT_UTF8 = new GenericContentType(
      "text", "plain", addCharsetToParameterMap(new HashMap<String, String>(),
          StandardCharsets.UTF_8));
  /**
   * JSON content type with charset set to UTF 8
   */
  public static final ContentType JSON_UTF8 = new GenericContentType(
      "application", "json", false, addCharsetToParameterMap(
          new HashMap<String, String>(), StandardCharsets.UTF_8));

  /**
   * XML content type (base type: text) with charset set to UTF 8
   */
  public static final ContentType XML_TEXT_UTF8 = new GenericContentType(
      "text", "xml", addCharsetToParameterMap(new HashMap<String, String>(),
          StandardCharsets.UTF_8));

  /**
   * XML content type (base type: application) with charset set to UFT 8
   */
  public static final ContentType XML_APPLICATION_UTF8 =
      new GenericContentType("application", "xml", false,
          addCharsetToParameterMap(new HashMap<String, String>(),
              StandardCharsets.UTF_8));

  /**
   * adds the charset parameter to the passed parameter map that can be used in
   * a fluent style.
   * 
   * @param map the map to modify
   * @param charset the charset to add
   * @return the modified map
   */
  private static Map<String, String> addCharsetToParameterMap(
      Map<String, String> map, Charset charset) {
    map.put("charset", charset.name());
    return map;
  }

  /* 
   * Implementation 
   */

  private String type;

  private String subtype;

  private boolean binary = false;

  private Map<String, String> parameters = new HashMap<String, String>();

  /**
   * constructs the content type and guesses the binary property based on the
   * type (currently: binary = type != "text")
   * 
   * @param type the base type, must not be null
   * @param subtype the subtype must not be null
   * @param parameters a map of parameters, the entries of the map are copied,
   *        so you can safely modify the passed parameter map afterwards without
   *        modifying the parameters of this content type
   * 
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public GenericContentType(String type, String subtype,
      Map<String, String> parameters) {
    if (type == null) {
      throw new IllegalArgumentException("A base type must be specified");
    }
    if (subtype == null) {
      throw new IllegalArgumentException("A subtype must be specified");
    }
    this.type = type;
    this.subtype = subtype;
    this.binary = !type.equalsIgnoreCase("text");
    this.parameters.putAll(parameters);
  }

  /**
   * 
   * @param type the base type, must not be null
   * @param subtype the subtype must not be null
   * @param binary a boolean indicating the content of the content type is
   *        binary encoded or text
   * @param parameters a map of parameters, the entries of the map are copied,
   *        so you can safely modify the passed parameter map afterwards without
   *        modifying the parameters of this content type
   * 
   * @throws IllegalArgumentException if the previously mentioned conditions are
   *         not met
   */
  public GenericContentType(String type, String subtype, boolean binary,
      Map<String, String> parameters) {
    if (type == null) {
      throw new IllegalArgumentException("A base type must be specified");
    }
    if (subtype == null) {
      throw new IllegalArgumentException("A subtype must be specified");
    }
    this.type = type;
    this.subtype = subtype;
    this.binary = binary;
    this.parameters.putAll(parameters);
  }

  @Override
  public String getContentTypeIdentifier() {
    StringBuilder contentTypeId = new StringBuilder();

    contentTypeId.append(type);
    contentTypeId.append("/");
    contentTypeId.append(subtype);

    if (!parameters.isEmpty()) {

      for (String key : parameters.keySet()) {
        contentTypeId.append("; ");
        contentTypeId.append(key);
        contentTypeId.append("=");
        contentTypeId.append(parameters.get(key));
      }
    }

    return contentTypeId.toString();
  }

  @Override
  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  @Override
  public boolean isBinary() {
    return binary;
  }

}
