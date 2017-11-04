package com.adactin.hotelapp.tests;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.adactin.hotelapp.pages.AdactinBookPage;
import com.adactin.hotelapp.pages.AdactinLoginPage;
import com.adactin.hotelapp.pages.AdactinSearchPage;
import com.adactin.hotelapp.pages.AdactinSelectPage;
import com.adactin.hotelapp.util.OrderNoException;

import Util.UtilKit;

public class AdactinBook  extends AdactinBase {

	@Test(enabled = true, dataProvider = "bookDataProvider")
	public void basicBooking(String firstName, String lastName, String address, String CCNum, String CCType,
			String CCExpMonth, String CCExpYear, String CCCvv) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinBookPage ABP = new AdactinBookPage(driver);
		AdactinSelectPage ASeP = new AdactinSelectPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASP.defaultSearch();
			Assert.assertTrue(ASP.verifySearch());

			ASeP.averagePrice().click();
			ASeP.continueButton().click();
			Assert.assertTrue(ASeP.verifySelect());

			ABP.firstNameTextBox().sendKeys(firstName);
			ABP.lastNameTextBox().sendKeys(lastName);
			ABP.addressTextBox().sendKeys(address);
			ABP.CC_NumTextBox().sendKeys(CCNum);
			ABP.CC_TypeSelect(CCType);
			ABP.CC_Exp_MonthSelect(CCExpMonth);
			ABP.CC_Exp_YearSelect(CCExpYear);
			ABP.CC_CvvTextBox().sendKeys(CCCvv);
			ABP.bookNowButtom().click();
			Assert.assertTrue(ABP.verifyOrderNo());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	// This case is the opposite of the other cases in this class.
	// This case expects the NoSuchElementException to occur, it will fail if
	// the order is created and verified
	@Test(enabled = false, dataProvider = "bookDataProvider", expectedExceptions = { NoSuchElementException.class })
	public void noOrderBooking(String firstName, String lastName, String address, String CCNum, String CCType,
			String CCExpMonth, String CCExpYear, String CCCvv) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinBookPage ABP = new AdactinBookPage(driver);
		AdactinSelectPage ASeP = new AdactinSelectPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASP.defaultSearch();
			Assert.assertTrue(ASP.verifySearch());

			ASeP.averagePrice().click();
			ASeP.continueButton().click();
			Assert.assertTrue(ASeP.verifySelect());

			ABP.firstNameTextBox().sendKeys(firstName);
			ABP.lastNameTextBox().sendKeys(lastName);
			ABP.addressTextBox().sendKeys(address);
			ABP.CC_NumTextBox().sendKeys(CCNum);
			ABP.CC_TypeSelect(CCType);
			ABP.CC_Exp_MonthSelect(CCExpMonth);
			ABP.CC_Exp_YearSelect(CCExpYear);
			ABP.CC_CvvTextBox().sendKeys(CCCvv);
			ABP.bookNowButtom().click();
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

		/*
		 * Move this outside the above Try/catch block since the Exception is
		 * expected and if it happens the test will be considered successful The
		 * instruction below should generate Exception because there is no wait.
		 * No Order number can be generated that fast.
		 */
		System.out.println("Order Unexpectedly Verified  : " + ABP.orderNoTextBox().getAttribute("value")); // Will
																											// fail
																											// if
																											// this
																											// happens

	}

	@Test(enabled = false, dataProvider = "bookDataProvider")
	// The order No. verification is optional.... it will be OK either way....
	// with Order No.verified or not
	public void optionalBooking(String firstName, String lastName, String address, String CCNum, String CCType,
			String CCExpMonth, String CCExpYear, String CCCvv) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinBookPage ABP = new AdactinBookPage(driver);
		AdactinSelectPage ASeP = new AdactinSelectPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASP.defaultSearch();
			Assert.assertTrue(ASP.verifySearch());

			ASeP.averagePrice().click();
			ASeP.continueButton().click();
			Assert.assertTrue(ASeP.verifySelect());

			ABP.firstNameTextBox().sendKeys(firstName);
			ABP.lastNameTextBox().sendKeys(lastName);
			ABP.addressTextBox().sendKeys(address);
			ABP.CC_NumTextBox().sendKeys(CCNum);
			ABP.CC_TypeSelect(CCType);
			ABP.CC_Exp_MonthSelect(CCExpMonth);
			ABP.CC_Exp_YearSelect(CCExpYear);
			ABP.CC_CvvTextBox().sendKeys(CCCvv);
			ABP.bookNowButtom().click();
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

		/*
		 * Move this outside the above Try/catch block since the
		 * OrderNoException may happen and if it happens the test will still be
		 * considered successful
		 */
		try {
			System.out.println("Order Verified !!! : " + ABP.orderNoTextBox(0).getAttribute("value"));
		} catch (OrderNoException e) {
			System.out.println("Order NOT Verified But still OK ... ");
			UtilKit.exceptionLogger(e);
		}
	}

	@Test(enabled = false, dataProvider = "bookDataProvider")
	public void topPerformanceBooking(String firstName, String lastName, String address, String CCNum, String CCType,
			String CCExpMonth, String CCExpYear, String CCCvv) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinBookPage ABP = new AdactinBookPage(driver);
		AdactinSelectPage ASeP = new AdactinSelectPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASP.defaultSearch();
			Assert.assertTrue(ASP.verifySearch());

			ASeP.averagePrice().click();
			ASeP.continueButton().click();
			Assert.assertTrue(ASeP.verifySelect());

			ABP.firstNameTextBox().sendKeys(firstName);
			ABP.lastNameTextBox().sendKeys(lastName);
			ABP.addressTextBox().sendKeys(address);
			ABP.CC_NumTextBox().sendKeys(CCNum);
			ABP.CC_TypeSelect(CCType);
			ABP.CC_Exp_MonthSelect(CCExpMonth);
			ABP.CC_Exp_YearSelect(CCExpYear);
			ABP.CC_CvvTextBox().sendKeys(CCCvv);
			ABP.bookNowButtom().click();
			// must produce the order no. in1 second, otherwise an exception is
			// thrown
			System.out.println("Order Verified : " + ABP.orderNoTextBox(1).getAttribute("value"));
		} catch (Exception e) {
			System.out.println("Order NOT Verified ... Order No. was not Produced on time ... OMG");
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@DataProvider
	public Object[][] bookDataProvider() {
		Object[][] bookData = UtilKit.getTestData(project, application, "Book");
		return bookData;

	}
}
