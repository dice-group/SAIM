package de.uni_leipzig.simba.saim;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;

public class ResourceTest {
	@Test
	public void testResource() throws FileNotFoundException {		
		assertNotNull(getClass().getClassLoader().getResourceAsStream("examples/dbpedia-linkedmdb.xml"));
		URL url = getClass().getClassLoader().getResource("examples");//dbpedia-linkedmdb.xml");
	
		String path;
		try {
			path = new File(url.toURI()).getAbsolutePath();
			File f = new File(path);
			assertTrue(f.exists());	
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//url.getFile();//URLDecoder.decode(url.getPath(),System.);
	
	}
}
