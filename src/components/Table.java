package components;

import java.util.ArrayList;

public class Table extends Element
{

	public Table(String xpath)
	{
		super(xpath);
		rows = new ArrayList<Element[]>();
	}
	
	public void addRow(Element[] row) {		
		rows.add(row);
	}
	
	public ArrayList<Element[]> getRows() {
		return rows;
	}
	
	
	private ArrayList<Element[]> rows;

}
