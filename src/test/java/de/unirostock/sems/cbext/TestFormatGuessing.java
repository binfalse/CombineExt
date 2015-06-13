/**
 * 
 */
package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import org.junit.Test;

import de.unirostock.sems.cbext.recognizer.BioPaxRecognizer;
import de.unirostock.sems.cbext.recognizer.CellMlRecognizer;
import de.unirostock.sems.cbext.recognizer.SbgnRecognizer;
import de.unirostock.sems.cbext.recognizer.SbmlRecognizer;
import de.unirostock.sems.cbext.recognizer.SbolRecognizer;
import de.unirostock.sems.cbext.recognizer.SedMlRecognizer;

/**
 * @author Martin Scharm
 *
 */
public class TestFormatGuessing
{
	
	/** The Constant XML_FILE. */
	public static final File XML_FILE = new File ("test/some.xml");
	
	/**
	 * Test SBML guessing.
	 */
	@Test
	public void testGuessSBML ()
	{
		File f = new File ("test/BIOMD0000000459.xml");
		String fn = f.getAbsolutePath ();
		String correctFormat = "http://identifiers.org/combine.specifications/sbml.level-2.version-4";
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		SbmlRecognizer recognizer = new SbmlRecognizer ();
		String mime = null;
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, "http://identifiers.org/combine.specifications/sbml", recognizer.getFormatFromExtension ("sbml").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
	}
	
	/**
	 * Test SBOL guessing.
	 */
	@Test
	public void testGuessSBOL ()
	{
		File f = new File ("test/guess-SBOLj-examples-data-BBa_I0462.xml");
		String fn = f.getAbsolutePath ();
		String correctFormat = "http://identifiers.org/combine.specifications/sbol";
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		SbolRecognizer recognizer = new SbolRecognizer ();
		String mime = null;
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatFromExtension ("sbol").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
	}
	
	/**
	 * Test BioPax guessing.
	 */
	@Test
	public void testGuessBioPax ()
	{
		File f = new File ("test/guess-biopax-paxtools-core-src-main-resources-org-biopax-paxtools-model-biopax-level3.owl");
		String fn = f.getAbsolutePath ();
		String correctFormat = "http://identifiers.org/combine.specifications/biopax";
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		BioPaxRecognizer recognizer = new BioPaxRecognizer ();
		String mime = null;
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatFromExtension ("biopax").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
	}
	
	/**
	 * Test CellML guessing.
	 */
	@Test
	public void testGuessSEDML ()
	{
		File f = new File ("test/BIOMD0000000459-SEDML.xml");
		String fn = f.getAbsolutePath ();
		String correctFormat = "http://identifiers.org/combine.specifications/sed-ml.level-1.version-1";
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		SedMlRecognizer recognizer = new SedMlRecognizer ();
		String mime = null;
		try
		{
			mime = Files.probeContentType (f.toPath ());
			System.out.println (mime);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, "http://identifiers.org/combine.specifications/sed-ml", recognizer.getFormatFromExtension ("sedml").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
	}
	
	/**
	 * Test CellML guessing.
	 */
	@Test
	public void testGuessCellML ()
	{
		File f = new File ("test/aguda_b_1999.cellml");
		String fn = f.getAbsolutePath ();
		String correctFormat = "http://identifiers.org/combine.specifications/cellml";
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		CellMlRecognizer recognizer = new CellMlRecognizer ();
		String mime = null;
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatFromExtension ("cellml").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
	}
	
	/**
	 * Test SBGN guessing.
	 */
	@Test
	public void testGuessSBGN ()
	{
		File f = new File ("test/guess-sbgn-AF-activity-nodes.sbgn");
		String fn = f.getAbsolutePath ();
		String correctFormat = "http://identifiers.org/combine.specifications/sbgn";
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		SbgnRecognizer recognizer = new SbgnRecognizer ();
		String mime = null;
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatFromExtension ("sbgn").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
		
		
		f = new File ("test/guess-sbgn-ER-binary-no-outcome.sbgn");
		fn = f.getAbsolutePath ();
		format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatFromExtension ("sbgn").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
		
		
		f = new File ("test/guess-sbgn-PD-clone-marker.sbgn");
		fn = f.getAbsolutePath ();
		format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + fn, correctFormat, format.toString ());
		
		
		// test the recognizer
		try
		{
			mime = Files.probeContentType (f.toPath ());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail ("wasn't able to get mime...");
		}

		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatByParsing (f, mime).toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatByParsing (XML_FILE, mime));
		
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime ("something"));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (mime));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromMime (null));
		
		assertEquals ("got wrong format for " + fn, correctFormat, recognizer.getFormatFromExtension ("sbgn").toString ());
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension (null));
		assertNull ("got wrong format for " + fn, recognizer.getFormatFromExtension ("stuff"));
	}
	
}
