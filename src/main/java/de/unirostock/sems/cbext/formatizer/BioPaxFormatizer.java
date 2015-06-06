package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.util.BioPaxIOException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatParser;
import de.unirostock.sems.cbext.Formatizer;

public class BioPaxFormatizer extends FormatParser {
	
	static {
		// adds format parser to formatizer
		Formatizer.addFormaParser( new BioPaxFormatizer() );
	}
	
	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public URI checkFormat(File file, String mimeType) {
		
		// mime type check
		if (mimeType == null || mimeType.equals("application/rdf+xml") )
			return null;
		
		try {
			BioPAXIOHandler handler = new org.biopax.paxtools.io.SimpleIOHandler (); // auto-detects
																																								// Level
			org.biopax.paxtools.model.Model model = handler
				.convertFromOWL (new FileInputStream (file));
			
			return buildUri (IDENTIFIERS_BASE, "biopax");
		}
		catch (IOException | BioPaxIOException e) {
			LOGGER.info (e, "file ", file, " seems to be no biopax file..");
		}
		
		// no format could be guessed
		return null;
	}

}
