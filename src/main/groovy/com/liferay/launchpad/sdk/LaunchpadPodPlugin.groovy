package com.liferay.launchpad.sdk

import jodd.http.HttpRequest
import jodd.http.HttpResponse
import jodd.io.FileUtil;
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
		description: 'Builds POD bundle', group: 'Launchpad Pod') {

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

	doLast { task ->
		def prj = task.project
		def name = task.project.name
		def podName = name
		if (podName.startsWith('pod-')) {
			podName = podName.substring(4)
		}

		def podFile = new File(prj.rootDir.absolutePath + '/' + name + '/build/distributions/' + podName + '.pod')
		def targetDir = new File(System.getProperty("user.home"), 'launchpad')

		FileUtil.copyFileToDir(podFile, targetDir)

		println "\nPOD \"$podName\" created."
	}
}

/**
 * Prepares data for fast development.
 */
project.task('link', dependsOn: 'podLibs',
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
		def podName = name
		if (podName.startsWith('pod-')) {
			podName = podName.substring(4)
		}

		def fileName = podName + ".pod.json"

		def file = new File(prj.rootDir.absolutePath + '/' + name + '/build/' + fileName)
		file.write data

		file = new File(System.getProperty("user.home") + '/launchpad', fileName)
		file.write data

		println "\nPOD \"$podName\" linked."
	}
}

/**
 * Removes fast development config.
 */
project.task('unlink',
	description: 'Disables POD fast development', group: 'Launchpad Pod') {

	doLast { task ->
		def name = task.project.name
		def podName = name
		if (podName.startsWith('pod-')) {
			podName = podName.substring(4)
		}

		def fileName = podName + ".pod.json"

		def file = new File(System.getProperty("user.home") + '/launchpad', fileName)
		file.delete()

		println "\nPOD \"$podName\" unlinked."
	}
}

/**
 * Deploys a POD.
 */
project.task('deploy',
	description: 'Deploys POD', group: 'Launchpad Pod') {

	doLast { task ->
		def name = task.project.name
		def podName = name
		if (podName.startsWith('pod-')) {
			podName = podName.substring(4)
		}

		HttpResponse response =
			HttpRequest
			.get('http://127.0.0.1:8080/_admin')
			.query('cmd', 'deploy')
			.query('target', podName)
			.timeout(5000)
			.send();

		int statusCode = response.statusCode();

		println "\nDeploy POD \"$podName\" : $statusCode"
	}
}

project.task('undeploy',
	description: 'Undeploys POD', group: 'Launchpad Pod') {

	doLast { task ->
		def name = task.project.name
		def podName = name
		if (podName.startsWith('pod-')) {
			podName = podName.substring(4)
		}

		HttpResponse response =
			HttpRequest
			.get('http://127.0.0.1:8080/_admin')
			.query('cmd', 'undeploy')
			.query('target', podName)
			.timeout(5000)
			.send();

		int statusCode = response.statusCode();

		println "\nUndeploy POD \"$podName\" : $statusCode"
	}
}

//-------------------------------------------------------------------

    }
}