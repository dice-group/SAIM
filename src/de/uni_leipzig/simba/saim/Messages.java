package de.uni_leipzig.simba.saim;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/** The property files are expected to be encoded in UTF8 contrary to the Java definition and displayed with a small hack.*/
public class Messages
{
	private static transient final Logger logger = Logger.getLogger(Messages.class);

	private static final String	BUNDLE_NAME	= "de.uni_leipzig.simba.saim.messages"; //$NON-NLS-1$
	private ResourceBundle	RESOURCE_BUNDLE;
	public synchronized void setLanguage(String language)
	{		
		Locale locale = new Locale(language);		
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME,locale,Thread.currentThread().getContextClassLoader());

		if(bundle==null)
		{
			logger.warn("Could not create the resource bundle for language \""+language+"\".");
		}
		else if(!bundle.getLocale().getLanguage().equals(language))
		{
			logger.warn("Language of resource bundle \""+bundle.getLocale().getLanguage()+"\" does not match requested language \""+language+"\". Language not changed.");
		}
		else
		{
			logger.info("language set to \""+language+"\"");this.RESOURCE_BUNDLE=bundle;
		}
	}
	
	public Messages(Locale locale) {RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME, locale, Thread.currentThread().getContextClassLoader());}

	// TODO: remove hack when Java permits UTF-8 in property files, see http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle
	public String getString(String key)
	{
		try{return new String(RESOURCE_BUNDLE.getString(key).getBytes("ISO-8859-1"), "UTF-8");}
		catch (MissingResourceException e){	return '!' + key + '!';	}
		catch (UnsupportedEncodingException e){	throw new RuntimeException("UTF-8 not supported",e);}
	}
}