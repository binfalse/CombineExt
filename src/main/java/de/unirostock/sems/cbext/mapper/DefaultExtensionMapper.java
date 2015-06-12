package de.unirostock.sems.cbext.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.cbext.ExtensionMapper;

/**
 * The Class DefaultExtensionMapper.
 */
public class DefaultExtensionMapper implements ExtensionMapper {
	
	/** known formats file. */
	private static final String	EXT2FORMAT_NAME		= "/ext2format.prop";
	
	/** The ext2 format. */
	private Properties ext2Format					= new Properties();
	
	/**
	 * Instantiates a new default extension mapper.
	 */
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

	/* (non-Javadoc)
	 * @see de.unirostock.sems.cbext.ExtensionMapper#getPriority()
	 */
	@Override
	public int getPriority() {
		return 100;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.cbext.ExtensionMapper#getFormatFromMime(java.lang.String)
	 */
	@Override
	public URI getFormatFromMime(String mime) {
		
		if( mime == null )
			return null;
		
		try {
			String uriString = ext2Format.getProperty(mime, null);
			if( uriString != null )
				return new URI(uriString);
			else
				return null;

		} catch (URISyntaxException e) {
			LOGGER.error(e, "Error generating URI");
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.cbext.ExtensionMapper#getFromatFromExtension(java.lang.String)
	 */
	@Override
	public URI getFromatFromExtension(String extension) {
		// extension and mime are handled equally
		return getFormatFromMime(extension);
	}

}
