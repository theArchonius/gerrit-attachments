/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.client;


import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.client.RestFileAttachmentClientFactory;


/**
 * @author Florian Zoubek
 *
 */
public class RestFileAttachmentClientFactoryTest {



  @Test
  public void testCreateFileAttachmentService() {
    // TODO improve this test as soon there is a
    // constructor that allows specification of parameters
    
    Assert.assertThat(new RestFileAttachmentClientFactory()
        .createFileAttachmentClientService(), CoreMatchers
        .instanceOf(RestFileAttachmentClientServiceTest.class));
  }

}
