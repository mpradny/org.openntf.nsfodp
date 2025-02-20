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
package org.openntf.maven.nsfodp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.openntf.maven.nsfodp.Messages;

public enum ODPMojoUtil {
	;
	
	private static final ThreadLocal<DateFormat> SNAPSHOT_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddhhmm")); //$NON-NLS-1$
	
	/**
	 * Adds server credential information from the user's settings.xml, if applicable.
	 * 
	 * @param wagonManager the active WagonManager to use for credential lookup
	 * @param serverId the server ID to find credentials for
	 * @param req the request to add credentials to
	 * @param log the logger to use
	 * @return the effective username of the request
	 * @throws MojoExecutionException if the server ID is specified but credentials cannot be found
	 */
	public static String addAuthenticationInfo(WagonManager wagonManager, String serverId, HttpRequest req, Log log) throws MojoExecutionException {
		String userName;
		if(serverId != null && !serverId.isEmpty()) {
			// Look up credentials for the server
			AuthenticationInfo info = wagonManager.getAuthenticationInfo(serverId);
			if(info == null) {
				throw new MojoExecutionException(Messages.getString("ODPMojoUtil.couldNotFindServerCredentials", serverId)); //$NON-NLS-1$
			}
			userName = info.getUserName();
			if(userName == null || userName.isEmpty()) {
				// Then just use Anonymous
				if(log.isDebugEnabled()) {
					log.debug(Messages.getString("ODPMojoUtil.usernameIsBlank")); //$NON-NLS-1$
				}
				userName = "Anonymous"; //$NON-NLS-1$
			} else {
				if(log.isDebugEnabled()) {
					log.debug(Messages.getString("ODPMojoUtil.authenticatingAsUser", userName)); //$NON-NLS-1$
				}
				String password = info.getPassword();
				
				// Create a Basic auth header
				// This is instead of HttpClient's credential handling because of how
				//   Domino handles the auth handshake.
				String enc = Base64.encodeBase64String((userName + ":" + password).getBytes()); //$NON-NLS-1$
				req.addHeader("Authorization", "Basic " + enc); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if(log.isDebugEnabled()) {
				log.debug(Messages.getString("ODPMojoUtil.noUsernameSpecified")); //$NON-NLS-1$
			}
			userName = "Anonymous"; //$NON-NLS-1$
		}
		return userName;
	}
	
	public static String calculateVersion(MavenProject project) {
		// Use a Tycho-provided version if present; otherwise, generate one
		String version = project.getProperties().getProperty("qualifiedVersion"); //$NON-NLS-1$
		if(version == null || version.isEmpty()) {
			version = project.getVersion();
			if(version.endsWith("-SNAPSHOT")) { //$NON-NLS-1$
				version = version.substring(0, version.length()-"-SNAPSHOT".length()); //$NON-NLS-1$
				version += '.' + SNAPSHOT_FORMAT.get().format(new Date());
			}
		}
		return version;
	}
}
