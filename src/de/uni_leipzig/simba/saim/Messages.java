package de.uni_leipzig.simba.saim;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{
	private static final String	BUNDLE_NAME	= "de.uni_leipzig.simba.saim.messages"; //$NON-NLS-1$
	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
	private Messages(){}
	
	// TODO: remove hack when Java permits UTF-8 in property files, see http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle
	public static String getString(String key)
	{
		try{return new String(RESOURCE_BUNDLE.getString(key).getBytes("ISO-8859-1"), "UTF-8");}
		catch (MissingResourceException e){	return '!' + key + '!';	}
		catch (UnsupportedEncodingException e){	throw new RuntimeException("UTF-8 not supported",e);}
	}
}