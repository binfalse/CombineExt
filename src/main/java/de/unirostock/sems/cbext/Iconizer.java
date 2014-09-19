/**
 * 
 */
package de.unirostock.sems.cbext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import de.binfalse.bflog.LOGGER;



/**
 * The Class Iconizer to retrieve icons for common file types..
 * 
 * @author Martin Scharm
 */
public class Iconizer
{
	
	/** known formats file. */
	private static final String	format2iconFile	= "/format2icon.prop";
	
	/** known formats. */
	private static Properties		format2icon			= new Properties ();
	static
	{
		try
		{
			InputStream is = Iconizer.class.getResourceAsStream (format2iconFile);
			if (is != null)
				format2icon.load (is);
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			LOGGER.error (e, "error reading known formats: ",
				Iconizer.class.getResourceAsStream (format2iconFile));
		}
	}
	
	/**
	 * The Constant GENERIC_UNKNOWN representing the icon file for unknown file
	 * types.
	 */
	public static final String	GENERIC_UNKNOWN	= "Blue-unknown.png";
	
	
	/**
	 * Get an icon file name given a format, as it can be found in our jar
	 * archive. Especially useful for people who are going to extract the icons.
	 * 
	 * @param format
	 *          the format
	 * @return the name of the icon file
	 */
	public static String formatToIcon (URI format)
	{
		if (format == null)
			return "Blue-unknown.png";
		return format2icon.getProperty (format.toString (), GENERIC_UNKNOWN);
	}
	
	
	/**
	 * Get an icon stream given a format. This stream will ship the icon as it is
	 * stored in our jar file.
	 * 
	 * @param format
	 *          the format
	 * @return the stream containing the icon
	 */
	public static InputStream formatToIconStream (URI format)
	{
		return Iconizer.class.getResourceAsStream ("/icons/"
			+ formatToIcon (format));
	}
	
	
	/**
	 * Example for extracting an icon. Not to be used.
	 * 
	 * @throws IOException
	 *           Signals that an I/O exception has occurred.
	 * @throws URISyntaxException
	 *           the uRI syntax exception
	 */
	public static void extractIconExample ()
		throws IOException,
			URISyntaxException
	{
		URI format = new URI ("http://identifiers.org/combine.specifications/sbml");
		
		String iconName = formatToIcon (format);
		
		System.out.println ("icon name: " + iconName);
		
		File expectedFile = new File ("/tmp/" + iconName);
		
		if (!expectedFile.exists ())
		{
			// extract the file
			InputStream fin = formatToIconStream (format);
			FileOutputStream fout = new FileOutputStream (expectedFile);
			
			byte[] b = new byte[1024];
			int noOfBytes = 0;
			
			while ( (noOfBytes = fin.read (b)) != -1)
			{
				fout.write (b, 0, noOfBytes);
			}
			
			System.out.println ("File copied!");
			fin.close ();
			fout.close ();
			
		}
		else
		{
			// cool. msg just for debugging purposes
			System.out.println ("file extracted previously");
		}
		
		// go on using expectedFile!
		System.out.println ("icon can be found in "
			+ expectedFile.getAbsolutePath ());
	}
}
