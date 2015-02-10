include_defs('//bucklets/gerrit_plugin.bucklet')
include_defs('//bucklets/maven_jar.bucklet')

gerrit_plugin(
  name = 'fileattachment',
  srcs = glob(['src/main/java/**/*.java'], excludes=['src/main/java/com/eclipsesource/gerrit/plugins/fileattachment/api/client/**/*.java']),
  resources = glob(['src/main/**/*']),
  manifest_entries = [
    'Gerrit-PluginName: fileattachment',
    'Gerrit-ApiType: plugin',
    'Gerrit-ApiVersion: 2.10-rc0',
    'Gerrit-Module: com.eclipsesource.gerrit.plugins.fileattachment.Module',
    'Gerrit-SshModule: com.eclipsesource.gerrit.plugins.fileattachment.SshModule',
    'Gerrit-HttpModule: com.eclipsesource.gerrit.plugins.fileattachment.HttpModule',
  ],
  deps = ['//lib/commons:codec'],
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
    'src/main/java/com/eclipsesource/gerrit/plugins/fileattachment/api/**/*.java',
  ]),
  deps = [':jersey-client', ':gson', ':javax.ws.rs-api', ':commons-codec']
)

# api tests

java_test(
  name = 'api-test',
  srcs = glob([
    'src/test/java/com/eclipsesource/gerrit/plugins/fileattachment/api/test/**/*Test.java',
  ]),
  resources = glob(['src/test/resources/**/*']),
  deps = [':api', ':junit', ':hamcrest', ':javax.ws.rs-api']
)

# dependencies

maven_jar(
  name = 'jersey-client',
  id = 'org.glassfish.jersey.core:jersey-client:2.14',
  license = 'CCDL1.1-GPL2',
  deps = [':jersey-common', ':javax.ws.rs-api']
)

maven_jar(
  name = 'gson',
  id = 'com.google.code.gson:gson:2.3.1',
  license = 'Apache2.0'
)

maven_jar(
  name = 'jersey-common',
  id = 'org.glassfish.jersey.core:jersey-common:2.14',
  license = 'GPL2'
)

maven_jar(
  name = 'javax.ws.rs-api',
  id = 'javax.ws.rs:javax.ws.rs-api:2.0.1',
  license = 'GPL2'
)

maven_jar(
  name = 'commons-codec',
  id = 'commons-codec:commons-codec:1.10',
  license = 'Apache2.0'
)

maven_jar(
  name = 'junit',
  id = 'junit:junit:4.11',
  license = 'EPLv1.0'
)

maven_jar(
  name = 'hamcrest',
  id = 'org.hamcrest:hamcrest-all:1.3',
  license = 'BSDv3'
)
