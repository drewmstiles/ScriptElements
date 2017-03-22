package components;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Element
{
	public Element(String xpath, WebDriver driver) {
		this.xpath = xpath;
		this.driver = driver;
		id = find().getAttribute("id");
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
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return find().getText();
	}
	
	public String getHtml() {
		return find().getAttribute("innerHTML");
	}

	public String getId() {
		return id != null ? id : find().getAttribute("id");
	}
	
	public Element hover() {
		Actions builder = new Actions(driver);
		builder.moveToElement(this.find());
		return this;
	}
	
	public void click() {
		WebElement physical = this.find();
		physical.click(); 
		try {
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.stalenessOf(physical));
		}
		catch (TimeoutException ex) {
			// Element did not trigger DOM update.
		}
	}
	
	private WebElement find() {
		WebElement we = (physical != null) ? physical : driver.findElement(By.xpath(getXPath()));
		id = we.getAttribute("id");
		text = we.getText();
		return we;
	}
	
	@Override
	public String toString() {
		return String.format("Element (%s)", getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		else if (!(obj instanceof Element)) {
			return false;
		}
		else {
			Element other = (Element)obj;
			return getId().equals(other.getId());
		}
	}

	protected String id;
	protected String xpath;
	protected String text;
	protected WebElement physical;
	protected WebDriver driver;
}
