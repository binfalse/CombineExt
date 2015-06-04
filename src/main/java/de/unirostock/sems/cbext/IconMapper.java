package de.unirostock.sems.cbext;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Interface for extending the known icons.
 * 
 * @author martin peters
 *
 */
public interface IconMapper {
	
	/**
	 * Defines the priority of this mapper.
	 * The higher the priority, the sooner this mapper gets called. <br>
	 * 
	 * Should not be negative. Default mapper have a priority around 100.
	 * 
	 * @return an integer > 0
	 */
	public int getPriority();
	
	public boolean hasIcon(URI format);
	
	public URL formatToIconUrl(URI format);
	
	public InputStream formatToIconStream(URI format);
	
	public String formatToIconName(URI format);
	
}
