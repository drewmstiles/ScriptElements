package components;

import java.util.ArrayList;
import java.util.Iterator;

public class Table extends Element implements Iterable<Element[]>
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

	@Override
	public Iterator<Element[]> iterator() {
		return rows.iterator();
	}

	private ArrayList<Element[]> rows;
}
