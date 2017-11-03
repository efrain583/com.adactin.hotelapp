package com.adactin.hotelapp.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.adactin.hotelapp.pages.AdactinLoginPage;
import com.adactin.hotelapp.pages.AdactinSearchPage;
import com.adactin.hotelapp.pages.AdactinSelectPage;
import Util.UtilKit;

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
