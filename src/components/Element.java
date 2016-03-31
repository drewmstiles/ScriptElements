package components;

public class Element
{

	public enum Locator { ID, XPATH, NONE};

	public static final String ID_LOCATOR = "id";
	
	public static final String XPATH_LOCATOR = "xpath";
	
	public static final String NONE_LOCATOR = "none";
	
	public Element()
	{
		this(Locator.NONE, null);
	}
	public Element(String id) {
		this(Locator.ID, id);
	}
	
	public Element(String locator, String location) {
		this(convertToLocator(locator), location);
	}
	
	public Element(Locator locator, String location) {
		this.location = location;
		this.locator = locator;
	}
	
	private static Locator convertToLocator(String l) {
		switch(l.toLowerCase()) {
			case ID_LOCATOR:
				return Locator.ID;
			case XPATH_LOCATOR:
				return Locator.XPATH;
			default:
				throw new RuntimeException("Locator \"" + l + "\" is not valid.");
		}
	}
	
	public void setText(String t) {
		text = t;
	}
	
	public String getText() {
		return text;
	}
	
	private String location;
	private Locator locator;
	private String text;
}
