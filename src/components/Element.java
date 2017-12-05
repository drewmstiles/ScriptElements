package components;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;

import managers.JavascriptManager;

public class Element
{
	public Element(String xpath, WebDriver driver) {
		this.xpath = xpath;
		this.driver = driver;
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
		try {
			physical.click();
		}
		catch (WebDriverException ex) 
		{
			JavascriptManager.forceClick(physical, driver);
		}
	}

	private WebElement find() {
		WebElement physical = driver.findElement(By.xpath(getXPath()));
		id = physical.getAttribute("id");
		text = physical.getText();
		return physical;
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
	protected WebDriver driver;
}
