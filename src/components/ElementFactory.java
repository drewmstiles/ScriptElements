package components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

public class ElementFactory {

	public static final String ELEMENT = "element";
	public static final String TABLE = "table";
	public static final String DROP_DOWN = "drop down";

	public static Element get(String element, String xpath, WebDriver driver) {
		try {
			if (element.equals(ELEMENT)) {
				return new Element(xpath, driver);
			}
			else if (element.equals(TABLE)) {
				return new Table(xpath, driver);
			}
			else if (element.equals(DROP_DOWN)) {
				return new DropDown(xpath, driver);
			}
			else {
				throw new RuntimeException("Attempted instantiation unkown element type.");
			}
		}
		catch (NoSuchElementException ex) {
			return null;
		}
	}
}
