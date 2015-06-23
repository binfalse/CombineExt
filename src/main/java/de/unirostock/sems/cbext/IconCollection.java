/**
 * Copyright © 2014-2015:
 * - Martin Peters <martin@freakybytes.net>
 * - Martin Scharm <martin@binfalse.de>
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

import java.io.InputStream;
import java.net.URI;
import java.net.URL;



/**
 * Interface for extending the collection of known icons.
 * 
 * @author martin peters
 * 
 */
public interface IconCollection
{
	
	/**
	 * Defines the priority of this mapper.
	 * The higher the priority, the sooner this mapper gets called. <br>
	 * 
	 * Should not be negative. Default mapper have a priority around 100.
	 * 
	 * @return an integer > 0
	 */
	public int getPriority ();
	
	
	/**
	 * Returns true, if the given format is available in this mapper.
	 * 
	 * @param format
	 * @return true, if we have an icon for that format
	 */
	public boolean hasIcon (URI format);
	
	
	/**
	 * Get a URL pointing to an icon in the archive.
	 * If no icon is available <code>null</code> is supposed to be returned.
	 * 
	 * @param format
	 * @return the URL pointing to the icon for that format
	 */
	public URL formatToIconUrl (URI format);
	
	
	/**
	 * Get an icon stream given a format. This stream will ship the icon as it is
	 * stored in our jar file.
	 * If no icon is available <code>null</code> is supposed to be returned.
	 * 
	 * @param format
	 * @return the stream delivering the icon for the format
	 */
	public InputStream formatToIconStream (URI format);
	
	
	/**
	 * Get an icon file name given a format, as it can be found in our jar
	 * archive. Especially useful for people who are going to extract the icons.
	 * If no icon is available <code>null</code> is supposed to be returned.
	 * 
	 * @param format
	 * @return the name of the icon for this format
	 */
	public String formatToIconName (URI format);
	
}
