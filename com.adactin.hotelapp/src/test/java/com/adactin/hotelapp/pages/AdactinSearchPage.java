package com.adactin.hotelapp.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import Util.UtilKit;

public class AdactinSearchPage {

	WebDriver driver;
	Actions mouseActions;

	Logger logger = Logger.getLogger(getClass());

	String[] welcomeLinksArr = { "SearchHotel.php", "BookedItinerary.php", "ChangePassword.php", "Logout.php" };

	public AdactinSearchPage(WebDriver driver) {
		this.driver = driver;
		this.mouseActions = new Actions(driver);
	}

	// Initialize Locators AKA DOM Query
	By welcomeLinksBlockL = UtilKit.UIMap("WELCOMELINKS_BLOCK");
	By bookedItirenaryLinkL = UtilKit.UIMap("BOOKEDITENARARY_LINK");
	By locationSelectL = UtilKit.UIMap("LOCATION_SELECT");
	By hotelSelectL = UtilKit.UIMap("HOTEL_SELECT");
	By roomTypeSelectL = UtilKit.UIMap("ROOMTYPE_SELECT");
	By roomNosSelectL = UtilKit.UIMap("ROOMNOS_SELECT");
	By dateCheckInTextBoxL = UtilKit.UIMap("DATECHECKIN_TEXTBOX");
	By dateCheckOutTextBoxL = UtilKit.UIMap("DATECHECKOUT_TEXTBOX");
	By adultsPerRoomSelectL = UtilKit.UIMap("ADULTSPERROOM_SELECT");
	By childrenPerRoomSelectL = UtilKit.UIMap("CHILDRENPERROOM_SELECT");
	By searchButtomL = UtilKit.UIMap("SEARCH_BUTTOM");
	By dowloadLinkL = UtilKit.UIMap("DOWLOAD_LINK");

	public WebElement downloadLink() {
		return (driver.findElement(dowloadLinkL));
	}

	// Perform the Robot steps necessary to download the test cases document
	public boolean downloadTestCases() {

		driver.manage().window().maximize(); // This is to make it consistent
												// since the mouse will be moved
												// around

		String fileName = UtilKit.getConfigProp("ADACTIN_TEST_CASES_DIR");

		// Delete the Old test Cases file if there
		UtilKit.fileOperation(fileName + "/Hotel App Test Cases.pdf", 'D');

		try {
			Robot myRobot = new Robot();
			myRobot.keyPress(KeyEvent.VK_TAB); // Shift-Tab
			myRobot.keyPress(KeyEvent.VK_TAB); // Shift-Tab
			myRobot.keyPress(KeyEvent.VK_TAB); // Shift-Tab
			Thread.sleep(3000); // necessary Delay
			myRobot.keyPress(KeyEvent.VK_ENTER); // Shift-Enter
			Thread.sleep(3000); // necessary Delay
			myRobot.mouseMove(70, 50); // This should be the coordinates for
											// the "EXTRACT" Button
											// in the Windows Zip File Manager
											// App
			// MouseInfo is part of the java.awt package 
			// print the mouse location to verify the mouseMove call was successful
			System.out.println("Curret Mouse Location : " + MouseInfo.getPointerInfo().getLocation().toString());

			myRobot.delay(1000);
			myRobot.mousePress(InputEvent.BUTTON1_MASK); // Mouse Left click
															// (press)
			myRobot.delay(2000);
			myRobot.mouseRelease(InputEvent.BUTTON1_MASK); // Release the press
			myRobot.delay(5000);

			String upperStr = fileName.toUpperCase();

			/*
			 * The following 2 lines are not working... to write to the
			 * clipboard I had to write arrayKeyPress() below StringSelection ss
			 * = new StringSelection(upperStr);
			 * Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,
			 * null);
			 */

			char[] myArray = upperStr.toCharArray();

			UtilKit.logger.info("In " + UtilKit.currentMethod() + " File Name : " + new String(myArray)); // Since
																											// myArray.toString()
																											// is
																											// not
																											// working
																											// here

			UtilKit.arrayKeyPress(myRobot, myArray); // will write the char
														// array to the
														// clipboard one chat at
														// the time
			myRobot.delay(1000);
			//myRobot.keyPress(KeyEvent.VK_TAB);
			//myRobot.keyPress(KeyEvent.VK_TAB); //myRobot.delay(500);
			
			myRobot.keyPress(KeyEvent.VK_ENTER);
			
			myRobot.delay(1000);
			// Exit the download window
			myRobot.mouseMove(20, 30); 
			myRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			myRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			myRobot.delay(500);
			myRobot.keyPress(KeyEvent.VK_ALT);
			myRobot.keyPress(KeyEvent.VK_F4);
			myRobot.keyRelease(KeyEvent.VK_ALT);

			myRobot.delay(50);

		} catch (AWTException e) {
			UtilKit.exceptionLogger(e);
			return false;
		} catch (InterruptedException e) {
			UtilKit.exceptionLogger(e);
			return false;
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			UtilKit.exceptionLogger(e);
			return false;
		}
		UtilKit.fileOperation(fileName + "/Hotel App Test Cases.pdf", 'M'); // Mark
																			// the
																			// modification
																			// date
																			// on
																			// the
																			// newly
																			// downloaded
																			// file
		return true;

	}

