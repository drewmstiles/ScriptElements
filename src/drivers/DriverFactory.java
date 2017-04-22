package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory
{
	public static final String CHROME = "chrome";

	private static final String CHROME_DRIVER = "webdriver.chrome.driver";
	private static final String DRIVERS_DIR = "/Users/dms/Developer/Java/ScriptElements/drivers/";
	private static final String CHROME_DRIVER_FILE = "chromedriver";
	
	public static WebDriver getDriverForBrowswer(String b)	{
		String driverPath = DRIVERS_DIR + CHROME_DRIVER_FILE;
		System.setProperty(CHROME_DRIVER, driverPath);
		return new ChromeDriver();
	}
}
