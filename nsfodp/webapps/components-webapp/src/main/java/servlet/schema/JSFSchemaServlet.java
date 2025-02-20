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
package servlet.schema;

import javax.servlet.annotation.WebServlet;

@WebServlet("/f.xsd")
public class JSFSchemaServlet extends AbstractSchemaServlet {
	private static final long serialVersionUID = 1L;

	public JSFSchemaServlet() {
		super("http://www.ibm.com/xsp/jsf/core");
	}
}
