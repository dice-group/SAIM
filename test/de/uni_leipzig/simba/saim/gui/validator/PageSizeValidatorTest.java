package de.uni_leipzig.simba.saim.gui.validator;

import static org.junit.Assert.*;
import org.junit.Test;

import com.vaadin.data.Validator.InvalidValueException;


public class PageSizeValidatorTest {

	String badValues[] = { "0", "-122", "sdhsa", "232.545", "34,6", "21n"};
	String goodValues[] = {"1000", "-1"};
	@Test
	public void testIsValid() {
		PageSizeValidator valid = new PageSizeValidator("test");
		for(String s : badValues) {
			try{
				assertFalse(valid.isValid(s));
				valid.validate(s);
				fail("Parsed "+s+" without exception.");
			} catch(InvalidValueException e) {

			}
		}
		for(String s : goodValues)
		{
			assertTrue(valid.isValid(s));			
			valid.validate(s);
		}
	}

}
