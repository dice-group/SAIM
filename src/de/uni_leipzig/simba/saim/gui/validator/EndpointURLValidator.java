package de.uni_leipzig.simba.saim.gui.validator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import de.uni_leipzig.simba.saim.Messages;
import de.uni_leipzig.simba.saim.core.EndpointTester;
import de.uni_leipzig.simba.saim.core.EndpointTester.EndpointStatus;
/** Validates the string format (starts with "http") and sends a sample sparql query to the endpoint.*/
public class EndpointURLValidator implements Validator
{
	private final Messages messages;
	private static final long	serialVersionUID	= -5470766225738299746L;
	protected static Map<String,EndpointStatus> validateCache = new HashMap<String,EndpointStatus>();
	final Component component;
	Logger logger = LoggerFactory.getLogger(EndpointURLValidator.class);

	public EndpointURLValidator() {this(null,new Messages(Locale.getDefault()));}
	public EndpointURLValidator(Component component,final Messages messages)
	{
		this.component = component;
		this.messages = messages;
	}

	private EndpointTester tester = new EndpointTester();

	//	protected validationColor()
	//	{
	//
	//	}

	/**	terminates the EndpointURLValidator and all it's running threads and connections. */
	public void close()
	{
		tester.shutdownNow();
	}

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		if(!(value instanceof String)) {throw new InvalidValueException(messages.getString("endpointurlnotastring"));}
		String s = (String)value;
		if(s.contains(".csv"))
			return;
		if(!(s.startsWith("http://"))) {
			//FIXME support local files
			logger.info("This is a local endpoint, skipping validation");
//			throw new InvalidValueException(messages.getString("endpointurldoesnotstartwithhttp"));
		}
		else {
			EndpointStatus status = validateCache.get(s);
			if(status==null)
			{
				status = tester.testSPARQLEndpointTimeOut(s);
				validateCache.put(s,status);
			}

			if(status==EndpointStatus.OK)
			{
				if(component!=null)
				{
					component.setStyleName("valid");
					component.removeStyleName("invalid");
				}
				return;
			}
			if(component!=null)
			{
				component.removeStyleName("valid");
				component.setStyleName("invalid");
			}
			throw new InvalidValueException(messages.getString("endpointstatus")+status.toString());
		}
	}
	@Override
	public boolean isValid(Object value)
	{
		try{validate(value);} catch (InvalidValueException e) {return false;}
		return true;
	}
	
	public static void main(String args[]) {
		String ep = "http://www4.wiwiss.fu-berlin.de/dailymed/sparql";
		EndpointURLValidator valid = new EndpointURLValidator();
		valid.isValid(ep);
	}

}
