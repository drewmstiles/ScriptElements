package foundations;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import components.Element;
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
			
			Element e = new Element(null);
			e.setText(we.getText());
			elements.add(e);
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
