package components;

public class Element
{
	
	
	public Element(String xpath) {
		this.xpath = xpath;
	}
	
	public String getXpath() {
		return xpath;
	}
	
	public void setText(String t) {
		text = t;
	}
	
	public String getText() {
		return text;
	}
	
	
	private String xpath;
	private String text;
}
