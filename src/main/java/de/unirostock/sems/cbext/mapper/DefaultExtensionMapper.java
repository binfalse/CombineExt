package de.unirostock.sems.cbext.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.ExtensionMapper;
import de.unirostock.sems.cbext.FormatParser;

public class DefaultExtensionMapper implements ExtensionMapper {
	
	/** known formats file. */
	private static final String	EXT2FORMAT_NAME		= "/ext2format.prop";
	
	private Properties ext2Format					= new Properties();
	
	public DefaultExtensionMapper() {
		
		try {
			InputStream input = DefaultExtensionMapper.class.getResourceAsStream(EXT2FORMAT_NAME);
			ext2Format.load(input);
			input.close();
		}
		catch (IOException e) {
			LOGGER.error (e, "error reading known formats: ", DefaultExtensionMapper.class.getResourceAsStream (EXT2FORMAT_NAME) );
		}
		
	}

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public URI getFormatFromMime(String mime) {
		
		if( mime == null )
			return null;
		
		String uriString = ext2Format.getProperty(mime, null);
		if( uriString != null )
			return FormatParser.buildUri (uriString, "");
		else
			return null;
	}

	@Override
	public URI getFormatFromExtension(String extension) {
		// extension and mime are handled equally
		return getFormatFromMime(extension);
	}

}
