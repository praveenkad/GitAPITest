package com.testsuite.basesuite;

import java.io.File;
import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.commonutils.ApiUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class TestSetUp extends ApiUtils {

	public static ExtentTest test;
	public static ExtentReports report;

	/**
	 * TestNG before test method used to initiate ExtentReport's
	 * {@code ExtentReports} object and load extent report configuration xml file &
	 * set target location of the generated TestReport.html file
	 * @author Praveen Kadambari
	 */
	@BeforeTest
	public void setUp() {
		report = new ExtentReports(System.getProperty("user.dir") + "\\Reports\\TestReport.html");
		report.addSystemInfo("Host Name", "GitHubAPI").addSystemInfo("Environment", "Local").addSystemInfo("User Name",
				"Praveen Kadambari");
		report.loadConfig(new File(System.getProperty("user.dir") + "\\extent-config.xml"));
		
		System.out.println("========================\n"+"Before Test Invoked"+"\n========================");
		
		
	}

	/**
	 * TestNG before method used to get the name of the running test method and
	 * initiate ExtentReport's {@code ExtentTest} object
	 * @param method a {@code Method} containing the TestNG test method details
	 * @author Praveen Kadambari
	 */
	@BeforeMethod
	public void startTest(Method method) {
		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmm\n"+"Before Method Invoked"+"\nmmmmmmmmmmmmmmmmmmmmmmmm");
		test = report.startTest(method.getName());
	}

	/**
	 * TestNG after method used to end ExtentReport's {@code ExtentTest} object
	 * @author Praveen Kadambari
	 */
	@AfterMethod
	public void endTest() {
		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmm\n"+"After Method Invoked"+"\nmmmmmmmmmmmmmmmmmmmmmmmm");
		report.endTest(test);
	}

	/**
	 * TestNG after test method used to terminate ExtentReport's
	 * {@code ExtentReport} object
	 * @author Praveen Kadambari
	 */
	@AfterTest
	public void tearDown() {
		System.out.println("========================\n"+"After Test Invoked"+"\n========================");
		report.flush();
	}

}
