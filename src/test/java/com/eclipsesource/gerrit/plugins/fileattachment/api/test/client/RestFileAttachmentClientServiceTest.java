/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.client;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.ContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.OperationResultType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.RestEndpoint;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.AttachmentTargetRestEndpointRegistry;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.FileAttachmentClientService;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.RestFileAttachmentClientService;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.InvalidAttachmentTargetException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.InvalidFileException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.OperationFailedException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.RequestException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.ResponseException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.UnsupportedFileOperationException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityWriter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileDescriptionEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetEntity.TargetType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity.FileState;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity.ResultStatus;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.ChangeTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericContentType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchSetTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchTargetDescription;

/**
 * @author Florian Zoubek
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RestFileAttachmentClientServiceTest {

  /**
   * default mock client for rest calls
   */
  @Mock
  Client client;

  /**
   * default mock {@link ClientBuilder}
   */
  @Mock
  ClientBuilder clientBuilder;

  /**
   * default mock {@link WebTarget}
   */
  @Mock
  WebTarget webTarget;

  /**
   * default mock REST call invocation {@link Builder}
   */
  @Mock
  Builder builder;

  /**
   * default mock REST {@link Response}
   */
  @Mock
  Response response;

  /**
   * default mock {@link AttachmentTargetRestEndpointRegistry}
   */
  @Mock
  AttachmentTargetRestEndpointRegistry restEndpointRegistry;

  /**
   * default mock operation result reader for the REST service
   */
  @Mock
  EntityReader<OperationResult, FileModificationResponseEntity, Object> operationResultReader;

  /**
   * default mock attachment target result reader for the REST service
   */
  @Mock
  EntityReader<AttachmentTarget, AttachmentTargetResponseEntity, AttachmentTargetDescription> attachmentTargetResponseReader;

  /**
   * default mock file entity writer
   */
  @Mock
  EntityWriter<File, FileEntity, AttachmentTarget> fileEntityWriter;

  // Model mocks
  /**
   * default mock file for test operations
   */
  @Mock
  File file;

  /**
   * default mock file description for test operations
   */
  @Mock
  FileDescription fileDescription;

  /**
   * default mock for {@link OperationResult}
   */
  @Mock
  OperationResult operationResult;

  /**
   * default mock for {@link RestEndpoint}
   */
  @Mock
  RestEndpoint restEndpoint;

  // Entity mocks
  // ==============

  /**
   * default mock for {@link FileModificationResponseEntity}
   */
  @Mock
  FileModificationResponseEntity fileModificationResponseEntity;

  /**
   * default mock for {@link OperationResultEntity}
   */
  @Mock
  OperationResultEntity operationResultEntity;

  /**
   * default mock for {@link FileEntity}
   */
  @Mock
  FileEntity fileEntity;

  /**
   * default mock for {@link AttachmentTargetResponseEntity}
   */
  @Mock
  AttachmentTargetResponseEntity attachmentTargetResponseEntity;

  /**
   * default mock for {@link AttachmentTargetEntity}
   */
  @Mock
  AttachmentTargetEntity attachmentTargetEntity;

  // AttachmentTarget mocks
  // =========================

  /**
   * default mock for attachmentTarget
   */
  @Mock
  AttachmentTarget attachmentTarget;

  // TargetDescription mocks
  // =========================

  /**
   * default mock for the {@link PatchTargetDescription}
   */
  @Mock
  PatchTargetDescription patchTargetDescription;

  /**
   * default mock for {@link PatchSetTargetDescription}
   */
  @Mock
  PatchSetTargetDescription patchSetTargetDescription;

  /**
   * default mock for {@link ChangeTargetDescription}
   */
  @Mock
  ChangeTargetDescription changeTargetDescription;

  // common properties

  /**
   * the id of the default change for testing
   */
  String changeId = "I00000000000000";

  /**
   * the id of the default patch set for testing
   */
  int patchSetId = 0;

  /**
   * the url of the default patch for testing
   */
  String patchUrl = "/somedir/somefile";
  /**
   * the url encoded version of {@link #patchUrl}
   */
  String patchUrlEncoded = "%2Fsomedir%2Fsomefile";
  /**
   * the default file name on the server for testing
   */
  String serverFileName = "file";
  /**
   * the default file path on the server for testing
   */
  String serverFilePath = "directory/";
  /**
   * the default file content for testing
   */
  byte[] fileContent = "Some text conent".getBytes();
  /**
   * the default content type of the default file for testing
   */
  ContentType fileContentType = GenericContentType.PLAIN_TEXT;

  /**
   * the default root URI for testing
   */
  String restRoot = "http://127.0.0.1/";
  /**
   * the path for the default REST endpoint (relative to {@link #restRoot} )
   */
  String restEndpointPath = "a/changes/" + changeId + "/revisions/"
      + patchSetId + "/files/" + changeId + "/" + patchUrlEncoded
      + "/fileattachment~file";

  /**
   * the default user name for testing
   */
  String user = "user";

  /**
   * the default password for testing
   */
  byte[] password = "test".getBytes();

  /**
   * number of file description mocks to generate
   * 
   * @see #fileDescriptions
   */
  int numFileDescriptions = 4;

  /**
   * list of file descriptions used by the default {@link #attachmentTarget}
   * mock
   */
  List<FileDescription> fileDescriptions;

  /**
   * array of file descriptions used by the default
   * {@link #attachmentTargetEntity}. This array contains the corresponding
   * entities of {@link #fileDescriptions}.
   */
  FileDescriptionEntity[] fileDescriptionEntities;


  // fixture

  RestFileAttachmentClientService serviceUnderTest;

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Before
  public void setUp() throws URISyntaxException,
      UnsupportedFileOperationException, FileAttachmentClientException {

    /*
     * initialize stubs common to most of the tests the default behaviour of the
     * stubs is to simulate operations that always succeed and throw no
     * exceptions. The response mock is the only one that is not initialized, as
     * it varies for all tests.
     */

    fileDescriptions = new ArrayList<FileDescription>(numFileDescriptions);
    fileDescriptionEntities = new FileDescriptionEntity[numFileDescriptions];
    for (int i = 0; i < numFileDescriptions; i++) {

      FileDescription fileDescription = mock(FileDescription.class);
      when(fileDescription.getServerFileName()).thenReturn(
          "list-" + i + "-" + serverFileName);
      when(fileDescription.getServerFilePath()).thenReturn(
          serverFilePath + "list-" + i + "/");

      FileDescriptionEntity fileDescriptionEntity =
          mock(FileDescriptionEntity.class);
      when(fileDescriptionEntity.getFileName()).thenReturn(
          "list-" + i + "-" + serverFileName);
      when(fileDescriptionEntity.getFilePath()).thenReturn(
          serverFilePath + "list-" + i + "/");
    }

    // restEndpointRegistry
    when(
        restEndpointRegistry.getRestEndpoint(any(AttachmentTarget.class),
            anyString())).thenReturn(restEndpoint);
    when(
        restEndpointRegistry.getRestEndpoint(
            any(AttachmentTargetDescription.class), anyString())).thenReturn(
        restEndpoint);

    // restEndpoint
    when(restEndpoint.getPath()).thenReturn(restEndpointPath);

    // changeTargetDescription
    when(changeTargetDescription.getChangeId()).thenReturn(changeId);

    // patchSetTargetDescription
    when(patchSetTargetDescription.getChangeTargetDescription()).thenReturn(
        changeTargetDescription);
    when(patchSetTargetDescription.getId()).thenReturn(patchSetId);

    // patchTargetDescription
    when(patchTargetDescription.getPatchUrl()).thenReturn(patchUrl);
    when(patchTargetDescription.getPatchSetTargetDescription()).thenReturn(
        patchSetTargetDescription);

    // fileDescription
    when(fileDescription.getServerFileName()).thenReturn(serverFileName);
    when(fileDescription.getServerFilePath()).thenReturn(serverFilePath);

    // file
    when(file.getContent()).thenReturn(fileContent);
    when(file.getContentType()).thenReturn(fileContentType);
    when(file.getFileDescription()).thenReturn(fileDescription);

    // clientBuilder
    when(clientBuilder.build()).thenReturn(client);

    // client
    when(client.target(anyString())).thenReturn(webTarget);
    when(client.target(any(Link.class))).thenReturn(webTarget);
    when(client.target(any(URI.class))).thenReturn(webTarget);
    when(client.target(any(UriBuilder.class))).thenReturn(webTarget);

    // webTarget
    when(webTarget.path(anyString())).thenReturn(webTarget);
    when(webTarget.request()).thenReturn(builder);

    // fileEntityWriter
    when(
        fileEntityWriter.toEntity(any(File.class), any(AttachmentTarget.class)))
        .thenReturn(fileEntity);

    // attachmentTargetResultReader
    when(
        attachmentTargetResponseReader.toObject(
            any(AttachmentTargetResponseEntity.class),
            any(AttachmentTargetDescription.class))).thenReturn(
        attachmentTarget);

    // attachmentTarget
    when(attachmentTarget.getAttachedFileDescriptions()).thenReturn(
        fileDescriptions);
    when(attachmentTarget.getTargetDescription()).thenReturn(
        patchTargetDescription);

    // fileEntity
    when(fileEntity.getContent()).thenReturn(
        Base64.encodeBase64String(fileContent));
    when(fileEntity.getContentType()).thenReturn(
        fileContentType.getContentTypeIdentifier());
    when(fileEntity.getEncodingMethod()).thenReturn("base64");
    when(fileEntity.getFileName()).thenReturn(serverFileName);
    when(fileEntity.getFilePath()).thenReturn(serverFilePath);


    // builder
    when(builder.put(any(Entity.class))).thenReturn(response);
    when(builder.get()).thenReturn(response);
    when(builder.delete()).thenReturn(response);
    when(builder.post(any(Entity.class))).thenReturn(response);


    // operationResultEntity
    when(operationResultEntity.getResultStatus()).thenReturn(
        ResultStatus.SUCCESS);
    when(operationResultEntity.getStatusMessage()).thenReturn("");

    // fileModificationResponseEntity
    when(fileModificationResponseEntity.getNewFileState()).thenReturn(
        FileState.NEW);
    when(fileModificationResponseEntity.getOperationResult()).thenReturn(
        operationResultEntity);

    // attachmentTargetResponseEntity
    when(attachmentTargetResponseEntity.getOperationResultEntity()).thenReturn(
        operationResultEntity);
    when(attachmentTargetResponseEntity.getAttachmentTargetEntity())
        .thenReturn(attachmentTargetEntity);

    // attachmentTargetEntity
    when(attachmentTargetEntity.getTargetType()).thenReturn(TargetType.PATCH);
    when(attachmentTargetEntity.getFileDescriptions()).thenReturn(
        fileDescriptionEntities);

    // operationResultReader
    when(
        operationResultReader.toObject(
            any(FileModificationResponseEntity.class), any())).thenReturn(
        operationResult);

    // operationResult
    when(operationResult.getResultType()).thenReturn(
        OperationResultType.SUCCESS);
    when(operationResult.getStatusMessage()).thenReturn("");

    serviceUnderTest =
        new RestFileAttachmentClientService(new URI(restRoot), clientBuilder,
            restEndpointRegistry, operationResultReader,
            attachmentTargetResponseReader, fileEntityWriter, user, password);
  }

  /*
   * ====================================================
   * RestFileAttachmentClientService#attachFile() tests
   * ====================================================
   */

  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * with valid parameters and no other errors occur.
   */
  @Test
  public void testAttachFileSuccess() throws InvalidAttachmentTargetException,
      InvalidFileException, RequestException, ResponseException,
      FileAttachmentClientException, URISyntaxException {

    // Initialize test specific stubs

    // response
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(FileModificationResponseEntity.class)).thenReturn(
        fileModificationResponseEntity);

    // perform operation
    OperationResult operationResult =
        serviceUnderTest.attachFile(file, patchTargetDescription);

    // check result
    assertThat(operationResult.getResultType(), is(OperationResultType.SUCCESS));

    // check behaviour
    InOrder inOrder = Mockito.inOrder(client, webTarget, builder, response);
    inOrder.verify(client).register(any(HttpAuthenticationFeature.class));
    inOrder.verify(client).target(new URI(restRoot));
    inOrder.verify(webTarget).path(Matchers.argThat(is(restEndpointPath)));
    inOrder.verify(webTarget).request();
    inOrder.verify(builder).put(any(Entity.class));
    inOrder.verify(response).readEntity(FileModificationResponseEntity.class);

    verify(restEndpointRegistry).getRestEndpoint(patchTargetDescription,
        FileAttachmentClientService.OPERATION_ATTACH_FILE);
    verify(restEndpoint).getPath();

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that the REST endpoint registry does not support that operation on
   * the given target.
   * 
   */
  @Test
  public void testAttachFileWithUnsupportedOperation()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(InvalidAttachmentTargetException.class);
    thrown.expectMessage(allOf(
        containsString("does not support the operation"),
        containsString("OPERATION_ATTACH_FILE")));

    // prepare mocks

    when(
        restEndpointRegistry.getRestEndpoint(any(AttachmentTarget.class),
            anyString())).thenThrow(
        new UnsupportedFileOperationException(
            FileAttachmentClientService.OPERATION_ATTACH_FILE));
    when(
        restEndpointRegistry.getRestEndpoint(
            any(AttachmentTargetDescription.class), anyString())).thenThrow(
        new UnsupportedFileOperationException(
            FileAttachmentClientService.OPERATION_ATTACH_FILE));

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that the attachment target is not supported.
   * 
   */
  @Test
  public void testAttachFileWithUnsupportedAttachmentTarget()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(InvalidAttachmentTargetException.class);
    thrown
        .expectMessage(containsString("does not support AttachmentTargetDescriptions"));

    // prepare mocks

    when(
        restEndpointRegistry.getRestEndpoint(any(AttachmentTarget.class),
            anyString())).thenReturn(null);
    when(
        restEndpointRegistry.getRestEndpoint(
            any(AttachmentTargetDescription.class), anyString())).thenReturn(
        null);

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that a {@link ProcessingException} is thrown during
   * {@link Builder#put(Entity)}.
   * 
   */
  @Test
  public void testAttachFileWithProcessingExceptionFromPut()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(RequestException.class);

    // prepare mocks
    when(webTarget.request()).thenThrow(new ProcessingException(""));

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }


  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that a {@link ResponseProcessingException} is thrown during
   * {@link Builder#put(Entity)}.
   * 
   */
  @Test
  public void testAttachFileWithResponseProcessingExceptionFromPut()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(ResponseException.class);

    // prepare mocks
    when(webTarget.request()).thenThrow(
        new ResponseProcessingException(null, ""));

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that {@link Builder#put(Entity)} returns null.
   * 
   */
  @Test
  public void testAttachFileWithNullFromPut()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(ResponseException.class);

    // prepare mocks
    when(builder.put(any(Entity.class))).thenReturn(null);

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that the response from the server has no entities.
   * 
   */
  @Test
  public void testAttachFileWithEmptyResponse()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(OperationFailedException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(false);

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }


  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that a {@link ProcessingException} is thrown during
   * {@link Response#readEntity(Class)}.
   * 
   */
  @Test
  public void testAttachFileWithProcessingExceptionFromReadEntity()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(ResponseException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(FileModificationResponseEntity.class)).thenThrow(
        new ProcessingException(""));

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }


  /**
   * Tests
   * {@link RestFileAttachmentClientService#attachFile(File, AttachmentTargetDescription)}
   * in case that {@link Response#readEntity(Class)} returns null.
   * 
   */
  @Test
  public void testAttachFileWhenNullFromToObject()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(ResponseException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(FileModificationResponseEntity.class)).thenReturn(
        fileModificationResponseEntity);
    when(
        operationResultReader.toObject(
            any(FileModificationResponseEntity.class), any())).thenReturn(null);

    // perform operation
    serviceUnderTest.attachFile(file, patchTargetDescription);

  }

  /*
   * =============================================================
   * RestFileAttachmentClientService#getAttachmentTarget() tests
   * =============================================================
   */

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * with valid parameters and no other errors occur.
   */
  @Test
  public void testGetAttachmentTargetSuccess() throws RequestException,
      ResponseException, FileAttachmentClientException, URISyntaxException {

    // prepare mocks
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(AttachmentTargetResponseEntity.class)).thenReturn(
        attachmentTargetResponseEntity);

    // perform operation
    AttachmentTarget attachmentTarget =
        serviceUnderTest.getAttachmentTarget(patchTargetDescription);

    assertThat(attachmentTarget.getAttachedFileDescriptions(),
        is(fileDescriptions));
    assertThat(attachmentTarget.getTargetDescription(),
        is((AttachmentTargetDescription) patchTargetDescription));

    // check behaviour
    InOrder inOrder = Mockito.inOrder(client, webTarget, builder, response);
    inOrder.verify(client).register(any(HttpAuthenticationFeature.class));
    inOrder.verify(client).target(new URI(restRoot));
    inOrder.verify(webTarget).path(Matchers.argThat(is(restEndpointPath)));
    inOrder.verify(webTarget).request();
    inOrder.verify(builder).get();
    inOrder.verify(response).readEntity(AttachmentTargetResponseEntity.class);

    verify(restEndpointRegistry).getRestEndpoint(patchTargetDescription,
        FileAttachmentClientService.OPERATION_GET_TARGET);
    verify(restEndpoint).getPath();
  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * in case that the REST endpoint registry does not support that operation on
   * the given target.
   * 
   */
  @Test
  public void testGetAttachmentTargetWithUnsupportedOperation()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(InvalidAttachmentTargetException.class);
    thrown.expectMessage(allOf(
        containsString("does not support the operation"),
        containsString("OPERATION_GET_TARGET")));

    // prepare mocks

    when(
        restEndpointRegistry.getRestEndpoint(any(AttachmentTarget.class),
            anyString())).thenThrow(
        new UnsupportedFileOperationException(
            FileAttachmentClientService.OPERATION_GET_TARGET));
    when(
        restEndpointRegistry.getRestEndpoint(
            any(AttachmentTargetDescription.class), anyString())).thenThrow(
        new UnsupportedFileOperationException(
            FileAttachmentClientService.OPERATION_GET_TARGET));

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * in case that the attachment target is not supported.
   * 
   */
  @Test
  public void testGetAttachmentTargetWithUnsupportedAttachmentTarget()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(InvalidAttachmentTargetException.class);
    thrown
        .expectMessage(containsString("does not support AttachmentTargetDescriptions"));

    // prepare mocks

    when(
        restEndpointRegistry.getRestEndpoint(any(AttachmentTarget.class),
            anyString())).thenReturn(null);
    when(
        restEndpointRegistry.getRestEndpoint(
            any(AttachmentTargetDescription.class), anyString())).thenReturn(
        null);

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * in case that a {@link ProcessingException} is thrown during
   * {@link Builder#get()}.
   * 
   */
  @Test
  public void testGetAttachmentTargetWithProcessingExceptionFromGet()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(RequestException.class);

    // prepare mocks
    when(webTarget.request()).thenThrow(new ProcessingException(""));

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * in case that {@link Builder#get()} returns null.
   * 
   */
  @Test
  public void testGetAttachmentTargetWithNullFromGet()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(ResponseException.class);

    // prepare mocks
    when(builder.get()).thenReturn(null);

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }


  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * in case that the response from the server has no entities.
   * 
   */
  @Test
  public void testGetAttachmentTargetWithEmptyResponse()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(OperationFailedException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(false);

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * in case that {@link Response#readEntity(Class)} returns null.
   * 
   */
  @Test
  public void testGetAttachmentTargetWhenNullFromToObject()
      throws InvalidAttachmentTargetException, InvalidFileException,
      RequestException, ResponseException, FileAttachmentClientException {

    // expected exception
    thrown.expect(ResponseException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(AttachmentTargetResponseEntity.class)).thenReturn(
        attachmentTargetResponseEntity);
    when(
        attachmentTargetResponseReader.toObject(
            any(AttachmentTargetResponseEntity.class),
            any(AttachmentTargetDescription.class))).thenReturn(null);

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }

  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * with valid parameters but the response contains a operation result status
   * of {@link ResultStatus#FAILED}.
   */
  @Test
  public void testGetAttachmentTargetWithResultFailed()
      throws RequestException, ResponseException,
      FileAttachmentClientException, URISyntaxException {

    // expected exception
    thrown.expect(OperationFailedException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(AttachmentTargetResponseEntity.class)).thenReturn(
        attachmentTargetResponseEntity);
    when(operationResultEntity.getResultStatus()).thenReturn(ResultStatus.FAILED);

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }
  
  /**
   * Tests
   * {@link RestFileAttachmentClientService#getAttachmentTarget(AttachmentTargetDescription)}
   * with valid parameters but the response contains a operation result status
   * of {@link ResultStatus#NOTPERMITTED}.
   */
  @Test
  public void testGetAttachmentTargetWithResultNotPermitted()
      throws RequestException, ResponseException,
      FileAttachmentClientException, URISyntaxException {

    // expected exception
    thrown.expect(OperationFailedException.class);

    // prepare mocks
    when(response.hasEntity()).thenReturn(true);
    when(response.readEntity(AttachmentTargetResponseEntity.class)).thenReturn(
        attachmentTargetResponseEntity);
    when(operationResultEntity.getResultStatus()).thenReturn(ResultStatus.NOTPERMITTED);

    // perform operation
    serviceUnderTest.getAttachmentTarget(patchTargetDescription);

  }
}
