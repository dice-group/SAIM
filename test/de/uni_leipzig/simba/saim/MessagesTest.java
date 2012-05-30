package de.uni_leipzig.simba.saim;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

public class MessagesTest
{
	//TODO: make test work for other default locales too
	@Test
	public void testGetString()
	{
		assertTrue(new Messages(new Locale("de")).getString("ClassMatchingPanel.nosuggestionsfound").equals("Keine Vorschläge gefunden"));
		assertTrue(new Messages(new Locale("en")).getString("ClassMatchingPanel.nosuggestionsfound").equals("No suggestions found"));
	}
}