package de.uni_leipzig.simba.saim.gui.validator;

import com.vaadin.data.Validator;

import de.uni_leipzig.simba.saim.core.EndpointTester;

public class EndpointURLValidator implements Validator
{
	private static final long	serialVersionUID	= -5470766225738299746L;

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		if(!(value instanceof String)) {throw new InvalidValueException("The Endpoint URL is not a string.");}
		String s = (String)value;
		if(!(s.startsWith("http://"))) {throw new InvalidValueException("The Endpoint URL does not start with \"http://\".");}
		else {
			
				try {
					Object ans[] = EndpointTester.testSPARQLEndpointTimeOut(s);
					if(!(Boolean)ans[0]) {throw new InvalidValueException((String) ans[1]);}
				} catch (Exception Exc)  {
					throw new InvalidValueException(Exc.getMessage());
				}
			}			
		//if(!(s.endsWith("/sparql"))) {throw new InvalidValueException("The Endpoint URL does not end with \"/sparql\".");}			
	}

	@Override
	public boolean isValid(Object value)
	{
		try{validate(value);} catch (InvalidValueException e) {return false;}
		return true;
	}

}