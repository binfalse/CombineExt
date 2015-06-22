package de.unirostock.sems.cbext.recognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.sbolstandard.core.SBOLFactory;
import org.sbolstandard.core.SBOLValidationException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;



/**
 * The Class SbolFormatizer to recognize sbol files.
 */
public class SbolRecognizer
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
			SBOLFactory.read (new FileInputStream (file));
			return buildUri (IDENTIFIERS_BASE, "sbol");
		}
		catch (IOException | SBOLValidationException e)
		{
			LOGGER.info (e, "file ", file, " seems to be no sbol file..");
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
		if (extension != null && extension.equals ("sbol"))
			return buildUri (IDENTIFIERS_BASE, "sbol");
		return null;
	}
	
}