/**
 * 
 */
package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.junit.Test;

import de.binfalse.bflog.LOGGER;


/**
 * The Class TestWeb.
 *
 * @author Martin Scharm
 */
public class TestFormats
{
	
	/**
	 * Test some stuff that definitely need to be corrent.
	 */
	@Test
	public void testStatics ()
	{
		try
		{
			assertEquals ("omex uri is incorrect", new URI ("http://identifiers.org/combine.specifications/omex"), Formatizer.getFormatFromExtension ("omex"));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			LOGGER.error (e, "something went wrong");
			fail ("error testing statics.");
		}
	}
	
	/**
	 */
	@Test
	public void testIconize ()
	{
		URI format = null;
		try
		{
			format = new URI ("http://identifiers.org/combine.specifications/sbml.level-1.version-1");
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			fail ("failed to gen sbml uri");
		}
		
		String iconName = Iconizer.formatToIcon (format);
		assertEquals ("expected to get the sbml icon", "Blue-sbml.png", iconName);
		

		InputStream fin = Iconizer.formatToIconStream (format);
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
			fail ("failed to read sbml image");
		}
    assertEquals ("sbml image has unexpected size", 1880, noOfBytes);
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testFormatize ()
	{
		checkFormat (new File ("test/aguda_b_1999.cellml"), "http://identifiers.org/combine.specifications/cellml", "http://identifiers.org/combine.specifications/cellml", "http://purl.org/NET/mediatypes/application/xml");
		checkFormat (new File ("test/aguda_b_1999.cellml.wrong.ext"), "http://identifiers.org/combine.specifications/cellml", "http://purl.org/NET/mediatypes/application/x.unknown", "http://purl.org/NET/mediatypes/application/xml");
	}
	
	
	private static void checkFormat (File f, String expectedGuess, String expectedExt, String expectedMime)
	{
		try
		{
			URI format = Formatizer.guessFormat (f);
			assertEquals ("got wrong format for guessing " + f.getAbsolutePath (), expectedGuess, format.toString ());
			
			format = Formatizer.getFormatFromMime (Files.probeContentType (f.toPath ()));
			assertEquals ("got wrong format for mime of " + f.getAbsolutePath (), expectedMime, format.toString ());
			
			format = Formatizer.getFormatFromExtension (f.getName ().substring (f.getName ().lastIndexOf (".") + 1));
			assertEquals ("got wrong format for ext of " + f.getAbsolutePath (), expectedExt, format.toString ());
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			fail ("couldn't test format for " + f.getAbsolutePath ());
		}
	}
}
