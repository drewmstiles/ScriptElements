package components;

import org.openqa.selenium.WebElement;

public class Element
{
	
	
	public Element(String xpath) {
		this.xpath = xpath;
	}
	
	public Element(WebElement e) {
		
		this.physical = e;
		text = physical.getText();
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
	private WebElement physical;
}
