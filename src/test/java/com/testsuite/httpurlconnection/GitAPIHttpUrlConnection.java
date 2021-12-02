package com.testsuite.httpurlconnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.testsuite.basesuite.TestSetUp;

@Listeners(com.listeners.BaseListener.class)

public class GitAPIHttpUrlConnection extends TestSetUp {

	static Logger logger = Logger.getLogger(GitAPIHttpUrlConnection.class);

	
	@Test
	public void dummyTest() {
		System.out.println("Hey there I'm created to demonstrate git merging");
	}
	
	
	/**
	 * TestNG test method to test "Fetching the total number of Github repositories
	 * for a given programming language" functionality using
	 * {@code HttpURLConnection}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpUrlConnectionFetchTotalRepositories() {
		HttpURLConnection huc = null;
		try {
			String url = getProperty("BASE_URL") + "?" + getQuery("QueryLanguage");
			//String url = "http://localhost:3000/movies";

			huc = openHttpURLConnection(url);
			
			logStep("HTTP Connection Established..!", test);

			if (getStatusCodeHttpUrlConnection(huc) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpUrlConnection(huc));
			}

			String responseString = getResponseBody(huc);
			
			System.out.println("===================== \n"+responseString+"\n =====================\n");

			logStep("HTTP Reponse Body Received..!", test);

			String total[] = responseString.split(",");
			logger.info("Total Number Of repositories " + total[0].replaceAll("[^0-9]", ""));
			logStep("Asserting if total number of results returned..!", test);
			Assert.assertTrue(total[0].replaceAll("[^0-9]", "") != null, "Total Number Of Results Not Returned..!");
			logStep("Assertion Passed..!", test);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			closeHttpURLConnection(huc);
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Filter repositories by a given creation date"
	 * functionality using {@code HttpURLConnection}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpUrlConnectionFilterReposByCreatedDate() {
		HttpURLConnection huc = null;

		try {
			String url = getProperty("BASE_URL") + "?" + getQuery("QueryReposByDate");

			String createdDate = getQuery("QueryReposByDate").substring(getQuery("QueryReposByDate").indexOf(":") + 1,
					getQuery("QueryReposByDate").indexOf("&"));

			huc = openHttpURLConnection(url);
			logStep("HTTP Connection Established..!", test);
			if (getStatusCodeHttpUrlConnection(huc) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpUrlConnection(huc));
			}

			String responseString = getResponseBody(huc);
			logStep("HTTP Reponse Body Received..!", test);

			String responseArr[] = responseString.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(responseArr));
			int count = 0;
			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("\"created_at\":\"" + createdDate)) {
					count++;
				}
			}
			logger.info("Actual Result Count for Specified Created Date : " + count);
			String query = getQuery("QueryReposByDate");
			int expectedCount = Integer.parseInt(query.substring(query.length() - 3).replaceAll("[^0-9]", ""));
			logStep("Asserting each repository returned contains the specified created date..!", test);
			Assert.assertTrue(count == expectedCount, "created_at date mismatch in results..!");
			logStep("Assertion Passed..!", test);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			closeHttpURLConnection(huc);
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Specify more than one programming language"
	 * functionality using {@code HttpURLConnection}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpUrlConnectionFetReposForMultipleProgLanguages() throws MalformedURLException, IOException {
		HttpURLConnection huc = null;
		try {
			String url = getProperty("BASE_URL") + "?" + getQuery("QueryMultipleLanguages");

			huc = openHttpURLConnection(url);
			logStep("HTTP Connection Established..!", test);

			if (getStatusCodeHttpUrlConnection(huc) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpUrlConnection(huc));
			}

			String responseString = getResponseBody(huc);
			logStep("HTTP Reponse Body Received..!", test);

			String responseArr[] = responseString.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(responseArr));

			String laguagesQuery = getQuery("QueryMultipleLanguages")
					.substring(2, getQuery("QueryMultipleLanguages").length()).replaceAll("language:", "")
					.replaceAll("[^a-zA-Z]", ",");
			String languages[] = laguagesQuery.split(",");

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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			closeHttpURLConnection(huc);
			logStep("HTTP Connection Closed..!", test);
		}
	}

	/**
	 * TestNG test method to test "Fetch repositories for a specific user"
	 * functionality using {@code HttpURLConnection}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpUrlConnectionFetchReposForSpecificUser() throws MalformedURLException, IOException {
		HttpURLConnection huc = null;
		try {
			String url = getProperty("USERS_URL") + "/" + getQuery("QueryUser") + "/repos";

			huc = openHttpURLConnection(url);
			logStep("HTTP Connection Established..!", test);

			if (getStatusCodeHttpUrlConnection(huc) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpUrlConnection(huc));
			}

			String responseString = getResponseBody(huc);
			logStep("HTTP Reponse Body Received..!", test);

			String responseArr[] = responseString.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(responseArr));

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
			closeHttpURLConnection(huc);
			logStep("HTTP Connection Closed..!", test);
		}
	}

	/**
	 * 
	 * TestNG test method to test "Fetch the most starred Github repositories and
	 * sort & order the results decreasingly" functionality using
	 * {@code HttpURLConnection}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpUrlConnectionFetchMostStartedReposOrderDescending() throws MalformedURLException, IOException {
		HttpURLConnection huc = null;
		try {
			String url = getProperty("BASE_URL") + "?" + getQuery("QuerySortStarredInDescendingOrder");
			huc = openHttpURLConnection(url);
			logStep("HTTP Connection Established..!", test);

			if (getStatusCodeHttpUrlConnection(huc) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpUrlConnection(huc));
			}

			String responseString = getResponseBody(huc);
			logStep("HTTP Reponse Body Received..!", test);

			String responseArr[] = responseString.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(responseArr));

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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			closeHttpURLConnection(huc);
			logStep("HTTP Connection Closed..!", test);
		}

	}

	/**
	 * TestNG test method to test "Specify a maximum number of results per page"
	 * functionality using {@code HttpURLConnection}
	 * 
	 * @author Praveen Kadambari
	 */
	@Test
	public void httpUrlConnectionSpecifyMaxNumberOfResultsPerPage() throws MalformedURLException, IOException {
		HttpURLConnection huc = null;
		try {
			String url = getProperty("BASE_URL") + "?" + getQuery("QueryMaxNumberOfResultsPerPage");

			String query = getQuery("QueryMaxNumberOfResultsPerPage");
			int expectedCount = Integer.parseInt(query.substring(query.length() - 3).replaceAll("[^0-9]", ""));

			huc = openHttpURLConnection(url);
			logStep("HTTP Connection Established..!", test);

			if (getStatusCodeHttpUrlConnection(huc) != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + getStatusCodeHttpUrlConnection(huc));
			}

			String responseString = getResponseBody(huc);
			logStep("HTTP Reponse Body Received..!", test);

			String responseArr[] = responseString.split(",");

			List<String> arrString = new ArrayList<String>(Arrays.asList(responseArr));

			int count = 0;
			for (String str : arrString) {
				if (str.replaceAll(" ", "").contains("\"git_url\"")) {
					count++;
				}
			}
			logger.info("Actual Result Count on the page : " + count);
			logStep("Asserting if repositories returned match the maximum page result count specified..!", test);
			Assert.assertTrue(count == expectedCount, "Maximum Number of Results mismatch");
			logStep("Assertion Passed..!", test);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception Occured..!", e);
			logger.info(e.getLocalizedMessage());
		} finally {
			closeHttpURLConnection(huc);
			logStep("HTTP Connection Closed..!", test);
		}
	}
}
