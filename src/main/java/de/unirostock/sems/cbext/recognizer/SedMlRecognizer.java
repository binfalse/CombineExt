package de.unirostock.sems.cbext.recognizer;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;



/**
 * The Class SedMlFormatizer to recognize sed-ml files.
 */
public class SedMlRecognizer
	extends FormatRecognizer
{
	
	/**
	 * The priority of this format parser.
	 * 
	 * The higher the priority, the earlier this format parser gets called. The
	 * first format parser, which is able to map this extension, determines it's
	 * format. Priority should not be negative. Default format parsers have a
	 * priority around 100.
	 * 
	 * If you need to change the priority of this parser you should resort the
	 * parsers in the Formatizer class by calling
	 * {@link de.unirostock.sems.cbext.Formatizer#resortFormatRecognizers()}.
	 */
	public static int	priority	= 100;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.FormatParser#getPriority()
	 */
	@Override
	public int getPriority ()
	{
		return priority;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.FormatParser#checkFormat(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public URI getFormatByParsing (File file, String mimeType)
	{
		
		// mime type check
		if (mimeType == null || mimeType.equals ("application/xml") == false)
			return null;
		
		try
		{
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
		catch (IOException | XMLException | org.jdom.IllegalAddException e)
		{
			LOGGER.info (e, "file ", file, " seems to be no sedml file..");
		}
		
		// no format could be guessed
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFormatFromMime(java.lang.String
	 * )
	 */
	@Override
	public URI getFormatFromMime (String mime)
	{
		// we cannot decide from just a mime type
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFormatFromExtension(java.lang
	 * .String)
	 */
	@Override
	public URI getFormatFromExtension (String extension)
	{
		if (extension.equals ("sedml"))
			return buildUri (IDENTIFIERS_BASE, "sed-ml");
		return null;
	}
	
}
