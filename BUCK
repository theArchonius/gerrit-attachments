include_defs('//bucklets/gerrit_plugin.bucklet')
include_defs('//bucklets/maven_jar.bucklet')

gerrit_plugin(
  name = 'fileattachment',
  srcs = glob(['src/main/java/**/*.java'], excludes=['src/main/java/com.eclipsesource/gerrit/plugins/fileattachment/api/client/**/*.java']),
  resources = glob(['src/main/**/*']),
  manifest_entries = [
    'Gerrit-PluginName: fileattachment',
    'Gerrit-ApiType: plugin',
    'Gerrit-ApiVersion: 2.10-rc0',
    'Gerrit-Module: com.eclipsesource.gerrit.plugins.fileattachment.Module',
    'Gerrit-SshModule: com.eclipsesource.gerrit.plugins.fileattachment.SshModule',
    'Gerrit-HttpModule: com.eclipsesource.gerrit.plugins.fileattachment.HttpModule',
  ],
)

# this is required for bucklets/tools/eclipse/project.py to work
java_library(
  name = 'classpath',
  deps = [':fileattachment__plugin',':jersey-client'],
)

# fileattachment client api
java_library(
  name = 'api',
  srcs = glob([
    'src/main/java/com.eclipsesource/gerrit/plugins/fileattachment/api/**/*.java',
  ]),
  deps = [':jersey-client']
)

maven_jar(
  name = 'jersey-client',
  id = 'org.glassfish.jersey.core:jersey-client:2.14',
  license = 'CCDL1.1-GPL2'
)
