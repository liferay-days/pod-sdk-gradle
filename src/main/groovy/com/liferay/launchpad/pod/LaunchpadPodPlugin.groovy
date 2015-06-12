package com.liferay.launchpad.pod;

import org.gradle.api.*;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.bundling.*;

class LaunchpadPodPlugin implements Plugin<Project> {
    void apply(Project project) {

//-------------------------------------------------------------------

//jar {
//	baseName = 'launchpad'
//	appendix = project.name
//}

/**
 * Prepares pod dependencies.
 */
project.task('bundleLibs', type: Copy) {
	from (project.configurations.compile + project.configurations.runtime - project.configurations.provided) into 'lib'
	into 'build/podlibs'
}

/**
 * Builds pod bundle.
 */
project.task('bundle', type: Zip, dependsOn: 'jar', overwrite: true) {
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
project.task('fastdev', dependsOn: 'bundleLibs') {
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

		println ""
		println target
		println "----------"
		println data
		println "----------"
	}
}

//-------------------------------------------------------------------

    }
}