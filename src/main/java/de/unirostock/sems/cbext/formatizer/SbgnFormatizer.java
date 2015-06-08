package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.xml.bind.JAXBException;

import org.sbgn.SbgnUtil;
import org.xml.sax.SAXException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatParser;

public class SbgnFormatizer extends FormatParser {
	
	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public URI checkFormat(File file, String mimeType) {
		
		// mime type check
		if (mimeType == null || mimeType.equals("application/xml") )
			return null;
		
		try {
			if (!SbgnUtil.isValid (file))
				throw new IOException ("error parsing sbgn doc");
			return buildUri (IDENTIFIERS_BASE, "sbgn");
		}
		catch (JAXBException | SAXException | IOException e) {
			LOGGER.info (e, "file ", file, " seems to be no sbgn file..");
		}
		
		// no format could be guessed
		return null;
	}

}
