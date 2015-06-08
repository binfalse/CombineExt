package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.net.URI;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatParser;

public class SbmlFormatizer extends FormatParser {
	
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
			SBMLDocument doc = SBMLReader.read (file);
			return buildUri (IDENTIFIERS_BASE, "sbml.level-" + doc.getLevel ()
				+ ".version-" + doc.getVersion ());
		}
		catch (Exception e) {
			LOGGER.info (e, "file ", file, " seems to be no sbml file..");
		}
		
		// no format could be guessed
		return null;
	}

}
