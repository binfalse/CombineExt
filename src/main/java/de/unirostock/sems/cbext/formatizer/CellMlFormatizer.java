package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.cellml.algorithm.CellMLValidator;
import de.unirostock.sems.cbext.FormatParser;

/**
 * The Class CellMlFormatizer to recognize cellml files.
 */
public class CellMlFormatizer extends FormatParser {
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.cbext.FormatParser#getPriority()
	 */
	@Override
	public int getPriority() {
		return 100;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.cbext.FormatParser#checkFormat(java.io.File, java.lang.String)
	 */
	@Override
	public URI checkFormat(File file, String mimeType) {
		
		// mime type check
		if (mimeType == null || mimeType.equals("application/xml") == false )
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
