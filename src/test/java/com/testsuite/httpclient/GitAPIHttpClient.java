package com.testsuite.httpclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.testsuite.basesuite.TestSetUp;

@SuppressWarnings("deprecation")
@Listeners(com.listeners.BaseListener.class)
public class GitAPIHttpClient extends TestSetUp {

	static Logger logger = Logger.getLogger(GitAPIHttpClient.class);

	/**
	 * TestNG test method to test "Fetching the total number of Github repositories
	 * for a given programming language" functionality using {@code HttpClient}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpClientfetchTotalRepositories() throws IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			URIBuilder queryParams = new URIBuilder(getProperty("HOST_URL") + getProperty("END_POINT"));
			queryParams.setParameter("accept", getProperty("ParamAccept"));
			queryParams.setParameter("q", getProperty("ParamLanguage"));
			logStep("Setting Query Parameters to GET request", test);
			CloseableHttpResponse response = getCloseableHttpResponse(httpclient, queryParams);
			logStep("HTTP Connection Established..!", test);
			logStep("HTTP Reponse Body Received..!", test);
			if (getStatusCodeHttpClient(response) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpClient(response));
			}

			String responseBody = getHttpClientResponseString(response);

			String total[] = responseBody.split(",");
			logger.info("Total Number Of results " + total[0].replaceAll("[^0-9]", ""));
			logStep("Asserting if total number of results returned..!", test);
			Assert.assertTrue(total[0].replaceAll("[^0-9]", "") != null, "Total Number Of Results Not Returned..!");
			logStep("Assertion Passed..!", test);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Filter repositories by a given creation date"
	 * functionality using {@code HttpClient}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpClientFilterReposByCreatedDate() throws MalformedURLException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			URIBuilder queryParams = new URIBuilder(getProperty("HOST_URL") + getProperty("END_POINT"));
			queryParams.setParameter("accept", getProperty("ParamAccept"));
			queryParams.setParameter("q", getProperty("ParamCreatedDate"));
			queryParams.setParameter("per_page", getProperty("ParamCreatedDatePerPage"));
			logStep("Setting Query Parameters to GET request", test);
			CloseableHttpResponse response = getCloseableHttpResponse(httpclient, queryParams);
			logStep("HTTP Connection Established..!", test);
			logStep("HTTP Reponse Body Received..!", test);
			if (getStatusCodeHttpClient(response) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpClient(response));
			}

			String responseBody = getHttpClientResponseString(response);

			String arr[] = responseBody.split(",");
			List<String> arrString = new ArrayList<String>(Arrays.asList(arr));

			String createdDate = getProperty("ParamCreatedDate").substring(
					getProperty("ParamCreatedDate").indexOf(":") + 1, getProperty("ParamCreatedDate").length());
			int count = 0;
			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("\"created_at\":\"" + createdDate)) {
					count++;
				}
			}
			logger.info("Status Code : " + getStatusCodeHttpClient(response));
			logger.info("Actual Result Count for Specified Created Date : " + count);
			logStep("Asserting each repository returned contains the specified created date..!", test);
			Assert.assertTrue(count == Integer.parseInt(getProperty("ParamCreatedDatePerPage")),
					"created_at date mismatch in results..!");
			logStep("Assertion Passed..!", test);

		} catch (URISyntaxException e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Specify more than one programming language"
	 * functionality using {@code HttpClient}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpClientGetReposForMultipleProgLanguages() throws MalformedURLException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			URIBuilder queryParams = new URIBuilder(getProperty("HOST_URL") + getProperty("END_POINT"));
			queryParams.setParameter("accept", getProperty("ParamAccept"));
			queryParams.setParameter("q", getProperty("ParamLanguage"));
			queryParams.setParameter("q", getProperty("ParamLanguage2"));
			queryParams.setParameter("q", getProperty("ParamLanguage3"));
			queryParams.setParameter("q", getProperty("ParamLanguage4"));
			queryParams.setParameter("per_page", getProperty("ParamMultiLanguagePerPage"));
			logStep("Setting Query Parameters to GET request", test);
			CloseableHttpResponse response = getCloseableHttpResponse(httpclient, queryParams);
			logStep("HTTP Connection Established..!", test);
			logStep("HTTP Reponse Body Received..!", test);
			if (getStatusCodeHttpClient(response) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpClient(response));
			}

			String responseBody = getHttpClientResponseString(response);

			String arr[] = responseBody.split(",");
			List<String> arrString = new ArrayList<String>(Arrays.asList(arr));

			String languages[] = new String[4];
			languages[0] = getProperty("ParamLanguage").substring(getProperty("ParamLanguage").indexOf(":") + 1,
					getProperty("ParamLanguage").length());
			languages[1] = getProperty("ParamLanguage2").substring(getProperty("ParamLanguage2").indexOf(":") + 1,
					getProperty("ParamLanguage2").length());
			languages[2] = getProperty("ParamLanguage3").substring(getProperty("ParamLanguage3").indexOf(":") + 1,
					getProperty("ParamLanguage3").length());
			languages[3] = getProperty("ParamLanguage4").substring(getProperty("ParamLanguage4").indexOf(":") + 1,
					getProperty("ParamLanguage4").length());

			int count = 0;
			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("\"language\":\"" + languages[0])
						|| str.replaceAll(" ", "").contains("\"language\":\"" + languages[1])
						|| str.replaceAll(" ", "").contains("\"language\":\"" + languages[2])
						|| str.replaceAll(" ", "").contains("\"language\":\"" + languages[3])) {
					count++;
				}
			}
			logger.info("Actual Result Count for Specified Program Language : " + count);
			logStep("Asserting the results returned for multiple programming languges..!", test);
			Assert.assertTrue(count != 0, "None of the results returned from specified programming languages..!");
			logStep("Assertion Passed..!", test);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Fetch repositories for a specific user"
	 * functionality using {@code HttpClient}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpClientFetchReposForSpecificUser() throws MalformedURLException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String url = getProperty("USERS_URL") + "/" + getQuery("QueryUser") + "/repos";
			CloseableHttpResponse response = getCloseableHttpResponse(httpclient, url);
			logStep("HTTP Connection Established..!", test);
			logStep("HTTP Reponse Body Received..!", test);
			if (getStatusCodeHttpClient(response) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpClient(response));
			}

			String responseBody = getHttpClientResponseString(response);

			String arr[] = responseBody.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(arr));

			int count = 0;
			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("\"full_name\":\"" + getQuery("QueryUser"))) {
					count++;
				}
			}
			logger.info("Total number of repositories found for specific user: " + count);
			logStep("Asserting each repository returned contains the specified user..!", test);
			Assert.assertTrue(count != 0, "No results returned for specified user..!");
			logStep("Assertion Passed..!", test);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
			logStep("HTTP Connection Closed..!", test);
		}
	}

	/**
	 * TestNG test method to test "sort & order the results decreasingly order the
	 * results decreasingly" functionality using {@code HttpClient}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpClientFetchMostStartedReposOrderDescending() throws MalformedURLException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			URIBuilder queryParams = new URIBuilder(getProperty("HOST_URL") + getProperty("END_POINT"));
			queryParams.setParameter("accept", getProperty("ParamAccept"));
			queryParams.setParameter("q", getProperty("ParamLanguage"));
			queryParams.setParameter("sort", getProperty("ParamSort"));
			queryParams.setParameter("order", getProperty("ParamOrder"));
			logStep("Setting Query Parameters to GET request", test);
			CloseableHttpResponse response = getCloseableHttpResponse(httpclient, queryParams);

			if (getStatusCodeHttpClient(response) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpClient(response));
			}

			String responseBody = getHttpClientResponseString(response);
			logStep("HTTP Connection Established..!", test);
			logStep("HTTP Reponse Body Received..!", test);
			String arr[] = responseBody.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(arr));

			List<Integer> starList = new ArrayList<Integer>();

			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("stargazers_count")) {
					starList.add(Integer.parseInt(str.replaceAll("[^0-9]", "")));
				}
			}

			logStep("Asserting if Repos returned are ordered in descending order", test);
			for (int i = 0; i < starList.size() - 1; i++) {
				if (!(starList.get(i) > starList.get(i + 1))) {
					Assert.fail("Repos are not ordered in descending order..!");
				}
			}
			logStep("Assertion Passed..!", test);

		} catch (URISyntaxException e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Specify a maximum number of results per page"
	 * functionality using {@code HttpClient}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpClientSpecifyMaxNumberOfResultsPerPage() throws MalformedURLException, IOException {

		logger.info("Running specifyMaxNumberOfResultsPerPage");
		test.log(LogStatus.PASS, "Running specifyMaxNumberOfResultsPerPage");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			URIBuilder queryParams = new URIBuilder(getProperty("HOST_URL") + getProperty("END_POINT"));
			queryParams.setParameter("accept", getProperty("ParamAccept"));
			queryParams.setParameter("q", getProperty("ParamLanguage"));
			queryParams.setParameter("per_page", getProperty("ParamMaxResultsPerPage"));
			logStep("Setting Query Parameters to GET request", test);
			CloseableHttpResponse response = getCloseableHttpResponse(httpclient, queryParams);
			logStep("HTTP Connection Established..!", test);
			logStep("HTTP Reponse Body Received..!", test);
			if (getStatusCodeHttpClient(response) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpClient(response));
			}

			String responseBody = getHttpClientResponseString(response);

			String arr[] = responseBody.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(arr));

			int count = 0;
			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("\"git_url\"")) {
					count++;
				}
			}
			logger.info("Actual Result Count on the page : " + count);
			logStep("Asserting if repositories returned match the maximum page result count specified..!", test);
			Assert.assertTrue(count == Integer.parseInt(getProperty("ParamMaxResultsPerPage")),
					"Maximum Number of Results mismatch");
			logStep("Assertion Passed..!", test);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
			logStep("HTTP Connection Closed..!", test);
		}

	}

}
