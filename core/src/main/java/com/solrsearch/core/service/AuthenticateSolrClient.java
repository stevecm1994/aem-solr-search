package com.solrsearch.core.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.NamedList;

public class AuthenticateSolrClient extends HttpSolrClient {

	private static final long serialVersionUID = -159534942123032468L;

	private String solrCredentials;
	
	private static final String UTF_8 = StandardCharsets.UTF_8.name();
	

	/**
	 * @param baseURL
	 * @param solrCredentials
	 * Instantiates a new authenticate http solr client.
	 */
	public AuthenticateSolrClient(String baseURL,String solrCredentials) {		
		super(baseURL);
		this.solrCredentials = solrCredentials;
	}
	
	/**
	 * Override request method in the extended base class
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.solr.client.solrj.impl.HttpSolrClient#request(org.apache.solr.
	 * client.solrj.SolrRequest, java.lang.String)
	 * 
	 */
	@Override
	public NamedList<Object> request(final SolrRequest request, String collection)
			throws SolrServerException, IOException {
		ResponseParser responseParser = request.getResponseParser();
		if (responseParser == null) {
			responseParser = parser;
		}
		return request(request, responseParser, collection);
	}
	
	/**
	 * Override request method in the extended base class
	 * Authenticates the Solr client using the custom user name and password
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.solr.client.solrj.impl.HttpSolrClient#request(org.apache.solr.
	 * client.solrj.SolrRequest, org.apache.solr.client.solrj.ResponseParser,
	 * java.lang.String)
	 * 
	 */
	public NamedList<Object> request(final SolrRequest request, final ResponseParser processor, String collection)
			throws SolrServerException, IOException {
		HttpRequestBase method = createMethod(request, collection);
		String userPass = solrCredentials;
		String encoded = Base64.byteArrayToBase64(userPass.getBytes(UTF_8));
		method.setHeader(new BasicHeader("Authorization", "Basic " + encoded));
		return executeMethod(method, processor);
	}

}
