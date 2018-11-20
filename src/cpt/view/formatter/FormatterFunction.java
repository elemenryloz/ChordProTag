package cpt.view.formatter;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Stack;

import cpt.model.ModelSong;

public class FormatterFunction extends FormatToken {

	public final static String[] FMT_FUNCTIONS = {
		"$add(n,n)",
		"$left(string,length)", 
		"$pos(needle,haystack)",
		"$right(string,length)", 
		"$substr(string,start[,length})",
		"$subtract(n,m)",
		"$time()",
		"$word(string,n)",
		"$replace(string,pattern,value)",
	}; 
	
	private String name;
	private Stack<Formatter> stack;
	
	public FormatterFunction(String name){
		this.name=name;
		stack = new Stack<Formatter>();
	}
	
	public void addParm(){
		stack.add(new Formatter());
	}
	
	public void addData(FormatToken token){
		if(stack.size()==0) addParm();
		stack.peek().addToken(token);
	}
	
	@Override
	public String toString(ModelSong item) {
	    Method m;
	    int s = stack.size();
	    if (s>0 && stack.peek().getTokenCount()==0) s--;;
	    Class<?> o[] = new Class<?>[s];
	    Object p[] = new String[s];
	    for(int i=0; i<s; i++){
	    	o[i]="".getClass();
	    	p[i]=stack.get(i).toString(item);
	    }
	    try {
	    	m = getClass().getMethod(name, o);
	    } catch (Exception e) {
	    	return "no method "+name;
	    }
		try {
			return (String) m.invoke(this, p);
		} catch (Exception e) {
	    	return "error in "+name;
		}
	}
	
	public String word(String s, String t) { return s.replaceAll("[ ]+"," ").split(" ")[new Integer(t)-1]; }
	public String time() { return ""+Calendar.getInstance().getTime(); }
	public String left(String s, String t) { return s.substring(0,new Integer(t)); }
	public String add(String s, String t) { return ""+(new Integer(s)+new Integer(t)); }
	public String subtract(String s, String t) { return ""+(new Integer(s)-new Integer(t)); }
	public String pos(String s, String t) { return ""+(1+t.indexOf(s)); }
	public String right(String s, String t) { return s.substring(s.length()-new Integer(t)); }
	public String substr(String s, String t) { return s.substring(new Integer(t)-1); }
	public String substr(String s, String t, String v) { int tt=new Integer(t); return s.substring(tt-1,new Integer(v)+tt-1); }
	public String replace(String s, String t, String v) { return s.replaceAll(t,v); }
}