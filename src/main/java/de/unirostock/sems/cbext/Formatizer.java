/**
 * 
 */
package de.unirostock.sems.cbext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.util.BioPaxIOException;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;
import org.sbgn.SbgnUtil;
import org.sbgn.bindings.Sbgn;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbolstandard.core.SBOLDocument;
import org.sbolstandard.core.SBOLFactory;
import org.sbolstandard.core.SBOLValidationException;
import org.xml.sax.SAXException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.cellml.algorithm.CellMLValidator;
import de.unirostock.sems.bives.cellml.parser.CellMLDocument;



/**
 * The Class Formatizer to generate format URIs for certain file types.
 * 
 * @author Martin Scharm
 */
public class Formatizer
{
	
	/** known formats file. */
	private static final String	ext2formatFile		= "/ext2format.prop";
	
	/** identifiers.org base uri. */
	private static final String	IDENTIFIERS_BASE	= "http://identifiers.org/combine.specifications/";
	
	/** known formats. */
	private static Properties		ext2format				= new Properties ();
	static
	{
		String defaultUri = "http://purl.org/NET/mediatypes/application/x.unknown";
		try
		{
			GENERIC_UNKNOWN = new URI (defaultUri);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace ();
			LOGGER.error (e, "error generating generic default uri: ", defaultUri);
		}
		try
		{
			InputStream is = Iconizer.class.getResourceAsStream (ext2formatFile);
			if (is != null)
				ext2format.load (is);
		}
		catch (IOException e)
		{
			e.printStackTrace ();
			LOGGER.error (e, "error reading known formats: ",
				Iconizer.class.getResourceAsStream (ext2formatFile));
		}
	}
	
