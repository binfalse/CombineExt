/**
 * Copyright © 2014-2015:
 * - Martin Scharm <martin@binfalse.de>
 * - Martin Peters <martin@freakybytes.net>
 * 
 * This file is part of the CombineExt library.
 * 
 * CombineExt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * CombineExt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CombineExt. If not, see <http://www.gnu.org/licenses/>.
 */
package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import org.junit.Test;

import de.binfalse.bflog.LOGGER;



/**
 * The Class TestFormats.
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
			assertEquals ("omex uri is incorrect", new URI (
				"http://identifiers.org/combine.specifications/omex"),
				Formatizer.getFormatFromExtension ("omex"));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
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
			format = new URI (
				"http://identifiers.org/combine.specifications/sbml.level-1.version-1");
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
			fail ("failed to gen sbml uri");
		}
		
		String iconName = Iconizer.formatToIcon (format);
		assertEquals ("expected to get the sbml icon", "Blue-sbml.png", iconName);
		
		URL iconUrl = Iconizer.formatToIconUrl (format);
		assertTrue ("unexpexted URL for SBML icon",
			iconUrl.toString ().endsWith ("Blue-sbml.png"));
		
		InputStream fin = Iconizer.formatToIconStream (format);
		byte[] bytes = new byte[1024];
		int noOfBytes = 0, b = 0;
		
		try
		{
			while ( (b = fin.read (bytes)) != -1)
			{
				noOfBytes += b;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			fail ("failed to read sbml image");
		}
		assertEquals ("sbml image has unexpected size", 1880, noOfBytes);
		
		assertEquals ("expected generic icon for null-format",
			Iconizer.GENERIC_UNKNOWN, Iconizer.formatToIcon (null));
		assertEquals ("expected generic icon for bullshit format",
			Iconizer.class.getResource ("/icons/" + Iconizer.GENERIC_UNKNOWN),
			Iconizer.formatToIconUrl (null));
		try
		{
			assertEquals ("expected generic icon for bullshit format",
				Iconizer.GENERIC_UNKNOWN,
				Iconizer.formatToIcon (new URI ("http://identifiers.org/bull/shit")));
			assertEquals ("expected generic icon for bullshit format",
				Iconizer.class.getResource ("/icons/" + Iconizer.GENERIC_UNKNOWN),
				Iconizer.formatToIconUrl (new URI ("http://identifiers.org/bull/shit")));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
			fail ("why did I end here?");
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testFormatize ()
	{
		checkFormat (new File ("test/aguda_b_1999.cellml"),
			"http://identifiers.org/combine.specifications/cellml",
			"http://identifiers.org/combine.specifications/cellml",
			"http://purl.org/NET/mediatypes/application/xml");
		
		checkFormat (new File ("test/aguda_b_1999.cellml.wrong.ext"),
			"http://identifiers.org/combine.specifications/cellml",
			"http://purl.org/NET/mediatypes/application/x.unknown",
			"http://purl.org/NET/mediatypes/application/xml");
		
		checkFormat (new File ("test/BIOMD0000000459.xml"),
			"http://identifiers.org/combine.specifications/sbml.level-2.version-4",
			"http://purl.org/NET/mediatypes/application/xml",
			"http://purl.org/NET/mediatypes/application/xml");
		
		checkFormat (new File ("test/BIOMD0000000459-SEDML.xml"),
			"http://identifiers.org/combine.specifications/sed-ml.level-1.version-1",
			"http://purl.org/NET/mediatypes/application/xml",
			"http://purl.org/NET/mediatypes/application/xml");
		
		checkFormat (
			new File (
				"test/guess-biopax-paxtools-core-src-main-resources-org-biopax-paxtools-model-biopax-level3.owl"),
			"http://identifiers.org/combine.specifications/biopax",
			"http://purl.org/NET/mediatypes/application/x.unknown",
			"http://purl.org/NET/mediatypes/application/rdf+xml");
		
		checkFormat (new File ("test/guess-SBOLj-examples-data-BBa_I0462.xml"),
			"http://identifiers.org/combine.specifications/sbol",
			"http://purl.org/NET/mediatypes/application/xml",
			"http://purl.org/NET/mediatypes/application/xml");
		
		checkFormat (new File ("test/some.xml"),
			"http://purl.org/NET/mediatypes/application/xml",
			"http://purl.org/NET/mediatypes/application/xml",
			"http://purl.org/NET/mediatypes/application/xml");
		
		checkFormat (new File ("test/some.rdf"),
			"http://purl.org/NET/mediatypes/application/rdf+xml",
			"http://purl.org/NET/mediatypes/application/x.unknown",
			"http://purl.org/NET/mediatypes/application/rdf+xml");
		
		checkFormat (new File ("test/plain.text"),
			"http://purl.org/NET/mediatypes/text/plain",
			"http://purl.org/NET/mediatypes/application/x.unknown",
			"http://purl.org/NET/mediatypes/text/plain");
	}
	
	
	/**
	 * Check format.
	 * 
	 * @param f
	 *          the file
	 * @param expectedGuess
	 *          the expected format by guess
	 * @param expectedExt
	 *          the expected format from the extension
	 * @param expectedMime
	 *          the expected format from the mime
	 */
	public static void checkFormat (File f, String expectedGuess,
		String expectedExt, String expectedMime)
	{
		try
		{
			URI format = Formatizer.guessFormat (f);
			assertEquals ("got wrong format for guessing " + f.getAbsolutePath (),
				expectedGuess, format.toString ());
			
			format = Formatizer.getFormatFromMime (Files.probeContentType (f
				.toPath ()));
			assertEquals ("got wrong format for mime of " + f.getAbsolutePath (),
				expectedMime, format.toString ());
			
			format = Formatizer.getFormatFromExtension (f.getName ().substring (
				f.getName ().lastIndexOf (".") + 1));
			assertEquals ("got wrong format for ext of " + f.getAbsolutePath (),
				expectedExt, format.toString ());
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			fail ("couldn't test format for " + f.getAbsolutePath ());
		}
	}
}
