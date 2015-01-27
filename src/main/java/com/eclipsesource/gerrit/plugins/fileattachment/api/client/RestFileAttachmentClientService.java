/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.File;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.FileAttachmentClientException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.InvalidAttachmentTargetException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.InvalidFileException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.RequestException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.exceptions.ResponseException;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.GenericOperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.OperationResult;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.RestEndpoint;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityMessageBodyReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityMessageBodyWriter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityWriter;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileModificationResponseEntity;

/**
 * Implementation of the {@link FileAttachmentClientService} that uses REST
 * calls.
 * 
 * @author Florian Zoubek
 *
 */
public class RestFileAttachmentClientService implements
    FileAttachmentClientService {

  /**
   * the root URI for all REST requests
   */
  private URI restRoot = null;

  /**
   * the entity writer used to convert {@link File} instances to
   * {@link FileEntity}s
   */
  private EntityWriter<File, FileEntity> fileEntityWriter;

  /**
   * the entity reader used to convert {@link FileModificationResponseEntity}
   * instances to {@link OperationResult}s
   */
  private EntityReader<OperationResult, FileModificationResponseEntity> operationResultReader;

  /**
   * the registry used to map {@link AttachmentTarget}s to {@link RestEndpoint}s
   */
  private AttachmentTargetRestEndpointRegistry restEndpointRegistry;

  /**
   * the client builder used to create new client instances used to access REST
   * endpoints
   */
  private ClientBuilder clientBuilder;

  /**
   * the user name used for authentication
   */
  private String username;

  /**
   * the password used for authentication
   */
  private byte[] password;

  /**
   * constructs a new {@link FileAttachmentClientService} instance
   * 
   * @param restRoot the root URI for all REST calls
   * @param restEndpointRegistry the registry used to map
   *        {@link AttachmentTarget}s to {@link RestEndpoint}s. Passing null is
   *        not permitted.
   * @param operationResultReader the {@link OperationResult} reader that is
   *        used to convert {@link FileModificationResponseEntity}s to
   *        {@link OperationResult}s
   * @param fileEntityWriter the {@link FileEntity} writer used to convert
   *        {@link File}s to {@link FileEntity}s
   * @param username the name of the user to authenticate with
   * @param password the password of the user as byte array (do not store
   *        passwords in {@link String}s for security reasons)
   * 
   */
  public RestFileAttachmentClientService(
      URI restRoot,
      AttachmentTargetRestEndpointRegistry restEndpointRegistry,
      EntityReader<OperationResult, FileModificationResponseEntity> operationResultReader,
      EntityWriter<File, FileEntity> fileEntityWriter, String username,
      byte[] password) {

    if (restRoot == null) {
      throw new IllegalArgumentException("A REST root URI must be specified");
    }
    this.restRoot = restRoot;

    if (restEndpointRegistry == null) {
      throw new IllegalArgumentException(
          "A REST endpoint registry must be specified");
    }
    this.restEndpointRegistry = restEndpointRegistry;

    this.username = username;
    this.password = password;

    if (operationResultReader == null) {
      throw new IllegalArgumentException(
          "An operation result reader must be specfied");
    }
    this.operationResultReader = operationResultReader;

    if (fileEntityWriter == null) {
      throw new IllegalArgumentException(
          "A file entity wirter must be specfied");
    }
    this.fileEntityWriter = fileEntityWriter;

    this.clientBuilder = ClientBuilder.newBuilder();
    clientBuilder.register(EntityMessageBodyWriter.class);
    clientBuilder.register(EntityMessageBodyReader.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * FileAttachmentClientService
   * #attachFile(com.eclipsesource.gerrit.plugins.fileattachment.api.File,
   * com.eclipsesource
   * .gerrit.plugins.fileattachment.api.AttachmentTargetDescription)
   */
  @Override
  public OperationResult attachFile(File file,
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException, InvalidAttachmentTargetException,
      InvalidFileException, RequestException, ResponseException {


    // create client
    Client client = clientBuilder.build();

    // add digest authentication
    HttpAuthenticationFeature authFeature =
        HttpAuthenticationFeature.digest(username, password);
    client.register(authFeature);

    // create root target
    WebTarget rootTarget = client.target(restRoot);

    // obtain REST endpoint information
    RestEndpoint restEndpoint =
        restEndpointRegistry.getRestEndpoint(attachmentTargetDescription);
    if (restEndpoint == null) {
      // no REST Endpoint found for the given target
      throw new InvalidAttachmentTargetException(attachmentTargetDescription,
          "Could not find a registered rest endpoint for the attachment target");
    }

    // create request
    WebTarget attachmentWebTarget = rootTarget.path(restEndpoint.getPath());
    Response response = null;
    try {

      Entity<FileEntity> fileEntity =
          Entity.entity(fileEntityWriter.toEntity(file),
              MediaType.APPLICATION_JSON_TYPE
                  .withCharset(StandardCharsets.UTF_8.name()));
      response = attachmentWebTarget.request().put(fileEntity);
    } catch (ResponseProcessingException rpe) {
      throw new ResponseException(
          "Internal error: An error occured during response processing.", rpe);
    } catch (ProcessingException pe) {
      throw new ResponseException(
          "Internal error: An error occured during response processing.", pe);
    }

    OperationResult result = null;
    if (response != null) {

      // handle response
      result = GenericOperationResult.getSuccessResult();

      if (response.hasEntity()) {
        try {
          result =
              operationResultReader.toObject(response
                  .readEntity(FileModificationResponseEntity.class));
        } catch (ProcessingException pe) {
          throw new ResponseException(
              MessageFormat.format(
                  "Internal error: Could not find an entity reader for the class {0}",
                  FileModificationResponseEntity.class), pe);
        }
      }
    } else {
      // this should never happen as the response cannot be null if no
      // exception occurs
      throw new ResponseException(
          "Internal error: Could not process put request, response was null");
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * FileAttachmentClientService
   * #getAttachmentTarget(com.eclipsesource.gerrit.plugins
   * .fileattachment.api.AttachmentTargetDescription)
   */
  @Override
  public AttachmentTarget getAttachmentTarget(
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException, RequestException, ResponseException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * FileAttachmentClientService
   * #getFile(com.eclipsesource.gerrit.plugins.fileattachment
   * .api.FileDescription,
   * com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription
   * )
   */
  @Override
  public File getFile(FileDescription fileDescription,
      AttachmentTargetDescription attachmentTargetDescripton)
      throws FileAttachmentClientException, RequestException, ResponseException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * FileAttachmentClientService
   * #deleteFile(com.eclipsesource.gerrit.plugins.fileattachment.api.File)
   */
  @Override
  public OperationResult deleteFile(File file)
      throws FileAttachmentClientException, IllegalArgumentException,
      RequestException, ResponseException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.eclipsesource.gerrit.plugins.fileattachment.api.client.
   * FileAttachmentClientService
   * #deleteFile(com.eclipsesource.gerrit.plugins.fileattachment
   * .api.FileDescription,
   * com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription
   * )
   */
  @Override
  public OperationResult deleteFile(FileDescription fileDescription,
      AttachmentTargetDescription attachmentTargetDescription)
      throws FileAttachmentClientException, RequestException, ResponseException {
    // TODO Auto-generated method stub
    return null;
  }

}
