/**
 * Copyright © 2018-2019 Jesse Gallagher
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
package org.openntf.nsfodp.lsp4xml.xsp;

import org.eclipse.lsp4xml.dom.DOMDocument;
import org.openntf.nsfodp.commons.NSFODPConstants;

public enum ContentAssistUtil {
	;

	public static boolean isXsp(DOMDocument doc) {
		if (!NSFODPConstants.XP_NS.equals(doc.getDocumentElement().getNamespaceURI())) {
			return false;
		}
		return true;
	}
}