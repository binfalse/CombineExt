package de.unirostock.sems.cbext.recognizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;



/**
 * The Class DefaultFormatRecognizer knows some common extensions to map it to
 * formats.
 */
public class DefaultRecognizer
	extends FormatRecognizer
{
	
	/** known formats file. */
	private static final String	EXT2FORMAT_NAME	= "/ext2format.prop";
	
	/** The ext2 format. */
	private Properties					ext2Format			= new Properties ();
	
	
	/**
	 * Instantiates a new default extension mapper.
	 */
	public DefaultRecognizer ()
	{
		
		try
		{
			InputStream input = DefaultRecognizer.class
				.getResourceAsStream (EXT2FORMAT_NAME);
			ext2Format.load (input);
			input.close ();
		}
		catch (IOException e)
		{
			LOGGER.error (e, "error reading known formats: ",
				DefaultRecognizer.class.getResourceAsStream (EXT2FORMAT_NAME));
		}
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.FormatRecognizer#getPriority()
	 */
	@Override
	public int getPriority ()
	{
		return 100;
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
		
		if (mime == null)
			return null;
		
		String uriString = ext2Format.getProperty (mime, null);
		if (uriString != null)
			return FormatRecognizer.buildUri (uriString, "");
		else
			return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFromatFromExtension(java.lang
	 * .String)
	 */
	@Override
	public URI getFormatFromExtension (String extension)
	{
		// extension and mime are handled equally
		return getFormatFromMime (extension);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFormatByParsing(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public URI getFormatByParsing (File file, String mimeType)
	{
		// this recognizer is not able to understand files from parsing it..
		return null;
	}
	
}
