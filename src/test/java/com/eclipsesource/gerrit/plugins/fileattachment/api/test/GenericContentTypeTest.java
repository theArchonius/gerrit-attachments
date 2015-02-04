/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType;

/**
 * @author Florian Zoubek
 *
 */
public class GenericContentTypeTest {

  private static final Map<String, String[]> VALID_TYPES = initializeTypes();

  private static final String[] VALID_PARAMETER_NAMES = {"charset",
      "lowercaseparameter", "0123456789"};

  private static final String[] VALID_PARAMETER_VALUES =
      {"utf-8", "lowercasevalue", "0123456789",
          "\"§$%&/()=?':;<>|,.-_#+~*{[]}\\^°µ´²³\""};


  private static Map<String, String[]> initializeTypes() {
    Map<String, String[]> types = new HashMap<String, String[]>();

    types.put("text", new String[] {"plain", "xml"});
    types.put("application", new String[] {"json"});

    // TODO add more content types

    return types;
  }

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType#GenericContentType(java.lang.String)}
   * .
   */
  @Test
  public void testGenericContentTypeFromContentTypeString() {

    for (String baseType : VALID_TYPES.keySet()) {

      String[] subTypes = VALID_TYPES.get(baseType);

      for (String subType : subTypes) {

        for (int pNameIndex = 0; pNameIndex < VALID_PARAMETER_NAMES.length; pNameIndex++) {

          for (int pValueIndex = 0; pValueIndex < VALID_PARAMETER_VALUES.length; pValueIndex++) {

            String contentTypeIdentifier = baseType + "/" + subType;

            Map<String, String> expectedParameters =
                new HashMap<String, String>();
            expectedParameters.put(VALID_PARAMETER_NAMES[pNameIndex],
                VALID_PARAMETER_VALUES[pValueIndex]);

            contentTypeIdentifier +=
                ";" + VALID_PARAMETER_NAMES[pNameIndex] + "="
                    + VALID_PARAMETER_VALUES[pValueIndex];

            ContentType contentType =
                new GenericContentType(contentTypeIdentifier);


            assertThat(
                "Content type parameter map contains unexpected parameters or values",
                contentType.getParameters(), is(expectedParameters));

          }
        }
      }
    }

  }

  /**
   * Test method for
   * {@link com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType#getContentTypeIdentifier()}
   * .
   */
  @Test
  public void testGetContentTypeIdentifier() {
    for (String baseType : VALID_TYPES.keySet()) {

      String[] subTypes = VALID_TYPES.get(baseType);

      for (String subType : subTypes) {

        for (int pNameIndex = 0; pNameIndex < VALID_PARAMETER_NAMES.length; pNameIndex++) {

          for (int pValueIndex = 0; pValueIndex < VALID_PARAMETER_VALUES.length; pValueIndex++) {

            String expectedContentTypeIdentifier = baseType + "/" + subType;

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(VALID_PARAMETER_NAMES[pNameIndex],
                VALID_PARAMETER_VALUES[pValueIndex]);

            expectedContentTypeIdentifier +=
                "; " + VALID_PARAMETER_NAMES[pNameIndex] + "="
                    + VALID_PARAMETER_VALUES[pValueIndex];

            ContentType contentType =
                new GenericContentType(baseType, subType, parameters);

            assertThat("Unexpected content type identifier",
                contentType.getContentTypeIdentifier(),
                is(expectedContentTypeIdentifier));

            // TODO add test for basetype and subtype once they are added to the
            // ContentType interface

          }
        }
      }
    }
  }

}
