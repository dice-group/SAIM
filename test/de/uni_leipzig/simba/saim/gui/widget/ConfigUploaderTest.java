package de.uni_leipzig.simba.saim.gui.widget;

import static org.junit.Assert.*;
import org.junit.Test;
import de.uni_leipzig.simba.io.ConfigReader;
import de.uni_leipzig.simba.saim.core.Configuration;

public class ConfigUploaderTest
{
	@Test
	public void test()
	{
		ConfigReader reader = new ConfigReader();
		Configuration config = new Configuration();
		reader.validateAndRead("resource/examples/drugbank-sider.xml");
		config.setFromConfigReader(reader);
		assertTrue(config.source.graph!=null);
		assertTrue(config.target.graph!=null);
	}
}
