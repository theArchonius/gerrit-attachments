/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter.BaseFileEntityConverter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.BinaryFile;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.ChangeTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericFileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchSetTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.TextFile;
import com.eclipsesource.gerrit.plugins.fileattachment.api.test.TextFileTest;

/**
 * @author Florian Zoubek
 *
 */
public class BaseFileEntityConverterTest {

  // text resources
  /**
   * the resources used as text content during testing, all resources must be
   * UTF-8 encoded
   */
  private static final String[] TEST_TEXT_RESOURCES = {"/text/loremIpsum.txt"};

  /**
   * the list of loaded text content of the entries in
   * {@link #TEST_TEXT_RESOURCES}
   */
  private static String[] text_contents;

  /**
   * the base content type of the entries in {@link #TEST_TEXT_RESOURCES}
   */
  private static final String[] TEST_TEXT_RESOURCES_TYPES = {"text"};

  /**
   * the content subtype of the entries in {@link #TEST_TEXT_RESOURCES}
   */
  private static final String[] TEST_TEXT_RESOURCES_SUBTYPES = {"plain"};

  /**
   * a list of encodings to test
   */
  private static final Charset[] ENCODINGS_TO_TEST = {
      StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII,
      StandardCharsets.UTF_16, StandardCharsets.UTF_16BE,
      StandardCharsets.UTF_16LE, StandardCharsets.UTF_8};

  // binary test resources

  /**
   * the resources used as binary content during testing
   */
  private static final String[] TEST_BINARY_RESOURCES =
      {"/text/loremIpsum.txt"};

  /**
   * the list of loaded text content of the entries in
   * {@link #TEST_BINARY_RESOURCES}
   */
  private static byte[][] binary_contents;

  /**
   * the base content type of the entries in {@link #TEST_BINARY_RESOURCES}
   */
  private static final String[] TEST_BINARY_RESOURCES_TYPES = {"application"};

  /**
   * the content subtype of the entries in {@link #TEST_BINARY_RESOURCES}
   */
  private static final String[] TEST_BINARY_RESOURCES_SUBTYPES =
      {"octet-stream"};

  @Before
  public void setUp() {

    loadTextResources();
    loadBinaryResources();

  }

