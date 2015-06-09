package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class TestExtension {

	@Test
	public void testFormatParserExtension() {
		
		// high priority, executing before default parser (prio = 100)
		Formatizer.addFormatParser( new TestFormatParser() );
		
		File f = new File ("test/BIOMD0000000459.xml");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("Does not got dummy format from TestFormatParser", "http://example.org/spec/dummy", format.toString ());
	}
	
	@Test
	public void testExtensionMapper() {
		
		// high priority, executing before default extension mapper (prio = 100)
		Formatizer.addExtensionMapper( new TestExtensionMapper() );
		
		URI format = Formatizer.getFormatFromExtension("txt");
		assertEquals("Does not got dummy format for txt extension", "http://example.org/spec/text", format.toString());
	}
	
	@Test
	public void testExtensionMapperMime() {
		
		// high priority, executing before default extension mapper (prio = 100)
		Formatizer.addExtensionMapper( new TestExtensionMapper() );
		
		URI format = Formatizer.getFormatFromMime("text/plain");
		assertEquals("Does not got dummy format for txt mime type", "http://example.org/spec/text", format.toString());
	}
	
	@Test
	public void testIconMapper() throws URISyntaxException {
		
		// high priority, executing before default extension mapper (prio = 100)
		Iconizer.addIconMapper( new TestIconMapper() );
		
		String name = Iconizer.formatToIcon(new URI("http://example.org/spec/dummy"));
		assertEquals("Does not got dummy icon name for dummy format", "test.png", name);
	}
	
	public static class TestFormatParser extends FormatParser {

		@Override
		public int getPriority() {
			return 900;
		}

		@Override
		public URI checkFormat(File file, String mimeType) {
			// always returns a dummy format
			try {
				return new URI("http://example.org/spec/dummy");
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	public static class TestExtensionMapper implements ExtensionMapper {

		@Override
		public int getPriority() {
			return 900;
		}

		@Override
		public URI getFormatFromMime(String mime) {
			if( mime.equals("text/plain") ) {
				try {
					return new URI("http://example.org/spec/text");
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			
			return null;
		}

		@Override
		public URI getFromatFromExtension(String extension) {
			if( extension.equals("txt") ) {
				try {
					return new URI("http://example.org/spec/text");
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
	}
	
	public static class TestIconMapper implements IconMapper {

		@Override
		public int getPriority() {
			return 900;
		}

		@Override
		public boolean hasIcon(URI format) {
			if( format.toString().equals("http://example.org/spec/dummy") )
				return true;
			else
				return false;
		}

		@Override
		public URL formatToIconUrl(URI format) {
			/* not allowed outside of this test case
			   if hasIcon returns true, the other methods
			   should return something for this format */
			return null;
		}

		@Override
		public InputStream formatToIconStream(URI format) {
			/* not allowed outside of this test case
			   if hasIcon returns true, the other methods
			   should return something for this format */
			return null;
		}

		@Override
		public String formatToIconName(URI format) {
			if( format.toString().equals("http://example.org/spec/dummy") )
				return "test.png";
			else
				return null;
		}
		
	}
}
