package de.uni_leipzig.simba.saim.core;

import java.io.File;
/**
 * Class to handle storing files. *
 * @author Lyko
 */
public class FileStore {

	public static final String store = "SAIM/EPStore";

	/**
	 * Method to create directories. Will attempt to create a directories "SAIM/EPStore"
	 * in the user home directory.
	 */
	public static boolean setUp() {
		if(new File(System.getProperty("user.home")+"/"+store).exists())
			return true;
		else
			return new File(System.getProperty("user.home")+"/"+store).mkdirs();
	}

	/**
	 * Return absolute path to the directory storing dumped endpoints.
	 * @return Absolute path to the directory to save dumped endpoints.
	 */
	public static String getPathToEPStore() {
		return new File(System.getProperty("user.home")+"/"+store).getAbsolutePath();
	}

}