  private void loadTextResources() {
    text_contents = new String[TEST_TEXT_RESOURCES.length];

    // load the contents of the text resources
    for (int i = 0; i < TEST_TEXT_RESOURCES.length; i++) {

      String resource = TEST_TEXT_RESOURCES[i];
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

        text_contents[i] = resourceContent.toString();

      } else {
        // we have to ensure that tests won't succeed if the resource could not
        // be found for whatever reason
        throw new RuntimeException(MessageFormat.format(
            "Could not find resource \"{0}\"", resource));
      }
    }
  }

  private void loadBinaryResources() {
    binary_contents = new byte[TEST_BINARY_RESOURCES.length][];

    // load the contents of the text resources
    for (int i = 0; i < TEST_BINARY_RESOURCES.length; i++) {


      String resource = TEST_BINARY_RESOURCES[i];
      InputStream resourceInputStream =
          TextFileTest.class.getResourceAsStream(resource);
      if (resourceInputStream != null) {
        List<Byte> resourceContent = new LinkedList<Byte>();

        // read the content
        try {
          int result = resourceInputStream.read();
          while (result != -1) {
            resourceContent.add((byte) result);
            result = resourceInputStream.read();
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

        // copy cached content
        binary_contents[i] = new byte[resourceContent.size()];

        int byteIndex = 0;
        for (Byte b : resourceContent) {
          binary_contents[i][byteIndex] = b.byteValue();
        }

      } else {
        // we have to ensure that tests won't succeed if the resource could not
        // be found for whatever reason
        throw new RuntimeException(MessageFormat.format(
            "Could not find resource \"{0}\"", resource));
      }
    }
  }

  /**
   * Tests conversions from a {@link TextFile} to a {@link FileEntity} using a
   * PatchTarget as context.
   */
  @Test
  public void testTextFileToFileEntityConversionForPatchTarget() {

    String fileName = "file1.txt";
    String filePath = "/directory/";

    BaseFileEntityConverter baseFileEntityConverter =
        new BaseFileEntityConverter();

    FileDescription fileDescription =
        new GenericFileDescription(filePath, fileName);

    for (Charset charset : ENCODINGS_TO_TEST) {

      for (int i = 0; i < text_contents.length; i++) {

        // build parameters
        String content = text_contents[i];
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("charset", charset.name());

        ContentType contentType =
            new GenericContentType(TEST_TEXT_RESOURCES_TYPES[i],
                TEST_TEXT_RESOURCES_SUBTYPES[i], parameters);

        File file = new TextFile(fileDescription, contentType, content);

        AttachmentTarget attachmentTarget =
            new PatchTarget(new PatchTargetDescription(
                new PatchSetTargetDescription(new ChangeTargetDescription(
                    "I000000000000000"), 0), "/dir/file"),
                Collections.<FileDescription> emptyList());

        // build expected entity

        FileEntity expectedEntity =
            new FileEntity(fileName, filePath,
                contentType.getContentTypeIdentifier(), content, "None");

        // test conversion
        FileEntity fileEntity =
            baseFileEntityConverter.toEntity(file, attachmentTarget);

        Assert.assertThat(fileEntity, CoreMatchers.is(expectedEntity));

      }
    }
  }

  /**
   * Tests conversions from a {@link BinaryFile} to a {@link FileEntity} using a
   * PatchTarget as context.
   */
  @Test
  public void testBinaryFileToFileEntityConversionForPatchTarget() {

    String fileName = "file1.txt";
    String filePath = "/directory/";

    BaseFileEntityConverter baseFileEntityConverter =
        new BaseFileEntityConverter();

    FileDescription fileDescription =
        new GenericFileDescription(filePath, fileName);


    for (int i = 0; i < binary_contents.length; i++) {

      // build parameters
      byte[] content = binary_contents[i];
      HashMap<String, String> parameters = new HashMap<String, String>();

      ContentType contentType =
          new GenericContentType(TEST_BINARY_RESOURCES_TYPES[i],
              TEST_BINARY_RESOURCES_SUBTYPES[i], parameters);

      File file = new BinaryFile(fileDescription, contentType, content);

      AttachmentTarget attachmentTarget =
          new PatchTarget(new PatchTargetDescription(
              new PatchSetTargetDescription(new ChangeTargetDescription(
                  "I000000000000000"), 0), "/dir/file"),
              Collections.<FileDescription> emptyList());

      // build expected entity

      FileEntity expectedEntity =
          new FileEntity(fileName, filePath,
              contentType.getContentTypeIdentifier(),
              Base64.encodeBase64String(content), "base64");

      // test conversion
      FileEntity fileEntity =
          baseFileEntityConverter.toEntity(file, attachmentTarget);

      Assert.assertThat(fileEntity, CoreMatchers.is(expectedEntity));

    }
  }


  /**
   * Tests conversions from a {@link FileEntity} to a {@link TextFile} using a
   * PatchTarget as context.
   */
  @Test
  public void testFileEntityToTextFileConversionForPatchTarget() {

    String fileName = "file1.txt";
    String filePath = "/directory/";

    BaseFileEntityConverter baseFileEntityConverter =
        new BaseFileEntityConverter();

    FileDescription fileDescription =
        new GenericFileDescription(filePath, fileName);

    for (Charset charset : ENCODINGS_TO_TEST) {

      for (int i = 0; i < text_contents.length; i++) {

        // build parameters
        String content = text_contents[i];
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("charset", charset.name());

        ContentType contentType =
            new GenericContentType(TEST_TEXT_RESOURCES_TYPES[i],
                TEST_TEXT_RESOURCES_SUBTYPES[i], parameters);


        AttachmentTarget attachmentTarget =
            new PatchTarget(new PatchTargetDescription(
                new PatchSetTargetDescription(new ChangeTargetDescription(
                    "I000000000000000"), 0), "/dir/file"),
                Collections.<FileDescription> emptyList());


        FileEntity fileEntity =
            new FileEntity(fileName, filePath,
                contentType.getContentTypeIdentifier(), content, "none");

        // build expected file
        File expectedFile =
            new TextFile(fileDescription, contentType, content,
                attachmentTarget);


        // test conversion
        File file =
            baseFileEntityConverter.toObject(fileEntity, attachmentTarget);

        Assert.assertThat(file, CoreMatchers.is(expectedFile));

      }
    }
  }

  /**
   * Tests conversions from a {@link FileEntity} to a {@link BinaryFile} using a
   * PatchTarget as context.
   */
  @Test
  public void testFileEntityToBinaryFileConversionForPatchTarget() {

    String fileName = "file1.txt";
    String filePath = "/directory/";

    BaseFileEntityConverter baseFileEntityConverter =
        new BaseFileEntityConverter();

    FileDescription fileDescription =
        new GenericFileDescription(filePath, fileName);


    for (int i = 0; i < binary_contents.length; i++) {

      // build parameters
      byte[] content = binary_contents[i];
      HashMap<String, String> parameters = new HashMap<String, String>();

      ContentType contentType =
          new GenericContentType(TEST_BINARY_RESOURCES_TYPES[i],
              TEST_BINARY_RESOURCES_SUBTYPES[i], parameters);


      AttachmentTarget attachmentTarget =
          new PatchTarget(new PatchTargetDescription(
              new PatchSetTargetDescription(new ChangeTargetDescription(
                  "I000000000000000"), 0), "/dir/file"),
              Collections.<FileDescription> emptyList());


      FileEntity fileEntity =
          new FileEntity(fileName, filePath,
              contentType.getContentTypeIdentifier(),
              Base64.encodeBase64String(content), "base64");

      // build expected file
      File expectedFile =
          new BinaryFile(fileDescription, contentType, content,
              attachmentTarget);


      // test conversion
      File file =
          baseFileEntityConverter.toObject(fileEntity, attachmentTarget);

      Assert.assertThat(file, CoreMatchers.is(expectedFile));

    }
  }

}
