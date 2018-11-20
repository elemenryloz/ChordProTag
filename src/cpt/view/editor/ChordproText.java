package cpt.view.editor;

public class ChordproText {
	
	String text;
	
	public ChordproText(String text){
		this.text=text;
	}
	
	public String getTag(String tag){
		int p0=text.indexOf("#{"+tag+":");
		String text2=tag;
		if (p0!=-1) {
			text2=text.substring(p0+tag.length()+3);
			p0=text2.indexOf("}");
			if (p0!=-1) text2=text2.substring(0, p0);
		}
		return text2;
	}
	
}
