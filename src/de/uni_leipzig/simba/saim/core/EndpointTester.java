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
	
	public enum EndpointStatus {
			OK, EMPTY, TIMED_OUT, HTTP_ERROR, EXECUTION_ERROR, OTHER_ERROR
	}
	
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
	
	public static EndpointStatus testSPARQLEndpointTimeOut(String url)
	{
		Object[] answer = new Object[2];
		final EndpointTester tester = new EndpointTester(url);
		boolean result = false;
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Boolean> task = new Callable<Boolean>() {
           public Boolean call() {
              return tester.querySPARQLEndpoint();
           }
        };
        Future<Boolean> future = executor.submit(task);
        try {
           result = future.get(5000, TimeUnit.MILLISECONDS);
           if(!result) {
//        	   answer[1] = "SPARQL endpoint exists but seems to be empty.";
        	   return EndpointStatus.EMPTY;
           }
           return EndpointStatus.OK;
        } catch (TimeoutException ex) {
//        	answer[1] = "Request to endpoint timed out";
        	return EndpointStatus.EMPTY;
       // 	throw ex;
        } catch (InterruptedException e) {
//        	answer[1] = "Request to endpoint was Interrupted.";
        	return EndpointStatus.OTHER_ERROR;
       //    throw e;
        } catch (ExecutionException e) {
//        	answer[1] = "Error while executing request";
        	return EndpointStatus.OTHER_ERROR;
       //   throw e;
        } catch (Exception e) {
//        	answer[1] = e.getMessage();
        	return EndpointStatus.OTHER_ERROR;
        }
	}
}
