/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * MessageBodyReader that reads JSON Entities from a stream using Gson
 * 
 * @author Florian Zoubek
 *
 */
public class EntityMessageBodyReader<T> implements MessageBodyReader<T> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  public boolean isReadable(Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType) {
    return mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.InputStream)
   */
  @Override
  public T readFrom(Class<T> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
      InputStream entityStream) throws IOException, WebApplicationException {
    Gson gson =
        new GsonBuilder().setFieldNamingPolicy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    // Note: this naming policy is required by gerrit


    Charset charset = StandardCharsets.UTF_8;
    Map<String, String> parameters = mediaType.getParameters();
    if (parameters.containsKey("charset")) {
      charset = Charset.forName(parameters.get("charset"));
    }

    return gson.fromJson(new InputStreamReader(entityStream, charset), type);
  }

}
