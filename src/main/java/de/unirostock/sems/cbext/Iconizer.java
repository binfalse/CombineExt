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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unirostock.sems.cbext.mapper.DefaultIconMapper;



/**
 * The Class Iconizer to retrieve icons for common file types..
 * 
 * @author Martin Scharm
 */
public class Iconizer
{	
	/** known icon mapper */
	private static List<IconMapper> iconMapperList		= new ArrayList<IconMapper>();
	
	static {
		
		// add default icon mapper
		iconMapperList.add( new DefaultIconMapper() );
		Collections.sort(iconMapperList, new IconMapperComparator());
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
			return GENERIC_UNKNOWN;
		
		String name = null;
		for( IconMapper mapper : iconMapperList ) {
			if( (name = mapper.formatToIconName(format)) != null )
				break;
		}
		
		return name != null ? name : GENERIC_UNKNOWN;
	}
	
	
	/**
	 * Get a URL pointing to an icon in our archive. Can for example be used with
	 * 
	 * <pre>
	 * new ImageIcon (Iconizer.formatToIconUrl (SOME_FORMAT));
	 * </pre>
	 * 
	 * @param format
	 *          the format
	 * @return the URL to the icon file
	 */
	public static URL formatToIconUrl (URI format)
	{
		if (format == null)
			return Iconizer.class.getResource( "/icons/" + GENERIC_UNKNOWN );
		
		URL url = null;
		for( IconMapper mapper : iconMapperList ) {
			if( (url = mapper.formatToIconUrl(format)) != null )
				break;
		}
		
		return url != null ? url : Iconizer.class.getResource( "/icons/" + GENERIC_UNKNOWN );
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
		if (format == null)
			return Iconizer.class.getResourceAsStream( "/icons/" + GENERIC_UNKNOWN );
		
		InputStream url = null;
		for( IconMapper mapper : iconMapperList ) {
			if( (url = mapper.formatToIconStream(format)) != null )
				break;
		}
		
		return url != null ? url : Iconizer.class.getResourceAsStream( "/icons/" + GENERIC_UNKNOWN );
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
	
	private static class IconMapperComparator implements Comparator<IconMapper> {
		@Override
		public int compare(IconMapper o1, IconMapper o2) {
			return o1.getPriority() - o2.getPriority();
		}
		
	}
}
