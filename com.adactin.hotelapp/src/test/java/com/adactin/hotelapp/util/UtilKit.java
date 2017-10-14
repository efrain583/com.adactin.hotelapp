package com.adactin.hotelapp.util;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import org.testng.ITestResult;

public class UtilKit {

	private static String projectFolder = "/Users/efrain/Java_practice_workspace/";
	//private static String projectFolder = "/Users/efrain/git/com.automation/";

	private static String resourcesFolder = "/src/test/resources";
	private static String projectName = null;
	private static String application;
	private static String className;
	private static Properties configProperties = new Properties();
	private static Properties UIMapProperties = new Properties();
	private static Properties DBProperties = new Properties();
	public static Logger logger = null;
	private static long startTime;
	private static long endTime;
	private static String browser;
	private static String version;

	private static Connection dbConn = null; // JDBC Connection

	public static WebDriver initTest(String project, String inApplication, String inBrowser, String inClassName) {

		WebDriver driver = null;
		
		projectName = project;
		className = inClassName;
		application = inApplication;
		DesiredCapabilities caps = null;

		System.out.println( "============================================================================================" );
		System.out.println( "Project : " + project + "          Application : " + inApplication );
		System.out.println( "============================================================================================" );

		// Load configurations
		UtilKit.loadConfigProperties(application);
		UtilKit.loadUIMapProperties(application);

		if(inBrowser.isEmpty())
			browser = getConfigProp("BROWSER");
		else
			browser=inBrowser;
		if (browser.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", getConfigProp("GECKO_DRIVER"));
			System.out.println("Gecko Driver prop : " + System.getProperty("webdriver.gecko.driver"));
			caps  = DesiredCapabilities.firefox();
			caps.setCapability("browserName", browser);
			//caps.setCapability("logLevel", "DEBUG");
			caps.setCapability("requireWindowFocus", true);
// Skip the firefox version for now
			version = getConfigProp("BROWSER_VERSION");
//			caps.setCapability("version", version);
//			caps.setVersion(version);
			driver = new FirefoxDriver(caps);
			driver.manage().deleteAllCookies();
			// The Implicit wait time is a property and apply for all findElement() calls
			driver.manage().timeouts().implicitlyWait(Long.valueOf(getConfigProp("IMPLICIT_WAIT")), TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(Long.valueOf(getConfigProp("PAGE_LOAD_WAIT")), TimeUnit.SECONDS);
			// navegate to application Url
			navegateToBaseURL(driver);
		}
		else if (browser.equalsIgnoreCase("ie")) {
			
			/*
			 * To make it work with IE The Windows SEcurity Update KB3024390 was un-installed.
			 */
			System.setProperty("webdriver.ie.driver", getConfigProp("IE_DRIVER")); // Set IE driver Path
			caps = DesiredCapabilities.internetExplorer();
			caps.setCapability("ignoreZoomSetting", true);
			caps.setCapability("requireWindowFocus", true);
			caps.setCapability("enablePersistentHover", true);
			caps.setCapability("disable-popup-blocking", true);
			caps.setCapability("ignoreProtectedModeSettings", true);
			caps.setCapability("nativeEvents", false);
			caps.setCapability("unexpectedAlertBehaviour", "accept");
			
			//caps.setCapability("logLevel", "DEBUG");
			driver = new InternetExplorerDriver(caps);
			// The Implicit wait time is a property and apply for all findElement() calls
			driver.manage().timeouts().implicitlyWait(Long.valueOf(getConfigProp("IMPLICIT_WAIT")), TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(Long.valueOf(getConfigProp("PAGE_LOAD_WAIT")), TimeUnit.SECONDS);
			// IE hides cookies until the URL is accessed so
			// We need to navegate to application Url first.
			// then clear cookies, then Navegate again 
			navegateToBaseURL(driver);
			driver.manage().deleteAllCookies();
			// navegate Again to application Url
			navegateToBaseURL(driver);
		} else {
			System.out.println("Initialization FATAL Error : Invalid Browser : " + browser + " Exiting Test .........");
			System.exit(10);

		}

		UtilKit.waitForPageToLoad(driver, 5);
		System.out.println( "Browser : " + browser + " Version : " + caps.getCapability("version") );
		System.out.println(("\n\t\tImplicit wait = " + getConfigProp("IMPLICIT_WAIT") + "\n"));
		System.out.println("Driver Time out String " + driver.manage().timeouts().toString());

		//driver.manage().window().maximize();
		
		return driver;
	}

	public static void initMethod(String testMethodName) {

		startTime = System.currentTimeMillis();
		if (logger == null) {
			logger = Logger.getLogger(application + "." + testMethodName);
			PropertyConfigurator.configure(projectFolder + projectName  + resourcesFolder + "/log4j.properties");
		}	
		logger.info("Test Method : " + className + ":" + testMethodName + " Started at " + getDateTime());
	}

	private static boolean dbConnect() {

		logger.info("Connecting to " + getDBProp("DBCONN_URL"));
		try {
			// Initiate a JDBC Connection
			dbConn = DriverManager.getConnection(getDBProp("DBCONN_URL"), getDBProp("DBUSER"), getDBProp("DBPASS"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.fatal("DB Connection Exception : " + getDBProp("DBCONN_URL"));
			e.printStackTrace();
		}
		try {
			if (dbConn.isValid(10)) { // 10 seconds timeout
				logger.info("DB Connection Success : " + getDBProp("DBCONN_URL"));
				return true;
			}
		} catch (SQLException e) {
			logger.fatal("DB Connection Invalid Exception : " + getDBProp("DBCONN_URL"));
			e.printStackTrace();
		}
		return false;
	}

	public static HtmlUnitDriver initUnitTest(String application, String testMethodName) {

		HtmlUnitDriver driver = null;

		UtilKit.loadConfigProperties(application);
		UtilKit.loadUIMapProperties(application);

		browser = getConfigProp("BROWSER");
		if (browser.equals("firefox")) {
			driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_52);

		} else if (browser.equalsIgnoreCase("explorer")) {
			driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER);
		} else {
			logger.fatal("Invalid Browser : " + browser + " Exiting Test .........");
			System.exit(10);

		}

		if (logger == null) {
			logger = Logger.getLogger(application + "." + testMethodName);
			PropertyConfigurator.configure(projectFolder + projectName  +  resourcesFolder + "/log4j.properties");
		}

		// driver.setJavascriptEnabled(true); // Only for headles, enable
		// javascript
		driver.setJavascriptEnabled(false); // Only for headles enable
											// javascript

		driver.manage().timeouts().implicitlyWait(Long.valueOf(getConfigProp("IMPLICIT_WAIT")), TimeUnit.SECONDS);

		navegateToBaseURL(driver);
		driver.manage().deleteAllCookies(); // here so that it works in IE

		logger.info("Headless Mode, Domain Name: " + (String) driver.executeScript("return document.domain")); // Needs
																												// javascript
																												// enabled

		logger.info("Test Method : " + testMethodName + " Started at " + getDateTime());
		startTime = System.currentTimeMillis();
		return driver;
	}

	public static void navegateToBaseURL(WebDriver driver) {
		
		//Get the Application URl
		driver.get(getConfigProp(application.toUpperCase() + "_URL"));

	}

	// ITestResult is a Testng Object This class describes the result of a test.
	public static void terminateMethod(WebDriver driver, ITestResult result) {

		endTime = System.currentTimeMillis();

		/*
		 * If there is a need to take a look at the results then un-comment this
		 * block Set<String> atNames = result.getAttributeNames(); for (String
		 * namestr : atNames) { System.out.println("Result Attribute : " +
		 * namestr); }
		 */

		logger.info("Test Method : " + className + "."+ result.getMethod().getMethodName() + " Completed at : " + getDateTime()
				+ " Elapsed Time in Seconds : " + (float) (endTime - startTime) / 1000 + "  Success Status = "
				+ result.isSuccess() + "\n\n");

		// Print current screen if the test case was not successful
		if (!result.isSuccess()) {
			UtilKit.printScreen(driver, result.getMethod().getMethodName());

		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void terminateTest(WebDriver driver) {
		driver.manage().deleteAllCookies();
		driver.quit(); // End Session Safely
		//driver.close(); // Close Windows
	}

	// Take the Screen Shot and capture in a file
	static public void printScreen(WebDriver driver, String methodName) {
		try {
			String destFileName = projectFolder + projectName + "/screenPrints/" + methodName + "_" + getPlainDateTime() + ".png";
			File destFile = new File(destFileName);

			System.out.println("File Name : " + destFileName);
			destFile.createNewFile();

			/*
			 * Since Driver Classes like FirefoxDriver and
			 * InternetExplorerDriver implement WebDriver and TakesScreenshot
			 * interfaces, then we can cast the driver object as a
			 * TakesScreenshot object to be able to invoke getScreenshotiAs().
			 */
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// Copy the screen shot the the destination file
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void terminateTestUnit(HtmlUnitDriver driver, ITestResult result) {

		endTime = System.currentTimeMillis();

		logger.info("Test Method : " + result.getMethod().getMethodName() + " Completed at : " + getDateTime()
				+ " Elapsed Time in Seconds : " + (float) (endTime - startTime) / 1000 + "\n\n");

		// Print current screen if the test case was not successful
		if (!result.isSuccess()) {
			UtilKit.printScreen(driver, result.getMethod().getMethodName());

		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.close();
		driver.quit();
	}

	protected static void loadConfigProperties(String application) {

		FileInputStream configPropFile = null;
		try {
			// The load() call requires an InputStream Object.
			// FileInputStream extends InputStream so it can also be used here
			// as a parameter to load()
			configPropFile = new FileInputStream(projectFolder + projectName + resourcesFolder + "/MasterTestConfig.properties");
			;
			configProperties.load(configPropFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void loadUIMapProperties(String Application) {

		FileInputStream UIMapInputFile = null;
		try {
			// The load() call requires an InputStream Object.
			// FileInputStream extends InputStream so it can also be used here
			// as a parameter to load()
			UIMapInputFile = new FileInputStream(getConfigProp(Application.toUpperCase() + "_UIMAP"));
			UIMapProperties.load(UIMapInputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void loadDBProperties(String Application) {

		// The load() call requires an InputStream Object.
		// FileInputStream extends InputStream so it can also be used here as a
		// parameter to load()
		try {
			FileInputStream DBInputFile = new FileInputStream(projectFolder + projectName + resourcesFolder + "/DBTest.properties");
			DBProperties.load(DBInputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getConfigProp(String property) {
		return (configProperties.getProperty(property));
	}

	public static String getUIMapProp(String property) {
		return (UIMapProperties.getProperty(property));
	}

	public static String getDBProp(String property) {
		return (DBProperties.getProperty(property));
	}

	/*
	 * The By locator info is stored as property in the UIMAP file The Target
	 * WebElement name itself corresponds to Property Name in the UIMAP file
	 * 
	 * The UIMAP file format is: PROPERTY = LOCATOR_TYPE.LOCATOR_VALUE ex.
	 * LOGIN_BUTTOM = xpath..//*[@id='login']
	 */
	public static By UIMap(String property) {

		String[] propStrings = parseLocator(property);

		return returnBy(propStrings);
	}

	public static By returnBy(String[] propStrings) {
		// Return the corresponding By Object.
		// This is done by calling the matching By method like By.id(),
		// By.xpath(), By.name(), etc
		// Each of those methods returns a By object which locates elements
		// by the given type and value attributes
		if (propStrings[0].equals("id")) {
			return (By.id(propStrings[1]));
		}
		if (propStrings[0].equals("xpath")) {
			return (By.xpath(propStrings[1]));
		}
		if (propStrings[0].equals("css")) {
			return (By.cssSelector(propStrings[1]));
		}
		if (propStrings[0].equals("name")) {
			return (By.name(propStrings[1]));
		}
		if (propStrings[0].equals("partialLinkText")) {
			return (By.partialLinkText(propStrings[1]));
		}

		logger.fatal("Invalid Locator :" + propStrings[0] + "." + propStrings[1]);
		return (null);
	}

	public static String[] parseLocator(String property) {
		// Parse out the the locator type and locator value,
		// type is stored in propStrings[0] and value in propStrings[1]
		String[] propStrings = UtilKit.getUIMapProp(property).split(Pattern.quote("."), 2);
		return propStrings;
	}

	public static boolean waitForElement(By Locator, WebDriver driver, String state, int waitPeriod) {

		float currentWait = (float) 0.0;
		System.out.println("INFO : in : " + Thread.currentThread().getStackTrace()[1].getMethodName() + "Locator String :" +     Locator.toString());
		while (currentWait < (float) waitPeriod) {

			// =================================================================
			if (state.equalsIgnoreCase("Exists")) {
				List<WebElement> waitElementList = driver.findElements(Locator);
				if (waitElementList.size() > 0) {
					System.out.println("INFO : In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
							+ currentWait + " Seconds...");
					return true;
				}
			}
			// =================================================================
			if (state.equalsIgnoreCase("Displayed")) {
				List<WebElement> waitElementList = driver.findElements(Locator);
				if (waitElementList.size() > 0) {
					WebElement waitElement = driver.findElement(Locator);
					if (waitElement.isDisplayed()) {
						System.out.println("INFO : In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
								+ currentWait + " Seconds...");
						return true;
					}
				}
			}
			// =================================================================
			if (state.equalsIgnoreCase("Enabled")) {
				List<WebElement> waitElementList = driver.findElements(Locator);
				if (waitElementList.size() > 0) {
					WebElement waitElement = driver.findElement(Locator);
					if (waitElement.isEnabled()) {
						System.out.println("INFO : In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
								+ currentWait + " Seconds...");
						return true;
					}
				}
			}
			// ===============================================================
			if (state.equalsIgnoreCase("Selected")) {
				List<WebElement> waitElementList = driver.findElements(Locator);
				if (waitElementList.size() > 0) {
					WebElement waitElement = driver.findElement(Locator);
					if (waitElement.isSelected()) {
						System.out.println("INFO : In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
								+ currentWait + " Seconds...");
						return true;
					}
				}
			}
			/*
			 * Do the actual wait here by sleeping for .25 of a second
			 */
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentWait = (float) (currentWait + (1.0 / 4.0)); // Add the
																// accumulated
																// time to
																// currentWait

		}
		// If it made it here then the time was expired and the Element did not
		// reach the desired status
		System.out.println("INFO : In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + state + ": " + Locator.toString() + " Expired after "
				+ currentWait + " Seconds...");
		return false;

	}
	
	public static boolean waitForElement(WebElement waitElement, String state, int waitPeriod) {

		float currentWait = (float) 0.0;
		while (currentWait < (float) waitPeriod) {
			
			System.out.println("Curren Wait : " + currentWait);

			// =================================================================
			try {
				if (state.equalsIgnoreCase("Displayed")) {
						if (waitElement.isDisplayed()) {
							logger.info("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
									+ currentWait + " Seconds...");
							return true;
						}
					}
				// =================================================================
				if (state.equalsIgnoreCase("Enabled")) {
						if (waitElement.isEnabled()) {
							logger.info("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
									+ currentWait + " Seconds...");
							return true;
						}
					}
				// ===============================================================
				if (state.equalsIgnoreCase("Selected")) {
						if (waitElement.isSelected()) {
							logger.info("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Waited for "
									+ currentWait + " Seconds...");
							return true;
						}
					}
			} catch (Exception StaleElementReferenceException) {
				System.out.println("Stale exception - 1");
			}
			/*
			 * Do the actual wait here by sleeping for .25 of a second
			 */
			try {
				Thread.sleep(250);
				System.out.println("Stale exception - 2");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentWait = (float) (currentWait + (1.0 / 4.0)); // Add the
																// accumulated
																// time to
																// currentWait

		}
		// If it made it here then the time was expired and the Element did not
		// reach the desired status
		logger.warn("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + state + " TagName: " + waitElement.getTagName() + " Expired after "
				+ currentWait + " Seconds...");
		return false;

	}


	public static boolean waitForPageToLoad(WebDriver driver, int waitPeriod) {

		float currentWait = (float) 0.0;
		System.out.println("INFO : in " + Thread.currentThread().getStackTrace()[1].getMethodName());
		while (currentWait < (float)waitPeriod ) {
			
			String pageState = null;

			// =================================================================
			try{
				pageState = (String) UtilKit.executeJavascript(driver, "return document.readyState");
			}
			catch (JavascriptException e){
				
			UtilKit.suspendAction(250);;
			currentWait = currentWait + (float)0.25;
			continue;
			}
			System.out.println("INFO: Page State : " + pageState);
			if(pageState.equals("complete")){
				System.out.println("INFO: Current Page Title : " + driver.getTitle());
				return true;

			}
		}
		
		System.out.println(" WARNNG : In " + Thread.currentThread().getStackTrace()[1].getMethodName() + "Wait for page to load expired");
		return false;
	}	

	public static boolean waitForPageTitle(WebDriver driver, int waitPeriod, String title) {

		float currentWait = (float) 0.0;
		logger.info("in : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		while (currentWait < (float)waitPeriod ) {

			// =================================================================
			if(driver.getTitle().equals(title)){
				logger.info("in :" + UtilKit.currentMethod() + "Page title : " + driver.getTitle());
				return true;

			}
			UtilKit.suspendAction(250);;
			currentWait = currentWait + (float)0.25;
		}
		
		logger.warn("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + "Wait for title to load expired");
		return false;
	}	


	public static Object[][] getTestData(String project, String application, String testCase) {

		/*
		 * This is here because this module is called before the init() module
		 * for test cases that specify a data Provider If thats the case, we
		 * need to initialize Logger here before we can use it
		 */
		if (projectName.isEmpty())
			projectName=project;
		if (logger == null) {
			logger = Logger.getLogger(application + "." + testCase);
			PropertyConfigurator.configure(projectFolder + projectName + resourcesFolder + "/log4j.properties");
		}
		UtilKit.loadDBProperties(application); // Load the DB properties

		// If a SQL statement is not provided for this This test case then use Excel Master Data file
		// Otherwise use the SQL Database
		System.out.println("BP PROP : " + application.toUpperCase() +"_" + testCase.toUpperCase() + "_SQL");
		if (getDBProp(application.toUpperCase() +"_" + testCase.toUpperCase() + "_SQL") == null) {
			return getExcelTestData(application, testCase);

		} else {
			return getSQLTestData(application, testCase);
		}
	}

	private static Object[][] getSQLTestDataOld(String application, String testCase) {

		// Connect to the SQL Database if we are not connected yet
		if (dbConn == null) {
			if (!dbConnect()) {
				logger.fatal("DB Connection Failure : " + getDBProp("DBCONN_URL") + " Exiting Test .........");
				System.exit(10);
			}
		}

		/*
		 * At this moment: login and Order search can use data from the database
		 * Other test scenarios will continue to use the Excel file Each of
		 * those modules returns a Object[][]
		 */
		if (testCase.equalsIgnoreCase("login")) {
			return getLoginSQLTestData(application, testCase);
		}

		if (testCase.equalsIgnoreCase("orderSearch")) {
			return getOrderSearchSQLTestData(application, testCase);
		}

		// Get the data from Excel is the default action
		return getExcelTestData(application, testCase);

	}

	private static Object[][] getSQLTestData(String application, String testCase) {

		try {
			// Connect to the SQL Database if we are not connected yet
			if (dbConn == null) {
				if (!dbConnect()) {
					logger.fatal("DB Connection Failure : " + getDBProp("DBCONN_URL") + " Exiting Test .........");
					System.exit(10);
				}
			}

			Statement st = dbConn.createStatement();
			// get the sql statement text
			String stString = UtilKit.getDBProp(application.toUpperCase() + "_" + testCase.toUpperCase() + "_SQL");
			logger.info("Executing SQL :" + stString);

			// Parse the columns names from the SQL statement
			String columnNames[] = UtilKit.parseColumns(stString);
			logger.info("Column Names :" + columnNames);

			ResultSet rs = st.executeQuery(stString);
			ResultSetMetaData rsmd = rs.getMetaData();
			// int rowCount = rs.getFetchSize();

			// To calculate the row count you will have to access the last row
			// and
			// get its row number
			rs.last();
			int rowCount = rs.getRow();
			rs.beforeFirst(); // back to the begining

			int columnsCount = rsmd.getColumnCount();
			logger.info("SQL Statement Excecuted :" + " Rows :" + rowCount + "  columns :" + columnsCount);

			Object[][] testDataArray = new Object[rowCount][columnsCount]; // To
																			// be
																			// returned
			// For each Row load every column. based on the column names Array
			for (int i = 0; i < rowCount; i++) {
				rs.next();
				for (int j = 0; j < columnNames.length; j++) {
					logger.info("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " DB Loading : "
							+ columnNames[j] + " : " + rs.getString(columnNames[j]));
					testDataArray[i][j] = rs.getString(columnNames[j]);
				}
			}
			return testDataArray;

		} catch (SQLException e) {
			logger.fatal("Sql Exception generated ");
			e.printStackTrace();
		}

		// Get the data from Excel is the default action
		return getExcelTestData(application, testCase);

	}

	// Gets order search data from the database
	private static Object[][] getOrderSearchSQLTestData(String application, String testCase) {
		try {
			Statement st = dbConn.createStatement();

			// The sql statement is stored as a DB Property
			String stString = UtilKit.getDBProp(application.toUpperCase() + "_" + testCase.toUpperCase() + "_SQL");
			logger.info("Executing SQL :" + stString);

			// Execute Query, get the ResultSet and the Metadata
			ResultSet rs = st.executeQuery(stString); // ResultSet stores the
														// result of the query
			ResultSetMetaData rsmd = rs.getMetaData(); // The results Metadata
														// is part of the
														// results

			// int rowCount = rs.getFetchSize(); //Not needed in the case

			// To calculate the row count you will have to access the last row
			// and get its row number

			rs.last();
			int rowCount = rs.getRow();

			// Point back to the begining
			rs.beforeFirst();

			// Get the columns count from the Metadata
			int columnsCount = rsmd.getColumnCount();

			logger.info("SQL Statement Excecuted :" + " Rows :" + rowCount + "  columns :" + columnsCount);

			Object[][] testDataArray = new Object[rowCount][columnsCount]; // Instantiate
																			// the
																			// returning
																			// Object
																			// Data
																			// Array

			for (int i = 0; i < rowCount; i++) {
				rs.next();
				logger.info("in : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " DB Loading :"
						+ rs.getString("orderId"));
				testDataArray[i][0] = rs.getString("orderId");
			}
			return testDataArray;

		} catch (SQLException e) {
			logger.fatal("Sql Exception generated ");
			e.printStackTrace();
		}
		return null;
	}
	
	private static String [] parseColumns(String sqlStm){
		
		String mySqlStm = sqlStm.toLowerCase();

		
		String secondPart = mySqlStm.replaceFirst("select", ""); // Replace "Select"
		System.out.println("secondPart :" + secondPart);
		
		String [] rawColumns = secondPart.split("from"); // Cut after "From"
		System.out.println("rawColumns :" + rawColumns[0] + ":");
		
		String plainColumns = rawColumns[0].replace(",", "");
		System.out.println("plainColumns :" + plainColumns +":");
		
		
		String columnsArray[] = plainColumns.split(" "); // Split on space
		String [] columnsNames = new String[columnsArray.length -1]; // The first string is not used, is just space
		
		for (int i = 0; i < columnsArray.length; i++){
			// Discard the first String after the split. it only contains a space.
			if (i == 0) continue;
			columnsNames[i -1]  =  columnsArray[i];
		}

		for (String myColumn: columnsNames ){
			System.out.println("column :" + myColumn + ":");
		}
		
		return columnsNames;
		
	}

	// Gets login data from the database
	private static Object[][] getLoginSQLTestData(String application, String testCase) {
		try {
			Statement st = dbConn.createStatement();
			String stString = UtilKit.getDBProp(application.toUpperCase() + "_" + testCase.toUpperCase() + "_SQL");
			logger.info("Executing SQL :" + stString);
			ResultSet rs = st.executeQuery(stString);
			ResultSetMetaData rsmd = rs.getMetaData();
			// int rowCount = rs.getFetchSize();

			// To calculate the row count you will have to access the last row
			// and
			// get its row number
			rs.last();
			int rowCount = rs.getRow();
			rs.beforeFirst();
			int columnsCount = rsmd.getColumnCount();
			logger.info("SQL Statement Excecuted :" + " Rows :" + rowCount + "  columns :" + columnsCount);
			Object[][] testDataArray = new Object[rowCount][columnsCount];
			for (int i = 0; i < rowCount; i++) {
				rs.next();
				logger.info("in : " + Thread.currentThread().getStackTrace()[1].getMethodName() + " DB Loading : "
						+ rs.getString("userName") + "  " + rs.getString("passwd"));
				testDataArray[i][0] = rs.getString("userName");
				testDataArray[i][1] = rs.getString("passwd");
			}
			return testDataArray;

		} catch (SQLException e) {
			logger.fatal("Sql Exception generated ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This Module makes the necessary Apache POI calls needed to get the test
	 * data from the Excel file into the Object[][] being returnrd to the Testng
	 * Data provider calls include XSSFWorkbook(), XSSFSheet(), XSSFRow() etc
	 */
	public static Object[][] getExcelTestData(String application, String testCase) {

		/*
		 * This is here because this module is called before the init() module
		 * for test cases that specify a data Provider If thats the case, we
		 * need to initialize Logger here before we can use it
		 */
		if (logger == null) {
			logger = Logger.getLogger(application + "." + testCase);
			PropertyConfigurator.configure(projectFolder + projectName + resourcesFolder + "/log4j.properties");
		}

		// XSSFWorkbook requires an InputStream as a parameter
		FileInputStream testF = null;
		try {
			testF = new FileInputStream(projectFolder + projectName + resourcesFolder + "/MasterTestData.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(testF); // Create the Workbook
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XSSFSheet sheet = wb.getSheet(application); // Get the Sheet in the
													// Workbook
		logger.info("sheet rows count :" + sheet.getLastRowNum());

		// Instantiate the list of XSSF rows
		ArrayList<XSSFRow> caseList = new ArrayList<XSSFRow>();

		caseList = UtilKit.findRows(sheet, testCase); // finds the list of rows
														// matching
		// the given test case String

		XSSFRow titlesRow = caseList.get(0); // The first row is the column
												// title, It will be ignored.

		// Allocate memory for the Data array. The -1 us used in both dimensions
		// since the column title row itself is not loaded.
		Object[][] testDataArray = new Object[caseList.size() - 1][(titlesRow.getLastCellNum()) - 1];
		logger.info(
				"testDataArray Allocation [" + (caseList.size() - 1) + "][" + (titlesRow.getLastCellNum() - 1) + "]");

		// This loop loads each cell into the test Data array
		for (int i = 0; i < (caseList.size() - 1); i++) {
			XSSFRow currRow = caseList.get(i + 1);
			for (int j = 0; j < (currRow.getLastCellNum()) - 1; j++) {
				logger.info("Loading " + currRow.getCell(j + 1).toString());
				testDataArray[i][j] = currRow.getCell(j + 1).toString();
			}

		}

		try {
			wb.close(); // Close the workbook
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger = null;
		return testDataArray;

	}

	public static ArrayList<XSSFRow> findRows(XSSFSheet sheet, String findStr) {

		ArrayList<XSSFRow> rowList = new ArrayList<XSSFRow>();// Will store the
																// list of rows
																// in the given
																// sheet

		/*
		 * TODO - Make the iterator work latter
		 * 
		 * Iterator<Row> rowIterator = sheet.iterator();
		 * while(rowIterator.hasNext()){ XSSFRow currRow = (XSSFRow)
		 * rowIterator.next(); uLog('D',"Checking Row : " + currRow.toString());
		 * if(currRow.getCell(1).getStringCellValue().equalsIgnoreCase(findStr))
		 * {
		 * 
		 * uLog('I', "Loading Row : " + currRow.toString());
		 * rowList.add(currRow); } }
		 */

		/*
		 * Loop thru every row in the sheet and store in the list the ones that
		 * match the findStr
		 */
		for (int i = 0; i < (sheet.getLastRowNum() + 1); i++) {
			XSSFRow currRow = sheet.getRow(i);
			if (currRow != null) {
				logger.info("Checking Cell : " + currRow.getCell(0).getStringCellValue());
				// Look at the first cell/column in each row, if it matches
				// findStr
				// then add it to the row list
				if (currRow.getCell(0).getStringCellValue().equalsIgnoreCase(findStr)) {
					logger.info("Loading " + findStr + " Row : ");
					rowList.add(currRow);
				}
			}

		}

		return rowList;
	}

	// Scroll Down to the end of the page
	public static void scrollDown(WebDriver driver) {
		Actions myActions = new Actions(driver);
		myActions.sendKeys(Keys.PAGE_DOWN);
		myActions.sendKeys(Keys.END);
		myActions.build().perform();
	}

	public static void suspendAction(long milliseconds){
		
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void scrollDownUnit(HtmlUnitDriver driver) {
		Actions myActions = new Actions(driver);
		myActions.sendKeys(Keys.PAGE_DOWN);
		myActions.build().perform();
	}

	// Scroll to the location of the given Element
	public static void scrollToElement(WebElement element, WebDriver driver) {
		Point elementCoordinates = element.getLocation();
		// This is done by executing a javaScript
		String scriptContent = new String(
				"window.scrollTo(" + elementCoordinates.getX() + "," + (elementCoordinates.getY() - 40) + ")");
		UtilKit.executeJavascript(driver, scriptContent);

	}
	// Scroll to a give Point 
	public static void scrollToPoint(Point coordinates, WebDriver driver) {
		// This is done by executing a javaScript
		String scriptContent = new String(
				"window.scrollTo(" + coordinates.getX() + "," + (coordinates.getY()) + ")");
		System.out.println("Scrolling to :" + coordinates.getX() + "," + coordinates.getY());
		UtilKit.executeJavascript(driver, scriptContent);

	}

	public static void scrollToElementUnit(WebElement element, HtmlUnitDriver driver) {
		Point elementCoordinates = element.getLocation();
		// This is done by executing a javaScript
		String scriptContent = new String(
				"window.scrollTo(" + elementCoordinates.getX() + "," + (elementCoordinates.getY() - 40) + ")");
		UtilKit.executeJavascript(driver, scriptContent);

	}

	// this will return thr calling method name
	public static String currentMethod() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	// print the stack and log the exception message
	public static String exceptionLogger(Exception e) {

		String excLog = new String();

		e.printStackTrace();

		String msg = (e.getMessage());
		if (!msg.isEmpty())
			excLog = excLog + "\n\n EXCEPTION MESSAGE : \n" + msg + "\n\n";
		logger.error(excLog);

		return excLog;

	}

	public static void uLog(char level, String msg) {

		// Levels (FATAL, ERROR, WARN, INFO, DEBUG)
		switch (level) {
		case 'F':
			logger.fatal(msg);
			break;
		case 'E':
			logger.error(msg);
			break;
		case 'W':
			logger.warn(msg);
			break;
		case 'I':
			logger.info(msg);
			break;
		case 'D':
			logger.info(msg);
			break;
		}
	}

	public static String getDateTime() {
		SimpleDateFormat dateTimeForm = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
		Date myDateTime = Calendar.getInstance().getTime();

		return (dateTimeForm.format(myDateTime));
	}

	public static String getPlainDateTime() {
		SimpleDateFormat dateTimeForm = new SimpleDateFormat("YYYYMMddHHmmss");
		Date myDateTime = Calendar.getInstance().getTime();

		return (dateTimeForm.format(myDateTime));
	}

	public static Object executeJavascript(WebDriver driver, String javaScript) {
		/*
		 * Cast the driveras JavaScriptExecutor to be able to call
		 * executeScript()
		 */
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (js.executeScript(javaScript));
	}

	public static boolean executeBooleanJavascript(WebDriver driver, String javaScript) {
		/*
		 * Cast the driver JavaScriptExecutor to be able to call
		 * executeScript()
		 */
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (Boolean) (js.executeScript(javaScript));
	}

	public static void executeJavascriptOnElement(WebDriver driver, String javaScript, WebElement element) {
		/*
		 * Cast the driver JavaScriptExecutor to be able to call
		 * executeScript()
		 */
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScript, element);
		UtilKit.logger.info("javaScript Excecuted : " + "|" + javaScript + "|");
	}
	
	public static void javaScriptClick(WebDriver driver, WebElement element){
		UtilKit.executeJavascriptOnElement(driver, "arguments[0].click();", element);
		
	}
	public static void javaScriptSendKeys(WebDriver driver, WebElement element, String keysString){
		UtilKit.executeJavascriptOnElement(driver, "arguments[0].setAttribute('value'," + "'" + keysString + "')", element);
		
	}

	// Using the java AWT robot class.
	// It will type in the characters in the given Char [] (one by one)
	// It will only handle 3 special characters (\ : _) since it was coded
	// For typing directory paths
	public static void arrayKeyPress(Robot myRobot, char[] myArray) {
		for (int i = 0; i < myArray.length; i++) {
			if (myArray[i] == '\\') {
				myRobot.keyPress('\\');
				myRobot.keyRelease('\\');
			} else if (myArray[i] == ':') {
				myRobot.keyPress(KeyEvent.VK_SHIFT);
				myRobot.keyPress(KeyEvent.VK_SEMICOLON);
				myRobot.keyRelease(KeyEvent.VK_SEMICOLON);
				myRobot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (myArray[i] == '_') {
				myRobot.keyPress(KeyEvent.VK_SHIFT);
				myRobot.keyPress(KeyEvent.VK_MINUS);
				myRobot.keyRelease(KeyEvent.VK_MINUS);
				myRobot.keyRelease(KeyEvent.VK_SHIFT);
			} else {
				myRobot.keyPress(myArray[i]);
				myRobot.keyRelease(myArray[i]);
			}
		}
	}

	/*
	 * Perform a boolean returning file operation like delete or change the last
	 * modification date on the given filename
	 */
	public static Boolean fileOperation(String fileName, char operation) {

		File myFile = new File(fileName);
		// As a switch to allow for more operations in the future
		switch (operation) {
		case 'D':
			return myFile.delete();
		case 'M':
			return myFile.setLastModified(startTime);
		case 'N':
			return myFile.exists();
		}
		return false;
	}
	
	public static int findFrameIndex(WebDriver driver, List<WebElement> framesList, By elementLocator) {

		boolean inChildFrame = false;
		
		//Try finding it in the current context
		if(driver.findElements(elementLocator).size() > 0){
			UtilKit.logger.info("Element : " + elementLocator.toString() + "Found in Current Context... URL: " + driver.getCurrentUrl());
			return 0;
		}
		for (int i = 0; i < framesList.size(); i++) {
			try {
				if(inChildFrame == true){
					driver.switchTo().parentFrame();
					inChildFrame = false;
				}
				driver.switchTo().frame(framesList.get(i));
				UtilKit.suspendAction(1000);
				inChildFrame = true;
				if(framesList.get(i).findElements(elementLocator).size() > 0){
					UtilKit.logger.info("Element :" + elementLocator.toString() + " found in Frame : " + framesList.get(i).getAttribute("outerHTML"));
					driver.switchTo().parentFrame();
					return (i+1);
				}
				else
					UtilKit.logger.info("Element :" + elementLocator.toString() + " Not found in Frame : " + framesList.get(i).getAttribute("outerHTML"));
					
			} catch (Exception e) {
				UtilKit.logger.info("Ignored Exeption In :" + UtilKit.currentMethod() + " : " + e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
		if(inChildFrame == true)
			driver.switchTo().parentFrame();
		UtilKit.logger.info("Element :" + elementLocator.toString() + " Not found in Current Context or Frame ");
		return -1;
	}
		
		
	public static List<WebElement> findFrames(WebDriver driver) {
		
		List<WebElement> allFramesList = new ArrayList<WebElement>();
		int i = 0;
		int j = 0; //allFramesList index
		try {
			System.out.println("Finding Frames in Page Title : " + driver.getTitle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<WebElement> frameList = driver.findElements(By.tagName("frame"));
		System.out.println("----------------------Frame count : " + frameList.size()
				+ "------------------------------------------------------");
		for (WebElement element : frameList) {
			allFramesList.add(j, element); j++;
			System.out.println("Frame No: " + i + " Frame Title: " + element.getAttribute("title") + "  Frame Name : "
					+ element.getAttribute("name"));
			System.out.println("Frame HTML: " + element.getAttribute("outerHTML"));
			System.out.println("----------------------------------------------------------------------------");
			i++;
		}

		i = 0;
		List<WebElement> iframeList = driver.findElements(By.tagName("iframe"));
		System.out.println("----------------------iFrame count : " + iframeList.size()
				+ "------------------------------------------------------");
		for (WebElement element : iframeList) {
			allFramesList.add(j, element); j++;
			System.out.println("iFrame No: " + i + " iFrame Title: " + element.getAttribute("title")
					+ "  iFrame Name : " + element.getAttribute("name"));
			System.out.println("iFrame HTML: " + element.getAttribute("outerHTML"));
			System.out.println("----------------------------------------------------------------------------");
			i++;
		}

		i = 0;
		List<WebElement> frameSetList = driver.findElements(By.tagName("frameset"));
		System.out.println("----------------------FrameSet count : " + frameSetList.size()
				+ "------------------------------------------------------");
		for (WebElement element : frameSetList) {
			allFramesList.add(j, element); j++;
			System.out.println("FrameSet No: " + i + " FrameSet Title: " + element.getAttribute("title")
					+ "  Frame Set Name : " + element.getAttribute("name"));
			System.out.println("FrameSet HTML: " + element.getAttribute("outerHTML"));
			System.out.println("----------------------------------------------------------------------------");
			i++;
		}
		
		return allFramesList;
	}
}
