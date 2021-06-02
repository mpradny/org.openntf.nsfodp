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
package org.openntf.nsfodp.commons.odp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

import org.openntf.nsfodp.commons.NSFODPUtil;
import org.openntf.nsfodp.commons.dxl.DXLUtil;
import org.openntf.nsfodp.commons.dxl.ODSConstants;
import org.openntf.nsfodp.commons.h.Ods;
import org.openntf.nsfodp.commons.odp.util.ODPUtil;
import org.w3c.dom.Document;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;

/**
 * Represents an image resource in the ODP.
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class ImageResource extends FileResource {

	public ImageResource(Path dataFile) {
		super(dataFile);
	}
	
	@Override
	public String getFileDataItem() {
		return "$ImageData"; //$NON-NLS-1$
	}
	
	@Override
	public String getFileSizeItem() {
		return null;
	}
	
	@Override
	protected Document attachFileData(Document dxlDoc) throws IOException, XMLException {
		byte[] data = getCompositeData();
		String itemName = getFileDataItem();
		
		DXLUtil.writeItemDataRaw(dxlDoc, itemName, data, ODSConstants.PER_IMAGE_ITEM_DATA_CAP, Ods.SIZE_CDIMAGEHEADER + Ods.SIZE_CDGRAPHIC);
		
		return dxlDoc;
	}

	@Override
	public byte[] getCompositeData() throws IOException, XMLException {
		Path file = getDataFile();
		if(!Files.isRegularFile(file)) {
			throw new IllegalArgumentException(MessageFormat.format(Messages.AbstractSplitDesignElement_cannotReadFile, file));
		}
		
		// Work around trouble where reading the XML from Files.newInputStream(p) only
		//   reads one WORD length properly before trailing off into nulls.
		// Observed in Domino V12b3 on Windows		
		Path dxlTempDxl = Files.createTempFile("dxl", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
		Files.copy(getDxlFile(), dxlTempDxl, StandardCopyOption.REPLACE_EXISTING);
		try(InputStream isDxl = Files.newInputStream(dxlTempDxl)) {
			Document dxlDoc = DOMUtil.createDocument(isDxl);
					
			Path dxlTemp = Files.createTempFile("dxl", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				Files.copy(file, dxlTemp, StandardCopyOption.REPLACE_EXISTING);
				try(InputStream is = Files.newInputStream(dxlTemp)) {
					return DXLUtil.getImageResourceData(dxlTemp, dxlDoc);
				}
			} finally {
				NSFODPUtil.deltree(dxlTemp);
			}
		} catch(IOException | XMLException e) {
			throw new RuntimeException(e);
		}finally {
			NSFODPUtil.deltree(dxlTempDxl);
		}
		
		
	}
}
