package managers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class JavascriptManager
{
	public static final String ORANGE = "#F68620";
	public static final String WHITE = "#FFFFFF";
	public static final String RED = "#FF0000";
	public static final String GREEN = "#00CC00";
	public static final String YELLOW = "#FFFF00";
	
	private static final int SCROLL_DELTA = 10;
	
	
	public static void setValue(WebElement field, String value, WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].value= \"" + value + "\";", field);
	}
	
	public static void directScroll(WebElement row, WebDriver driver)
	{
		// set window scroll coordinates
		int winHeight = driver.manage().window().getSize().getHeight();
		int rowHeight = row.getSize().getHeight();
		int offset = (winHeight - rowHeight) / 2;
		int y = (row.getLocation().getY()) - offset;

		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("javascript:window.scrollTo(0," + y + ")");
	}
	
	public static void highlight(WebElement element, WebDriver driver) {
		setColor(YELLOW, element, driver);
	}

	public static void setColor(String color, String id, WebDriver driver)
	{
		WebElement element = driver.findElement(By.id(id));
		setColor(color, element, driver);
	}
	
	// TODO deprecate
	public static void setColor(String color, WebElement element, WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].style.background='" + color + "'", element);
	}
	
	public static void setColor(WebDriver driver, WebElement element, String color)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].style.background='" + color + "'", element);
	}
	
	public static void scrollTo(WebElement element, WebDriver driver)
	{
		smoothScroll(element, SCROLL_DELTA, driver);
	}
	
	public static void smoothScroll(WebElement elem, int delta, WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		 
		int windowHeight = getWindowHeight(js);
		int elemHeight = getClientHeight(elem, js);
		
		int topHalf = (int)((0.5)*(windowHeight - elemHeight));
		
		int prevRemainingDist = 0;
		while (true)
		{
			scrollBy(0, delta, js);
			
			int remainingDist = getScrollTop(elem, js) - topHalf;
			
			if (remainingDist <= 0) break;
			
			if (remainingDist == prevRemainingDist) break;
			
			prevRemainingDist = remainingDist;
		}
		
		try
		{
			Thread.sleep(60);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static void scrollBy(int dx, int dy, JavascriptExecutor js)
	{
		js.executeScript("window.scrollBy(" + dx + "," + dy + ")");
	}
	
	
	private static int getClientHeight(WebElement elem, JavascriptExecutor js)
	{
		return ((Long)js.executeScript("return arguments[0].clientHeight;", elem)).intValue();
	}
	
	
	private static int getScrollTop(WebElement elem, JavascriptExecutor js)
	{
		Object obj = js.executeScript("return arguments[0].getBoundingClientRect().top;", elem);
		if (obj == null)
		{
			return 0;
		}
		else
		{
			if (obj instanceof Long) 
			{
				return ((Long)obj).intValue();
			}
			else if (obj instanceof Double)
			{
				return ((Double)obj).intValue();
			}
			else
			{
				throw new RuntimeException();
			}
		}
	}
	
	public static boolean elementIsVisible(WebElement elem, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String script = "return window.getComputedStyle(arguments[0]).getPropertyValue('visibility')";
		return ((String)js.executeScript(script, elem)).equals("visible");
	}
	
	// TODO does this work still?
	public static String getProperty(String property, WebElement element, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String script = "return window.getComputedStyle(arguments[0]).getPropertyValue('" + property + "')";
		return (String)js.executeScript(script, element);
	}
	
	private static int getWindowHeight(JavascriptExecutor js)
	{
		return ((Long)(js.executeScript("return window.innerHeight;"))).intValue();
	}
	
	
	public static void forceClick(WebElement dest, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click()", dest);
	}
	
	public static boolean forceClick(String id, WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		
		String script = "return document.getElementById(arguments[0]);";
		WebElement clickTarget = (WebElement)js.executeScript(script, id);
		if (clickTarget == null)
		{
			return false;
		}
		else
		{
			forceClick(clickTarget, driver);
			return true;
		}
	}
	
	
	public static void hide(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String hideScript = "arguments[0].style.display = 'none'";
		js.executeScript(hideScript, element);
	}
	
	
	public static void waitForPageToLoad(WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		
		while (true)
		{
			String documentReadyState = (String)js.executeScript("return document.readyState");
			
			if (documentReadyState.equals("complete")) return;
		}
	}
}
