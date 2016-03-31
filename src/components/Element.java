package components;

public class Element
{

	public enum Locator { ID, XPATH};

	public static final String ID_LOCATOR = "id";
	
	public static final String XPATH_LOCATOR = "xpath";
	
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
	
	private String location;
	private Locator locator;
}
