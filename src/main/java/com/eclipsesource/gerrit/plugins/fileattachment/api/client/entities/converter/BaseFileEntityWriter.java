/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.converter;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityWriter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileEntity;

/**
 * An entity writer which creates {@link FileEntity}s from {@link File}s.
 * 
 * @author Florian Zoubek
 *
 */
public class BaseFileEntityWriter implements EntityWriter<File, FileEntity> {

  @Override
  public FileEntity toEntity(File file) {

    FileEntity fileEntity = new FileEntity();

    FileDescription fileDescription = file.getFileDescription();
    fileEntity.setFileName(fileDescription.getFileName());
    fileEntity.setFilePath(fileDescription.getFilePath());

    ContentType contentType = file.getContentType();
    fileEntity.setContentType(contentType.getContentTypeIdentifier());

    if (contentType.isBinary()) {

      // encode the content as base64 as JSON does not support binary data (Gson
      // supports it, but then we are bound to Gson forever - also there is
      // currently no easy way to modify the Gson configuration through the
      // Gerrit API )

      String content = Base64.encodeBase64String(file.getContent());
      fileEntity.setContent(content);
      fileEntity.setEncodingMethod("base64");

    } else {

      // obtain the text and write it directly into the the entity - we don't need to encode textual data

      /*
       * TODO refactor to avoid unnecessary conversions
       * 
       * actually we convert strings to bytes in the TextFile.getContent()
       * method and back to a string here. Maybe we should think of a solution
       * that avoid this unnecessary conversion?
       */
      Charset charset = Charset.defaultCharset();

      // search for specific charset in the content type, if there exists none,
      // use the system default charset

      if (contentType != null) {
        String sCharset = contentType.getParameters().get("charset");
        if (sCharset != null) {
          charset = Charset.forName(sCharset);
        }
      }

      fileEntity.setContent(new String(file.getContent(), charset));
    }


    return fileEntity;
  }
}
