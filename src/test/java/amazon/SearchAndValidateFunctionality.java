package amazon;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SearchAndValidateFunctionality {

	private WebDriver wd;
	private WebDriverWait wait;

	@BeforeMethod
	public void setup() {

		System.out.println("Test Started......");

		wd = new ChromeDriver();

		wd.manage().window().maximize();

		wd.get("https://www.amazon.in/");

		wait = new WebDriverWait(wd, Duration.ofSeconds(20));
	}

	@AfterMethod
	public void tearDown() {

		wd.quit();

		System.out.println("Test Completed..!");
	}

	@Test
	public void SearchAndValidateProduct() throws InterruptedException {

		SearchProduct();
		Thread.sleep(4000);
	}

	private void SearchProduct() {

		String productName = "Laptop";
		String expectBrand = "Asus";

		// Search Product

		By searchBoxLocator = By.xpath("//input[@role='searchbox']");

		WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(searchBoxLocator));

		searchBar.sendKeys(productName);

		By searchButton = By.xpath("//input[@value='Go']");

		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();

		// Get Product List

		By productListLocator = By.xpath("//div[@role='listitem' and contains(@data-asin,'B0')]"
				+ "//h2[contains(@aria-label,'" + productName + "')]");

		List<WebElement> productList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productListLocator));

		// Store Parent Window

		String parentWindow = wd.getWindowHandle();

		// Find & Click Asus Product

		boolean productFound = false;

		for (WebElement prod : productList) {

			String productText = prod.getText();

			if (productText.toLowerCase().contains(expectBrand.toLowerCase())) {

				System.out.println("Selected Product: " + productText);

				prod.click();

				productFound = true;

				break;
			}
		}

		Assert.assertTrue(productFound, "product not found in search results");

		// Switch to New Product Window

		Set<String> windows = wd.getWindowHandles();

		for (String window : windows) {

			if (!window.equals(parentWindow)) {

				wd.switchTo().window(window);

				break;
			}
		}

		// Validate Product Title

		By prodTitleLocator = By.xpath("//span[@id='productTitle']");

		WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(prodTitleLocator));

		String actualTitle = productTitle.getText();

		System.out.println("Actual Title: " + actualTitle);

		Assert.assertTrue(actualTitle.toLowerCase().contains(expectBrand.toLowerCase()),
				"Product title does not contain Asus");

		// Close Product Window

		wd.close();

		wd.switchTo().window(parentWindow);

		System.out.println("Returned to Parent Window: " + wd.getWindowHandle());

		// Get All Product Cards

		By productCardsLocator = By.xpath("//div[@role='listitem' and contains(@data-asin,'B0')]");

		List<WebElement> productCards = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCardsLocator));

		// Find Lowest Price Product

		int lowestPrice = Integer.MAX_VALUE;

		WebElement lowestPriceProduct = null;

		String lowestProductTitle = "";

		for (WebElement card : productCards) {

			try {

				// Get product title from card
				WebElement cardTitleElement = card.findElement(By.xpath(".//h2"));

				String cardTitle = cardTitleElement.getText();

				// Get product price
				WebElement priceElement = card.findElement(By.xpath(".//span[@class='a-price-whole']"));

				String priceText = priceElement.getText();

				priceText = priceText.replace(",", "").trim();

				int price = Integer.parseInt(priceText);

				System.out.println("Product: " + cardTitle);
				System.out.println("Price: " + price);

				// Find lowest price
				if (price < lowestPrice) {

					lowestPrice = price;

					lowestPriceProduct = card;

					lowestProductTitle = cardTitle;
				}

			} catch (Exception e) {

				// Skip products where price/title is not available
				continue;
			}
		}

		// Validate Lowest Price

		Assert.assertNotNull(lowestPriceProduct, "No product with valid price was found");

		System.out.println("Lowest Price Product Details");

		System.out.println("Product: " + lowestProductTitle);

		System.out.println("Price: " + lowestPrice);

		// Click Lowest Price Product

		lowestPriceProduct.click();

		System.out.println("Clicked Lowest Price Product");
	}
}