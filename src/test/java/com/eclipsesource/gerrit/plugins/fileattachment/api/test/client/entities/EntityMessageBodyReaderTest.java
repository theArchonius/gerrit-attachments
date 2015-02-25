/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.client.entities;

import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyReader;

/**
 * @author Florian Zoubek
 *
 */
public class EntityMessageBodyReaderTest {

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
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyReader#isReadable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)}
   * .
   */
  @Test
  public void testIsReadable() {
    EntityMessageBodyReader<String> entityMessageBodyReader =
        new EntityMessageBodyReader<String>();

    for (MediaType mediaType : VALID_MEDIA_TYPES) {
      assertThat(entityMessageBodyReader.isReadable(String.class, null,
          new Annotation[0], mediaType), CoreMatchers.is(true));
    }

    for (MediaType mediaType : INVALID_MEDIA_TYPES) {
      assertThat(entityMessageBodyReader.isReadable(String.class, null,
          new Annotation[0], mediaType), CoreMatchers.is(false));
    }
  }

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.EntityMessageBodyReader#readFrom(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.InputStream)}
   * .
   * 
   * @throws IOException
   * @throws WebApplicationException
   */
  @Test
  public void testReadFrom() throws WebApplicationException, IOException {
    EntityMessageBodyReader<TestObject> entityMessageBodyReader =
        new EntityMessageBodyReader<TestObject>();

    MultivaluedHashMap<String, String> httpHeaders =
        new MultivaluedHashMap<String, String>();

    int id = 123;
    String text = "Some test string";

    String SerializedTestObject =
        ")]}'\n" + "{" + "\"id\":\"" + id + "\"," + "\"text\":\"" + text
            + "\"" + "}";

    InputStream inputStream =
        new ByteArrayInputStream(
            SerializedTestObject.getBytes(StandardCharsets.UTF_8));

    TestObject testObj =
        entityMessageBodyReader.readFrom(TestObject.class, null,
            new Annotation[0], MediaType.APPLICATION_JSON_TYPE, httpHeaders,
            inputStream);

    assertThat(testObj.id, CoreMatchers.is(id));
    assertThat(testObj.text, CoreMatchers.is(text));
  }

  /**
   * class that is used to test deserialization
   * 
   * @author Florian Zoubek
   *
   */
  static class TestObject {
    public int id;
    public String text;
  }

}
