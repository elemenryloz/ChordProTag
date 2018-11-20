package cpt.view.browser;

public class BrowserTableColumn {
	
	private String identifier;
	private String headerValue;
	private int preferredWidth;

	public BrowserTableColumn(String tag){
		this.setIdentifier(tag);
		this.setHeaderValue(tag);
		this.setPreferredWidth(100);
	}
	
	
	public BrowserTableColumn(String tag, String title){
		this.setIdentifier(tag);
		this.setHeaderValue(title);
		this.setPreferredWidth(100);
	}
	
	public BrowserTableColumn(String tag, String title, int width){
		this.setIdentifier(tag);
		this.setHeaderValue(title);
		this.setPreferredWidth(width);
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}
	
	public int getPreferredWidth() {
		return preferredWidth;
	}

	private void setPreferredWidth(int width) {
		this.preferredWidth = width;
	}

}
