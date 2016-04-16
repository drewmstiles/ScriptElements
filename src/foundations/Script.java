package foundations;

import java.util.ArrayList;
import java.util.List;

import managers.JavascriptManager;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
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
	public Script(String b)
	{
		driver = DriverFactory.getDriverForBrowswer(b);
	}
	
	public void scrollTo(Element e) {
		JavascriptManager.scrollTo(find(e), driver);
	}
	
	public void goTo(String url) {
		driver.get(url);
	}
	
	private WebElement find(Element e) {
		return driver.findElement(By.xpath(e.getXpath()));
	}
	
	public void waitFor(Element e) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(e.getXpath())));
	}
	
	
//	public void fill(Table t) {
//		WebElement table = find(t);
//		
//		List<WebElement> rows = table.findElements(By.tagName("tr"));
//		for (WebElement row : rows) {
//			String color = JavascriptManager.getProperty("style.backgroundColor", row, driver);
//			JavascriptManager.scrollTo(row, driver);
//			JavascriptManager.highlight(row, driver);
//			t.addRow(getChildren(row));
//			JavascriptManager.setColor(color, row, driver);
//		}
//	}
//	
//	public Element[] getChildren(Element e) {
//		return getChildren(find(e));
//	}
	
//	public Element[] getChildren(WebElement e) {
//		
//		List<WebElement> children = e.findElements(By.xpath("*"));
//		
//		Element[] childElements = new Element[children.size()];
//		for (int i = 0; i < childElements.length; i++) {
//			childElements[i] = new Element(children.get(i));
//		}
//		
//		return childElements;
//	}
	
	public void select(String text, DropDown dd) {
		Select s = new Select(find(dd));
		s.selectByVisibleText(text);	
	}
	
	public void write(String text, Element e) {
		find(e).sendKeys(text);
	}
	
	public void click(Element e) {
		
		WebElement physical = find(e);
		physical.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 3);
		try {
			wait.until(ExpectedConditions.stalenessOf(physical));
		}
		catch (TimeoutException ex) {
			// risk of staleness period has passed
		}
	}
	
	public Element[] findElementsByXpath(String xpath) 
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
		
		return elements;
	}
	
	
	public void end() {
		driver.close();
	}
	
	
	@Override
	public abstract void run();
	
	private WebDriver driver;
}
