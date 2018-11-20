package cpt.view.tagchanger;
import cpt.model.ModelSong;

class TagChangerAnchor extends TagChangerToken {

	static final int END = -1;
	static final int LIT = -2;
	
	
	public String text;
	public int pos;
	
	public TagChangerAnchor(String text){
		this.text=text;
		this.pos=LIT;
	}
	
	public TagChangerAnchor(int pos){
		this.text="";
		this.pos=pos;
	}

	public int getPos(String text) {
		int p=pos;
		if(text!=null){
			 if (pos==-1) {
				 p=text.length();
			 } else {
				 p=text.indexOf(this.text);
				 if (p==-1) p=text.length();
			 }
		}
		return p;
	}
	
	@Override
	public String toString(ModelSong item) {
		return text==null ?  "numeric anchor: "+pos : "literal anchor \""+this.text;
	}
}