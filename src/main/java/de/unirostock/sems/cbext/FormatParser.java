package de.unirostock.sems.cbext;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import de.binfalse.bflog.LOGGER;

/**
 * Interface for extending the parsable formats.
 * 
 * @author martin peters
 *
 */
public abstract class FormatParser {
	
	/** identifiers.org base uri. */
	protected static final String	IDENTIFIERS_BASE	= "http://identifiers.org/combine.specifications/";
	
	/**
	 * Defines the priority of this format parser.
	 * The higher the priority, the sooner this parser gets called. <br>
	 * 
	 * Should not be negative. Default format parser have a priority around 100.
	 * 
	 * @return an integer > 0
	 */
	public abstract int getPriority();
	
	/**
	 * Parses the given file and tries to determine an identifying URL, like purl.org.
	 * If this is not possible, null should be returned.
	 * 
	 * @param file Path to the file
	 * @param mimeType MIME type for quick evaluation.
	 * @return A format URI or null.
	 */
	public abstract URI checkFormat(File file, String mimeType);
	
	
	/**
	 * Builds an URI as `start+end` without caring about an exception. Only use if
	 * you're sure it's not going to fail. If we cannot produce this URI, we're
	 * returning a default.
	 *
	 * @param pre the start
	 * @param post the end
	 * @param defaultUri the default URI
	 * @return the URI as start+end
	 */
	public static URI buildUri (String pre, String post, URI defaultUri) {
		try
		{
			return new URI (pre + post);
		}
		catch (URISyntaxException e)
		{
			LOGGER.error ("wasn't able to create URI " + pre + post);
		}
		return defaultUri;
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
		return buildUri (pre, post, null);
	}
}
