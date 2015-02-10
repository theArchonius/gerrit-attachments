/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.client;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.GenericRestEndpoint;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;

/**
 * @author Florian Zoubek
 *
 */
public class GenericRestEndpointTest {

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.client.GenericRestEndpoint#GenericRestEndpoint(java.lang.String[])}
   * .
   * @throws FileAttachmentClientException 
   */
  @Test
  public void testGenericRestEndpointStringArray() throws FileAttachmentClientException {

    UrlEncodedString[] pathParts =
        new UrlEncodedString[] {
            new UrlEncodedString("abcdefghijklmnopqrstuvwxyz",
                "abcdefghijklmnopqrstuvwxyz"),
            new UrlEncodedString("0123456789", "0123456789"),
            new UrlEncodedString("äöü", "%C3%A4%C3%B6%C3%BC"),
            new UrlEncodedString(
                "âĉêĝĥîĵôŝûŵŷẑ",
                "%C3%A2%C4%89%C3%AA%C4%9D%C4%A5%C3%AE%C4%B5%C3%B4%C5%9D%C3%BB%C5%B5%C5%B7%E1%BA%91"),
            new UrlEncodedString(
                "áćéǵíḱĺḿńóṕŕśúǘẃýź",
                "%C3%A1%C4%87%C3%A9%C7%B5%C3%AD%E1%B8%B1%C4%BA%E1%B8%BF%C5%84%C3%B3%E1%B9%95%C5%95%C5%9B%C3%BA%C7%98%E1%BA%83%C3%BD%C5%BA"),
            new UrlEncodedString(
                "src/main/java/com/eclipsesource/example/Example.java",
                "src%2Fmain%2Fjava%2Fcom%2Feclipsesource%2Fexample%2FExample.java"),
            new UrlEncodedString(
                "°!\"§$%&/()=?^²³{[]}\\ß+#*'~-.,;:_µ<>|@",
                "%C2%B0!%22%C2%A7%24%25%26%2F()%3D%3F%5E%C2%B2%C2%B3%7B%5B%5D%7D%5C%C3%9F%2B%23*%27~-.%2C%3B%3A_%C2%B5%3C%3E%7C%40")};

    for (int numElements = 1; numElements < pathParts.length; numElements++) {

      // use some subsets for testing endpoints with varying part count
      UrlEncodedString[] partsSubset =
          Arrays.copyOfRange(pathParts, 0, numElements);
      String[] actualParts = new String[partsSubset.length];

      StringBuilder expectedPath = new StringBuilder();
      // build expected path and actual parameters for the constructor
      for (int i = 0; i < partsSubset.length; i++) {
        UrlEncodedString part = partsSubset[i];
        if (expectedPath.length() > 0) {
          expectedPath.append("/");
        }
        expectedPath.append(part.encodedString);
        actualParts[i] = part.originalString;
      }

      // test if the path matches the expected path
      assertThat(new GenericRestEndpoint(actualParts).getPath(),
          CoreMatchers.is(expectedPath.toString()));
    }
  }



  /**
   * represents a URL encoded String containing also information about the
   * original String
   * 
   * @author Florian Zoubek
   *
   */
  private class UrlEncodedString {
    private String originalString;
    private String encodedString;

    /**
     * @param originalString the original string
     * @param encodedString the encoded string
     */
    public UrlEncodedString(String originalString, String encodedString) {
      super();
      this.originalString = originalString;
      this.encodedString = encodedString;
    }
  }
}