	public WebElement welcomeLinksBlock() {

		return (driver.findElement(welcomeLinksBlockL));
	}

	public WebElement bookedIteneraryLink() {
		return driver.findElement(bookedItirenaryLinkL);
	}

	public void bookedItineraryLinkMouse(String target) {

		WebElement myElement = driver.findElement(bookedItirenaryLinkL);

		if (target.equalsIgnoreCase("window"))
			mouseActions.keyDown(Keys.SHIFT).click(myElement).perform();
		else
			mouseActions.keyDown(Keys.CONTROL).click(myElement).perform(); // This
																			// is
																			// not
																			// working
																			// properly

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void openItineraryLinkInNewTab(String link) {

		UtilKit.logger.info("LINK : " + link);
		WebElement myElement = driver.findElement(By.cssSelector("body"));
		myElement.sendKeys(Keys.CONTROL + "t");
		driver.get(link);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void locationSelect(String location) {
		WebElement myElement = driver.findElement(locationSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(location);
	}

	public void hotelSelect(String hotel) {
		WebElement myElement = driver.findElement(hotelSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(hotel);
	}

	public void roomTypeSelect(String roomType) {
		WebElement myElement = driver.findElement(roomTypeSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(roomType);
	}

	public void roomNosSelect(String room_nos) {
		WebElement myElement = driver.findElement(roomNosSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(room_nos);
	}

	public WebElement dateChekInTextBox() {
		return driver.findElement(dateCheckInTextBoxL);
	}

	public WebElement dateCheckOutTextBox() {
		return driver.findElement(dateCheckOutTextBoxL);
	}

	public void adultsPerRoomSelect(String adultsPerRoom) {
		WebElement myElement = driver.findElement(adultsPerRoomSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(adultsPerRoom);
	}

	public void childrenPerRoomSelect(String childrenPerRoom) {
		WebElement myElement = driver.findElement(childrenPerRoomSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(childrenPerRoom);
	}

	public WebElement searchButtom() {
		return driver.findElement(searchButtomL);
	}

	public boolean verifySearch() {

		/*
		 * Wait for the page title is necessary because of a bug in Selenium 3 ... Implicit wait is not working for the page title
		 * Delete the wait call once the bug is fixed
		 */
		UtilKit.waitForPageTitle(driver, 2, "AdactIn.com - Select Hotel"); 
		if (!driver.getTitle().equals("AdactIn.com - Select Hotel")) {
			UtilKit.logger.info(
				"in " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Title :" + driver.getTitle() + " Expected : AdactIn.com - Select Hotel ");
			return false;
		}

		// Verify the Welcome Links are provided in the Welcome block
		List<WebElement> welcomeLinks = welcomeLinksBlock().findElements(By.tagName("a"));
		if (welcomeLinks.size() != 4) {
			UtilKit.logger.info("incorrect Number of welcome links :" + welcomeLinks.size());
			return false;
		}

		for (int i = 0; i < welcomeLinks.size(); i++) {
			UtilKit.logger.info("Welcome Link " + i + " : " + welcomeLinks.get(i).getAttribute("href") + " Expected : "
					+ welcomeLinksArr[i]);
			if (!welcomeLinks.get(i).getAttribute("href").endsWith(welcomeLinksArr[i])) {
				return false;
			}
		}

		AdactinSelectPage ASP = new AdactinSelectPage(this.driver);
		return ASP.verifySearchResultsTable();
	}

	public boolean defaultSearch() {

		UtilKit.logger.info("testing the D level");
		locationSelect("New York");
		// hotelSelect("Hotel Creek");
		hotelSelect("Hotel Sunshine");
		roomNosSelect("2 - Two");
		dateChekInTextBox().sendKeys("24/03/2016");
		dateCheckOutTextBox().sendKeys("26/03/2016");
		adultsPerRoomSelect("2 - Two");
		childrenPerRoomSelect("2 - Two");

		searchButtom().click();

		return verifySearch();
	}
}