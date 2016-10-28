package components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Element
{
	public Element(String xpath, WebDriver driver) {
		this.xpath = xpath;
	}
	
	public Element(WebElement e, WebDriver driver) {
		this.physical = e;
	}
	
	public String getXpath() {
		return xpath;
	}
	
	public void setText(String t) {
		text = t;
	}
	
	public String getText() {
		return text;
	}
	
	protected String xpath;
	protected WebElement physical;
	protected WebDriver driver;
	
	private String text;
}
