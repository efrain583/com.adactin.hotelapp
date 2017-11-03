package com.adactin.hotelapp.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Util.UtilKit;

public class AdactinSelectPage {

	WebDriver driver;
	int subscript = 0; // Holds the value of the subscript containing the price
						// closest to the average

	private class Selection {
		String hotelName;
		String location;
		String nosRooms;
		String pricePerNight;
	}

	private static Selection selectedHotelInfo = null;

	public AdactinSelectPage(WebDriver driver) {
		this.driver = driver;
	}

	// Initialize Locators AKA DOM Query
	By searchResultsTableL = UtilKit.UIMap("SEARCHRESULTS_TABLE");
	By continueButtonL = UtilKit.UIMap("CONTINUE_BUTTON");

	public WebElement continueButton() {
		return driver.findElement(continueButtonL);
	}

	// returns the highest Price corresponding radioButton
	public WebElement highestPrice() {

		WebElement radioButtonElement = null; // To be returned
		int highestPrice = 0;

		WebElement resultsTable = driver.findElement(searchResultsTableL);
		List<WebElement> inputList = resultsTable.findElements(By.tagName("input")); // List
																						// of
																						// all
																						// elements
																						// with
																						// Tagname
																						// =
																						// "input"
																						// in
																						// the
																						// table
		int[] priceArr = new int[4];

		populatePriceArray(inputList, priceArr);

		// Find the highest price
		subscript = 0; // Holds the value of the subscript containing the
						// highest price

		for (int i = 0; i < priceArr.length; i++) {
			int absValue = Math.abs(priceArr[i]); // Absolute value
			if (absValue > highestPrice) {
				highestPrice = absValue;
				subscript = (i + 1);
			}
		}

		// Get the Selected Hotel info.
		selectedHotelInfo = getSelectionInfo(inputList, subscript);

		// Find the Radio button Element Matching the derived subscript
		radioButtonElement = getSelectedRadioButtom(radioButtonElement, inputList);
		return radioButtonElement;
	}

	// Calculate the average price and return the corresponding radioButton
	public WebElement averagePrice() {

		WebElement radioButtonElement = null; // To be returned
		int smallestDiff; // Initialize it to the highest possible value

		WebElement resultsTable = driver.findElement(searchResultsTableL);
		List<WebElement> inputList = resultsTable.findElements(By.tagName("input")); // List
																						// of
																						// all
																						// elements
																						// with
																						// Tagname
																						// =
																						// "input"
																						// in
																						// the
																						// table
		int[] priceArr = new int[4];

		populatePriceArray(inputList, priceArr);

		// Calculate the average
		int sum = 0;
		for (int i = 0; i < priceArr.length; i++) {

			sum = sum + (priceArr[i]);
		}
		int ave = sum / priceArr.length;
		UtilKit.logger.info("SUM :" + sum + " Ave :" + ave);

		// Find the closest price to the average(the one with the smallest
		// difference
		smallestDiff = sum; // Initialize it to the highest possible value
		subscript = 0; // Holds the value of the subscript containing the price
						// closest to the average
		for (int i = 0; i < priceArr.length; i++) {
			int absValue = Math.abs(ave - priceArr[i]);
			if (absValue < smallestDiff) {
				smallestDiff = absValue;
				subscript = (i + 1);
			}
		}

		// Get the Selected Hotel info.
		selectedHotelInfo = getSelectionInfo(inputList, subscript);

		// Find the Radio button Element Matching the derived subscript
		radioButtonElement = getSelectedRadioButtom(radioButtonElement, inputList);
		return radioButtonElement;
	}

	private void populatePriceArray(List<WebElement> inputList, int[] priceArr) {
		// List is an interface and cannot be instantiated. But ArrayList
		// implements List
		ArrayList<WebElement> priceList = new ArrayList<WebElement>();// contains
																		// all
																		// the
																		// Total
																		// Price
																		// Elements
		for (int i = 0; i < inputList.size(); i++) {
			if (inputList.get(i).getAttribute("id").contains("total_price_")) {
				priceList.add(inputList.get(i));
			}
		}

		for (int i = 0; i < priceList.size(); i++) {
			UtilKit.logger.info("Price String " + i + ": " + priceList.get(i).getAttribute("value").substring(6, 9));
			priceArr[i] = Integer.parseInt(priceList.get(i).getAttribute("value").substring(6, 9));
		}
	}

	// Returns the corresponding RadioButton
	private WebElement getSelectedRadioButtom(WebElement radioButtonElement, List<WebElement> inputList) {
		for (int i = 0; i < inputList.size(); i++) {
			WebElement tmpElement = inputList.get(i);
			if (tmpElement.getAttribute("id").equals(("radiobutton_" + subscript))) {
				radioButtonElement = tmpElement;
				break;
			}
		}
		return radioButtonElement;
	}

