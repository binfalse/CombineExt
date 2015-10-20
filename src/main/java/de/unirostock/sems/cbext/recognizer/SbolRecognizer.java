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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.sbolstandard.core.SBOLFactory;
import org.sbolstandard.core.SBOLValidationException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.FormatRecognizer;
import de.unirostock.sems.cbext.Formatizer;



/**
 * The Class SbolFormatizer to recognize sbol files.
 */
public class SbolRecognizer
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.cbext.FormatParser#checkFormat(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public URI getFormatByParsing (File file, String mimeType)
	{
		
		// mime type check
		if (mimeType == null || mimeType.equals ("application/xml") == false)
			return null;
		
		try
		{
			SBOLFactory.read (new FileInputStream (file));
			return buildUri (IDENTIFIERS_BASE, "sbol");
		}
		catch (IOException | SBOLValidationException e)
		{
			LOGGER.info (e, "file ", file, " seems to be no sbol file..");
		}
		
		// no format could be guessed
		return null;
	}
	
	
	@Override
	public URI getFormatFromMime (String mime)
	{
		// we cannot decide from just a mime type
		return null;
	}
	
	
	@Override
	public URI getFormatFromExtension (String extension)
	{
		if (extension != null && extension.equals ("sbol"))
			return buildUri (IDENTIFIERS_BASE, "sbol");
		return null;
	}
	
}
