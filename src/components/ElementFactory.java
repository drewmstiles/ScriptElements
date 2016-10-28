package components;

import org.openqa.selenium.WebDriver;

public class ElementFactory {

	public static final String ELEMENT = "element";
	public static final String TABLE = "table";
	
	public ElementFactory(WebDriver driver) {
		this.driver = driver;
	}
	
	public Element get(String element, String xpath) {
		if (element.equals(ELEMENT)) {
			return new Element(xpath, driver);
		}
		else if (element.equals(TABLE)) {
			return new Table(xpath, driver);
		}
		else {
			throw new RuntimeException("Attempted instantiation unkown element type.");
		}
	}
	
	private WebDriver driver;

}
