package de.unirostock.sems.cbext.formatizer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatParser;

public class SedMlFormatizer extends FormatParser {
	
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
			SEDMLDocument doc = Libsedml.readDocument (file);
			doc.validate ();
			if (doc.hasErrors ())
			{
				StringBuilder errors = new StringBuilder ();
				for (SedMLError e : doc.getErrors ())
					if (e.getSeverity ().compareTo (SedMLError.ERROR_SEVERITY.ERROR) >= 0)
						errors.append ("[" + e.getMessage () + "]");
				if (errors.length () > 0)
					throw new IOException ("error reading sedml file: "
						+ errors.toString ());
			}
			org.jlibsedml.Version v = doc.getVersion ();
			return buildUri (IDENTIFIERS_BASE, "sed-ml.level-" + v.getLevel ()
				+ ".version-" + v.getVersion ());
		}
		catch (IOException | XMLException | org.jdom.IllegalAddException e) {
			LOGGER.info (e, "file ", file, " seems to be no sedml file..");
		}
		
		// no format could be guessed
		return null;
	}

}
