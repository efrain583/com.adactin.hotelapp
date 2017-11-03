package com.adactin.hotelapp.pages;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Util.UtilKit;

public class AdactinItineraryPage {

	WebDriver driver;
	String searchedOrderId;

	public String getSearchedOrderId() {
		return searchedOrderId;
	}

	public void setSearchedOrderId(String searchedOrderId) {
		this.searchedOrderId = searchedOrderId;
	}

	public AdactinItineraryPage(WebDriver driver) {
		this.driver = driver;
	}

	// Initialize Locators AKA DOM Query
	By itineraryInfoTableL = UtilKit.UIMap("ITINERARYINFO_TABLE");
	By searchOrderIdTextBoxL = UtilKit.UIMap("SEARCHORDERID_TEXTBOX");
	By goButtonL = UtilKit.UIMap("GO_BUTTON");
	By checkAllCheckBoxL = UtilKit.UIMap("CHECKALL_CHECKBOX");
	By cancelSelectedButtomL = UtilKit.UIMap("CANCELSELECTED_BUTTOM");

	public WebElement itineraryInfoTable() {

		return driver.findElement(itineraryInfoTableL);
	}

	public WebElement searchOrderIdTextBox() {

		return driver.findElement(searchOrderIdTextBoxL);
	}

	public WebElement goButton() {

		return driver.findElement(goButtonL);
	}

	public WebElement checkAll_CheckBox() {
		return driver.findElement(checkAllCheckBoxL);
	}

	public WebElement cancelSelectedButtom() {
		return driver.findElement(cancelSelectedButtomL);
	}

	public Alert orderCancelationAlert() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver.switchTo().alert();
	}

	public boolean verifyItineraryPage() {

		/*
		 * Wait for the page title is necessary because of a bug in Selenium 3 ... Implicit wait is not working for the page title
		 * Delete the wait call once the bug is fixed
		 */

		UtilKit.waitForPageTitle(driver, 5, "AdactIn.com - Select Hotel");
		if (driver.getTitle().equals("AdactIn.com - Select Hotel")) {
			return true;
		}
		UtilKit.logger.info("In : " + Thread.currentThread().getStackTrace()[1].getMethodName() + "Expected : "
				+ "AdactIn.com - Select Hotel" + "Page Title : " + driver.getTitle());
		return false;

	}

	public boolean verifyItineraryPageInNewWindow() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String mainHandle = driver.getWindowHandle(); // Original Window - keep
														// the main handle to
														// switch back

		Set<String> windowHandles = driver.getWindowHandles();
		Object[] handleArr = windowHandles.toArray(); // Convert the Set to an
														// Object Array
		for (int i = 0; i < windowHandles.size(); i++) {
			driver.switchTo().window(handleArr[i].toString()); // Switch to the
																// given Window
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (verifyItineraryPage()) {
				driver.switchTo().window(mainHandle); // Switch back to the
														// original window
				return true;
			}
		}
		driver.switchTo().window(mainHandle); // Switch back to the original
												// window
		return false;
	}

	public boolean verifyItineraryPageInNewTab() {

		if (verifyItineraryPage()) {
			/*
			 * Toggle on the browser tabs once to point the focus to the
			 * original tab This id done by sending by sending CONTROL-Tab key
			 */
			WebElement alternateBody = driver.findElement(By.cssSelector("body"));
			alternateBody.sendKeys(Keys.CONTROL + "\t");
			return true;
		}

		return false;
	}

	public void basicOrderSearch(String orderId) {

		// Capture and save the orderId being searched
		searchedOrderId = orderId;
		UtilKit.logger.info("Searching Order : " + searchedOrderId);

		searchOrderIdTextBox().sendKeys(orderId);
		goButton().click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean verifySearchOrder() {
		
		System.out.println("In :" + Thread.currentThread().getStackTrace()[1] + " Title : " + driver.getTitle());
		//TODO Selenium 3 fix - Get rid of it After Selenium 3 is fixed
		/*
		 * Wait  for 5 secs is the workaround:  Otherwise you get StaleElementReferenceException
		 * 
		 * */
			UtilKit.suspendAction(5000);

		List<WebElement> inputElementList = itineraryInfoTable().findElements(By.tagName("input"));
		for (WebElement currElement : inputElementList) {
			if (currElement.getAttribute("value").equals(searchedOrderId)) {
				return true;
			}
		}
		UtilKit.logger.info("Order searched :" + searchedOrderId + "  was not found : ");
		return false;

	}
}
