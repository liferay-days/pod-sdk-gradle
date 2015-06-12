# pod-sdk-gradle

Gradle plugin for Launchpad Pods.

## Provided scope

Gradle does not have `provided` scope out-of-box, so we must enable it:

	configurations {
		provided
		compile.extendsFrom provided
	}

Provided scope is needed for only `pod-sdk` module, that is provided by
Launchpad runtime.


## Usage

Enable plugin:

```
buildscript {
    repositories {
        mavenLocal()	// put here the maven repo!
    }
    dependencies {
        classpath group: 'com.liferay.launchpad', name: 'pod', version: '1.0-SNAPSHOT'
    }
}
```

Apply plugin:

	apply plugin: 'com.liferay.launchpad.pod'

That is all.


## Plugin tasks

This plugin adds the following tasks:

### bundle

Task `bundle` creates the pod bundle in `build/distributions` folder.
Pod bundle extension is `pod`. This file can be copied to Launchpad folder
and deployed to Launchpad.

### fastdev

Task `fastdev` prepares everything for the fast development. It does the
following:

+ prepares all JAR files needed for the bundle
+ creates `build/*.pod.json` file that can be copied to Launchpad folder to
  enable fast development
+ outputs the content of created `build/*.pod.json` file.

