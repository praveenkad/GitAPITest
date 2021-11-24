package com.listeners;

import org.apache.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.LogStatus;
import com.testsuite.basesuite.TestSetUp;

public class BaseListener implements ITestListener {

	static Logger logger = Logger.getLogger(BaseListener.class);
	

	/**
	 * TestNG listener method that gets executed before test method gets executed
	 * @param result a {@code ITestResult} object used to get running test method name & write logs to log4j and ExtentReports
	 * @author Praveen Kadambari
	 */
	@Override
	public void onTestStart(ITestResult result) {
		logger.info("Running the Test "+result.getName());
		TestSetUp.test.log(LogStatus.PASS, "Running the Test "+result.getName());
	}

	/**
	 * TestNG listener method that gets executed when there is a test failure
	 * @param result a {@code ITestResult} object used to get failed test method name & write logs to log4j and ExtentReports
	 * @author Praveen Kadambari
     */
	@Override
	public void onTestFailure(ITestResult result) {
		TestSetUp.test.log(LogStatus.FAIL, "Test "+result.getName()+" is failed");
		TestSetUp.test.log(LogStatus.FAIL, result.getThrowable());
		logger.info("Test "+result.getName()+" is failed");
		logger.error(result.getThrowable());
	}

	/**
	 * TestNG listener method that gets executed when there is a test skip
	 * @param result a {@code ITestResult} object used to get failed test method name & write logs to log4j and ExtentReports
	 * @author Praveen Kadambari
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		TestSetUp.test.log(LogStatus.SKIP, "Test "+result.getName()+" is skipped");
		logger.info("Test "+result.getName()+" is skipped");
	}

	
	/**
	 * TestNG listener method that gets executed when a test gets passed
	 * @param result a {@code ITestResult} object used to get failed test method name & write logs to log4j and ExtentReports
	 * @author Praveen Kadambari
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		TestSetUp.test.log(LogStatus.PASS, "Test "+result.getName()+" is Passed");
		logger.info("Test "+result.getName()+" is passed");
	}
}
