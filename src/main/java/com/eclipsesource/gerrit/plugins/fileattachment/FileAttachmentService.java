/**
 *
 */
package com.eclipsesource.gerrit.plugins.fileattachment;

import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.server.IdentifiedUser;

import java.util.List;


/**
 * @author Florian Zoubek
 *
 */
public interface FileAttachmentService {

  /**
   * attaches a unicode text file to the given patch
   *
   * @param filePath the URI of the file
   * @param patchKey the key of the patch
   * @param data the text data
   * @param user the user who attaches the file
   */
  public void attachFile(String filePath, Patch.Key patchKey, String data,
      IdentifiedUser user) throws FileAttachmentException;

  /**
   * lists all attached files for the given patch
   *
   * @param patchKey the key of the patch
   */
  public List<TextFile> listAllFiles(Patch.Key patchKey);

  /**
   * deletes an already attached file
   *
   * @param filePath the file path of the file
   * @param patchKey the key of the patch where the file is attached
   * @param user the user who attaches the file
   */
  public void deleteFile(String filePath, Patch.Key patchKey,
      IdentifiedUser user) throws FileAttachmentException;

}
