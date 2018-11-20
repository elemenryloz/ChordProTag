package cpt.view.tagchanger;
import java.util.ArrayList;
import java.util.Stack;

import cpt.model.ModelSong;


public class TagChangerTree {

	Stack<TagChangerToken> st = new Stack<>();
	ArrayList<TagChangerToken> tokens = new ArrayList<>();
	
	public TagChangerTree() {
	}
	
	public String toString(ModelSong item) {
		String out="";
		for (TagChangerToken t : this.tokens) {
			out+=t.toString(item);
		}
		return out;
	}

	public String[] toArray(ModelSong item, String tag) {
		int i=0;
		for (TagChangerToken t : this.tokens) {
			if(t.getClass()==TagChangerTag.class) i++;
		}
		String [] out = new String[i];
		String text = item.meta.get(tag);
		if(text!=null) {
			i=0;
			TagChangerTag lastTag=null;
			for (TagChangerToken t : this.tokens) {
				if(t.getClass()==TagChangerTag.class){
					lastTag=(TagChangerTag)t;
				} else {
					String txt = ((TagChangerAnchor)t).text;
					int pos=text.indexOf(txt);
					if (pos==-1) break;
					else if (lastTag!=null){
						out[i]=text.substring(0,pos);
						i++;
						text=text.substring(pos+txt.length());
						lastTag=null;
					}
				}
			}
		}
		return out;
	}
}
