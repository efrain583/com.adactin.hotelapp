package com.adactin.hotelapp.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import Util.UtilKit;

public class AdactinLoginPage {

	WebDriver driver;

	public AdactinLoginPage(WebDriver driver) {
		this.driver = driver;
	}

	// Initialize Locators AKA DOM Query
	// Define Locators for all Elements being tested
	By passwordTextBoxL = UtilKit.UIMap("PASSWORD_TEXTBOX");
	By goToBuild2LinkL = UtilKit.UIMap("GOTOBUILD2_LINK");
	By userNameTextBoxL = UtilKit.UIMap("USERNAME_TEXTBOX");
	By loginButtomL = UtilKit.UIMap("LOGIN_BUTTOM");

	public WebElement goToBuild2Link() {
		return driver.findElement(goToBuild2LinkL);
	}

	public WebElement userNameTextBox() {
		return driver.findElement(userNameTextBoxL);
	}

	public WebElement passwordTextBox() {
		return driver.findElement(passwordTextBoxL);
	}

	public WebElement loginButtom() {
		return driver.findElement(loginButtomL);
	}

	public boolean verifyLogin() {

		//UtilKit.waitForPageToLoad(driver, 3);
		System.out.println("Adactin Search Page Title : " + driver.getTitle());
		if (driver.getTitle().equals("AdactIn.com - Search Hotel"))
			return true;
		return false;
	}

	public boolean defaultLogin() {
		driver.findElement(goToBuild2LinkL).click();
		//UtilKit.waitForPageToLoad(driver, 3);
		driver.findElement(userNameTextBoxL).sendKeys("efrain583");
		driver.findElement(passwordTextBoxL).sendKeys("efra12");
		driver.findElement(loginButtomL).click();

		return verifyLogin();
	}

}