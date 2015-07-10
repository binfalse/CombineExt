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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import de.unirostock.sems.cbext.recognizer.SbmlRecognizer;
import de.unirostock.sems.cbext.recognizer.SbolRecognizer;
import de.unirostock.sems.cbext.recognizer.SedMlRecognizer;



/**
 * The Class TestExtension.
 */
public class TestExtension
{
	
	/**
	 * Test format parser extension.
	 */
	@Test
	public void testFormatParserExtension ()
	{
		
		// high priority, executing before default parser (prio = 100)
		Formatizer.addFormatRecognizer (new TestFormatRecognizer ());
		
		File f = new File ("test/BIOMD0000000459.xml");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("Did not get dummy format from TestFormatParser",
			"http://example.org/spec/dummy", format.toString ());
		
		Formatizer.removeRecognizers ();
		Formatizer.addDefaultRecognizers ();
	}
	
	
	/**
	 * Test extension mapper.
	 */
	@Test
	public void testExtensionMapper ()
	{
		
		// high priority, executing before default extension mapper (prio = 100)
		Formatizer.addFormatRecognizer (new TestFormatRecognizer ());
		
		URI format = Formatizer.getFormatFromExtension ("txt");
		assertEquals ("Did not get dummy format for txt extension",
			"http://example.org/spec/text", format.toString ());
		
		Formatizer.removeRecognizers ();
		Formatizer.addDefaultRecognizers ();
	}
	
	
	/**
	 * Test extension mapper mime.
	 */
	@Test
	public void testExtensionMapperMime ()
	{
		
		// high priority, executing before default extension mapper (prio = 100)
		Formatizer.addFormatRecognizer (new TestFormatRecognizer ());
		
		URI format = Formatizer.getFormatFromMime ("text/plain");
		assertEquals ("Did not get dummy format for txt mime type",
			"http://example.org/spec/text", format.toString ());
		
		Formatizer.removeRecognizers ();
		Formatizer.addDefaultRecognizers ();
	}
	
	
	/**
	 * Test priorities.
	 */
	@Test
	public void testPriorities ()
	{
		final URI low = FormatRecognizer.buildUri ("http://", "lower.priority");
		final URI high = FormatRecognizer.buildUri ("http://", "higher.priority");
		
		FormatRecognizer lowRecognizer = new FormatRecognizer ()
		{
			
			@Override
			public int getPriority ()
			{
				return 200;
			}
			
			
			@Override
			public URI getFormatByParsing (File file, String mimeType)
			{
				return low;
			}
			
			
			@Override
			public URI getFormatFromMime (String mime)
			{
				return low;
			}
			
			
			@Override
			public URI getFormatFromExtension (String extension)
			{
				return low;
			}
		};
		
		class FormatRecognizerTmp
			extends FormatRecognizer
		{
			
			public int	priority	= 201;
			
			
			@Override
			public int getPriority ()
			{
				return priority;
			}
			
			
			@Override
			public URI getFormatByParsing (File file, String mimeType)
			{
				return high;
			}
			
			
			@Override
			public URI getFormatFromMime (String mime)
			{
				return high;
			}
			
			
			@Override
			public URI getFormatFromExtension (String extension)
			{
				return high;
			}
		}
		;
		FormatRecognizerTmp highRecognizer = new FormatRecognizerTmp ();
		File f = new File ("test/BIOMD0000000459.xml");
		
		Formatizer.removeRecognizers ();
		Formatizer.addFormatRecognizer (highRecognizer);
		Formatizer.addFormatRecognizer (lowRecognizer);
		
		assertEquals (
			"Did not get format of high priority recognizer for mime type", high,
			Formatizer.getFormatFromMime ("who cares"));
		assertEquals (
			"Did not get format of high priority recognizer for extension", high,
			Formatizer.getFormatFromExtension ("who cares"));
		assertEquals ("Did not get format of high priority recognizer by guessing",
			high, Formatizer.guessFormat (f));
		
		// test again with submitting recognizers in other order
		Formatizer.removeRecognizers ();
		Formatizer.addFormatRecognizer (lowRecognizer);
		Formatizer.addFormatRecognizer (highRecognizer);
		
		assertEquals (
			"Did not get format of high priority recognizer for mime type", high,
			Formatizer.getFormatFromMime ("who cares"));
		assertEquals (
			"Did not get format of high priority recognizer for extension", high,
			Formatizer.getFormatFromExtension ("who cares"));
		assertEquals ("Did not get format of high priority recognizer by guessing",
			high, Formatizer.guessFormat (f));
		
		// now change priority
		highRecognizer.priority = 50;
		
		// test again, this time it should correctly be incorrect
		assertEquals (
			"Did not get format of high priority recognizer for mime type", high,
			Formatizer.getFormatFromMime ("who cares"));
		assertEquals (
			"Did not get format of high priority recognizer for extension", high,
			Formatizer.getFormatFromExtension ("who cares"));
		assertEquals ("Did not get format of high priority recognizer by guessing",
			high, Formatizer.guessFormat (f));
		
		// now resort the recognizers
		Formatizer.resortRecognizers ();
		
		// test again, this time it should be correct gain
		assertEquals (
			"Did not get format of low priority recognizer for mime type", low,
			Formatizer.getFormatFromMime ("who cares"));
		assertEquals (
			"Did not get format of low priority recognizer for extension", low,
			Formatizer.getFormatFromExtension ("who cares"));
		assertEquals ("Did not get format of low priority recognizer by guessing",
			low, Formatizer.guessFormat (f));
		
		Formatizer.removeRecognizers ();
		Formatizer.addDefaultRecognizers ();
		
		SbmlRecognizer.setPriority (123);
		assertEquals ("wrong priority for SbmlRecognizer", 123, new SbmlRecognizer ().getPriority ());
		assertEquals ("wrong priority for SbolRecognizer", 100, new SbolRecognizer ().getPriority ());
		assertEquals ("wrong priority for SbgnRecognizer", 100, new SbolRecognizer ().getPriority ());
		assertEquals ("wrong priority for SedmlRecognizer", 100, new SedMlRecognizer ().getPriority ());
		SbmlRecognizer.setPriority (100);
		assertEquals ("wrong priority for SbmlRecognizer", 100, new SbmlRecognizer ().getPriority ());
		assertEquals ("wrong priority for SbolRecognizer", 100, new SbolRecognizer ().getPriority ());
		
	}
	
	
	/**
	 * Test icon mapper.
	 * 
	 * @throws URISyntaxException
	 *           the uRI syntax exception
	 */
	@Test
	public void testIconMapper () throws URISyntaxException
	{
		
		// high priority, executing before default extension mapper (prio = 100)
		TestIconMapper tim = new TestIconMapper ();
		Iconizer.addIconCollection (tim);
		
		assertTrue ("test icon mapper has no icon for dummy format",
			tim.hasIcon (new URI ("http://example.org/spec/dummy")));
		assertFalse ("test icon mapper has an icon for unknown format",
			tim.hasIcon (new URI ("http://somet.hing")));
		assertFalse (
			"test icon mapper has an icon for sbml format",
			tim
				.hasIcon (new URI (
					"http://identifiers.org/combine.specifications/sbml.level-2.version-4")));
		
		assertEquals ("Did not get dummy icon name for dummy format", "test.png",
			Iconizer.formatToIcon (new URI ("http://example.org/spec/dummy")));
		assertEquals ("Did not get default icon name for unknown format",
			"Blue-unknown.png", Iconizer.formatToIcon (new URI ("http://somet.hing")));
		assertEquals (
			"Did not get sbml icon name for sbml format",
			"Blue-sbml.png",
			Iconizer
				.formatToIcon (new URI (
					"http://identifiers.org/combine.specifications/sbml.level-2.version-4")));
		
		assertTrue ("Did not get default icon url for dummy format", Iconizer
			.formatToIconUrl (new URI ("http://example.org/spec/dummy")).toString ()
			.endsWith ("Blue-unknown.png"));
		assertTrue ("Did not get default icon url for unknown format",
			Iconizer.formatToIconUrl (new URI ("http://somet.hing")).toString ()
				.endsWith ("Blue-unknown.png"));
		assertTrue (
			"Did not get sbml icon url for sbml format",
			Iconizer
				.formatToIconUrl (
					new URI (
						"http://identifiers.org/combine.specifications/sbml.level-2.version-4"))
				.toString ().endsWith ("Blue-sbml.png"));
		
		assertNotNull ("did not expect null stream for dummy format",
			Iconizer.formatToIconStream (new URI ("http://example.org/spec/dummy")));
		assertNotNull ("did not expect null stream for unknown format",
			Iconizer.formatToIconStream (new URI ("http://somet.hing")));
		assertNotNull (
			"did not expect null stream for sbml format",
			Iconizer
				.formatToIconStream (new URI (
					"http://identifiers.org/combine.specifications/sbml.level-2.version-4")));
		
		Iconizer.removeCollections();
		Iconizer.addDefaultCollection();
	}
	
