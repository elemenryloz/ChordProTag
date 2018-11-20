package cpt.view.editor;
import java.util.Vector;

public class PlainText {

    public static final int EMPTY = 0;
    public static final int CHORDS = 1;
    public static final int CHORDS_OVER_TEXT = 2;
    public static final int TEXT = 3;
    public static final int TAG = 4;
    public static final int COMMENT = 5;

    Vector<Integer> l=new Vector<Integer>();
    int c[];
    String v[];
	String tt[];

    
    
    public PlainText(String t, String[] tt){
    	this.tt = tt;
        int n=0;
        for(char c: t.toCharArray()){
            n++;
            if(c=='\n') {
                l.add(n);
            }
        }
        if(!t.endsWith("\n")) l.add(t.length()+1);
        
        c=new int[l.size()];
        v=t.split("\n");
        for(int l=0; l<v.length; l++){
            c[l]=this.isChord(v[l]);
            if(l>0 && c[l]==PlainText.TEXT && c[l-1]==PlainText.CHORDS) c[l-1]=PlainText.CHORDS_OVER_TEXT;
        }
    }
    
    public String toChordPro() {
        String t="";
        for(int n=0; n<v.length; n++){
            int m=0;
            switch (this.getType(n)) {
	            case PlainText.EMPTY:
	                break;
	            case PlainText.CHORDS:
	                t+=v[n].replaceAll("(^| )([^\\s]+)", "$1\\[$2\\]")+"\n";
	                break;
                case PlainText.CHORDS_OVER_TEXT:
                    String cc=v[n];
                    String tt=v[n+1];
                    for(int x=cc.length(); x<tt.length(); x++) cc+=" ";
                    for(int x=tt.length(); x<cc.length(); x++) tt+=" ";
                    int c=0;
                    for(int x=0; x<tt.length(); x++){
                    	if(m==0) {
                            if(cc.charAt(c)!=' '){
                                t+="[";
                                while(c+m<cc.length() && cc.charAt(c+m)!=' '){
                                    t+=cc.charAt(c+m);
                                    m++;
                                }
                                t+="]";
                            }
                    	} else {
                    		m--;
                    	}
                        t+=tt.charAt(x);
                        c++;
                    }
                    t+="\n";
                    n++;
                    break;
                default:
                    t+=v[n]+"\n";
            }
        }
        return t.replaceAll("~[ ]+","~ ");
    }

    int isChord(String s) {
    	if(s.length()==0) return PlainText.EMPTY;
    	if(s.startsWith("{")) return PlainText.TAG;
    	if(s.startsWith("#")) return PlainText.COMMENT;
		String[] arr = s.split(" ");
		for ( String ss : arr) {
			if(ss.length()==0) continue;
			boolean found=false;
			if(ss.length()<21){
				for ( String t : tt) {
					if(t.length()==0 || ss.startsWith(t)){
						found=true;
						break;
					}
				}
			}
			if (!found) return PlainText.TEXT;
		}
		return PlainText.CHORDS;
    }

    public int getLine(int c){
        for(int n=0; n<this.l.size(); n++ ){
            if(c<l.get(n)) return n;
        }
        return -1;
    }

    public int getType(int l){
        return c[l];
    }
    
    public void setType(int n, int t) {
        if(t==PlainText.CHORDS_OVER_TEXT){
            c[n]=PlainText.CHORDS;
            if(n<l.size()-1) {
                c[n+1]=PlainText.TEXT;
            }
        } else c[n]=t;
    }
}
