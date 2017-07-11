package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory
{
	public static final String CHROME = "chrome";

	private static final String CHROME_DRIVER = "webdriver.chrome.driver";
	private static final String DRIVERS_DIR = "drivers/";
	private static final String CHROME_DRIVER_FILE = "chromedriver";
	
	public static WebDriver getDriverForBrowswer(String b)	{

		switch(b.toLowerCase()) {
		case "chrome":
			System.setProperty(CHROME_DRIVER, DRIVERS_DIR + CHROME_DRIVER_FILE);
			return new ChromeDriver();
		case "safari":
			return new SafariDriver();
		case "firefox":
			return new FirefoxDriver();
		default:
			System.out.printf("Error: '%s' is not a supported browser.\n");
			return null;
		}
	}
}
