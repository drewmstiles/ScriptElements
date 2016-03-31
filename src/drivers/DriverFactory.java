package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory
{
	public static final String WEB_DRIVER_CHROME = "webdriver.chrome.driver";

	public static final String DRIVERS_DIR = "drivers/";
	
	public static final String CHROME_DRIVER_FILE = "chromedriver";
	
	public static final String EXE_EXTENSION = ".exe";
	
	public static WebDriver getDriverForBrowswer(String b)	{
		
		String driverPath = DRIVERS_DIR + CHROME_DRIVER_FILE;
		try {
			System.setProperty(WEB_DRIVER_CHROME, driverPath);
		}
		catch (IllegalStateException ex) {
			// Exception present on Windows machines.
			System.setProperty(WEB_DRIVER_CHROME, driverPath + EXE_EXTENSION);
		}
		
		return new ChromeDriver();
	}
}
