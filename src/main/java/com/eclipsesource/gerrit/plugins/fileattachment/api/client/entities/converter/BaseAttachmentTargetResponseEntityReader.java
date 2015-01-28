/**
 * 
 */
package com.eclipsesource.gerrit.plugins.fileattachment.api.client.entities.converter;

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.AttachmentTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.FileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.GenericFileDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.PatchTarget;
import com.eclipsesource.gerrit.plugins.fileattachment.api.client.model.PatchTargetDescription;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetEntity.TargetType;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.AttachmentTargetResponseEntity;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.EntityReader;
import com.eclipsesource.gerrit.plugins.fileattachment.api.entities.FileDescriptionEntity;

/**
 * An entity reader which creates {@link AttachmentTarget}s from
 * {@link AttachmentTargetEntity}s.
 * 
 * @author Florian Zoubek
 *
 */
public class BaseAttachmentTargetResponseEntityReader
    implements
    EntityReader<AttachmentTarget, AttachmentTargetResponseEntity, AttachmentTargetDescription> {

  @Override
  public AttachmentTarget toObject(AttachmentTargetResponseEntity jsonEntity,
      AttachmentTargetDescription context) {

    AttachmentTargetEntity attachmentTargetEntity =
        jsonEntity.getAttachmentTargetEntity();

    if (attachmentTargetEntity.getTargetType() == TargetType.PATCH) {

      // check if the attachment target description in the context matches the
      // type

      if (!(context instanceof PatchTargetDescription)) {
        // invalid target description
        return null;
      }

      PatchTargetDescription patchTargetDescription =
          (PatchTargetDescription) context;

      // convert file description entities to file descriptions

      FileDescriptionEntity[] fileDescriptionEntities =
          attachmentTargetEntity.getFileDescriptions();
      List<FileDescription> attachedFileDescriptions =
          new ArrayList<FileDescription>(fileDescriptionEntities.length);

      for (FileDescriptionEntity fileDescriptionEntity : fileDescriptionEntities) {
        attachedFileDescriptions.add(new GenericFileDescription(
            fileDescriptionEntity.getFilePath(), fileDescriptionEntity
                .getFileName()));
      }

      return new PatchTarget(patchTargetDescription, attachedFileDescriptions);

    }

    return null;
  }

}
