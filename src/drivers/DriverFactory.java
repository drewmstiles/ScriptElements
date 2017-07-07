package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory
{
	public static final String CHROME = "chrome";

	private static final String CHROME_DRIVER = "webdriver.chrome.driver";
	private static final String DRIVERS_DIR = "drivers/";
	private static final String CHROME_DRIVER_FILE = "chromedriver";
	
	public static WebDriver getDriverForBrowswer(String b)	{
		System.setProperty(CHROME_DRIVER, DRIVERS_DIR + CHROME_DRIVER_FILE);
		return new ChromeDriver();
	}
}
