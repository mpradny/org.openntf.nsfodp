/**
 * Copyright © 2018-2021 Jesse Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.maven.nsfodp;

import java.io.File;
import java.net.URL;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractEquinoxMojo extends AbstractMojo {
	@Parameter(defaultValue="${project}", readonly=true, required=false)
	protected MavenProject project;

	@Component
	protected WagonManager wagonManager;
	
	@Parameter(defaultValue="${plugin}", readonly=true)
	protected PluginDescriptor pluginDescriptor;
	
	@Parameter(defaultValue="${session}", readonly=true)
	protected MavenSession mavenSession;
	
	/**
	 * The path to a Notes or Domino executable directory to allow for local
	 * compilation.
	 * 
	 * <p>This must be paired with {@code notesPlatform}.</p>
	 */
	@Parameter(property="notes-program", required=false)
	protected File notesProgram;
	/**
	 * The path to a Domino OSGi runtime directory to allow for local
	 * compilation.
	 * 
	 * <p>This must be paired with {@code notesPlatform}.</p>
	 * @see <a href="https://stash.openntf.org/projects/P2T/repos/generate-domino-update-site/browse">
	 * 		https://stash.openntf.org/projects/P2T/repos/generate-domino-update-site/browse
	 * 		</a>
	 */
	@Parameter(property="notes-platform")
	protected URL notesPlatform;
	
	/**
	 * The path to the notes.ini file to use on launch. This is primarily of importance for local
	 * compilation on Linux systems, where the INI file is not reliably located by the runtime.
	 * 
	 * @since 3.0.0
	 */
	@Parameter(property="notes-ini", required=false)
	protected File notesIni;
	
	/**
	 * Sets the project to require server execution even if a local environment is
	 * available.
	 * 
	 * <p>This may be useful for project that use LS2J, which can crash the Equinox JVM during
	 * compilation.</p>
	 */
	@Parameter(property="nsfodp.requireServerExecution", required=false)
	protected boolean requireServerExecution = false;
	
	/**
	 * Sets process arguments to append to the Equinox JVM launcher, such as debug options.
	 * @since 3.5.0
	 */
	@Parameter(property="nsfodp.equinoxJvmArgs", required=false)
	protected String equinoxJvmArgs;

	protected boolean isRunLocally() {
		return notesProgram != null && notesPlatform != null && !requireServerExecution;
	}
}
