package com.commonutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.fileutils.FileReader;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ApiUtils extends FileReader{

	static Logger logger = Logger.getLogger(ApiUtils.class);

	/**
	 * Reusable method to start Http connection for given URL
	 *
	 * @param url a {@code String} containing the URL
	 * @return the {@code HttpURLConnection} to be used further in tests.
	 * @throws MalformedURLException to indicate that a malformed URL has occurred. Either no legal protocol could be found in a specification string or the string could not be parsed.
	 * @throws IOException during an input-output operation
	 * @author Praveen Kadambari
	 */
	public HttpURLConnection openHttpURLConnection(String url) {
		HttpURLConnection huc = null;
		try {
			huc = (HttpURLConnection) (new URL(url).openConnection());
			huc.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Assert.fail("MalformedURLException Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("IOException Occured..!", e);
			logger.info(e.getLocalizedMessage());

		}
		return huc;

	}

	/**
	 * Reusable method to get http response body string for given {@code HttpURLConnection} object
	 *
	 * @param huc a {@code HttpURLConnection} to get the InputSteam of the response body
	 * @return output a {@code String} that contains http response body
	 * @throws IOException during an input-output operation
	 * @author Praveen Kadambari
	 */
	public String getResponseBody(HttpURLConnection huc) {

		InputStream inputStream;
		String output = null;
		try {
			huc.setConnectTimeout(5000);
			inputStream = huc.getInputStream();
			output = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("IOException Occured..!", e);
			logger.info(e.getLocalizedMessage());
		}

		return output;

	}

	/**
	 * Reusable method to close Http connection for given {@code HttpURLConnection} object
	 * @param huc a {@code HttpURLConnection} to be closed
	 * @author Praveen Kadambari
	 */
	public void closeHttpURLConnection(HttpURLConnection huc) {
		huc.disconnect();
	}

	/**
	 * Reusable method to get http response body string for given {@code CloseableHttpResponse} object
	 *
	 * @param response a {@code CloseableHttpResponse} used to get response body
	 * @return responseBody a {@code String} that contains http response body
	 * @throws Exception any exception occurred during the process
	 * @author Praveen Kadambari
	 */
	public String getHttpClientResponseString(CloseableHttpResponse response) {
		String responseBody = null;
		try {
			HttpEntity responseString = response.getEntity();
			responseBody = EntityUtils.toString(responseString);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		}
		return responseBody;

	}

	/**
	 * Reusable method to get http response body for given {@code CloseableHttpClient} object and query parameters to be set in GET request
	 *
	 * @param httpclient a {@code CloseableHttpClient} object used to perform execute operation
	 * @param queryParams a {@code URIBuilder} object used to set parameters to GET request
	 * @return response a {@code CloseableHttpResponse} object that contains http response body
	 * @throws Exception any exception occurred during the process
	 * @author Praveen Kadambari
	 */
	public CloseableHttpResponse getCloseableHttpResponse(CloseableHttpClient httpclient, URIBuilder queryParams) {
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(queryParams.toString());
			response = httpclient.execute(httpget);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		}

		return response;

	}

	/**
	 * Reusable method to get http response body for given {@code CloseableHttpClient} object
	 *
	 * @param httpclient a {@code CloseableHttpClient} object used to perform execute operation
	 * @param url a {@code String} object used to set URL in GET request
	 * @return response a {@code CloseableHttpResponse} object that contains http response body
	 * @throws Exception any exception occurred during the process
	 * @author Praveen Kadambari
	 */
	public CloseableHttpResponse getCloseableHttpResponse(CloseableHttpClient httpclient, String url) {
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			response = httpclient.execute(httpget);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		}

		return response;

	}

	/**
	 * Reusable method to get http status code for given {@code CloseableHttpResponse} object
	 * @param response a {@code CloseableHttpResponse} object used to get http status code
	 * @return statusCode a {@code Integer} object that contains http status code
	 * @throws Exception any exception occurred during the process
	 * @author Praveen Kadambari
	 */
	public Integer getStatusCodeHttpClient(CloseableHttpResponse response) {
		int statusCode = 0;
		try {
			statusCode = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		}
		return statusCode;
	}
	
	/**
	 * Reusable method to get http status code for given {@code HttpURLConnection} object
	 * @param huc a {@code HttpURLConnection} object used to get http status code
	 * @return statusCode a {@code Integer} object that contains http status code
	 * @throws Exception any exception occurred during the process
	 * @author Praveen Kadambari
	 */
	public Integer getStatusCodeHttpUrlConnection(HttpURLConnection huc) {
		int statusCode = 0;
		try {
			statusCode = huc.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		}
		return statusCode;
	}

	/**
	 * Reusable method to set the given message in log4j logs and ExtentReports
	 * @param message a {@code String} object used in log4j and ExtentReports 
	 * @param test a {@code ExtentTest} object used to set logs in ExtentReports
	 * @author Praveen Kadambari
	 */
	public void logStep(String message, ExtentTest test) {
		test.log(LogStatus.PASS, message);
		logger.info(message);
	}

}
