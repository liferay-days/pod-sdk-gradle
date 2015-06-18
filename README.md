# pod-sdk-gradle

Gradle plugin for Launchpad Pods.

## Provided scope

Gradle does not have `provided` scope out-of-box, so we must enable it:

	configurations {
		provided
		compile.extendsFrom provided
	}

Provided scope is needed for following dependencies:

+ `com.liferay.launchpad:sdk`
+ `com.liferay.launchpad:api`

However, even if you put these as `compile` dependency, the plugin will ignore
these jars and they will not be bundled in the pod.

## Usage

Enable plugin:

```
buildscript {
    repositories {
		jcenter()
    }
    dependencies {
        classpath group: 'com.liferay.launchpad', name: 'sdk-gradle', version: '0.1.1'
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
