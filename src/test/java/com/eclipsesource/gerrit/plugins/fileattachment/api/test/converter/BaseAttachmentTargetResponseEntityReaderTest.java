/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.test.converter;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetEntity.TargetType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileDescriptionEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.OperationResultEntity.ResultStatus;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.converter.BaseAttachmentTargetResponseEntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.ChangeTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.GenericFileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchSetTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.impl.PatchTargetDescription;

/**
 * @author Florian Zoubek
 *
 */
public class BaseAttachmentTargetResponseEntityReaderTest {

  /**
   * tests if the method
   * {@link BaseAttachmentTargetResponseEntityReader#toObject(AttachmentTargetResponseEntity, com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription)}
   * correctly handles Success responses with file descriptions.
   */
  @Test
  public void testSuccessConversionWithFiles() {
    BaseAttachmentTargetResponseEntityReader entityReader =
        new BaseAttachmentTargetResponseEntityReader();


    // build expected result
    PatchTargetDescription patchTargetDescription =
        new PatchTargetDescription(new PatchSetTargetDescription(
            new ChangeTargetDescription("I0000000000000000"), 0),
            "/project/README");

    List<FileDescription> expectedFileDescriptions =
        new ArrayList<FileDescription>(2);

    expectedFileDescriptions.add(new GenericFileDescription("/subdir/",
        "file1.txt"));
    expectedFileDescriptions.add(new GenericFileDescription("/subdir/",
        "file2.txt"));

    AttachmentTarget expectedAttachmentTarget =
        new PatchTarget(patchTargetDescription, expectedFileDescriptions);

    OperationResultEntity operationResultEntity =
        new OperationResultEntity(ResultStatus.SUCCESS, "");


    // build input parameters
    FileDescriptionEntity[] fileDescriptionEntities =
        new FileDescriptionEntity[2];

    fileDescriptionEntities[0] =
        new FileDescriptionEntity("/subdir/", "file1.txt");
    fileDescriptionEntities[1] =
        new FileDescriptionEntity("/subdir/", "file2.txt");

    AttachmentTargetEntity attachmentTargetEntity =
        new AttachmentTargetEntity(TargetType.PATCH, fileDescriptionEntities);

    AttachmentTargetResponseEntity jsonEntity =
        new AttachmentTargetResponseEntity(attachmentTargetEntity,
            operationResultEntity);


    // begin testing
    AttachmentTarget attachmentTarget =
        entityReader.toObject(jsonEntity, patchTargetDescription);

    Assert.assertThat(attachmentTarget,
        CoreMatchers.is(expectedAttachmentTarget));
  }

  /**
   * tests if the method
   * {@link BaseAttachmentTargetResponseEntityReader#toObject(AttachmentTargetResponseEntity, com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription)}
   * correctly handles Success responses without file descriptions.
   */
  @Test
  public void testSuccessConversionWithoutFiles() {
    BaseAttachmentTargetResponseEntityReader entityReader =
        new BaseAttachmentTargetResponseEntityReader();


    // build expected result
    PatchTargetDescription patchTargetDescription =
        new PatchTargetDescription(new PatchSetTargetDescription(
            new ChangeTargetDescription("I0000000000000000"), 0),
            "/project/README");

    List<FileDescription> expectedFileDescriptions =
        new ArrayList<FileDescription>(0);

    AttachmentTarget expectedAttachmentTarget =
        new PatchTarget(patchTargetDescription, expectedFileDescriptions);

    OperationResultEntity operationResultEntity =
        new OperationResultEntity(ResultStatus.SUCCESS, "");


    // build input parameters
    FileDescriptionEntity[] fileDescriptionEntities =
        new FileDescriptionEntity[0];

    AttachmentTargetEntity attachmentTargetEntity =
        new AttachmentTargetEntity(TargetType.PATCH, fileDescriptionEntities);

    AttachmentTargetResponseEntity jsonEntity =
        new AttachmentTargetResponseEntity(attachmentTargetEntity,
            operationResultEntity);


    // begin testing
    AttachmentTarget attachmentTarget =
        entityReader.toObject(jsonEntity, patchTargetDescription);

    Assert.assertThat(attachmentTarget,
        CoreMatchers.is(expectedAttachmentTarget));
  }

  /**
   * tests if the method
   * {@link BaseAttachmentTargetResponseEntityReader#toObject(AttachmentTargetResponseEntity, com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription)}
   * correctly handles unsupported attachment targets correctly.
   */
  @Test
  public void testInvalidAttachmentTargetConversion() {
    BaseAttachmentTargetResponseEntityReader entityReader =
        new BaseAttachmentTargetResponseEntityReader();

    // build input parameters
    OperationResultEntity operationResultEntity =
        new OperationResultEntity(ResultStatus.SUCCESS, "");

    FileDescriptionEntity[] fileDescriptionEntities =
        new FileDescriptionEntity[0];

    AttachmentTargetEntity attachmentTargetEntity =
        new AttachmentTargetEntity(TargetType.PATCH, fileDescriptionEntities);

    AttachmentTargetResponseEntity jsonEntity =
        new AttachmentTargetResponseEntity(attachmentTargetEntity,
            operationResultEntity);

    // begin testing

    // PatchSetTargetDescription
    AttachmentTargetDescription attachmentTargetDescription =
        new PatchSetTargetDescription(new ChangeTargetDescription(
            "I0000000000000000"), 0);

    AttachmentTarget attachmentTarget =
        entityReader.toObject(jsonEntity, attachmentTargetDescription);

    Assert.assertThat(attachmentTarget,
        CoreMatchers.is(CoreMatchers.nullValue()));

    // ChangeTargetDescription
    attachmentTargetDescription =
        new ChangeTargetDescription("I0000000000000000");

    Assert.assertThat(attachmentTarget,
        CoreMatchers.is(CoreMatchers.nullValue()));

  }

