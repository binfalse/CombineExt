/**
 * 
 */
package de.unirostock.sems.cbext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Martin Scharm
 *
 */
public class TestStuff
{
	@Test
	public void testFormatParser ()
	{
		assertNotNull ("valid URI shouln't return null", FormatParser.buildUri ("http://", "binfalse.de"));
		assertNull ("valid URI shouln't return null", FormatParser.buildUri (":", "binfalse.de"));
	}
	
}
