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
	
	public Element[] getRow(int r) {
		return _rows[r];
	}

	@Override
	public Iterator<Element[]> iterator() {
		return Arrays.asList(_rows).iterator();
	}
	
	private Element[][] initializeRows() {
		
		ArrayList<Element[]> logRows = new ArrayList<Element[]>();
		
		WebElement physTable= driver.findElement(By.xpath(getXPath()));
		
		int numRows = physTable.findElements(By.xpath(".//tr")).size();
	
		for (int i = 1; i < numRows; i++) { // XPath starts at one, ends at count minus one, do the math.	
			
			// Get physical row.
			String rowXPath = this.getXPath() + "//tr[" + (i) + "]";
			WebElement physRow = driver.findElement(By.xpath(rowXPath));
			
			// Animating row scroll.
			String color = JavascriptManager.getProperty("style.backgroundColor", physRow, driver);
			JavascriptManager.scrollTo(physRow, driver);
			JavascriptManager.setColor(physRow, "yellow", driver);
			
			List<WebElement> physCells = physRow.findElements(By.xpath(".//td"));
			if (physCells.size() == 0) {
				// empty row
			}
			else  {
				logRows.add(parseElements(rowXPath, physCells));
			}
			
			// animation cleanup
			JavascriptManager.setColor(physRow, color, driver);
		}
		
		return logRows.toArray(new Element[0][0]);
	}
	
	private Element[] parseElements(String rowXPath, List<WebElement> physCells) {
		Element[] logCells = new Element[physCells.size()];
		for (int j = 0; j < physCells.size(); j++) {
			logCells[j] = ElementFactory.get("element", rowXPath + "//td[" + (j + 1) + "]", driver);
		}
		return logCells;
	}
	
	private Element[][] _rows;
}
