package de.unirostock.sems.cbext.recognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.util.BioPaxIOException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;



/**
 * The Class BioPaxFormatizer to recognize BioPax files.
 */
public class BioPaxRecognizer
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
		if (mimeType == null || mimeType.equals ("application/rdf+xml") == false)
			return null;
		
		try
		{
			BioPAXIOHandler handler = new org.biopax.paxtools.io.SimpleIOHandler (); // auto-detects
																																								// Level
			org.biopax.paxtools.model.Model model = handler
				.convertFromOWL (new FileInputStream (file));
			
			return buildUri (IDENTIFIERS_BASE, "biopax");
		}
		catch (IOException | BioPaxIOException e)
		{
			LOGGER.info (e, "file ", file, " seems to be no biopax file..");
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
		if (extension.equals ("biopax"))
			return buildUri (IDENTIFIERS_BASE, "biopax");
		return null;
	}
	
}
