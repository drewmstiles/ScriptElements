package foundations;

import java.util.Arrays;
import java.util.List;

import managers.JavascriptManager;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import components.Element;
import components.ElementFactory;
import components.DropDown;
import components.Table;
import drivers.DriverFactory;

public abstract class Script extends Thread
{
	public static final int ONE_SEC = 1000; // ms
	public static final int ALERT_WAIT_DURATION = 250; // ms
	
	/*
	 * Constructor
	 */
	
	public Script(String b)
	{
		driver = DriverFactory.getDriverForBrowswer(b);
	}
	
	
	/*
	 * Animation Methods
	 */
	
	public void scrollTo(Element e) 
	{
		JavascriptManager.scrollTo(find(e), driver);
	}
	
	
	/*
	 * Navigation Methods
	 */
	
	public void refresh() {
		driver.navigate().refresh();
	}
	
	public void goTo(String url) 
	{
		driver.get(url);
	}
	
	public void waitForPageToLoad() {
		JavascriptManager.waitForPageToLoad(driver);
	}
	
	public void wait(int ms) {
		try {
			Thread.sleep(ms);
		} 
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Location Methods
	 */
	
	private WebElement find(Element e) {
		return driver.findElement(By.xpath(e.getXPath()));
	}
	
	public Element getElement(String xpath) {
		return ElementFactory.get("element", xpath, driver);
	}
	
	public Table getTable(String xpath) {
		return (Table)ElementFactory.get("table", xpath, driver);
	}
	
	public List<Element> find(String xpath) 
	{
		List<WebElement> webElements = driver.findElements(By.xpath(xpath));
		int numElements = webElements.size(); 
		Element[] elements = new Element[numElements];
		for (int i = 0; i < numElements; i++) 
		{
			String uniqueXPath = xpath + "[" + (i + 1) + "]"; // xpath indexing starts at 1
			Element e = ElementFactory.get("element", uniqueXPath, driver);
			WebElement we = webElements.get(i);
			e.write(we.getText());
			elements[i] = e;
		}
		return Arrays.asList(elements);
	}
	
	public int getElementCount(String xpath) {
		return driver.findElements(By.xpath(xpath)).size();
	}
	
	public void waitFor(Element e) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(e.getXPath())));
	}

	
	/*
	 * Access Methods
	 */
	
	public String getAttr(String a, Element e) {
		return find(e).getAttribute(a);
	}
	
	public String getStyle(String s, Element e) {
		return JavascriptManager.getStyle(s, find(e), driver);
	}
	
	public void write(String text, Element e) 
	{
		find(e).sendKeys(text);
	}
	
	public void clear(Element e) {
		find(e).clear();
	}
	
	/*
	 * Interaction Methods
	 */
	
	public void click(Element e) 
	{
		
		WebElement physical = find(e);
		try 
		{
			physical.click();
		}
		catch (ElementNotVisibleException ex) 
		{
			JavascriptManager.forceClick(physical, driver);
		}
	}
	
	
	public void select(String text, DropDown dd) 
	{
		Select s = new Select(find(dd));
		s.selectByVisibleText(text);	
	}
	
	
	public void switchToFrame(Element frame) 
	{
		driver.switchTo().frame(find(frame));
	}
	
	
	public void switchToDefaultFrame()
	{
		driver.switchTo().defaultContent();
	}

	public boolean alertHandler() {
		
		return handleAlert(driver);
	}
	
	private static boolean handleAlert(WebDriver driver) {
	
		boolean alertAccepted;
		
		try {
			Thread.sleep(ALERT_WAIT_DURATION);
			driver.switchTo().alert().accept();
			alertAccepted = true;
		}
		catch (NoAlertPresentException ex) {
			alertAccepted = false;
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			alertAccepted = false;
		}	
		
		return alertAccepted;
	}
	
	public void setStyle(String style, String value, Element e) {
		JavascriptManager.setStyle(style, value, find(e), driver);
	}
	
	/*
	 * Exit Methods
	 */
	
	public void end() {
		driver.close();
	}
	
	
	@Override
	public abstract void run(); 
	
	private WebDriver driver;
}
