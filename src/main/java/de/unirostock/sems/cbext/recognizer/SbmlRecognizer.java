package de.unirostock.sems.cbext.recognizer;

import java.io.File;
import java.net.URI;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;



/**
 * The Class SbmlFormatizer to recognize sbml files.
 */
public class SbmlRecognizer
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
			SBMLDocument doc = SBMLReader.read (file);
			return buildUri (IDENTIFIERS_BASE, "sbml.level-" + doc.getLevel ()
				+ ".version-" + doc.getVersion ());
		}
		catch (Exception e)
		{
			LOGGER.info (e, "file ", file, " seems to be no sbml file..");
		}
		
		// no format could be guessed
		return null;
	}
	
	
	@Override
	public URI getFormatFromMime (String mime)
	{
		// we cannot decide from just a mime type
		return null;
	}
	
	
	@Override
	public URI getFormatFromExtension (String extension)
	{
		if (extension != null && extension != null && extension.equals ("sbml"))
			return buildUri (IDENTIFIERS_BASE, "sbml");
		return null;
	}
	
}
