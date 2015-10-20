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
package de.unirostock.sems.cbext.recognizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;
import de.unirostock.sems.cbext.Formatizer;



/**
 * The Class DefaultFormatRecognizer knows some common extensions to map it to
 * formats.
 */
public class DefaultRecognizer
	extends FormatRecognizer
{
	
	/** priority for this format recognizer */
	protected static int 			priority			= 100;
	
	/**
	 * Sets the priority of this format recognizer and triggers a resort of all
	 * format recognizers.
	 * 
	 * The higher the priority, the earlier this recognizer gets called.
	 * The first recognizer, which is able to identify a file, determines it's
	 * format.
	 * Setting a negative priority will be ignored.
	 * Default recognizers have a priority of 100.
	 * 
	 * @param newPriority the new priority of this recogniser
	 */
	public static void setPriority (int newPriority) {
		
		// no negative priorities!
		if( priority < 0 )
			return;
		
		priority = newPriority;
		Formatizer.resortRecognizers();
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.cbext.FormatRecognizer#getPriority()
	 */
	@Override
	public int getPriority ()
	{
		return priority;
	}
	
	/** known formats file. */
	private static final String	EXT2FORMAT_NAME	= "/ext2format.prop";
	
	/** The ext2 format. */
	private Properties					ext2Format			= new Properties ();
	
	
	/**
	 * Instantiates a new default extension mapper.
	 */
	public DefaultRecognizer ()
	{
		
		try
		{
			InputStream input = DefaultRecognizer.class
				.getResourceAsStream (EXT2FORMAT_NAME);
			ext2Format.load (input);
			input.close ();
		}
		catch (IOException e)
		{
			LOGGER.error (e, "error reading known formats: ",
				DefaultRecognizer.class.getResourceAsStream (EXT2FORMAT_NAME));
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFormatFromMime(java.lang.String
	 * )
	 */
	@Override
	public URI getFormatFromMime (String mime)
	{
		
		if (mime == null)
			return null;
		
		String uriString = ext2Format.getProperty (mime, null);
		if (uriString != null)
			return FormatRecognizer.buildUri (uriString, "");
		else
			return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFromatFromExtension(java.lang
	 * .String)
	 */
	@Override
	public URI getFormatFromExtension (String extension)
	{
		// extension and mime are handled equally
		return getFormatFromMime (extension);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.cbext.FormatRecognizer#getFormatByParsing(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public URI getFormatByParsing (File file, String mimeType)
	{
		// this recognizer is not able to understand files from parsing it..
		return null;
	}
	
}
