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
	
	/**
	 * Returns true, if the given format is available in this mapper.
	 * 
	 * @param format
	 * @return true, if we have an icon for that format
	 */
	public boolean hasIcon(URI format);
	
	/**
	 * Get a URL pointing to an icon in the archive.
	 * If no icon is available <pre>null</pre> is supposed to be returned.
	 * 
	 * @param format
	 * @return the URL pointing to the icon for that format
	 */
	public URL formatToIconUrl(URI format);
	
	/**
	 * Get an icon stream given a format. This stream will ship the icon as it is
	 * stored in our jar file.
	 * If no icon is available <pre>null</pre> is supposed to be returned.
	 * 
	 * @param format
	 * @return the stream delivering the icon for the format
	 */
	public InputStream formatToIconStream(URI format);
	
	/**
	 * Get an icon file name given a format, as it can be found in our jar
	 * archive. Especially useful for people who are going to extract the icons.
	 * If no icon is available <pre>null</pre> is supposed to be returned.
	 * 
	 * @param format
	 * @return the name of the icon for this format
	 */
	public String formatToIconName(URI format);
	
}
