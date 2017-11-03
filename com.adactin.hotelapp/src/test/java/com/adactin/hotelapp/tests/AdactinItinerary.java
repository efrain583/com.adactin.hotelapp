package com.adactin.hotelapp.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.adactin.hotelapp.pages.AdactinItineraryPage;
import com.adactin.hotelapp.pages.AdactinLoginPage;
import com.adactin.hotelapp.pages.AdactinSearchPage;

import Util.UtilKit;

public class AdactinItinerary extends AdactinBase {

	@Test(enabled = true, dataProvider = "orderSearchDataProvider")
	public void testOrderSearch(String OrderId) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinItineraryPage AIP = new AdactinItineraryPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			//TODO Fix this After Selenium 3 is fixed
			/*
			 * Clicking on the Itinerary Link is not working in Selenium 3
			 * a direct opening of this link (navigate) is the workaround for now
			 */
			// ASP.bookedIteneraryLink().click(); 
			// get the link href (Entired URL)
			String hrefStr = ASP.bookedIteneraryLink().getAttribute("href");
			System.out.println(" \n\t\t  Booked Itinerary Href : " + hrefStr);
			driver.navigate().to(hrefStr);

			Assert.assertTrue(AIP.verifyItineraryPage());

			AIP.searchOrderIdTextBox().sendKeys(OrderId);
			AIP.setSearchedOrderId(OrderId); // Save the Searched order Id
			AIP.goButton().click();
			Assert.assertTrue(AIP.verifySearchOrder());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

	}

	@Test(enabled = true, dataProvider = "cancelOrderDismissDataProvider")
	public void cancelOrderDismiss(String orderId) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinItineraryPage AIP = new AdactinItineraryPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			//TODO Fix this After Selenium 3 is fixed
			/*
			 * Clicking on the Itinerary Link is not working in Selenium 3
			 * a direct opening of this link (navigate) is the workaround for now
			 */
			// ASP.bookedIteneraryLink().click(); 
			// get the link href (Entired URL)
			String hrefStr = ASP.bookedIteneraryLink().getAttribute("href");
			System.out.println(" \n\t\t  Booked Itinerary Href : " + hrefStr);
			driver.navigate().to(hrefStr);

			Assert.assertTrue(AIP.verifyItineraryPage());

			AIP.basicOrderSearch(orderId);
			Assert.assertTrue(AIP.verifySearchOrder());
			AIP.checkAll_CheckBox().click();
			AIP.cancelSelectedButtom().click();

			AIP.orderCancelationAlert().dismiss(); // Dismiss the Cancellation
													// of the
			// order on Pop-up

			// Verify the order was not cancelled by searching for it again
			AIP.basicOrderSearch(orderId);
			Assert.assertTrue(AIP.verifySearchOrder());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true, dataProvider = "cancelOrderAcceptDataProvider")
	public void cancelOrderAccept(String orderId) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinItineraryPage AIP = new AdactinItineraryPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			//TODO Fix this After Selenium 3 is fixed
			/*
			 * Clicking on the Itinerary Link is not working in Selenium 3
			 * a direct opening of this link (navigate) is the workaround for now
			 */
			// ASP.bookedIteneraryLink().click(); 
			// get the link href (Entired URL)
			String hrefStr = ASP.bookedIteneraryLink().getAttribute("href");
			System.out.println(" \n\t\t  Booked Itinerary Href : " + hrefStr);
			driver.navigate().to(hrefStr);

			Assert.assertTrue(AIP.verifyItineraryPage());

			AIP.basicOrderSearch(orderId);
			Assert.assertTrue(AIP.verifySearchOrder(), "Order was Not Verified");
			AIP.checkAll_CheckBox().click();
			AIP.cancelSelectedButtom().click();

			AIP.orderCancelationAlert().accept(); // Accept/Confirm the
													// Cancellation of
			// the order on Pop-up

			// Verify the order was not cancelled by searching for it again
			AIP.basicOrderSearch(orderId);
			Assert.assertFalse(AIP.verifySearchOrder()); // It should not find
															// the order, Thats
															// why we are
															// asserting false
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@DataProvider
	public Object[][] cancelOrderDismissDataProvider() {
		return UtilKit.getTestData("com.adactin.hotelapp", "adactin", "DismissCancel");
	}

	@DataProvider
	public Object[][] cancelOrderAcceptDataProvider() {
		return UtilKit.getTestData("com.adactin.hotelapp", "adactin", "AcceptCancel");
	}

	@DataProvider
	public Object[][] orderSearchDataProvider() {
		return UtilKit.getTestData("com.adactin.hotelapp", "adactin", "OrderSearch");
	}
}
