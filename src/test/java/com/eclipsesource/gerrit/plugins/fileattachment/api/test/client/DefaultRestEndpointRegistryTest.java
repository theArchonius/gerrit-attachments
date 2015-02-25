/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.client;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.DefaultRestEndpointRegistry;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.FileAttachmentClientService;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.UnsupportedFileOperationException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.ChangeTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchSetTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchTargetDescription;

/**
 * @author Florian Zoubek
 *
 */
public class DefaultRestEndpointRegistryTest {

  private final static String PLUGIN_NAME = "fileattachment";
  private final static String FILE_ENDPOINT_NAME = "file";

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.DefaultRestEndpointRegistry#getRestEndpoint(com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget, java.lang.String)}
   * .
   */
  @Test
  public void testGetRestEndpointAttachmentTargetString() {

    DefaultRestEndpointRegistry endpointRegistry =
        new DefaultRestEndpointRegistry();

    // The DefaultRestTargetEndpointRegistry currently does not support any
    // AttachmentTarget implementation, so we only have to check for null.
    // However this test case must be updated if new AttachmentTargets are
    // supported.
    assertThat(endpointRegistry.getRestEndpoint((AttachmentTarget) null,
        FileAttachmentClientService.OPERATION_ATTACH_FILE),
        CoreMatchers.nullValue());
    assertThat(endpointRegistry.getRestEndpoint((AttachmentTarget) null,
        FileAttachmentClientService.OPERATION_DELETE_FILE),
        CoreMatchers.nullValue());
    assertThat(endpointRegistry.getRestEndpoint((AttachmentTarget) null,
        FileAttachmentClientService.OPERATION_GET_FILE),
        CoreMatchers.nullValue());
    assertThat(endpointRegistry.getRestEndpoint((AttachmentTarget) null,
        FileAttachmentClientService.OPERATION_GET_TARGET),
        CoreMatchers.nullValue());
  }



  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.DefaultRestEndpointRegistry#getRestEndpoint(com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription, java.lang.String)}
   * .
   * 
   * @throws FileAttachmentClientException
   * @throws UnsupportedFileOperationException
   */
  @Test
  public void testGetRestEndpointAttachmentTargetDescriptionString()
      throws UnsupportedFileOperationException, FileAttachmentClientException {
    DefaultRestEndpointRegistry endpointRegistry =
        new DefaultRestEndpointRegistry();

    // test PatchTargetDescription endpoints

    String[] changeIds = new String[] {"I16238bdso273812630587", "2"};
    int[] patchSetIds = new int[] {1, 13, 42};
    UrlEncodedPath[] patchUrls =
        new UrlEncodedPath[] {
            new UrlEncodedPath("README.md", "README.md"),
            new UrlEncodedPath(
                "src/main/java/com/eclipsesource/example/Example.java",
                "src%2Fmain%2Fjava%2Fcom%2Feclipsesource%2Fexample%2FExample.java")};

    for (String changeId : changeIds) {
      for (int patchSetId : patchSetIds) {
        for (UrlEncodedPath patchUrl : patchUrls) {
          PatchTargetDescription patchTargetDescription =
              new PatchTargetDescription(new PatchSetTargetDescription(
                  new ChangeTargetDescription(changeId), patchSetId),
                  patchUrl.path);

          String expectedPath =
              "a/changes/" + changeId + "/revisions/" + patchSetId + "/files/"
                  + patchUrl.encodedPath + "/" + PLUGIN_NAME + "~";

          assertThat(
              endpointRegistry.getRestEndpoint(patchTargetDescription,
                  FileAttachmentClientService.OPERATION_ATTACH_FILE).getPath(),
              CoreMatchers.is(expectedPath + FILE_ENDPOINT_NAME));
          assertThat(
              endpointRegistry.getRestEndpoint(patchTargetDescription,
                  FileAttachmentClientService.OPERATION_DELETE_FILE).getPath(),
              CoreMatchers.is(expectedPath + FILE_ENDPOINT_NAME));
          assertThat(
              endpointRegistry.getRestEndpoint(patchTargetDescription,
                  FileAttachmentClientService.OPERATION_GET_FILE).getPath(),
              CoreMatchers.is(expectedPath + FILE_ENDPOINT_NAME));
          assertThat(
              endpointRegistry.getRestEndpoint(patchTargetDescription,
                  FileAttachmentClientService.OPERATION_GET_TARGET).getPath(),
              CoreMatchers.is(expectedPath + FILE_ENDPOINT_NAME));
        }
      }
    }



    // test unsupported AttachmentTargetDescriptions
    assertThat(endpointRegistry.getRestEndpoint(
        (AttachmentTargetDescription) null,
        FileAttachmentClientService.OPERATION_ATTACH_FILE),
        CoreMatchers.nullValue());
    assertThat(endpointRegistry.getRestEndpoint(
        (AttachmentTargetDescription) null,
        FileAttachmentClientService.OPERATION_DELETE_FILE),
        CoreMatchers.nullValue());
    assertThat(endpointRegistry.getRestEndpoint(
        (AttachmentTargetDescription) null,
        FileAttachmentClientService.OPERATION_GET_FILE),
        CoreMatchers.nullValue());
    assertThat(endpointRegistry.getRestEndpoint(
        (AttachmentTargetDescription) null,
        FileAttachmentClientService.OPERATION_GET_TARGET),
        CoreMatchers.nullValue());

  }

  /**
   * represents a URL encoded path containing also information about the
   * original path
   * 
   * @author Florian Zoubek
   *
   */
  private class UrlEncodedPath {
    private String path;
    private String encodedPath;

    /**
     * @param path the real path
     * @param encodedPath the encoded path
     */
    public UrlEncodedPath(String path, String encodedPath) {
      super();
      this.path = path;
      this.encodedPath = encodedPath;
    }
  }

}
