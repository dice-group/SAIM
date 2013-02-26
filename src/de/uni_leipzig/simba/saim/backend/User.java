package de.uni_leipzig.simba.saim.backend;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * Class to hold data about user. Is just a stub.
 * @author Klaus Lyko
 *
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private byte[] password;
	private String role = "user";
	
	/**
	 * 
	 * @param name
	 * @param password
	 * @param role
	 */
	public User(String name, String password, String role) {
		this.name = name;
		this.role = role;
		setPassword(password);
	}
	
	public String getRole() {
		return role;
	}
	
	public byte[] getPasswordHash() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Set a new password. The password will be stored as a MD5 Hash for security reasons.
	 * @param passwordString
	 */
	public void setPassword(String passwordString) {
		this.password = DigestUtils.md5(passwordString);
	}
	
	@Override
	public String toString() {
		return name+"("+role+")";
	}
}
