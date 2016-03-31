package foundations;

import org.openqa.selenium.WebDriver;

import drivers.DriverFactory;

public abstract class Script extends Thread
{

	public Script(String b)
	{
		driver = DriverFactory.getDriverForBrowswer(b);
	}
	
	@Override
	public abstract void run();
	
	private WebDriver driver;
}
