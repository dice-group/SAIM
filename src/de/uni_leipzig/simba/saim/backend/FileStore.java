package de.uni_leipzig.simba.saim.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import de.uni_leipzig.simba.io.KBInfo;
/**
 * Class to handle storing files. *
 * @author Lyko
 */
public class FileStore {

	public static final String base = System.getProperty("user.home");
	public static final String store = "SAIM/EPStore";
	public static final String infosDump = "kbinfos.dump";
	/**
	 * Method to create directories. Will attempt to create a directories store and infos.
	 * in the user home directory.
	 */
	public static boolean setUp() {
		if(new File(base+"/"+store).exists() && new File(base+"/"+store+"/"+infosDump).exists())
			return true;
		else {
			File EPStore = new File(base+"/"+store);
			File infoStore = new File(base+"/"+store+"/"+infosDump);
			EPStore.mkdirs();
			
			boolean fileIsSetUp = infoStore.exists();
			if(fileIsSetUp) {
				return true;
			} else {
			    return setUpInfoStore();
			}
		}	
	}
	
	private static boolean setUpInfoStore() {
		return saveInfoList(new LinkedList<KBInfo>());
	}

	public static boolean saveInfoList(LinkedList<KBInfo> list) {
		File infoStore = new File(base+"/"+store+"/"+infosDump);
		 try {
		      FileOutputStream fout = new FileOutputStream(infoStore.getAbsolutePath());
		      ObjectOutputStream oos = new ObjectOutputStream(fout);
		      oos.writeObject(list);
		      oos.close();
		      }
		   catch (Exception e) { 
			   e.printStackTrace();
			   return false;
		   }
		return true;		
	}

	/**
	 * Return absolute path to the directory storing dumped endpoints.
	 * @return Absolute path to the directory to save dumped endpoints.
	 */
	public static String getPathToEPStore() {
		return new File(base+"/"+store).getAbsolutePath();
	}
	
	/**
	 * Attempts to read dumped list of KBInfos.
	 * @return
	 */
	public static LinkedList<KBInfo> getListOfInfos() {
		File infoStore = new File(base+"/"+store+"/"+infosDump);
//		logger.info("unserializing KBInfo list for dumped endpoints");
		try {
		    FileInputStream fin = new FileInputStream(infoStore.getAbsolutePath());
		    ObjectInputStream ois = new ObjectInputStream(fin);
		    LinkedList<KBInfo> list = (LinkedList<KBInfo>) ois.readObject();
		    ois.close();
		    return list;
		}
		   catch (Exception e) {
			   e.printStackTrace(); 
			   return new LinkedList<KBInfo>();
		}		      
	}

}
