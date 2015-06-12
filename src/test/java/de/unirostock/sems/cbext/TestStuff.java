/**
 * 
 */
package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Test;

import de.unirostock.sems.cbext.mapper.DefaultExtensionMapper;
import de.unirostock.sems.cbext.mapper.DefaultIconMapper;

/**
 * @author Martin Scharm
 *
 */
public class TestStuff
{
	@Test
	public void testFormatParser ()
	{
		assertNotNull ("valid URI shouln't return null", FormatParser.buildUri ("http://", "binfalse.de"));
		assertNull ("valid URI shouln't return null", FormatParser.buildUri (":", "binfalse.de"));
	}
	
	@Test
	public void testDefaultIconMapper ()
	{
		DefaultIconMapper dim = new DefaultIconMapper ();
		assertEquals ("expected a priority of 100 for the DefaultIconMapper", 100, dim.getPriority ());
		
		Set<Object> keys = dim.getAvailableFormatIcons ();
		for (Object k : keys)
		{
			try
			{
				URI format = new URI ((String) k);
				assertTrue ("did not find icon for format: " + format, dim.hasIcon (format));
				assertNotNull ("cannot find icon name for format: " + format, dim.formatToIconName (format));
				assertNotNull ("cannot find icon url for format: " + format, dim.formatToIconUrl (format));
				assertNotNull ("cannot open icon stream for format: " + format, dim.formatToIconStream (format));
				

				format = new URI ("http://binfalse.de");
				assertFalse ("did not expect to find icon for format: " + format, dim.hasIcon (format));
				assertNull ("did not expect to find icon name for format: " + format, dim.formatToIconName (format));
				assertNull ("did not expect to find icon url for format: " + format, dim.formatToIconUrl (format));
				assertNull ("did not expect to open icon stream for format: " + format, dim.formatToIconStream (format));
				
			}
			catch (URISyntaxException e)
			{
				e.printStackTrace();
				fail ("there is apparently a wrong format url in the icon mapper list?");
			}
		}
	}
	
	@Test
	public void testDefaultExtensionMapper ()
	{
		DefaultExtensionMapper dem = new DefaultExtensionMapper ();
		assertEquals ("expected a priority of 100 for the DefaultExtensionMapper", 100, dem.getPriority ());
		
		assertNull ("didn't expect format for null", dem.getFormatFromMime (null));
		
		
		try
		{
			URI format = new URI ("http://purl.org/NET/mediatypes/application/xml");
			assertEquals ("unexpected format for .xml", format, dem.getFormatFromExtension ("xml"));
			assertNull ("unexpected format for .xml", dem.getFormatFromMime ("application/xml"));
			assertEquals ("unexpected format for .xml", format, Formatizer.getFormatFromMime ("application/xml"));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			fail ("couldn't create URI");
		}
	}
	
	@Test
	public void testIconizer ()
	{
		Iconizer iconizr = new Iconizer ();
		try
		{
			File tmp = iconizr.extractIconExample ();
			tmp = iconizr.extractIconExample ();
			tmp.delete ();
		}
		catch (IOException|URISyntaxException e)
		{
			e.printStackTrace();
			fail ("unexpected exception");
		}
		
		try
		{
			Iconizer.addIconMapper (null);
			fail ("expected to get an exception from a null mapper");
		}
		catch (IllegalArgumentException e)
		{
			// that's ok
		}
		
		// test empty icon
		InputStream fin = Iconizer.formatToIconStream (null);
    byte[] bytes = new byte[1024];
    int noOfBytes = 0, b = 0;

    try
		{
			while( (b = fin.read(bytes)) != -1 )
			{
				noOfBytes += b;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("failed to read generic icon");
		}
    assertEquals ("generic icon has unexpected size", 1487, noOfBytes);
    
    
		// test icon for unknown uri
		fin = Iconizer.formatToIconStream (FormatParser.buildUri ("http://", "binfalse.de"));
    bytes = new byte[1024];
    noOfBytes = 0; b = 0;

    try
		{
			while( (b = fin.read(bytes)) != -1 )
			{
				noOfBytes += b;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("failed to read generic icon");
		}
    assertEquals ("generic icon has unexpected size", 1487, noOfBytes);
		
		
	}
	
}
