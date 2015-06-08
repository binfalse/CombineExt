package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.sbolstandard.core.SBOLFactory;
import org.sbolstandard.core.SBOLValidationException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatParser;
import de.unirostock.sems.cbext.Formatizer;

public class SbolFormatizer extends FormatParser {
	
	static {
		// adds format parser to formatizer
		Formatizer.addFormatParser( new SbolFormatizer() );
	}
	
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
			SBOLFactory.read (new FileInputStream (file));
			return buildUri (IDENTIFIERS_BASE, "sbol");
		}
		catch (IOException | SBOLValidationException e) {
			LOGGER.info (e, "file ", file, " seems to be no sbol file..");
		}
		
		// no format could be guessed
		return null;
	}

}