	/** The generic unknown format URI. */
	public static URI						GENERIC_UNKNOWN;
	
	
	/**
	 * Guess format given a file.
	 * 
	 * @param file
	 *          the file
	 * @return the format
	 */
	public static URI guessFormat (File file)
	{
		if (!file.isFile ())
			return null;
		
		String mime = null;
		try
		{
			mime = Files.probeContentType (file.toPath ());
		}
		catch (IOException e)
		{
			LOGGER.warn (e, "could not get mime from file " + file);
		}
		
		if (mime != null && mime.equals ("application/xml"))
		{
			// test for special identifiers files
			
			// try sedml
			try
			{
				SEDMLDocument doc = Libsedml.readDocument (file);
				doc.validate ();
				if (doc.hasErrors ())
				{
					StringBuilder errors = new StringBuilder ();
					for (SedMLError e : doc.getErrors ())
						if (e.getSeverity ().compareTo (SedMLError.ERROR_SEVERITY.ERROR) >= 0)
							errors.append ("[" + e.getMessage () + "]");
					if (errors.length () > 0)
						throw new IOException ("error reading sedml file: "
							+ errors.toString ());
				}
				org.jlibsedml.Version v = doc.getVersion ();
				return buildUri (IDENTIFIERS_BASE, "sed-ml.level-" + v.getLevel ()
					+ ".version-" + v.getVersion ());
			}
			catch (IOException | XMLException | org.jdom.IllegalAddException e)
			{
				LOGGER.info (e, "file ", file, " seems to be no sedml file..");
			}
			
			// try sbml
			try
			{
				SBMLDocument doc = SBMLReader.read (file);
				return buildUri (IDENTIFIERS_BASE, "sbml.level-" + doc.getLevel ()
					+ ".version-" + doc.getVersion ());
			}
			catch (Exception e)
			{
				LOGGER.info (e, "file ", file, " seems to be no sbml file..");
			}
			
			// try CellML
			try
			{
				CellMLValidator validator = new CellMLValidator ();
				if (!validator.validate (file))
					throw new IOException ("error parsing cellml doc: "
						+ validator.getError ().getMessage ());
				return buildUri (IDENTIFIERS_BASE, "cellml");
			}
			catch (IOException e)
			{
				LOGGER.info (e, "file ", file, " seems to be no cellml file..");
			}
			
			// try sbol
			try
			{
				SBOLFactory.read (new FileInputStream (file));
				return buildUri (IDENTIFIERS_BASE, "sbol");
			}
			catch (IOException | SBOLValidationException e)
			{
				LOGGER.info (e, "file ", file, " seems to be no sbol file..");
			}
			
			// try sbgn
			try
			{
				if (!SbgnUtil.isValid (file))
					throw new IOException ("error parsing sbgn doc");
				return buildUri (IDENTIFIERS_BASE, "sbgn");
			}
			catch (JAXBException | SAXException | IOException e)
			{
				LOGGER.info (e, "file ", file, " seems to be no sbgn file..");
			}
			
			// ok, parsing failed. let's still try file extensions.
			String name = file.getName ();
			int dot = name.lastIndexOf (".");
			if (dot > 0)
			{
				String ext = name.substring (dot + 1);
				if (ext.equals ("sbml") || ext.equals ("sedml")
					|| ext.equals ("sed-ml") || ext.equals ("sbgn")
					|| ext.equals ("omex") || ext.equals ("cellml")
					|| ext.equals ("biopax")
				/*
				 * || ext.equals ("")
				 * || ext.equals ("")
				 */
				)
					return getFormatFromExtension (ext);
			}
		}
		
		if (mime != null && mime.equals ("application/rdf+xml"))
		{
			// try biopax
			try
			{
				BioPAXIOHandler handler = new org.biopax.paxtools.io.SimpleIOHandler (); // auto-detects
																																									// Level
				org.biopax.paxtools.model.Model model = handler
					.convertFromOWL (new FileInputStream (file));
				
				return buildUri (IDENTIFIERS_BASE, "biopax");
			}
			catch (IOException | BioPaxIOException e)
			{
				LOGGER.info (e, "file ", file, " seems to be no biopax file..");
			}
			
			// ok, parsing failed. let's still try file extensions.
			String name = file.getName ();
			int dot = name.lastIndexOf (".");
			if (dot > 0)
			{
				String ext = name.substring (dot + 1);
				if (ext.equals ("biopax")
				/*
				 * || ext.equals ("")
				 * || ext.equals ("")
				 */
				)
					return getFormatFromExtension (ext);
			}
		}
		
		return getFormatFromMime (mime);
	}
	
	
	/**
	 * Gets the format given a mime type.
	 * 
	 * @param mime
	 *          the mime type
	 * @return the format
	 */
	public static URI getFormatFromMime (String mime)
	{
		if (mime == null)
			return GENERIC_UNKNOWN;
		String uri = ext2format.getProperty (mime, null);
		try
		{
			return uri == null ? new URI ("http://purl.org/NET/mediatypes/" + mime)
				: new URI (uri);
		}
		catch (URISyntaxException e)
		{
			LOGGER.warn (e, "error generating URI.");
			return GENERIC_UNKNOWN;
		}
	}
	
	
	/**
	 * Gets the format given a file extension.
	 * 
	 * @param extension
	 *          the file extension
	 * @return the format
	 */
	public static URI getFormatFromExtension (String extension)
	{
		String uri = ext2format.getProperty (extension, null);
		try
		{
			return uri == null ? GENERIC_UNKNOWN : new URI (uri);
		}
		catch (URISyntaxException e)
		{
			LOGGER.warn (e, "error generating URI.");
			return GENERIC_UNKNOWN;
		}
	}
	
	
	/**
	 * Builds an URI as `start+end` without caring about an exception. Only use if
	 * you're sure it's not going to fail. If we cannot produce this URI, we're
	 * returning null.
	 * 
	 * @param pre
	 *          the start
	 * @param post
	 *          the end
	 * @return the URI as start+end
	 */
	public static URI buildUri (String pre, String post)
	{
		try
		{
			return new URI (pre + post);
		}
		catch (URISyntaxException e)
		{
			LOGGER.error ("wasn't able to create URI " + pre + post);
		}
		return null;
	}
}