	/**
	 * The Class TestFormatParser.
	 */
	public static class TestFormatRecognizer
		extends FormatRecognizer
	{
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.unirostock.sems.cbext.FormatParser#getPriority()
		 */
		@Override
		public int getPriority ()
		{
			return 900;
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
			// always returns a dummy format
			try
			{
				return new URI ("http://example.org/spec/dummy");
			}
			catch (URISyntaxException e)
			{
				e.printStackTrace ();
				return null;
			}
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.unirostock.sems.cbext.ExtensionMapper#getFormatFromMime(java.lang.
		 * String)
		 */
		@Override
		public URI getFormatFromMime (String mime)
		{
			if (mime.equals ("text/plain"))
			{
				try
				{
					return new URI ("http://example.org/spec/text");
				}
				catch (URISyntaxException e)
				{
					e.printStackTrace ();
				}
			}
			
			return null;
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * de.unirostock.sems.cbext.ExtensionMapper#getFormatFromExtension(java.
		 * lang.String)
		 */
		@Override
		public URI getFormatFromExtension (String extension)
		{
			if (extension.equals ("txt"))
			{
				try
				{
					return new URI ("http://example.org/spec/text");
				}
				catch (URISyntaxException e)
				{
					e.printStackTrace ();
				}
			}
			
			return null;
		}
		
	}
	
