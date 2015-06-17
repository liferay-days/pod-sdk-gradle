package com.liferay.launchpad.sdk;

import org.gradle.api.*;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.bundling.*;

class LaunchpadPodPlugin implements Plugin<Project> {
    void apply(Project project) {

//-------------------------------------------------------------------

/**
 * Prepares pod dependencies (libraries).
 */
project.task('podLibs', type: Copy) {
	from (project.configurations.compile + project.configurations.runtime - project.configurations.provided) into 'lib'
	into 'build/podlibs'
}

/**
 * Builds pod bundle.
 */
project.task('pod', type: Zip, dependsOn: 'jar', overwrite: true,
		description: 'Builds POD bundle distribution', group: 'Launchpad Pod') {

	baseName = project.name

	if (baseName.startsWith('pod-')) baseName = baseName.substring(4);

	extension = 'pod'

	from('src/main/webapp/') {
		into '/web'
	}

	from('src/main/assets/') {
		into '/assets'
	}

	from('src/main/js/') {
		into '/js'
	}

	from('src/main/config/') {
		into '/'
	}

	from (project.configurations.compile - project.configurations.provided) into 'lib'
	from project.jar.outputs.files into 'lib'
}

/**
 * Prepares data for fast development.
 */
project.task('pod-link', dependsOn: 'podLibs',
		description: 'Enables POD fast development', group: 'Launchpad Pod') {
	doLast { task ->
		def prj = task.project
		def name = task.project.name
		def data = """
{
	"path.config"     : "${prj.rootDir}/${name}/src/main/config",
	"path.javascript" : "${prj.rootDir}/${name}/src/main/js",
	"path.web"        : "${prj.rootDir}/${name}/src/main/webapp",
	"path.assets"     : "${prj.rootDir}/${name}/src/main/assets",
	"path.lib" : [
		"${prj.rootDir}/${name}/build/classes/main",
		"${prj.rootDir}/${name}/build/resources/main",
		"${prj.rootDir}/${name}/build/podlibs"
	]
}
"""

		def fileName = name
		if (fileName.startsWith('pod-')) fileName = fileName.substring(4)
		def target = prj.rootDir.absolutePath + '/' + name + '/build/' + fileName + ".pod.json"
		def file = new File(target)
		file.write data

		file = new File(System.getProperty("user.home"), 'launchpad')
		file.write data

		println "POD $name linked"
	}
}

//-------------------------------------------------------------------

    }
}