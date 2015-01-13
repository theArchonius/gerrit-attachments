# Gerrit File Attachment plugin (Proof of Concept)

A gerrit plugin that allows attaching of text files to any file of a patch set.
These file attachments will be stored within the git repository on the gerrit server.

This plugin is currently just a proof of concept and under active development, so don't use it in a production environment.

# Supported Gerrit Versions

This plugin currently supports only **Gerrit v2.10-rc0**.

# Building the Plugin

To build the gerrit plugin, you have to use [Buck build tool](http://facebook.github.io/buck/) as this is currently the preferred way of building Gerrit plugins.
Although there exists a pom.xml file you **cannot use Maven to build the gerrit plugin**.
Additionally you have to do check out the gerrit tree or use the gerrit bucklets:

## Building using the Gerrit Tree

 1. First clone & check out the desired version of gerrit tree (for a list of supported versions see the section *Supported Gerrit versions*) from the gerrit repository.
 2. Clone and checkout the desired version of this plugin into the *plugins* directory of the checked out gerrit tree
 3. Exclude your local plugin repository from your local gerrit repository 

      echo /plugins/fileattachment >>.git/info/exclude 

 4. build the plugin using 

      buck build //plugins/fileattachment:fileattachment 

The plugin jar file can be found afterwards in the `buck-out/gen/plugins/fileattachment` directory.

## Building using Gerrit Bucklets

**Warning: This way of building has not been thoroughly tested yet**

 1. Clone and checkout the desired version of this repository
 2. Clone and checkout the desired version of the Gerrit Bucklets into your previosuly created local repository

       git clone https://gerrit.googlesource.com/bucklets

 3. Exclude your local bucklet repository from your local plugin repository 

      echo /plugins/fileattachment >>.git/info/exclude 
      
 4. build the plugin using 

      buck build :fileattachment 
      
The plugin jar file can be found afterwards in the `buck-out/gen/` directory.


# Building the API

You have two options to build the API: either you use Buck or you use Maven.
If you want to build & install the API library with Maven simply build it with

    maven clean install
    
If you use Buck, follow the steps in the *Building the Plugin* section and use the rule `:api` instead of `:fileattachment` in the build step.

    

# License

See [LICENSE](https://github.com/theArchonius/gerrit-attachments/blob/master/LICENSE)
