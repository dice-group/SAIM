package de.uni_leipzig.simba.saim.widget;

import org.junit.Test;

import de.konrad.commons.sparql.PrefixHelper;
import de.uni_leipzig.simba.io.KBInfo;

public class TestMetricPanel {
	@Test
	public void testAddProperty() {
		String ex[] ={ "foaf:name",
				"http://sfdsfjiirgi.scp0.fsf/onto/fff#"};
		
		System.out.println(PrefixHelper.expand(ex[0]));
		System.out.println(PrefixHelper.expand(ex[1]));
		
		for (String s : ex) {
			KBInfo info = new KBInfo();
			s = PrefixHelper.expand(s);
			info.var = "?src";
			
			PrefixHelper.generatePrefix(s);
			String base = PrefixHelper.getBase(s);
			
			
			System.out.println(base);
			info.prefixes.put(PrefixHelper.getPrefix(base), PrefixHelper.getURI(PrefixHelper.getPrefix(base)));
			System.out.println(info.var+": adding property: "+s+" with prefix "+PrefixHelper.getPrefix(base)+" - "+PrefixHelper.getURI(PrefixHelper.getPrefix(base)));
		}
	}
}
