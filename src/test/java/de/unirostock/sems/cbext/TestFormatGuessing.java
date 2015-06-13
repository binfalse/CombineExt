/**
 * 
 */
package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;

import org.junit.Test;

import de.unirostock.sems.cbext.recognizer.SbmlRecognizer;

/**
 * @author Martin Scharm
 *
 */
public class TestFormatGuessing
{
	
	/**
	 * Test SBML guessing.
	 */
	@Test
	public void testGuessSBML ()
	{
		File f = new File ("test/BIOMD0000000459.xml");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/sbml.level-2.version-4", format.toString ());
	}
	
	/**
	 * Test SBOL guessing.
	 */
	@Test
	public void testGuessSBOL ()
	{
		File f = new File ("test/guess-SBOLj-examples-data-BBa_I0462.xml");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/sbol", format.toString ());
	}
	
	/**
	 * Test BioPax guessing.
	 */
	@Test
	public void testGuessBioPax ()
	{
		File f = new File ("test/guess-biopax-paxtools-core-src-main-resources-org-biopax-paxtools-model-biopax-level3.owl");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/biopax", format.toString ());
	}
	
	/**
	 * Test CellML guessing.
	 */
	@Test
	public void testGuessSEDML ()
	{
		File f = new File ("test/BIOMD0000000459-SEDML.xml");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/sed-ml.level-1.version-1", format.toString ());
	}
	
	/**
	 * Test CellML guessing.
	 */
	@Test
	public void testGuessCellML ()
	{
		File f = new File ("test/aguda_b_1999.cellml");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/cellml", format.toString ());
	}
	
	/**
	 * Test SBGN guessing.
	 */
	@Test
	public void testGuessSBGN ()
	{
		File f = new File ("test/guess-sbgn-AF-activity-nodes.sbgn");
		URI format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/sbgn", format.toString ());
		
		
		f = new File ("test/guess-sbgn-ER-binary-no-outcome.sbgn");
		format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/sbgn", format.toString ());
		
		
		f = new File ("test/guess-sbgn-PD-clone-marker.sbgn");
		format = Formatizer.guessFormat (f);
		assertEquals ("got wrong format for " + f.getAbsolutePath (), "http://identifiers.org/combine.specifications/sbgn", format.toString ());
	}
	
}
