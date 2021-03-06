/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
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

  /**
   * Constructs the content type by parsing a RFC2045 (Chapter 5) compliant
   * content type value
   *
   * @param contentTypeIdentifier a RFC2045 (Chapter 5) compliant content type
   *        value
   */
  public GenericContentType(String contentTypeIdentifier) {
    this(contentTypeIdentifier, !contentTypeIdentifier.trim().startsWith("text"));
  }

  /**
   * Constructs the content type by parsing a RFC2045 (Chapter 5) compliant
   * content type value forces the binary property to have the given value
   *
   * @param contentTypeIdentifier a RFC2045 (Chapter 5) compliant content type
   *        value
   * @param binary the value of the binary property to set
   */
  public GenericContentType(String contentTypeIdentifier, boolean binary) {

    // extract the type, subtype and parameter strings
    int typeDelimiterPos = contentTypeIdentifier.indexOf('/');
    if (typeDelimiterPos < 0) {
      throw new IllegalArgumentException(
          MessageFormat
              .format(
                  "Could not parse content type value - could not find type delimiter \"/\" in identifier \"{0}\"",
                  contentTypeIdentifier));
    }

    type = contentTypeIdentifier.substring(0, typeDelimiterPos);
    subtype = contentTypeIdentifier.substring(typeDelimiterPos + 1);

    int parameterDelimiterPos = subtype.indexOf(';');
    if (parameterDelimiterPos >= 0) {
      String parameterString = subtype.substring(parameterDelimiterPos + 1);
      subtype = subtype.substring(0, parameterDelimiterPos);

      parseParameters(parameterString, this.parameters);
    }


    this.binary = binary;

  }

  /**
   * parses the parameter name & value pairs form the parameter part of a
   * content type string
   *
   * @param parameterString the parameter string of a content type (without the
   *        prefix {@code "<subtype>/<type>;"} )
   * @param map the map to write the parameters to, existing parameters will be
   *        overriden
   */
  private static void parseParameters(String parameterString,
      Map<String, String> map) {
    // parse parameter string to extract the parameters and their values
    // simple splitting is not possible as separator chars in quoted strings
    // should be ignored

    StringBuilder sbName = new StringBuilder();
    StringBuilder sbValue = new StringBuilder();

    // true if we parse a parameter name
    boolean isParam = true;
    // true if we are inside a quoted string
    boolean isQuoted = false;

    // loop through all characters and build the parameter name and values based
    // on the current state and update the state if necessary
    for (int i = 0; i < parameterString.length(); i++) {

      char currentChar = parameterString.charAt(i);
      if (currentChar == '=' && !isQuoted) {
        // parameter name ends here update state for value parsing
        isParam = false;

      } else if (currentChar == ';' && !isQuoted) {
        // parameter value ends here, update state for parameter name parsing
        isParam = true;
        // parameter name & value pair has been found, add it to the map
        map.put(sbName.toString(), sbValue.toString());
        sbName.setLength(0);
        sbValue.setLength(0);

      } else if (currentChar == '"') {
        // we pass an quotation mark, update state accordingly
        isQuoted = !isQuoted;
        if (isParam) {
          sbName.append(currentChar);
        } else {
          sbValue.append(currentChar);
        }

      } else if (isParam) {
        // character is part of the parameter name
        sbName.append(currentChar);

      } else {
        // character is part of the parameter value
        sbValue.append(currentChar);
      }
    }

    // the last parameter value does not need to be marked with a special
    // character, so add the last parameter name and value if it exists
    if (sbName.length() > 0) {
      map.put(sbName.toString().trim(), sbValue.toString().trim());
    }
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (binary ? 1231 : 1237);
    result =
        prime * result + ((parameters == null) ? 0 : parameters.hashCode());
    result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    GenericContentType other = (GenericContentType) obj;
    if (binary != other.binary) return false;
    if (parameters == null) {
      if (other.parameters != null) return false;
    } else if (!parameters.equals(other.parameters)) return false;
    if (subtype == null) {
      if (other.subtype != null) return false;
    } else if (!subtype.equals(other.subtype)) return false;
    if (type == null) {
      if (other.type != null) return false;
    } else if (!type.equals(other.type)) return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "GenericContentType [type=" + type + ", subtype=" + subtype
        + ", binary=" + binary + ", parameters=" + parameters + "]";
  }

}
