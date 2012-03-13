package de.uni_leipzig.simba.saim.gui.validator;

import com.vaadin.data.Validator;

public class EndpointURLValidator implements Validator
{
	private static final long	serialVersionUID	= -5470766225738299746L;

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		if(!(value instanceof String)) {throw new InvalidValueException("The Endpoint URL is not a string.");}
		String s = (String)value;
		if(!(s.startsWith("http://"))) {throw new InvalidValueException("The Endpoint URL does not start with \"http://\".");}
		if(!(s.endsWith("/sparql"))) {throw new InvalidValueException("The Endpoint URL does not end with \"/sparql\".");}			
	}

	@Override
	public boolean isValid(Object value)
	{
		try{validate(value);} catch (InvalidValueException e) {return false;}
		return true;
	}

}