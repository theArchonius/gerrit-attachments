/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import java.nio.charset.Charset;

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

    fileEntity.setContentType(file.getContentType().getContentTypeIdentifier());

    ContentType contentType = file.getContentType();

    if (contentType.isBinary()) {
      // TODO do some binary to text encoding

      // fileEntity.setContent(content);
      // fileEntity.setEncodingMethod(encodingMethod);
      throw new UnsupportedOperationException(
          "Binary files are currently not supported");
    } else {
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