  /**
   * tests if the method
   * {@link BaseAttachmentTargetResponseEntityReader#toObject(AttachmentTargetResponseEntity, com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription)}
   * correctly handles responses containing a failed operation result.
   */
  @Test
  public void testFailedOperationConversion() {
    BaseAttachmentTargetResponseEntityReader entityReader =
        new BaseAttachmentTargetResponseEntityReader();
    
    OperationResultEntity operationResultEntity =
        new OperationResultEntity(ResultStatus.FAILED, "Some error occured");

    FileDescriptionEntity[] fileDescriptionEntities =
        new FileDescriptionEntity[2];
    fileDescriptionEntities[0] =
        new FileDescriptionEntity("/subdir/", "file1.txt");
    fileDescriptionEntities[1] =
        new FileDescriptionEntity("/subdir/", "file2.txt");
    for (TargetType targetType : TargetType.values()) {

      // build input parameters

      AttachmentTargetEntity attachmentTargetEntityWithFiles =
          new AttachmentTargetEntity(targetType, new FileDescriptionEntity[0]);

      AttachmentTargetResponseEntity jsonEntityWithFiles =
          new AttachmentTargetResponseEntity(attachmentTargetEntityWithFiles,
              operationResultEntity);

      AttachmentTargetEntity attachmentTargetEntityWithoutFiles =
          new AttachmentTargetEntity(targetType, new FileDescriptionEntity[0]);

      AttachmentTargetResponseEntity jsonEntityWithoutFiles =
          new AttachmentTargetResponseEntity(
              attachmentTargetEntityWithoutFiles, operationResultEntity);

      AttachmentTargetDescription attachmentTargetDescription =
          new PatchTargetDescription(new PatchSetTargetDescription(
              new ChangeTargetDescription("I0000000000000000"), 0),
              "/project/README");

      // begin testing

      // without files
      AttachmentTarget attachmentTarget =
          entityReader.toObject(jsonEntityWithoutFiles,
              attachmentTargetDescription);

      Assert.assertThat("Unexpected result for failed operation with entity: "
          + jsonEntityWithoutFiles.toString(), attachmentTarget,
          CoreMatchers.is(CoreMatchers.nullValue()));

      // with files
      attachmentTarget =
          entityReader.toObject(jsonEntityWithFiles,
              attachmentTargetDescription);

      Assert.assertThat("Unexpected result for failed operation with entity: "
          + jsonEntityWithFiles.toString(), attachmentTarget,
          CoreMatchers.is(CoreMatchers.nullValue()));

    }
  }
  
  /**
   * tests if the method
   * {@link BaseAttachmentTargetResponseEntityReader#toObject(AttachmentTargetResponseEntity, com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription)}
   * correctly handles responses containing a "not permitted" operation result.
   */
  @Test
  public void testNotPermittedOperationConversion() {
    BaseAttachmentTargetResponseEntityReader entityReader =
        new BaseAttachmentTargetResponseEntityReader();
    
    OperationResultEntity operationResultEntity =
        new OperationResultEntity(ResultStatus.NOTPERMITTED, "Some error occured");

    FileDescriptionEntity[] fileDescriptionEntities =
        new FileDescriptionEntity[2];
    fileDescriptionEntities[0] =
        new FileDescriptionEntity("/subdir/", "file1.txt");
    fileDescriptionEntities[1] =
        new FileDescriptionEntity("/subdir/", "file2.txt");
    for (TargetType targetType : TargetType.values()) {

      // build input parameters

      AttachmentTargetEntity attachmentTargetEntityWithFiles =
          new AttachmentTargetEntity(targetType, new FileDescriptionEntity[0]);

      AttachmentTargetResponseEntity jsonEntityWithFiles =
          new AttachmentTargetResponseEntity(attachmentTargetEntityWithFiles,
              operationResultEntity);

      AttachmentTargetEntity attachmentTargetEntityWithoutFiles =
          new AttachmentTargetEntity(targetType, new FileDescriptionEntity[0]);

      AttachmentTargetResponseEntity jsonEntityWithoutFiles =
          new AttachmentTargetResponseEntity(
              attachmentTargetEntityWithoutFiles, operationResultEntity);

      AttachmentTargetDescription attachmentTargetDescription =
          new PatchTargetDescription(new PatchSetTargetDescription(
              new ChangeTargetDescription("I0000000000000000"), 0),
              "/project/README");

      // begin testing

      // without files
      AttachmentTarget attachmentTarget =
          entityReader.toObject(jsonEntityWithoutFiles,
              attachmentTargetDescription);

      Assert.assertThat("Unexpected result for failed operation with entity: "
          + jsonEntityWithoutFiles.toString(), attachmentTarget,
          CoreMatchers.is(CoreMatchers.nullValue()));

      // with files
      attachmentTarget =
          entityReader.toObject(jsonEntityWithFiles,
              attachmentTargetDescription);

      Assert.assertThat("Unexpected result for failed operation with entity: "
          + jsonEntityWithFiles.toString(), attachmentTarget,
          CoreMatchers.is(CoreMatchers.nullValue()));

    }
  }

}
