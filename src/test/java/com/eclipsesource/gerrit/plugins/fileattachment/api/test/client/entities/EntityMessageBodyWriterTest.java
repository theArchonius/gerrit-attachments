/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.client.entities;

import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyWriter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Florian Zoubek
 *
 */
public class EntityMessageBodyWriterTest {

  private static final MediaType[] VALID_MEDIA_TYPES =
      new MediaType[] {MediaType.APPLICATION_JSON_TYPE};

  /**
   * a small collection of not readable media types by EntityMessageBodyReader
   */
  private static final MediaType[] INVALID_MEDIA_TYPES = new MediaType[] {
      MediaType.APPLICATION_ATOM_XML_TYPE, MediaType.TEXT_PLAIN_TYPE,
      MediaType.APPLICATION_OCTET_STREAM_TYPE,};

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyWriter#getSize(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)}
   * .
   */
  @Test
  public void testGetSize() {
    EntityMessageBodyWriter<String> entityMessageBodyWriter =
        new EntityMessageBodyWriter<String>();

    assertThat(entityMessageBodyWriter.getSize("", String.class, null,
        new Annotation[0], MediaType.APPLICATION_JSON_TYPE),
        CoreMatchers.is((long) -1));
  }

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)}
   * .
   */
  @Test
  public void testIsWriteable() {
    EntityMessageBodyWriter<String> entityMessageBodyWriter =
        new EntityMessageBodyWriter<String>();

    for (MediaType mediaType : VALID_MEDIA_TYPES) {
      assertThat(entityMessageBodyWriter.isWriteable(String.class, null,
          new Annotation[0], mediaType), CoreMatchers.is(true));
    }

    for (MediaType mediaType : INVALID_MEDIA_TYPES) {
      assertThat(entityMessageBodyWriter.isWriteable(String.class, null,
          new Annotation[0], mediaType), CoreMatchers.is(false));
    }
  }

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyWriter#writeTo(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)}
   * . Serialization is performed with only with the default charset obtained by
   * {@link Charset#defaultCharset()}.
   * 
   * @throws IOException
   * @throws WebApplicationException
   */
  @Test
  public void testWriteTo() throws WebApplicationException, IOException {
    EntityMessageBodyWriter<TestObject> entityMessageBodyWriter =
        new EntityMessageBodyWriter<TestObject>();

    MultivaluedHashMap<String, Object> httpHeaders =
        new MultivaluedHashMap<String, Object>();

    TestObject testObject = new TestObject();
    testObject.id = 123;
    testObject.text = "Some test string";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    entityMessageBodyWriter.writeTo(testObject, TestObject.class, null,
        new Annotation[0], MediaType.APPLICATION_JSON_TYPE, httpHeaders,
        outputStream);

    Gson gson =
        new GsonBuilder().setFieldNamingPolicy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    TestObject result =
        gson.fromJson(new InputStreamReader(new ByteArrayInputStream(
            outputStream.toByteArray()), Charset.defaultCharset()),
            TestObject.class);

    assertThat(result, CoreMatchers.is(testObject));

  }

  /**
   * class that is used to test serialization
   * 
   * @author Florian Zoubek
   *
   */
  static class TestObject {
    public int id;
    public String text;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + id;
      result = prime * result + ((text == null) ? 0 : text.hashCode());
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
      TestObject other = (TestObject) obj;
      if (id != other.id) return false;
      if (text == null) {
        if (other.text != null) return false;
      } else if (!text.equals(other.text)) return false;
      return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return "TestObject [id=" + id + ", text=" + text + "]";
    }

  }

}
