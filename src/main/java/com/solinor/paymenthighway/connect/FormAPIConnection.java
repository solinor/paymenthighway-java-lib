/**
 * 
 */
package com.solinor.paymenthighway.connect;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.solinor.paymenthighway.security.SecureSigner;

/**
 * PaymentHighway Form API Connections
 * 
 * @author Tero Kallio <tero.kallio@solinor.com>
 */
public class FormAPIConnection {

	private final static String USER_AGENT = "PaymentHighway Java Lib";
	private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private final static String METHOD_POST = "POST";
	
	String serviceUrl = null;
	String signatureKeyId = null;
	String signatureSecret = null;
	
	/**
	 * Constructor
	 */
	public FormAPIConnection(String serviceUrl, 
			String signatureKeyId, 
			String signatureSecret) {	
		
		this.serviceUrl = serviceUrl;
		this.signatureKeyId = signatureKeyId;
		this.signatureSecret = signatureSecret;
	}
		
	/**
	 * Form API call to add card
	 * 
	 * @param formParameters
	 * @return String responseBody
	 * @throws IOException
	 */
	public String addCardRequest(List<NameValuePair> nameValuePairs) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		final String formUri = "/form/view/add_card";
		
        try {
            HttpPost httpPost = new HttpPost(this.serviceUrl + formUri);
            
            // sort alphabetically per key
            this.sortParameters(nameValuePairs);
            
            // create signature
            String signature = this.createSignature(METHOD_POST, formUri, nameValuePairs);
            nameValuePairs.add(new BasicNameValuePair("signature", signature));
            
            // add request headers
            this.addHeaders(httpPost);
            
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            	// TODO: shall we handle responses like 401 unauthorized in some way?
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            return httpclient.execute(httpPost, responseHandler);
    
        } finally {
            httpclient.close();
        }
    }
	
	/**
	 * Form API call to make a payment
	 * 
	 * @param paymentParameters
	 * @return 
	 * @throws IOException
	 */
	public String paymentRequest(List<NameValuePair> nameValuePairs) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		final String formPaymentUri = "/form/view/pay_with_card";
		
        try {
            HttpPost httpPost = new HttpPost(this.serviceUrl + formPaymentUri);
            
            // sort alphabetically per key
            this.sortParameters(nameValuePairs);
            
            // create signature
            String signature = this.createSignature(METHOD_POST, formPaymentUri, nameValuePairs);
            nameValuePairs.add(new BasicNameValuePair("signature", signature));

        	// add request headers
            this.addHeaders(httpPost);
            
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            return httpclient.execute(httpPost, responseHandler);

        } finally {
            httpclient.close();
        }
	}
	
	/**
	 * Form API call to add card and make a payment
	 * 
	 * @param paymentParameters
	 * @return 
	 * @throws IOException
	 */
	public String addCardAndPayRequest(
			List<NameValuePair> nameValuePairs) throws IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String formPaymentUri = "/form/view/add_and_pay_with_card";
		
        try {
            HttpPost httpPost = new HttpPost(this.serviceUrl + formPaymentUri);
            
            // sort alphabetically per key
            this.sortParameters(nameValuePairs);
            
            // create signature
            String signature = this.createSignature(METHOD_POST, formPaymentUri, nameValuePairs);
            nameValuePairs.add(new BasicNameValuePair("signature", signature));

            // add request headers
            this.addHeaders(httpPost);
            
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            return httpclient.execute(httpPost, responseHandler);

	    } finally {
            httpclient.close();
        }
	}	

	/** 
	 * Create secure signature
	 * 
	 * @param method
	 * @param uri
	 * @param formPaymentSphParameters
	 * @return String signature
	 */
	private String createSignature(String method, String uri, 
				List<NameValuePair> nameValuePairs) {
		
		nameValuePairs = this.parseParameters(nameValuePairs);
		SecureSigner ss = new SecureSigner(this.signatureKeyId, this.signatureSecret);
		return ss.createSignature("POST", uri, nameValuePairs, "");
	}

	/**
	 * Signature is formed from parameters that start with "sph-"
	 * Therefore we remove other parameters from the signature param set.
	 * 
	 * @param map TreeMap that may include all params
	 * @return TreeMap with only params starting "sph-"
	 */
	protected List<NameValuePair> parseParameters(
			List<NameValuePair> map) {
		for(Iterator<NameValuePair> it = map.iterator(); it.hasNext(); ) {
		      NameValuePair entry = it.next();
		      if(!entry.getName().startsWith("sph-")) {
		        it.remove();
		      }
		}
		return map;
	}

	/**
	 * Sort alphabetically per key
	 * @param nameValuePairs
	 * @return List sorted list
	 */
	protected void sortParameters(
			List<NameValuePair> nameValuePairs) {
		Comparator<NameValuePair> comp = new Comparator<NameValuePair>() { 
			@Override
            public int compare(NameValuePair p1, NameValuePair p2) {
              return p1.getName().compareTo(p2.getName());
            }
        };
        Collections.sort(nameValuePairs, comp);
	}
	
	/**
	 * Add headers to request
	 * @param httpPost
	 * @return HttpPost with headers
	 */
	protected void addHeaders(HttpPost httpPost) {
		httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.addHeader("Content-Type", CONTENT_TYPE);
	}
	
}
