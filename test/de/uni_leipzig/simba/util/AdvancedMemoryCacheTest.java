package de.uni_leipzig.simba.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
/** @author Konrad Höffner */
public class AdvancedMemoryCacheTest
{
//	@Test
//	public void loadSaveToFileTest() throws IOException
//	{
//		File f = new File("temp/test.ser");
//		if(f.exists()) {f.delete();}
//		f.getParentFile().mkdirs();
//		AdvancedMemoryCache cache =  new AdvancedMemoryCache();
//		cache.addTriple("test","west", "schmest");
//		cache.addTriple("rest","mest", "best");
//
//		cache.saveToFile(f);
//		AdvancedMemoryCache cacheLoaded = AdvancedMemoryCache.loadFromFile(f);
//		assertTrue(cache.toString().equals(cacheLoaded.toString()));
//	}

	@Test
	public void  getCommonPropertiesTest() throws Exception
	{
		AdvancedMemoryCache cache =  new AdvancedMemoryCache();
		GetAllSparqlQueryModule queryModule = new GetAllSparqlQueryModule(Knowledgebases.LINKEDGEODATA_CITY,40);
		queryModule.fillCache(cache);
		Set<String> properties = new HashSet<String>(Arrays.asList(cache.getCommonProperties(0.8,10)));
		System.out.println(properties);
	}
}
