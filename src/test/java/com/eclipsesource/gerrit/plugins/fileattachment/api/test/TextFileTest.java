package com.eclipsesource.gerrit.plugins.fileattachment.api.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericFileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.TextFile;

/**
 * Basic tests for {@link TextFile} methods and instances
 * 
 * @author Florian Zoubek
 *
 */
public class TextFileTest {

  /**
   * the resources used as text content during testing, all resources must be
   * UTF-8 encoded
   */
  private static final String[] TEST_RESOURCES = {"/text/loremIpsum.txt"};

  /**
   * the base content type of the entries in {@link #TEST_RESOURCES}
   */
  private static final String[] TEST_RESOURCES_TYPES = {"txt"};

  /**
   * the content subtype of the entries in {@link #TEST_RESOURCES}
   */
  private static final String[] TEST_RESOURCES_SUBTYPES = {"plain"};

  /**
   * a list of encodings to test
   */
  private static final Charset[] ENCODINGS_TO_TEST = {
      StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII,
      StandardCharsets.UTF_16, StandardCharsets.UTF_16BE,
      StandardCharsets.UTF_16LE, StandardCharsets.UTF_8};

  /**
   * the list of loaded text content of the entries in {@link #TEST_RESOURCES}
   */
  private static String[] contents;

  @Before
  public void setUp() {

    contents = new String[TEST_RESOURCES.length];

    // load the contents of the text resources
    for (int i = 0; i < TEST_RESOURCES.length; i++) {

      String resource = TEST_RESOURCES[i];
      InputStream resourceInputStream =
          TextFileTest.class.getResourceAsStream(resource);
      if (resourceInputStream != null) {

        InputStreamReader resourceReader =
            new InputStreamReader(resourceInputStream, StandardCharsets.UTF_8);
        StringBuffer resourceContent = new StringBuffer();
        char[] cBuffer = new char[256];

        // read the content
        try {
          int readChars = resourceReader.read(cBuffer, 0, cBuffer.length);
          while (readChars != -1) {
            resourceContent.append(cBuffer, 0, readChars);
            readChars = resourceReader.read(cBuffer, 0, cBuffer.length);
          }
        } catch (IOException e) {
          // we have to ensure that tests won't succeed if an IO error occurs
          throw new RuntimeException(MessageFormat.format(
              "Error during content loading of resource \"{0}\"", resource));
        } finally {
          try {
            resourceInputStream.close();
          } catch (IOException e) {
            // we have to ensure that tests won't succeed if an IO error occurs
            throw new RuntimeException(MessageFormat.format(
                "IO Error Could not close input stream of resource \"{0}\"",
                resource));
          }
        }

        contents[i] = resourceContent.toString();

      } else {
        // we have to ensure that tests won't succeed if the resource could not
        // be found for whatever reason
        throw new RuntimeException(MessageFormat.format(
            "Could not find resource \"{0}\"", resource));
      }
    }

  }

  /**
   * Tests proper encoding handling of {@link TextFile#getContent()} .
   */
  @Test
  public void testEncodingOfGetContent() {

    FileDescription fileDescription =
        new GenericFileDescription("somePath", "someFile");

    for (Charset charset : ENCODINGS_TO_TEST) {

      for (int i = 0; i < contents.length; i++) {

        String content = contents[i];
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("charset", charset.name());
        ContentType contentType =
            new GenericContentType(TEST_RESOURCES_TYPES[i],
                TEST_RESOURCES_SUBTYPES[i], parameters);
        File fileToTest = new TextFile(fileDescription, contentType, content);

        byte[] correctContent = content.getBytes(charset);
        byte[] actualContent = fileToTest.getContent();

        assertThat(
            MessageFormat.format(
                "Encoded content bytes does not match the expected content bytes for charset {0}",
                charset.name()), actualContent, is(correctContent));
      }
    }
  }
}
