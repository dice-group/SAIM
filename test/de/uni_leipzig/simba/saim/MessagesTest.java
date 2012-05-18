package de.uni_leipzig.simba.saim;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessagesTest
{
	//TODO: make test work for other default locales too
	@Test
	public void testGetString()
	{
		assertTrue(new Messages().getString("ClassMatchingPanel.nosuggestionsfound").equals("Keine Vorschläge gefunden"));
	}

}