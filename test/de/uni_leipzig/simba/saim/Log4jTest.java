package de.uni_leipzig.simba.saim;
import org.apache.log4j.Logger;
import org.junit.Test;

public class Log4jTest
{	
 private static Logger log = Logger.getLogger(Log4jTest.class);
 
 @Test
 public void testLog4j()
 {	 	 
	 log.error("test error");
	 log.warn("test warning");
	 log.info("test info");
	 log.debug("test debug");
	 log.trace("test trace");
 }
}