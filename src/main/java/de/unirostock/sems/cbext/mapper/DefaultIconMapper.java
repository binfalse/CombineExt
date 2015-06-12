package de.unirostock.sems.cbext.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.IconMapper;

public class DefaultIconMapper implements IconMapper {
	
	private static final String FORMAT2ICON_NAME		= "/format2icon.prop";
	private static final String ICON_DIR				= "/icons/";
	
	protected Properties format2Icon					= new Properties();
	
	public DefaultIconMapper() {
		
		try {
			InputStream input = DefaultIconMapper.class.getResourceAsStream(FORMAT2ICON_NAME);
			format2Icon.load(input);
			input.close();
		}
		catch (IOException e) {
			LOGGER.error (e, "error reading known formats: ", DefaultIconMapper.class.getResourceAsStream (FORMAT2ICON_NAME) );
		}
		
	}

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public boolean hasIcon(URI format) {
		return format2Icon.getProperty (format.toString()) != null; 
	}

	@Override
	public URL formatToIconUrl(URI format) {
		String name = formatToIconName(format);
		if( name != null )
			return DefaultIconMapper.class.getResource( ICON_DIR + name );
		else
			return null;
	}

	@Override
	public InputStream formatToIconStream(URI format) {
		String name = formatToIconName(format);
		if( name != null )
			return DefaultIconMapper.class.getResourceAsStream( ICON_DIR + name );
		else
			return null;
	}

	@Override
	public String formatToIconName(URI format) {
		return format2Icon.getProperty(format.toString(), null);
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
