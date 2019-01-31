package com.tripaction;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TripActionAssignment {
	WebDriver driver; 
	String SEARCH_TEXT = "Los Angeles"; 
  
	@Test
	public void UITest() throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "//Users//Animesh//bin//chromedriver2");

		driver = new ChromeDriver();
		
		driver.get("https://www.booking.com");
		driver.manage().window().maximize();
		
		WebDriverWait wait = new WebDriverWait(driver, 120);
		//Find Search box
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("ss")))).sendKeys(SEARCH_TEXT);
		//Select Los Angeles from Autocomplete
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//*[@data-label='Los Angeles, California, USA']")))).click();
		//CheckinDate 3 days from Current Date
		String checkinDate = AutomationUtilities.getCheckinDate();
		
		//CheckoutDate 5 days from current date 
		String checkoutDate = AutomationUtilities.getCheckOutDate();
		
		//Select Checkin date from calendar
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//*[@data-date='"+checkinDate+"']")))).click();
		//Select Checkout date from calendar
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//*[@data-date='"+checkoutDate+"']")))).click();
		//Click Search
		driver.findElement(By.className("sb-searchbox__button  ")).click();
		
		//Get first 3 elements and filter sold out property
		List<WebElement> searchResults = new ArrayList<WebElement>();
		for (int i=1;i<=3;i++){
		WebElement selectedResult;
		selectedResult=	wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//*[@id='hotellist_inner']/div["+i+"]"))));
		if(selectedResult.findElements(By.className("sold_out_property")).isEmpty())
			searchResults.add(selectedResult);
		}
		//Fail if all top 3 hotels are booked
		Assert.assertTrue(!searchResults.isEmpty(), "All Top 3 rooms unavailable");
		
		//Click on Random Available hotel from Top 3
		searchResults.get(AutomationUtilities.getRandomNumber(searchResults.size())).findElement(By.cssSelector(".b-button_primary")).click();
		
		ArrayList<String> tabList = new ArrayList<String>(driver.getWindowHandles());
		//Navigate to new window
		driver.switchTo().window(tabList.get(1));
		//Get the cheapest row and select room count as 1
		WebElement selector =	wait.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector(".js-hprt-table-cheapest-block .hprt-nos-select"))));
		Select roomCountSelector = new Select(selector);
		roomCountSelector.selectByValue("1");	
		//click Reserve Button
		driver.findElement(By.cssSelector(".js-reservation-button")).click();
		

	}
	
	@AfterTest
	public void tearDown(){
		driver.quit();
	}

}
