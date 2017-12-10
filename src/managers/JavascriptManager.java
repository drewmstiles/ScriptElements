package managers;

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
	
	public static void setColor(WebElement element, String color, WebDriver driver) {
		setStyle("background", color, element, driver);
	}
	
	public static void setStyle(String style, String value, WebElement element, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].style.setProperty('" + style + "','" + value + "','important');", element);
	}
	
	public static void scrollTo(WebElement element, WebDriver driver) {
		smoothScroll(element, SCROLL_DELTA, driver);
	}
	
	public static void smoothScroll(WebElement elem, int delta, WebDriver driver) {
		
		JavascriptExecutor js = (JavascriptExecutor)driver;

		int windowY = getWindowY(js);
		int windowHeight = getWindowHeight(js);
		int elemY = getElementY(elem, js);
		int elemHeight = getElementHeight(elem, js);

		// Go backwards if window is passed element
		delta = (windowY > elemY) ? (-1 * delta) : delta;
		
		int halfScreen = (windowHeight / 2);
		int distance = Math.abs(windowY - elemY);

		// Add padding in accordance with direction
		distance = (delta < 0 ) ? distance + halfScreen : distance - halfScreen;
		
		while ((distance -= Math.abs(delta)) > 0) {
			scrollBy(0, delta, js);
		}
	}
	
	private static void scrollBy(int dx, int dy, JavascriptExecutor js)	{
		js.executeScript("window.scrollBy(" + dx + "," + dy + ")");
	}
	
	private static int getWindowHeight(JavascriptExecutor js) {
		return ((Number)js.executeScript("return window.innerHeight;")).intValue();
	}

	private static int getWindowY(JavascriptExecutor js) {
		return ((Number)js.executeScript("return window.screenY;")).intValue();
	}

	private static int getElementHeight(WebElement elem, JavascriptExecutor js) {
		return ((Number)js.executeScript("return arguments[0].getBoundingClientRect().height;", elem)).intValue();
	}

	private static int getElementY(WebElement elem, JavascriptExecutor js) {
		return ((Number)js.executeScript("return arguments[0].getBoundingClientRect().y", elem)).intValue();
	}

	private static int getScrollTop(WebElement elem, JavascriptExecutor js)	{
		
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
	
	public static String getStyle(String style, WebElement element, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String script = "return window.getComputedStyle(arguments[0]).getPropertyValue('" + style + "')";
		return (String)js.executeScript(script, element);
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
