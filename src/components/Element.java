package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Element
{
	public Element(String xpath, WebDriver driver) {
		this.xpath = xpath;
		this.driver = driver;
	}
	
	public Element(WebElement e, WebDriver driver) {
		this.physical = e;
	}
	
	public String getXPath() {
		return xpath;
	}
	
	public void write(String text) {
		find().sendKeys(text);
	}
	
	public String getText() {
		return find().getText();
	}
	
	public void click() {
		find().click();
	}
	
	private WebElement find() {
		return (physical != null) ? physical : driver.findElement(By.xpath(getXPath()));
	}
	
	
	protected String xpath;
	protected WebElement physical;
	protected WebDriver driver;
}
