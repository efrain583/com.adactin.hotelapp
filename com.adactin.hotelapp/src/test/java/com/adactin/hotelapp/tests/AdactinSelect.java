package com.adactin.hotelapp.tests;

import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.adactin.hotelapp.pages.AdactinLoginPage;
import com.adactin.hotelapp.pages.AdactinSearchPage;
import com.adactin.hotelapp.pages.AdactinSelectPage;
import com.adactin.hotelapp.util.UtilKit;

public class AdactinSelect extends AdactinBase {

	@Test()
	private void averagePriceSelect() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinSelectPage ASeP = new AdactinSelectPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASP.defaultSearch();
			Assert.assertTrue(ASP.verifySearch());

			ASeP.averagePrice().click();
			ASeP.continueButton().click();

			Assert.assertTrue(ASeP.verifySelect());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

	}

	@Test(enabled = true)

	private void highestPriceSelect() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinSelectPage ASeP = new AdactinSelectPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASP.defaultSearch();
			Assert.assertTrue(ASP.verifySearch());

			ASeP.highestPrice().click();

			// Assert.assertTrue(ASeP.verifySelect());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

	}

}
