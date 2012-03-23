import static org.junit.Assert.*;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.junit.Test;

public class EhcacheTest
{
	@Test
	public void testEhcache()
	{
		Ehcache cache = CacheManager.getInstance().getCache("test");
		Element element = new Element("var",new String("data"));
		cache.put(element);
		assertTrue(cache.get("var").getValue().equals("data"));		
	}
}