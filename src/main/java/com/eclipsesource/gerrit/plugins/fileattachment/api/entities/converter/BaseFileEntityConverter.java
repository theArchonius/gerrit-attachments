/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityConverter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericFileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.TextFile;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/**
 * An entity writer which creates {@link FileEntity}s from {@link File}s.
 *
 * @author Florian Zoubek
 *
 */
public class BaseFileEntityConverter implements
    EntityConverter<File, FileEntity, AttachmentTarget, AttachmentTarget> {

  @Override
  public FileEntity toEntity(File file, AttachmentTarget context) {

    FileEntity fileEntity = new FileEntity();

    FileDescription fileDescription = file.getFileDescription();
    fileEntity.setFileName(fileDescription.getServerFileName());
    fileEntity.setFilePath(fileDescription.getServerFilePath());

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

      // obtain the text and write it directly into the the entity - we don't
      // need to encode textual data

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

  @Override
  public File toObject(FileEntity jsonEntity, AttachmentTarget context) {
    ContentType contentType = new GenericContentType(jsonEntity.getContentType());
    File file = null;
    FileDescription fileDescription= new GenericFileDescription(jsonEntity.getFilePath(), jsonEntity.getFileName());

    // decode, if necessary
    String encodingMethod = jsonEntity.getEncodingMethod();
    byte[] content = null;
    String sContent = null;

    if (encodingMethod == null || encodingMethod.isEmpty()
        || encodingMethod.equalsIgnoreCase("none")){
      sContent = jsonEntity.getContent();
    }else{
      if(encodingMethod.equalsIgnoreCase("base64")){
        content = Base64.decodeBase64(jsonEntity.getContent());
      }else{
        // unsupported encoding method
        return null;
      }
    }

    // TODO replace by decision based on the type, not on the binary property
    if(contentType.isBinary()){
      // TODO implement
    }else{
      file = new TextFile(fileDescription, contentType, sContent, null);
    }
    return file;
  }
}
