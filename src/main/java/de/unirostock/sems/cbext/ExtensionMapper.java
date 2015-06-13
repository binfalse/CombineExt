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
	 * 
	 * The higher the priority, the earlier this mapper gets called.
	 * The first mapper, which is able to map this extension, determines it's format.
	 * Priority should not be negative. Default mappers have a priority around 100.
	 * 
	 * @return an integer > 0
	 */
	public int getPriority();
	
	/**
	 * Tries to map format identifying URI to the given mime type.
	 * 
	 * If this mapper is not able to understand the mime type it should return null.
	 * 
	 * @param mime MIME type
	 * @return A format URI or null
	 */
	public URI getFormatFromMime(String mime);
	
	/**
	 * Tries to map format identifying URI to the given file extension.
	 * 
	 * If this mapper is not able to understand the extension it should return null.
	 * 
	 * @param extension file extension
	 * @return A format URI or null
	 */
	public URI getFormatFromExtension(String extension);
}
