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
import org.testng.annotations.Test;

public class CalenderHandling {

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

	@Test
	public void handleCalender() throws InterruptedException {
		SelectJourneyDate();
		Thread.sleep(3000);

	}

	private void SelectJourneyDate() {

		wait = new WebDriverWait(wd, Duration.ofSeconds(20));

		By journeyDateLocator = By.xpath("//div[@aria-label='Select date of journey']");

		wait.until(ExpectedConditions.elementToBeClickable(journeyDateLocator)).click();

		By monthYear = By.xpath("//p[contains(@class, 'monthYear')]");

		By nextMonth = By.xpath("//i[contains(@aria-label, 'Next month,')]");

		String targetMonthYear = "September 2026";
		String targetDate = "18";

		while (true) {

			WebElement monthYearText = wait.until(ExpectedConditions.visibilityOfElementLocated(monthYear));

			String currentMonth = monthYearText.getText();

			System.out.println("Current Month: " + currentMonth);

			if (currentMonth.equalsIgnoreCase(targetMonthYear)) {
				break;
			}

			WebElement nextArrow = wait.until(ExpectedConditions.elementToBeClickable(nextMonth));

			nextArrow.click();

			wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(monthYear, currentMonth)));
		}

		By targetDateLocator = By.xpath("//div[contains(@class, 'Date')]//span[text()='" + targetDate + "']");

		WebElement targetDateElement = wait.until(ExpectedConditions.elementToBeClickable(targetDateLocator));

		targetDateElement.click();

		System.out.println("Selected Date: " + targetDate);
	}

}
