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
		for (int i = 0; i < row.length; i++) {
			System.out.print(row[i].getText() + "\t");
		}
		
		System.out.println();
		
		rows.add(row);
	}
	
	
	private ArrayList<Element[]> rows;

}
