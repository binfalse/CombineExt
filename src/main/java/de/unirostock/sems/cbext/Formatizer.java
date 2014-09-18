/**
 * 
 */
package de.unirostock.sems.cbext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Properties;

import de.binfalse.bflog.LOGGER;


/**
 * The Class Formatizer to generate formats URIs.
 *
 * @author Martin Scharm
 */
public class Formatizer
{
	/** known formats file. */
	private static final String	ext2formatFile	= "/ext2format.prop";
	
	/** known formats. */
	private static Properties		ext2format			= new Properties ();
	static
	{
		String defaultUri = "http://purl.org/NET/mediatypes/application/octet-stream";
		try
		{
			GENERIC_UNKNOWN = new URI (defaultUri);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
			LOGGER.error (e, "error generating generic default uri: ", defaultUri);
		}
		try
		{
			InputStream is = Iconizer.class.getResourceAsStream (ext2formatFile);
			if (is != null)
				ext2format.load (is);
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			LOGGER.error (e, "error reading known formats: ",
				Iconizer.class.getResourceAsStream (ext2formatFile));
		}
	}
	
	/** The generic unknown format URI. */
	public static URI GENERIC_UNKNOWN;
	
	/**
	 * Guess format given a file.
	 *
	 * @param file the file
	 * @return the format
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException 
	 */
	public static URI guessFormat (File file) throws IOException, URISyntaxException
	{
		if (!file.isFile ())
			return null;
		
		String mime = Files.probeContentType (file.toPath ());
		if (mime != null && mime.equals ("application/xml"))
		{
			// test for special identifiers files
			// TODO: parse file. instead of just looking at extension..
			String name = file.getName ();
			int dot = name.lastIndexOf (".");
			if (dot > 0)
			{
				String ext = name.substring (dot + 1);
				if (ext.equals ("sbml")
					|| ext.equals ("sedml")
					|| ext.equals ("sed-ml")
					|| ext.equals ("sbgn")
					|| ext.equals ("omex")
					|| ext.equals ("cellml")
					|| ext.equals ("biopax")
					/*|| ext.equals ("")
					|| ext.equals ("")*/
					)
					return getFormatFromExtension (ext);
			}
		}
		return getFormatFromMime (mime);
	}
	
	
	/**
	 * Gets the format given a mime type.
	 *
	 * @param mime the mime type
	 * @return the format
	 * @throws URISyntaxException 
	 */
	public static URI getFormatFromMime (String mime) throws URISyntaxException
	{
		if (mime == null)
			return GENERIC_UNKNOWN;
		String uri = ext2format.getProperty (mime, null);
		return uri == null ? new URI ("http://purl.org/NET/mediatypes/" + mime) : new URI (uri);
	}
	
	
	/**
	 * Gets the format given a file extension.
	 *
	 * @param extension the file extension
	 * @return the format
	 * @throws URISyntaxException 
	 */
	public static URI getFormatFromExtension (String extension) throws URISyntaxException
	{
		String uri = ext2format.getProperty (extension, null);
		return uri == null ? GENERIC_UNKNOWN : new URI (uri);
	}
	
	
	
}
