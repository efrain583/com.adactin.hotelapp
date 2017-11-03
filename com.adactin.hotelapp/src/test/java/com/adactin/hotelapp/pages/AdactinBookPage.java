package com.adactin.hotelapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.adactin.hotelapp.util.OrderNoException;
import Util.UtilKit;

public class AdactinBookPage {

	WebDriver driver;

	public AdactinBookPage(WebDriver driver) {
		this.driver = driver;
	}

	// Initialize Locators AKA DOM Query
	By hotelNameTextBoxL = UtilKit.UIMap("HOTELNAME_TEXTBOX");
	By locationTextBoxL = UtilKit.UIMap("LOCATION_TEXTBOX");
	By roomTypeTextBoxL = UtilKit.UIMap("ROOMTYPE_TEXTBOX");
	By roomNumTextBoxL = UtilKit.UIMap("ROOMNUM_TEXTBOX");
	By totalDaysTextBoxL = UtilKit.UIMap("TOTALDAYS_TEXTBOX");
	By pricePerNightTextBoxL = UtilKit.UIMap("PRICEPERNIGHT_TEXTBOX");
	By totalPriceTextBoxL = UtilKit.UIMap("TOTALPRICE_TEXTBOX");
	By gstTextBoxL = UtilKit.UIMap("GST_TEXTBOX");
	By finalPriceTextBoxL = UtilKit.UIMap("FINALPRICE_TEXTBOX");

	By firstNameTextBoxL = UtilKit.UIMap("FIRSTNAME_TEXTBOX");
	By lastNameTextBoxL = UtilKit.UIMap("LASTNAME_TEXTBOX");
	By addressTextBoxL = UtilKit.UIMap("ADDRESS_TEXTBOX");
	By CC_NumTextBoxL = UtilKit.UIMap("CC_NUM_TEXTBOX");
	By CC_TypeSelectL = UtilKit.UIMap("CC_TYPE_SELECT");
	By CC_Exp_MonthSelectL = UtilKit.UIMap("CC_EXP_MONTH_SELECT");
	By CC_Exp_YearSelectL = UtilKit.UIMap("CC_EXP_YEAR_SELECT");
	By CC_CvvTextBoxL = UtilKit.UIMap("CC_CVV_TEXTBOX");
	By bookNowButtomL = UtilKit.UIMap("BOOKNOW_BUTTOM");
	By orderNoTextBoxL = UtilKit.UIMap("ORDER_NO_TEXTBOX");

	public WebElement hotelNameTextBox() {
		return driver.findElement(hotelNameTextBoxL);
	}

	public WebElement locationTextBox() {
		return driver.findElement(locationTextBoxL);
	}

	public WebElement roomTypeTextBox() {
		if (UtilKit.waitForElement(roomTypeTextBoxL, driver, "Exists", 5)) {
			return driver.findElement(roomTypeTextBoxL);
		}
		// Insert exception here
		return driver.findElement(roomTypeTextBoxL);
	}

	public WebElement roomNumTextBox() {
		return driver.findElement(roomNumTextBoxL);
	}

	public WebElement totalDaysTextBox() {
		return driver.findElement(totalDaysTextBoxL);
	}

	public WebElement pricePerNightTextBox() {
		return driver.findElement(pricePerNightTextBoxL);
	}

	public WebElement totalPriceTextbox() {
		return driver.findElement(totalPriceTextBoxL);
	}

	public WebElement gstTextBox() {
		return driver.findElement(gstTextBoxL);
	}

	public WebElement finalPriceTextBox() {
		return driver.findElement(finalPriceTextBoxL);
	}

	public WebElement firstNameTextBox() {
		return driver.findElement(firstNameTextBoxL);
	}

	public WebElement lastNameTextBox() {
		return driver.findElement(lastNameTextBoxL);
	}

	public WebElement addressTextBox() {
		return driver.findElement(addressTextBoxL);
	}

	public WebElement CC_NumTextBox() {
		return driver.findElement(CC_NumTextBoxL);
	}

	public void CC_TypeSelect(String CCType) {
		WebElement myElement = driver.findElement(CC_TypeSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(CCType);
	}

	public void CC_Exp_MonthSelect(String expMonth) {
		WebElement myElement = driver.findElement(CC_Exp_MonthSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(expMonth);
	}

	public void CC_Exp_YearSelect(String expYear) {
		WebElement myElement = driver.findElement(CC_Exp_YearSelectL);
		Select mySelect = new Select(myElement);
		mySelect.selectByVisibleText(expYear);
	}

	public WebElement CC_CvvTextBox() {
		return driver.findElement(CC_CvvTextBoxL);
	}

	public WebElement bookNowButtom() {
		return driver.findElement(bookNowButtomL);
	}

	public WebElement orderNoTextBox() {
		return this.driver.findElement(orderNoTextBoxL);
	}

	public WebElement orderNoTextBox(int waitPeriod) throws OrderNoException {

		if (UtilKit.waitForElement(orderNoTextBoxL, this.driver, "Exists", waitPeriod))
			return this.driver.findElement(orderNoTextBoxL);
		else
			throw new OrderNoException(); // throw exception if the order number
											// is not generated quick enough
	}

	public boolean verifyOrderNo() throws OrderNoException {

		if (UtilKit.waitForElement(orderNoTextBoxL, this.driver, "Displayed", 2)) {
			if (orderNoTextBox().isDisplayed()) {
				UtilKit.logger.info("Order Number Verified !!!  Value : " + orderNoTextBox().getAttribute("value"));
				return true;
			} else {
				UtilKit.logger.info(
						Thread.currentThread().getStackTrace()[1].getMethodName() + " : FAILED TO GENERATE ORDER NO");
				return false;
			}
		} else
			throw new OrderNoException(); // throw exception if the order number
											// is not generated quick enough
	}
}