	/**
	 * The Class TestIconMapper.
	 */
	public static class TestIconMapper
		extends IconCollection
	{
		
		/** priority for this icon collection */
		protected static int 			priority			= 100;
		
		/**
		 * Sets the priority of this format recognizer and triggers a resort of all
		 * format recognizers.
		 * 
		 * The higher the priority, the earlier this recognizer gets called.
		 * The first recognizer, which is able to identify a file, determines it's
		 * format.
		 * Setting a negative priority will be ignored.
		 * Default recognizers have a priority of 100.
		 * 
		 * @param newPriority
		 */
		public static void setPriority (int newPriority) {
			
			// no negative priorities!
			if( priority < 0 )
				return;
			
			priority = newPriority;
			Iconizer.resortCollections ();;
		}

		/* (non-Javadoc)
		 * @see de.unirostock.sems.cbext.FormatRecognizer#getPriority()
		 */
		@Override
		public int getPriority ()
		{
			return priority;
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.unirostock.sems.cbext.IconMapper#hasIcon(java.net.URI)
		 */
		@Override
		public boolean hasIcon (URI format)
		{
			if (format.toString ().equals ("http://example.org/spec/dummy"))
				return true;
			else
				return false;
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.unirostock.sems.cbext.IconMapper#formatToIconUrl(java.net.URI)
		 */
		@Override
		public URL formatToIconUrl (URI format)
		{
			/*
			 * not allowed outside of this test case
			 * if hasIcon returns true, the other methods
			 * should return something for this format
			 */
			return null;
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.unirostock.sems.cbext.IconMapper#formatToIconStream(java.net.URI)
		 */
		@Override
		public InputStream formatToIconStream (URI format)
		{
			/*
			 * not allowed outside of this test case
			 * if hasIcon returns true, the other methods
			 * should return something for this format
			 */
			return null;
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.unirostock.sems.cbext.IconMapper#formatToIconName(java.net.URI)
		 */
		@Override
		public String formatToIconName (URI format)
		{
			if (format.toString ().equals ("http://example.org/spec/dummy"))
				return "test.png";
			else
				return null;
		}
		
	}
}
