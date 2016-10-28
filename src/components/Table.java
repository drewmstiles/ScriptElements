package components;

import java.util.ArrayList;
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
		rows = new ArrayList<Element[]>();
	}
	
	public void addRow(Element[] row) {		
		rows.add(row);
	}
	
	public ArrayList<Element[]> getRows() {
		return rows;
	}

	@Override
	public Iterator<Element[]> iterator() {
		return rows.iterator();
	}
	
	public String[][] initialize(Table t) 
	{
		// initialize
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		// build
		WebElement table = driver.findElement(By.xpath(this.getXpath()));
		List<WebElement> rows = table.findElements(By.xpath(".//tr"));
		for (int i = 0; i < rows.size(); i++) 
		{	
			ArrayList<String> rowData = new ArrayList<String>();
			ArrayList<Element> row = new ArrayList<Element>();
			// get physical row
			WebElement r = rows.get(i);
			// animating row scroll
			String color = JavascriptManager.getProperty("style.backgroundColor", r, driver);
			JavascriptManager.scrollTo(r, driver);
			JavascriptManager.setColor(r, "yellow", driver);
			// adding row of cells
			String rowXPath = t.getXpath() + "//tr[" + (i + 1) + "]";
			row.addAll(find(rowXPath + "//th" + "|" + rowXPath + "//td"));
			if (row.size() == 0)
			{
				// empty row
			}
			else 
			{
				// read text from table cell
				for (int j = 0; j < row.size(); j++) {
					rowData.add(row.get(j).getText());
				}
				data.add(rowData.toArray(new String[0]));
			}
			// animation cleanup
			JavascriptManager.setColor(r, color, driver);
		}
		
		return data.toArray(new String[0][0]);
		// return
	}

	private ArrayList<Element[]> rows;
}
