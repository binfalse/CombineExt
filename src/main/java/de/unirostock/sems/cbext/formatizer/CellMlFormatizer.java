package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.cellml.algorithm.CellMLValidator;
import de.unirostock.sems.cbext.FormatParser;
import de.unirostock.sems.cbext.Formatizer;

public class CellMlFormatizer extends FormatParser {
	
	static {
		// adds format parser to formatizer
		Formatizer.addFormaParser( new CellMlFormatizer() );
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
			CellMLValidator validator = new CellMLValidator ();
			if (!validator.validate (file))
				throw new IOException ("error parsing cellml doc: "
					+ validator.getError ().getMessage ());
			return buildUri (IDENTIFIERS_BASE, "cellml");
		}
		catch (IOException e) {
			LOGGER.info (e, "file ", file, " seems to be no cellml file..");
		}
		
		// no format could be guessed
		return null;
	}

}
