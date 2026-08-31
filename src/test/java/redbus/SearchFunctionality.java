package redbus;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchFunctionality {

	private WebDriver wd;
	private WebDriverWait wait;

	@BeforeMethod
	public void setup() {
		System.out.println("Test Started......");
		wd = new ChromeDriver();
		wd.manage().window().maximize();
		wd.get("https://www.redbus.in/");
	}

	@AfterMethod
	public void tearDown() {
		wd.quit();
		System.out.println("Test Completed..!");
	}

	@Test(dataProvider = "locationData")
	public void handleCityDropDown(String city, String area) throws InterruptedException {
		selectCity(city, area);
		Thread.sleep(2000);
	}

	private void selectCity(String city, String area) {

		wait = new WebDriverWait(wd, Duration.ofSeconds(20));

		By hotelTabLocator = By.xpath("//img[@title='Online Hotel Booking']");
		WebElement hotelTab = wait.until(ExpectedConditions.elementToBeClickable(hotelTabLocator));
		hotelTab.click();

		By cityFieldLocator = By.xpath("//div[contains(@class, 'locationRow')]");

		WebElement cityField = wait.until(ExpectedConditions.elementToBeClickable(cityFieldLocator));

		cityField.click();

		WebElement activeElement = wd.switchTo().activeElement();

		activeElement.sendKeys(city);

		By areaLocator = By
				.xpath("//div[contains(@aria-label,'Area in " + city + "') and @aria-label]//div[@aria-label]");

		List<WebElement> areaNames = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(areaLocator));

		System.out.println("\nAreas available in " + city + ":");

		boolean areaFound = false;

		for (WebElement cityArea : areaNames) {

			String areaName = cityArea.getText().trim();

			System.out.println("Area: " + areaName);

			if (areaName.equalsIgnoreCase(area)) {

				System.out.println("Selected Area: " + areaName);

				cityArea.click();

				areaFound = true;
				break;
			}
		}

		if (!areaFound) {
			throw new RuntimeException("Area '" + area + "' was not found for city '" + city + "'");
		}
	}

	@DataProvider(name = "locationData")
	private Object[][]

			locationTestdata() {

		Object[][] dataObjects = new Object[][] { { "Pune", "Viman Nagar" }, { "Pune", "Hinjewadi" },
				{ "Pune", "Pimpri-Chinchwad" }, { "Pune", "Koregaon Park" } };

		return dataObjects;
	}

}
