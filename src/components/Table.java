package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import managers.JavascriptManager;

public class Table extends Element implements Iterable<Element[]>
{

	public Table(String xpath, WebDriver driver)
	{
		super(xpath, driver);
		_rows = initializeRows();
	}
	
	public Table (WebElement e, WebDriver driver) {
		super(e, driver);
		_rows = initializeRows();	
	}
	
	public Element[] getRow(int r) {
		return _rows[r];
	}

	@Override
	public Iterator<Element[]> iterator() {
		return Arrays.asList(_rows).iterator();
	}
	
	private Element[][] initializeRows() {
		
		ArrayList<Element[]> logRows = new ArrayList<Element[]>();
		
		WebElement physTable= driver.findElement(By.xpath(getXpath()));
		List<WebElement> physRows = physTable.findElements(By.xpath(".//tr"));
		
		for (int i = 0; i < physRows.size(); i++) {	
			
			// Get physical row.
			WebElement physRow = physRows.get(i);
			
			// Animating row scroll.
			String color = JavascriptManager.getProperty("style.backgroundColor", physRow, driver);
			JavascriptManager.scrollTo(physRow, driver);
			JavascriptManager.setColor(physRow, "yellow", driver);
			
			// Adding row of cells.
			String rowXPath = getXpath() + "//tr[" + (i + 1) + "]";
			List<WebElement> physCells = driver.findElements(By.xpath(rowXPath + "//th" + "|" + rowXPath + "//td"));
			if (physCells.size() == 0) {
				// empty row
			}
			else  {
				logRows.add(parseElements(physCells));
			}
			
			// animation cleanup
			JavascriptManager.setColor(physRow, color, driver);
		}
		
		return logRows.toArray(new Element[0][0]);
	}
	
	private Element[] parseElements(List<WebElement> physCells) {
		Element[] logCells = new Element[physCells.size()];
		for (int j = 0; j < physCells.size(); j++) {
			logCells[j] = ElementFactory.get("element", physCells.get(j), driver);
		}
		return logCells;
	}
	
	private Element[][] _rows;
}
