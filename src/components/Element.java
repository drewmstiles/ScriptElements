package components;

import org.openqa.selenium.WebDriver;

public class Element
{
	public Element(String xpath, WebDriver driver) {
		this.xpath = xpath;
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
	
	
	private String xpath;
	private String text;
	protected WebDriver driver;
}
