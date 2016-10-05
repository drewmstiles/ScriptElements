package foundations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import managers.JavascriptManager;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import components.Element;
import components.DropDown;
import components.Table;
import drivers.DriverFactory;

public abstract class Script extends Thread
{
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
	
	public void goTo(String url) 
	{
		driver.get(url);
	}
	
	
	/*
	 * Location Methods
	 */
	
	private WebElement find(Element e) {
		return driver.findElement(By.xpath(e.getXpath()));
	}
	
	
	public List<Element> find(String xpath) 
	{
		List<WebElement> webElements = driver.findElements(By.xpath(xpath));
		int numElements = webElements.size(); 
		Element[] elements = new Element[numElements];
		for (int i = 0; i < numElements; i++) 
		{
			String uniqueXPath = xpath + "[" + (i + 1) + "]"; // xpath indexing starts at 1
			Element e = new Element(uniqueXPath);
			WebElement we = webElements.get(i);
			e.setText(we.getText());
			elements[i] = e;
		}
		return Arrays.asList(elements);
	}
	
	
	public void waitFor(Element e) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(e.getXpath())));
	}

	
	/*
	 * Access Methods
	 */
	
	public String[][] read(Table t) 
	{
		// initialize
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		// build
		WebElement table = find(t);
		List<WebElement> rows = table.findElements(By.xpath(".//tr"));
		for (int i = 0; i < rows.size(); i++) 
		{	
			ArrayList<String> rowData = new ArrayList<String>();
			ArrayList<Element> row = new ArrayList<Element>();
			// get physical row
			WebElement r = rows.get(i);
			// animating row scroll
			String color = JavascriptManager.getProperty("style.backgroundColor", r, driver);
			JavascriptManager.scrollTo(r, driver);
			JavascriptManager.highlight(r, driver);
			// adding row of cells
			String rowXPath = t.getXpath() + "//tr[" + (i + 1) + "]";
			row.addAll(find(rowXPath + "//th" + "|" + rowXPath + "//td"));
			if (row.size() == 0)
			{
				// empty row
			}
			else 
			{
				// read text from table cell
				for (int j = 0; j < row.size(); j++) {
					rowData.add(row.get(j).getText());
				}
				data.add(rowData.toArray(new String[0]));
			}
			// animation cleanup
			JavascriptManager.setColor(color, r, driver);
		}
		
		return data.toArray(new String[0][0]);
		// return
	}
	
	public String getValue(Element e) {
		String v = find(e).getAttribute("value");
		System.out.println(v);
		return v;
	}
	
	public void write(String text, Element e) 
	{
		find(e).sendKeys(text);
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
		
		WebDriverWait wait = new WebDriverWait(driver, 3);
		try {
			wait.until(ExpectedConditions.stalenessOf(physical));
		}
		catch (TimeoutException ex) {
			// risk of staleness period has passed
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
