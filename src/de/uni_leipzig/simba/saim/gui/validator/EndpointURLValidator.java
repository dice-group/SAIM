package de.uni_leipzig.simba.saim.gui.validator;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Validator;

import de.uni_leipzig.simba.saim.core.EndpointTester;
import de.uni_leipzig.simba.saim.core.EndpointTester.EndpointStatus;

/** Validates the string format (starts with "http") and sends a sample sparql query to the endpoint.*/
public class EndpointURLValidator implements Validator
{
	private static final long	serialVersionUID	= -5470766225738299746L;
	protected static Map<String,EndpointStatus> validateCache = new HashMap<>();

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		if(!(value instanceof String)) {throw new InvalidValueException("The Endpoint URL is not a string.");}
		String s = (String)value;
		if(!(s.startsWith("http://"))) {throw new InvalidValueException("The Endpoint URL does not start with \"http://\".");}

		else
		{
			EndpointStatus status = validateCache.get(s);
			if(status!=null)
			{
				if(status==EndpointStatus.OK) {return;}
				throw new InvalidValueException("Error: Endpoint Status: "+status.toString());
			} else
			{
				status = EndpointTester.testSPARQLEndpointTimeOut(s);
				validateCache.put(s,status); 
				if(status!=EndpointStatus.OK) {throw new InvalidValueException("Endpoint Status: "+status.toString());}}
			//"The URL is no (working) SPARQL endpoint."
		}
	}			
	//if(!(s.endsWith("/sparql"))) {throw new InvalidValueException("The Endpoint URL does not end with \"/sparql\".");}			


	@Override
	public boolean isValid(Object value)
	{
		try{validate(value);} catch (InvalidValueException e) {return false;}
		return true;
	}

}