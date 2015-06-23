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
package de.unirostock.sems.cbext.collections;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.IconCollection;



/**
 * The Class DefaultIconCollection provides you with a bunch of default icons.
 */
public class DefaultIconCollection
	implements IconCollection
{
	
	/** The Constant FORMAT2ICON_NAME. */
	private static final String	FORMAT2ICON_NAME	= "/format2icon.prop";
	
	/** The Constant ICON_DIR. */
	private static final String	ICON_DIR					= "/icons/";
	
	/** The format2 icon. */
	protected Properties				format2Icon				= new Properties ();
	
	
	/**
	 * Instantiates a new default icon mapper.
	 */
	public DefaultIconCollection ()
	{
		
		try
		{
			InputStream input = DefaultIconCollection.class
				.getResourceAsStream (FORMAT2ICON_NAME);
			format2Icon.load (input);
			input.close ();
		}
		catch (IOException e)
		{
			LOGGER.error (e, "error reading known formats: ",
				DefaultIconCollection.class.getResourceAsStream (FORMAT2ICON_NAME));
		}
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.IconMapper#getPriority()
	 */
	@Override
	public int getPriority ()
	{
		return 100;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.IconMapper#hasIcon(java.net.URI)
	 */
	@Override
	public boolean hasIcon (URI format)
	{
		return format2Icon.getProperty (format.toString ()) != null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.IconMapper#formatToIconUrl(java.net.URI)
	 */
	@Override
	public URL formatToIconUrl (URI format)
	{
		String name = formatToIconName (format);
		if (name != null)
			return DefaultIconCollection.class.getResource (ICON_DIR + name);
		else
			return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.IconMapper#formatToIconStream(java.net.URI)
	 */
	@Override
	public InputStream formatToIconStream (URI format)
	{
		String name = formatToIconName (format);
		if (name != null)
			return DefaultIconCollection.class.getResourceAsStream (ICON_DIR + name);
		else
			return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.IconMapper#formatToIconName(java.net.URI)
	 */
	@Override
	public String formatToIconName (URI format)
	{
		return format2Icon.getProperty (format.toString (), null);
	}
	
	
	/**
	 * Get a list of formats for that we have an icon.
	 * 
	 * @return set of objects that are actually strings
	 */
	public Set<Object> getAvailableFormatIcons ()
	{
		return format2Icon.keySet ();
	}
	
}
