/**
 * 
 */
package de.unirostock.sems.cbext;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import de.binfalse.bflog.LOGGER;



/**
 * Instances of the class FormatRecognizer are able to recognize the format of
 * files.
 * 
 * They determine the formats by either
 * <ul>
 * <li>parsing the files: {@link FormatRecognizer.checkFormat (java.io.File,
 * java.lang.String)}</li>
 * <li>understanding a mime-type: {@link FormatRecognizer.getFormatFromMime
 * (java.lang.String)}</li>
 * <li>recognizing a file extension:
 * {@link FormatRecognizer.getFormatFromExtension (java.lang.String)}</li>
 * </ul>
 * 
 * If one of the methods fail to determin the format they must return
 * <code>null</code>.
 * 
 * The priority determines the order we use to ask different recognizers.
 * Default recognizers have a priority of 100.
 * The first recognizer, which returns a valid format, determines the format to
 * be used.
 * If you ever need to change the priority of different recognizers you should
 * resort the recognizers
 * in the Formatizer class by calling {@link Formatizer.resortRecognizers ()}.
 * 
 * 
 * @author Martin Scharm
 * 
 */
public abstract class FormatRecognizer
{
	
	/** purl.org base uri. */
	protected static final String	PURL_BASE					= "http://purl.org/NET/mediatypes/";
	
	/** identifiers.org base uri. */
	public static final String		IDENTIFIERS_BASE	= "http://identifiers.org/combine.specifications/";
	
	
	/**
	 * Defines the priority of this format recognizer.
	 * 
	 * The higher the priority, the earlier this recognizer gets called.
	 * The first recognizer, which is able to identify a file, determines it's
	 * format.
	 * Priority should not be negative. Default recognizers have a priority around
	 * 100.
	 * 
	 * @return an integer > 0
	 */
	public abstract int getPriority ();
	
	
	/**
	 * Parses the given file and tries to determine the format, such as purl.org
	 * or identifiers.org URI.
	 * 
	 * If the recognizer is not able to understand the file it must return
	 * <code>null</code>.
	 * 
	 * @param file
	 *          Path to the file
	 * @param mimeType
	 *          MIME type for quick evaluation.
	 * @return A format URI or null.
	 */
	public abstract URI getFormatByParsing (File file, String mimeType);
	
	
	/**
	 * Tries to map the given mime type to a format.
	 * 
	 * If the recognizer is not able to understand the mime type it must return
	 * <code>null</code>.
	 * 
	 * @param mime
	 *          MIME type
	 * @return A format URI or null
	 */
	public abstract URI getFormatFromMime (String mime);
	
	
	/**
	 * Tries to map the given file extension to a format.
	 * 
	 * If the recognizer is not able to understand the extension it must return
	 * <code>null</code>.
	 * 
	 * @param extension
	 *          file extension
	 * @return A format URI or null
	 */
	public abstract URI getFormatFromExtension (String extension);
	
	
	/**
	 * Builds an URI as `start+end` without caring about an exception. Only use if
	 * you're sure it's not going to fail. If we cannot produce this URI, we're
	 * returning a default.
	 * 
	 * @param pre
	 *          the start
	 * @param post
	 *          the end
	 * @param defaultUri
	 *          the default URI
	 * @return the URI as start+end
	 */
	public static URI buildUri (String pre, String post, URI defaultUri)
	{
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
	 * returning <code>null</code>.
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
