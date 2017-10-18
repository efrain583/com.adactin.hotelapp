package com.adactin.hotelapp.tests;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.adactin.hotelapp.util.UtilKit;

/*
 * githup : https://github.com/efrain583/com.adactin.hotelapp.git
 */
public class AdactinBase {
	
	WebDriver driver;
	String myBrowser = new String("firefox");
	
	// Optional parameter "browser",  default value is "firefox"
	// This optional parameter is set in the testng.xml file
	@Parameters("browser")
	@BeforeClass
	public void startClass(@Optional("firefox") String browser){
		myBrowser = browser;
		driver = UtilKit.initTest("com.adactin.hotelapp", "adactin", myBrowser, getClass().getName());
		
	}
	
	@BeforeMethod
	public void startMethod(Method method){
		UtilKit.initMethod(method.getName());
	}

	@AfterMethod
	public void stopMethod(ITestResult result) {
		UtilKit.terminateMethod(driver, result);
		UtilKit.navegateToBaseURL(driver);
	}
	
	@AfterClass
	public void stopClass(){
		UtilKit.terminateTest(driver);
	}
	
}
