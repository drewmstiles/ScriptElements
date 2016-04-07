package foundations;

import java.util.ArrayList;

import java.util.List;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
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
	
	
	public void fill(Table t) {
		WebElement table = find(t);
		
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			
			List<WebElement> children = row.findElements(By.xpath("*"));
			
			Element[] data = new Element[children.size()];
			for (int i = 0; i < data.length; i++) {
				data[i] = new Element(children.get(i));
			}
			
			t.addRow(data);
			
		}
	}
	
	public void select(String text, DropDown dd) {
		Select s = new Select(find(dd));
		s.selectByVisibleText(text);	
	}
	
	public void write(String text, Element e) {
		find(e).sendKeys(text);
	}
	
	public void click(Element e) {
		find(e).click();
	}
	
	public Element[] findElementsByXpath(String xpath) {
		return extractElementsFromList(driver.findElements(By.xpath(xpath)));
	}
	
	private Element[] extractElementsFromList(List<WebElement> webElements) {
		
		ArrayList<Element> elements = new ArrayList<Element>();
		for (WebElement we : webElements) {
			elements.add(new Element(we));
		}
		
		return elements.toArray(new Element[0]);
	}
	
	public void end() {
		driver.close();
	}
	
	
	@Override
	public abstract void run();
	
	private WebDriver driver;
}
