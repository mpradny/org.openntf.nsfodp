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

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal which generates an "odp" folder (replacing any existing in the directory or project)
 * from an NSF path within the context of a Maven project.
 * 
 * @author Jesse Gallagher
 * @since 1.4.0
 */
@Mojo(name="generate-odp")
public class GenerateODPMojo extends AbstractExportMojo {

}
