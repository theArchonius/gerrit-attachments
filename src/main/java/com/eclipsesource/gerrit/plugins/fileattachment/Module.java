// Copyright (C) 2014 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.eclipsesource.gerrit.plugins.fileattachment;

import static com.google.gerrit.server.change.FileResource.FILE_KIND;

import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.inject.AbstractModule;

/**
 * A example module to test out the possibilities of the gerrit plugin API
 *
 * @author Florian Zoubek
 *
 */
class Module extends AbstractModule {
  @Override
  protected void configure() {
    DynamicSet.bind(binder(), FileAttachmentService.class).to(GitFileAttachmentService.class);
    bind(FileAttachmentService.class).to(GitFileAttachmentService.class);
    install(new RestApiModule() {
      @Override
      protected void configure() {
        put(FILE_KIND, "file").to(AttachFileAction.class);
        get(FILE_KIND, "files").to(ListFilesAction.class);
        delete(FILE_KIND, "file").to(DeleteAttachedFileAction.class);
      }
    });
  }
}
