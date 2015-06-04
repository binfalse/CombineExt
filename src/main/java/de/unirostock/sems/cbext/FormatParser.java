package de.unirostock.sems.cbext;

import java.io.File;
import java.net.URI;

/**
 * Interface for extending the parsable formats.
 * 
 * @author martin peters
 *
 */
public interface FormatParser {
	
	/**
	 * Defines the priority of this format parser.
	 * The higher the priority, the sooner this parser gets called. <br>
	 * 
	 * Should not be negative. Default format parser have priority around 100.
	 * 
	 * @return an integer > 0
	 */
	public int getPriority();
	
	/**
	 * Parses the given file and tries to determine an identifying URL, like purl.org.
	 * If this is not possible, null should be returned.
	 * 
	 * @param file Path to the file
	 * @param mimeType MIME type for quick evaluation.
	 * @return A formate URL or null.
	 */
	public URI checkFormat(File file, String mimeType);
	
}
