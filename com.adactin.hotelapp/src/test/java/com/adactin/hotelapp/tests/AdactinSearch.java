package com.adactin.hotelapp.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.adactin.hotelapp.pages.AdactinItineraryPage;
import com.adactin.hotelapp.pages.AdactinLoginPage;
import com.adactin.hotelapp.pages.AdactinSearchPage;
import Util.UtilKit;

public class AdactinSearch extends AdactinBase {

	@Test(enabled = true)
	public void downloadTestCases() {
		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASD = new AdactinSearchPage(driver);

		try {
			ALP.defaultLogin();
			Assert.assertTrue(ALP.verifyLogin());

			ASD.downloadLink().click();
			ASD.downloadTestCases();
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true, dataProvider = "searchDataProvider")
	public void basicSearch(String location, String hotel, String roomNos, String checkInDate, String checkOutDate,
			String adultsPerRoom, String childrenPerRoom) {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);

		try {
			Assert.assertEquals(ALP.defaultLogin(), true, "basicSearch FAILED");

			ASP.locationSelect(location);
			ASP.hotelSelect(hotel);
			ASP.roomNosSelect(roomNos);
			ASP.dateChekInTextBox().sendKeys(checkInDate);
			ASP.dateCheckOutTextBox().sendKeys(checkOutDate);
			ASP.adultsPerRoomSelect(adultsPerRoom);
			ASP.childrenPerRoomSelect(childrenPerRoom);

			ASP.searchButtom().click();

			Assert.assertEquals(ASP.verifySearch(), true, "basicSearch FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true)
	public void defaultSearchtest() {
		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);

		try {
			Assert.assertEquals(ALP.defaultLogin(), true, "defaultSearch FAILED");

			Assert.assertEquals(ASP.defaultSearch(), true, "defaultSearch FAILED");
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}
	}

	@Test(enabled = true)
	public void openItineraryOnNewPage() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinItineraryPage AIP = new AdactinItineraryPage(driver);

		try {
			Assert.assertEquals(ALP.defaultLogin(), true, "defaultSearch FAILED");
			ASP.bookedItineraryLinkMouse("window");
			Assert.assertTrue(AIP.verifyItineraryPageInNewWindow());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

	}

	@Test(enabled = true)
	public void openItineraryOnNewTab() {

		AdactinLoginPage ALP = new AdactinLoginPage(driver);
		AdactinSearchPage ASP = new AdactinSearchPage(driver);
		AdactinItineraryPage AIP = new AdactinItineraryPage(driver);

		try {
			Assert.assertEquals(ALP.defaultLogin(), true, "defaultSearch FAILED");

			String itinearyLink = ASP.bookedIteneraryLink().getAttribute("href");
			ASP.openItineraryLinkInNewTab(itinearyLink);

			Assert.assertTrue(AIP.verifyItineraryPageInNewTab());
		} catch (Exception e) {
			Assert.fail(UtilKit.exceptionLogger(e));
		}

	}

	@DataProvider
	public Object[][] searchDataProvider() {

		return UtilKit.getTestData("com.adactin.hotelapp", "adactin", "search");
	}

}