	private Selection getSelectionInfo(List<WebElement> inputList, int subscript) {

		Selection Lselect = new Selection();

		for (int i = 0; i < inputList.size(); i++) {
			if (inputList.get(i).getAttribute("id").equals("hotel_name_" + subscript)) {
				Lselect.hotelName = inputList.get(i).getAttribute("value");
			}
			if (inputList.get(i).getAttribute("id").equals("location_" + subscript)) {
				Lselect.location = inputList.get(i).getAttribute("value");
			}
			if (inputList.get(i).getAttribute("id").equals("rooms_" + subscript)) {
				Lselect.nosRooms = inputList.get(i).getAttribute("value");
			}
			if (inputList.get(i).getAttribute("id").equals("price_night_" + subscript)) {
				Lselect.pricePerNight = inputList.get(i).getAttribute("value");
			}
		}
		UtilKit.logger.info("Selected Info :" + Lselect.hotelName + ":" + Lselect.location + ":" + Lselect.nosRooms
				+ ":" + Lselect.pricePerNight);

		return Lselect;
	}

	// Inspect the search result table for completeness
	public boolean verifySearchResultsTable() {

		boolean returnVal = true;

		WebElement resultsTable = driver.findElement(searchResultsTableL);
		List<WebElement> rowsList = resultsTable.findElements(By.tagName("tr")); // tr
																					// (rows)

		UtilKit.logger.info("row List size :" + rowsList.size());
		if (rowsList.size() < 1) {
			UtilKit.logger.warn("Search results are incomplete, No. of rows :" + rowsList.size());
			return false;
		}
		for (int i = 0; i < rowsList.size(); i++) {

			List<WebElement> colList = rowsList.get(i).findElements(By.tagName("td")); // td
																						// (columns)
			String colsValue = new String();
			for (int j = 0; j < colList.size(); j++) {
				if (i == 0) {
					colsValue = colsValue.concat(colList.get(j).getText().toString());
					colsValue = colsValue.concat("|");
				} else {
					colsValue = colsValue.concat(colList.get(j).findElement(By.tagName("input")).getAttribute("value"));
					colsValue = colsValue.concat("|");
				}
			}
			UtilKit.logger.info("Row " + i + " Value : |" + colsValue);
			if (colList.size() != 10) {
				UtilKit.logger
						.warn("Search results are incomplete, Row :" + i + "Contains " + colList.size() + "Columns");
				returnVal = false;
			}
		}

		return returnVal;
	}

	// Verify the select page is displayed
	public boolean verifySelect() {
		/*
		 * Wait for the page title is necessary because of a bug in Selenium 3 ... Implicit wait is not working for the page title
		 * Delete the wait call once the bug is fixed
		 */
		UtilKit.waitForPageTitle(driver, 2, "AdactIn.com - Book A Hotel");
		if (!driver.getTitle().equals("AdactIn.com - Book A Hotel")) {
			UtilKit.logger.error("Verification Failed, Unexpected Page Title :" + driver.getTitle() + " Expected : AdactIn.com - Book A Hotel");
			return false;
		}

		AdactinBookPage ABP = new AdactinBookPage(this.driver);

		// Ensure Hotel info is on display mode only and can't be modified
		if (ABP.hotelNameTextBox().isEnabled() || ABP.locationTextBox().isEnabled() ||
		// ABP.roomTypeTextBox().isEnabled() ||
				ABP.roomNumTextBox().isEnabled() || ABP.totalDaysTextBox().isEnabled()
				|| ABP.pricePerNightTextBox().isEnabled() || ABP.totalPriceTextbox().isEnabled()
				|| ABP.gstTextBox().isEnabled() || ABP.finalPriceTextBox().isEnabled()) {

			UtilKit.logger.info("In " + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ " : Display only Mode TextBox Enabled");
			return false;
		}

		UtilKit.uLog('D', ABP.hotelNameTextBox().getAttribute("value") + ":" + selectedHotelInfo.hotelName);
		UtilKit.uLog('D', ABP.locationTextBox().getAttribute("value") + ":" + selectedHotelInfo.location);
		UtilKit.uLog('D', ABP.roomNumTextBox().getAttribute("value") + ":" + selectedHotelInfo.nosRooms);
		UtilKit.uLog('D', ABP.pricePerNightTextBox().getAttribute("value") + ":" + selectedHotelInfo.pricePerNight);

		// Ensure the same selected hotel info is displayed in the booking page
		if ((!ABP.hotelNameTextBox().getAttribute("value").equals(selectedHotelInfo.hotelName))
				|| (!ABP.locationTextBox().getAttribute("value").equals(selectedHotelInfo.location))
				|| (!ABP.pricePerNightTextBox().getAttribute("value").equals(selectedHotelInfo.pricePerNight))) {

			UtilKit.logger.error("Verification Failed, Unexpected Page content :" + driver.getTitle());
			return false;
		}

		return true;

	}
}
