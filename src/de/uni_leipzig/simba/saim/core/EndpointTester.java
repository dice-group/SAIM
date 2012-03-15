package de.uni_leipzig.simba.saim.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

/** Class to test if a certain endpoint is available */
public class EndpointTester {
	
	private String url;
	
	public EndpointTester(String url){
		this.url = url;
	}
	
	/**
	 * Tests whether the URL is available.s
	 * @param url
	 * @return
	 */
	public static boolean pingEndpoint(String url) {
		boolean available = false;
		try{
		    final URLConnection connection = new URL(url).openConnection();
		    connection.connect();
		    available = true;
		} catch(final MalformedURLException e){
		    throw new IllegalStateException("Bad URL: " + url, e);
		} catch(final IOException e){
		    available = false;
		}
		return available;
	}
	
	private boolean querySPARQLEndpoint() {
		boolean answer = false;
		String query = "SELECT ?s WHERE { ?s ?p ?o . } LIMIT 1";		
		Query sparqlQuery = QueryFactory.create(query);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(url, sparqlQuery);
        ResultSet res = qexec.execSelect();
        if(res.hasNext())
        	answer = true;       
        return answer;				
	}
	
	public static boolean testSPARQLEndpoint(String url) {
		EndpointTester tester = new EndpointTester(url);
		return tester.querySPARQLEndpoint();
	}
	
//	public static boolean testSPARQLEndpointTimeOut(String url) {
//		EndpointTester tester = new EndpointTester(url);
//		   
//        ExecutorService executor = Executors.newCachedThreadPool();
//        Callable<Object> task = new Callable<Object>() {
//           public Object call() {
//              return querySPARQLEndpoint(url);
//           }
//        };
//        Future<Object> future = executor.submit(task);
//        try {
//           boolean result = future.get(5, TimeUnit.SECONDS); 
//        } catch (TimeoutException ex) {
//           // handle the timeout
//        } catch (InterruptedException e) {
//           // handle the interrupts
//        } catch (ExecutionException e) {
//           // handle other exceptions
//        } finally {
//           future.cancel(); // may or may not desire this
//        }
//	}
	
}
