package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
		WebElement physical = this.find();
		physical.click(); 
		WebDriverWait wait = new WebDriverWait(driver, 1000);
		wait.until(ExpectedConditions.stalenessOf(physical));
	}
	
	private WebElement find() {
		return (physical != null) ? physical : driver.findElement(By.xpath(getXPath()));
	}
	
	
	protected String xpath;
	protected WebElement physical;
	protected WebDriver driver;
}
