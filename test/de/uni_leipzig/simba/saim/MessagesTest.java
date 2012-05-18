package de.uni_leipzig.simba.saim;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessagesTest
{

	@Test
	public void testGetString()
	{
		System.out.println(Messages.getString("ClassMatchingPanel.nosuggestionsfound"));
	}

}
