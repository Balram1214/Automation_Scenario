package fast_com;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Fast_Automation {

	public static void main(String[] args) throws InterruptedException {

		ChromeOptions op = new ChromeOptions();
		op.addArguments("--start-maximized");
		op.addArguments("--incognito");
		op.addArguments("--disabled-notifications");

		WebDriver driver = new ChromeDriver(op);

		driver.get("https://fast.com/");

		WebElement speed = driver.findElement(By.id("speed-value"));

		String succeedValue = speed.getAttribute("class");

		// System.out.println(succeedValue);

		String previousSpeed = "";
		while (true) {

			String currentSpeed = speed.getText();

			if (!currentSpeed.isEmpty() && !currentSpeed.equals(previousSpeed)) {
				System.out.println(currentSpeed);
				previousSpeed = currentSpeed;
			}

			succeedValue = speed.getAttribute("class");

			if (succeedValue.contains("succeeded")) {
				break;
			}
			Thread.sleep(50);
		}

		System.out.println("Final speed tested: " + speed.getText());

		driver.quit();
	}

}
