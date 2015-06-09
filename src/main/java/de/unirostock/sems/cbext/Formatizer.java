/**
 * 
 */
package de.unirostock.sems.cbext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.formatizer.BioPaxFormatizer;
import de.unirostock.sems.cbext.formatizer.CellMlFormatizer;
import de.unirostock.sems.cbext.formatizer.SbgnFormatizer;
import de.unirostock.sems.cbext.formatizer.SbmlFormatizer;
import de.unirostock.sems.cbext.formatizer.SbolFormatizer;
import de.unirostock.sems.cbext.formatizer.SedMlFormatizer;
import de.unirostock.sems.cbext.mapper.DefaultExtensionMapper;


/**
 * The Class Formatizer to generate format URIs for certain file types.
 * 
 * @author Martin Scharm
 */
public class Formatizer
{
	
	/** list of registered format parser */
	private static List<FormatParser> formatizerList			= new ArrayList<FormatParser>();
	/** list of registered extension mapper */
	private static List<ExtensionMapper> extensionMapperList	= new ArrayList<ExtensionMapper>();
	
	static
	{
		String defaultUri = "http://purl.org/NET/mediatypes/application/x.unknown";
		try
		{
			GENERIC_UNKNOWN = new URI (defaultUri);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
			LOGGER.error (e, "error generating generic default uri: ", defaultUri);
		}
		
		// add default parser
		formatizerList.add( new BioPaxFormatizer() );
		formatizerList.add( new CellMlFormatizer() );
		formatizerList.add( new SbgnFormatizer() );
		formatizerList.add( new SbmlFormatizer() );
		formatizerList.add( new SbolFormatizer() );
		formatizerList.add( new SedMlFormatizer() );
		Collections.sort(formatizerList, new FormatParserComparator());
		
		// add default extension mapper
		extensionMapperList.add( new DefaultExtensionMapper() );
		Collections.sort(extensionMapperList, new ExtensionMapperComparator());
	}
	
	/** The generic unknown format URI. */
	public static URI						GENERIC_UNKNOWN;
	
	/**
	 * Adds a format parser to the formatizer
	 *  
	 * @param formatizer
	 */
	public static void addFormatParser(FormatParser parser) {
		if( parser == null )
			throw new IllegalArgumentException("The formatizer is not allowed to be null.");
		
		formatizerList.add(parser);
		Collections.sort(formatizerList, new FormatParserComparator());
	}
	
	/**
	 * Adds a extension mapper to the formatizer
	 * 
	 * @param mapper
	 */
	public static void addExtensionMapper(ExtensionMapper mapper) {
		if( mapper == null )
			throw new IllegalArgumentException("The mapper is not allowed to be null.");
		
		extensionMapperList.add(mapper);
		Collections.sort(extensionMapperList, new ExtensionMapperComparator());
	}
	
	/**
	 * Guess format given a file.
	 * 
	 * @param file
	 *          the file
	 * @return the format
	 */
	public static URI guessFormat (File file)
	{
		if (!file.isFile ())
			return null;
		
		String mime = null;
		try
		{
			mime = Files.probeContentType (file.toPath ());
		}
		catch (IOException e)
		{
			LOGGER.warn (e, "could not get mime from file " + file);
		}
		
		URI format = null;
		for( FormatParser parser : formatizerList ) {
			if( (format = parser.checkFormat(file, mime)) !=  null)
				break;
		}
		
		if( format != null ) {
			// found a format
			return format;
		}
		else {
			// ok, parsing failed. let's still try file extensions.
			String name = file.getName ();
			int dot = name.lastIndexOf (".");
			if (dot > 0)
			{
				String ext = name.substring (dot + 1);
				if (ext.equals ("sbml") || ext.equals ("sedml")
					|| ext.equals ("sed-ml") || ext.equals ("sbgn")
					|| ext.equals ("omex") || ext.equals ("cellml")
					|| ext.equals ("biopax")
				)
					return getFormatFromExtension (ext);
			}
			
		}
		
		// parsing still failed, try to map mime-type
		return getFormatFromMime (mime);
		
	}
	
	/**
	 * Gets the format given a mime type.
	 * 
	 * @param mime
	 *          the mime type
	 * @return the format
	 */
	public static URI getFormatFromMime (String mime) {
		if (mime == null)
			return GENERIC_UNKNOWN;
		
		URI format = null;
		for( ExtensionMapper mapper : extensionMapperList ) {
			if( (format = mapper.getFormatFromMime(mime)) != null )
				break;
		}
		
		if( format != null )
			return format;
		else {
			try {
				return new URI("http://purl.org/NET/mediatypes/" + mime);
			}
			catch (URISyntaxException e) {
				LOGGER.warn (e, "error generating URI.");
				return GENERIC_UNKNOWN;
			}
		}
	}
	
	
	/**
	 * Gets the format given a file extension.
	 * 
	 * @param extension
	 *          the file extension
	 * @return the format
	 */
	public static URI getFormatFromExtension (String extension) {
		if (extension == null)
			return GENERIC_UNKNOWN;
		
		URI format = null;
		for( ExtensionMapper mapper : extensionMapperList ) {
			if( (format = mapper.getFromatFromExtension(extension)) != null )
				break;
		}
		
		if( format != null )
			return format;
		else 
			return GENERIC_UNKNOWN;
	}
	
	
	/**
	 * Builds an URI as `start+end` without caring about an exception. Only use if
	 * you're sure it's not going to fail. If we cannot produce this URI, we're
	 * returning null.
	 * 
	 * @param pre
	 *          the start
	 * @param post
	 *          the end
	 * @return the URI as start+end
	 */
	public static URI buildUri (String pre, String post)
	{
		try
		{
			return new URI (pre + post);
		}
		catch (URISyntaxException e)
		{
			LOGGER.error ("wasn't able to create URI " + pre + post);
		}
		return null;
	}
	
	/**
	 * Comparator for Format Parser
	 * 
	 */
	private static class FormatParserComparator implements Comparator<FormatParser> {
		@Override
		public int compare(FormatParser o1, FormatParser o2) {
			return o2.getPriority() - o1.getPriority();
		}
		
	}
	
	private static class ExtensionMapperComparator implements Comparator<ExtensionMapper> {
		@Override
		public int compare(ExtensionMapper o1, ExtensionMapper o2) {
			return o2.getPriority() - o1.getPriority();
		}
		
	}
}
