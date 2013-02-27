package de.uni_leipzig.simba.saim.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

//import org.apache.catalina.tribes.util.Arrays;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Class handles user authentication. As of now we maintain a list of User which is serialized to a file.
 * @author Klaus Lyko
 *
 */
public class UserAuthenticator{
	private final static String userFile = "userFile";
	LinkedList<User> userList;
	
	/**
	 * Construcor loads List of users.
	 */
	public UserAuthenticator() {
		readUsers();
	}
	
	/**
	 * Method retrieves a user identified by it's name.
	 * @param name Name of the user.
	 * @return User instance, if it exists or null.
	 */
	public User getUser(String name) {
		for(User u : userList) {
			if(u.getName().equalsIgnoreCase(name))
				return u;
		}
		return null;
	}
	
	/**
	 * Authenticate the user with the password.
	 * @param user
	 * @param password
	 * @return true if given password is the password for the user.
	 */
	public boolean authenticate(User user, String password) {
		byte[] passHash = DigestUtils.md5(password);
		if(Arrays.equals(passHash, user.getPasswordHash())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method reads users from serialization.
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	private void readUsers() {
		URL url;String path;
		url = getClass().getClassLoader().getResource(userFile);		
		InputStream fis = null;
		LinkedList<User> users = null;
		try {
			path = new File(url.toURI()).getAbsolutePath();
			fis = new FileInputStream (path);
			ObjectInputStream o = new ObjectInputStream( fis );
			users = (LinkedList<User>) o.readObject();
		}
		catch ( IOException e ) { System.err.println( e ); }
		catch ( ClassNotFoundException e ) { System.err.println( e ); } 
		catch (URISyntaxException e) { System.err.println( e ); } 
		finally { try { fis.close(); } catch ( Exception e ) { System.err.println( e ); }}
		this.userList = users;
	}
	
	/**
	 * Add the given user to the list of users and serialize it. If a user with this name already
	 * exists it will be replaced. So make sure to check whether a user with this name already exists.
	 * @param user
	 */
	public void addUser(User user) {
		User u = getUser(user.getName());
		if(u != null)
			u = user;
		else
			userList.add(user);
		saveUserList();
	}
	
	/**
	 * Indicates whether a user with the given name already exists.
	 * @param name Name of the User to look for.
	 * @return True if a user with this name exists, false otherwise.
	 */
	public boolean existsUser(String name) {
		for(User u : userList) {
			if(u.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Serialize the user list.
	 */
	public void saveUserList() {
		OutputStream fos = null;
		try	{
			fos = new FileOutputStream(userFile);
			@SuppressWarnings("resource")
			ObjectOutputStream o = new ObjectOutputStream( fos );
			o.writeObject( userList );
		}
		catch ( IOException e ) { System.err.println( e ); }
		finally { try { fos.close(); } catch ( Exception e ) { e.printStackTrace(); } }
	}
	
	public static void main(String args[]) {
		UserAuthenticator auth = new UserAuthenticator();
		User u = auth.getUser("Admin");
		boolean authenticated = auth.authenticate(u, "ThisIsAPasswrd");
		System.out.println(u+" auth:"+authenticated);
	}

}
