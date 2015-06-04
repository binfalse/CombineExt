package de.unirostock.sems.cbext;

import java.net.URI;

/**
 * Interface for extending the known extensions and mime types.
 * 
 * @author martin peters
 *
 */
public interface ExtensionMapper {
	
	/**
	 * Defines the priority of this mapper.
	 * The higher the priority, the sooner this mapper gets called. <br>
	 * 
	 * Should not be negative. Default mapper have a priority around 100.
	 * 
	 * @return an integer > 0
	 */
	public int getPriority();
	
	/**
	 * Tries to map format identifying URI to the given mime type. <br>
	 * If this is not possible, this method should return null.
	 * 
	 * @param mime MIME type
	 * @return A format URI or null
	 */
	public URI getFormatFromMime(String mime);
	
	/**
	 * Tries to map format identifying URI to the given file extension. <br>
	 * If this is not possible, this method should return null.
	 * 
	 * @param extension file extension
	 * @return A format URI or null
	 */
	public URI getFromatFromExtension(String extension);
}
