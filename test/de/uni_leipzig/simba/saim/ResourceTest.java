package de.uni_leipzig.simba.saim;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
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

		//new ClassResource("img/no_crystal_clear_32.png",getApplication()))
	}

	@Test
	public void testColorProperties() throws IOException
	{//TODO make test compilable with Java 1.6
		try
		{
			InputStream in=getClass().getClassLoader().getResourceAsStream("de/uni_leipzig/simba/saim/colors/default.properties");
			assertNotNull(in);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			for(String key: new String[]{"measure","operator","output","sourceproperty","targetproperty"})
			{
				Color.decode(properties.get(key).toString());
			}
		} catch(Exception e) {
			// workaround
			assertNotNull(null);
		}
	}
}
