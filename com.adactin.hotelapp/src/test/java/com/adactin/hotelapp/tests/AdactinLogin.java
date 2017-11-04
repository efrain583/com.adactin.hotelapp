package com.adactin.hotelapp.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.adactin.hotelapp.pages.AdactinLoginPage;
import Util.UtilKit;

public class AdactinLogin extends AdactinBase {


	@Test(enabled = true, dataProvider = "loginDataProvider")
	public void simpleLoginTest(String userName, String password) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);

		try {
			ALP.goToBuild2Link().click();

			ALP.userNameTextBox().sendKeys(userName);
			ALP.passwordTextBox().sendKeys(password);
			ALP.loginButtom().click();

			Assert.assertEquals(ALP.verifyLogin(), true, "simpleLoginTest FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

	}

	@Test(enabled = true)
	public void deafaultLoginTest() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);

		try {
			Assert.assertEquals(ALP.defaultLogin(), true, "defaultLoginTest FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true)
	public void LogValidateUserAndPassword() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);

		try {
			ALP.goToBuild2Link().click();

			ALP.userNameTextBox().sendKeys("dummy");
			ALP.passwordTextBox().sendKeys("dummy");

			// Execute log_validate script and ensure it completed successfully
			Assert.assertTrue((boolean) UtilKit.executeBooleanJavascript(driver, "return log_validate()"),
					UtilKit.currentMethod() + "FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true)
	public void LogValidateUserOnly() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);

		try {
			ALP.goToBuild2Link().click();

			ALP.userNameTextBox().sendKeys("dummy");
			// ALP.passwordTextBox().sendKeys("dummy");

			// Execute log_validate script and ensure it failed
			Assert.assertFalse((boolean) UtilKit.executeBooleanJavascript(driver, "return log_validate()"),
					UtilKit.currentMethod() + "FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true)
	public void LogValidatePasswordOnly() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);

		try {
			ALP.goToBuild2Link().click();

			// ALP.userNameTextBox().sendKeys("dummy");
			ALP.passwordTextBox().sendKeys("dummy");

			// Execute log_validate script and ensure it failed
			Assert.assertFalse((boolean) UtilKit.executeBooleanJavascript(driver, "return log_validate()"),
					UtilKit.currentMethod() + "FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true)
	public void LogValidateNoUserNoPassword() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);

		try {
			ALP.goToBuild2Link().click();

			// Execute log_validate script and ensure it failed
			Assert.assertFalse((boolean) UtilKit.executeBooleanJavascript(driver, "return log_validate()"),
					UtilKit.currentMethod() + "FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@DataProvider
	public Object[][] loginDataProvider() {
		return UtilKit.getTestData(project, application, "login");
	}
}