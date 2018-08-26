package foundations;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import components.Element;
import components.ElementFactory;
import components.DropDown;
import components.Table;
import drivers.DriverFactory;
import managers.JavascriptManager;

import file.JsonUtils;

public abstract class Script implements Runnable
{
	public static final String ID = "id";
	public static final String PASS = "pass";
	public static final String FAIL = "fail";
	public static final int ONE_SEC = 1000; // ms
	public static final int ALERT_WAIT_DURATION = 250; // ms
	public static final int WAIT_MS = 5 * 1000;
	public static final int WAIT_MS_INC = 500;
	public static final int DEFAULT_WAIT = 10 * ONE_SEC;
	
	/*
	 * Constructor
	 */
	
	public Script(String b)
	{
		browser = b;
	}
	
	
	/*
	 * Animation Methods
	 */
	
	public void scrollTo(Element e) {
		JavascriptManager.scrollTo(find(e), driver);
	}

	public void scrollDirectlyTo(Element e) {
		JavascriptManager.directScroll(find(e), driver);
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

	public String getUrl() {
		return driver.getCurrentUrl();
	}
	
	/*
	 * Location Methods
	 */
	
	public Element create(String locator) {

		String xpath = "";

		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(locator);
		if (m.matches()) {
			xpath = String.format("//*[@id='%s']", locator);
		}
		else {
			xpath = locator;
		}
		
		return ElementFactory.get("element", xpath, driver);
	}
	
	private WebElement find(Element e) {
		
		By locator = By.xpath(e.getXPath());

		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			waitFor(ExpectedConditions.presenceOfElementLocated(locator));
		}
		catch (NoSuchElementException ex) {
			return null;
		}
		
		return driver.findElement(locator);
	}

	public Element[] findAll(String xpath) {
		return findAllRelative(find("/html/body"), xpath);
	}

	public Element[] findAllRelative(Element root, String relXpath) {
		
		String xpath = root.getXPath() + relXpath; // TODO xpath or xPath?

		while (true) {
			try {
				List<WebElement> webElements = driver.findElements(By.xpath(xpath));
				int numElements = webElements.size(); 
				Element[] elements = new Element[numElements];
				for (int i = 0; i < numElements; i++) 
				{
					String uniqueXPath = "(" + xpath + ")" + "[" + (i + 1) + "]"; // xpath indexing starts at 1
					Element e = ElementFactory.get("element", uniqueXPath, driver);
					WebElement we = webElements.get(i);
					e.setText(we.getText()); // TODO should set in Element
					elements[i] = e;
				}
				return elements;
			}
			catch (StaleElementReferenceException ex) {
				// try again
			}
		}
	}


	/*
	 * XPath Methods
	 */
	
	public String xpathForText(String text) {
		return xpathForText(text, "*");
	}
	
	public String xpathForText(String text, String tag) {
		return String.format("//%s[contains(.,'%s')]", tag, text);
	}
	
	/*
	 * Wait Methods
	 */
	
	
	public void wait(int ms) { // TODO should be seconds
		try {
			Thread.sleep(ms);
		} 
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private <T> void waitFor(ExpectedCondition<T> cond, int seconds) throws TimeoutException {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(cond);
	}

	private <T> void waitFor(ExpectedCondition<T> cond) throws TimeoutException {
		waitFor(cond, 60);
	}
	
	public void waitForPageToLoad() {
		JavascriptManager.waitForPageToLoad(driver);
	}

	public Element waitForPresenceOf(String xpath, int seconds) {
		try {
			waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)), seconds);
			return find(xpath);
		}
		catch (TimeoutException ex) {
			return null;
		}
	}

	public void waitForStalenessOf(Element e) {
		waitFor(ExpectedConditions.stalenessOf(find(e)));
	}

	public Element waitForVisibilityOf(Element e, int seconds) {
		return waitForVisibilityOf(e.getXPath(), seconds);
	}
	

	public Element waitForVisibilityOf(String xpath, int seconds) {
		Element e = waitForPresenceOf(xpath, DEFAULT_WAIT);
		try {
			waitFor(ExpectedConditions.visibilityOf(find(e)), seconds);
			return e;
		}
		catch (TimeoutException ex) {
			return null;
		}
	}
	
	public void waitForAttributeValue(String attr, String value, Element e) throws Exception {
		try {
			waitFor(ExpectedConditions.attributeContains(find(e), attr, value));
		}
		catch (TimeoutException ex) {
			throw new Exception();
		}
	}
	
	public void waitForAnimationBegin(String style, Element e) {
		
		String previousStyle = getStyle(style, e);
		
		int msWaited = 0;
		while(msWaited < WAIT_MS) {
			
			wait(WAIT_MS_INC);
			msWaited += WAIT_MS_INC;
			
			String currentStyle = getStyle(style, e);
			if (currentStyle.equals(previousStyle)) {
				// keep waiting
			}
			else {
				return;
			}
		}
	}
	
	public void waitForAnimationEnd(String style, Element e) {
		
		String previousStyle = getStyle(style, e);
		
		int msWaited = 0;
		while(msWaited < WAIT_MS) {
			
			wait(WAIT_MS_INC);
			msWaited += WAIT_MS_INC;
			
			String currentStyle = getStyle(style, e);
			if (currentStyle.equals(previousStyle)) {
				return;
			}
			else {
				previousStyle = currentStyle;
			}
		}
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
	
	public void write(String text, Element e) {
		find(e).sendKeys(text);
	}

	public void writeJson(Map<String,Object> map, File f) {
		JsonUtils.write(map, f);
	}
	
	public String read(Element e) {
		return find(e).getText();
	}

	public String read(String xpath) {
		return read(find(xpath));
	}
	
	public void clear(Element e) {
		find(e).clear();
	}
	
	/*
	 * Interaction Methods
	 */
	
	public void click(String s) {
		click(ElementFactory.get("element", s, driver));
	}
	
	public void click(Element e) {
		WebElement physical = find(e);
		try {
			physical.click();
		}
		catch (WebDriverException ex) 
		{
			JavascriptManager.forceClick(physical, driver);
		}
	}

	public void select(String text, Element e) {
		Select s = new Select(find(e));
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
	 * Enter Methods
	 */
	
	public void begin(Set<String> settings) {

		driver = DriverFactory.getDriverForBrowswer(browser);

		if (settings.contains("max")) {
			driver.manage().window().maximize();		   
		}
	}
	
	/*
	 * Exit Methods
	 */
	
	public void end() {
		driver.quit();
	}
	
	
	@Override
	public abstract void run(); 
	
	private WebDriver driver;
	private String browser;
}
