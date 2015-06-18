# pod-sdk-gradle

Gradle plugin for Launchpad Pods.

## Provided scope

This plugin is aware of `provided` scope. Gradle does not offer `provided` scope
out-of-box. It can be defined like this:

	configurations {
		provided
		compile.extendsFrom provided
	}

Provided scope is needed for following dependencies:

+ `com.liferay.launchpad:sdk`
+ `com.liferay.launchpad:api`

### Provided scope is optional

Even if you put these dependencies as `compile` dependency and do not define
`provided` scope - everything will work! Plugin will ignore these jars and
they will not be bundled in the pod.


## Usage

Enable plugin:

```
buildscript {
    repositories {
		jcenter()
    }
    dependencies {
        classpath group: 'com.liferay.launchpad', name: 'sdk-gradle', version: '0.1.2'
    }
}

```

Apply plugin:

	apply plugin: 'com.liferay.launchpad.sdk'

That is all.


## Plugin tasks

This plugin adds the following tasks:

### pod

Task `pod` task creates pod bundle in `build/distributions` folder and in
`~/launchpad`. Pod bundle extension is `pod`.

### link

Task `link` does not create the pod bundle, but prepares the configuration so to
enable _fast_ development. It creates pod configuration file in `~/launchpad`
folder.

### unlink

Task `unlink` removes configuration file from the `~/launchpad` folder,
disabling _fast_ development.

### deploy

Sends HTTP request to server to deploy a pod. Pod is previously built or linked.

### undeploy

Sends HTTP request to server to undeploy a pod. Pod bundle file is not removed.
