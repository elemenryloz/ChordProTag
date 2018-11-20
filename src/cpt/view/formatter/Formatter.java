package cpt.view.formatter;

import java.util.ArrayList;
import java.util.Stack;

import parser.UdoParser;
import parser.UdoParserChoice;
import parser.UdoParserList;
import parser.UdoParserListener;
import parser.UdoParserRule;
import parser.UdoParserTerminal;
import cpt.model.ModelSong;

/**
 * A Formatter to create text from a given library item.
 * <p>
 * These tokens have been parsed from a format description and are stored in the list.
 * 
 * @see  {@link FormatToken}
 * @see  {@link FormatText}
 * @see  {@link FormatTag}
 * @see  {@link FormatFuction}
 *
 */
public class Formatter implements UdoParserListener {

	/** A list of parsed tokens */
	private ArrayList<FormatToken> tokens = new ArrayList<FormatToken>();
	/** A stack for functions in functions */
	private Stack<FormatterFunction> stack = new Stack<FormatterFunction>();
	/** The parser object */	
	private UdoParser parser=new UdoParser();
	/** the oot object of the parsing grammar */
	private UdoParserList root=new UdoParserList();

	/** 
	 * Constructor 
	 * <p>
	 * Fills the parsing grammar.
	 *  
	 */
	public Formatter(){
		String fn = "";
		for(String func: FormatterFunction.FMT_FUNCTIONS){
			int en = func.indexOf("(");
			if(!fn.equals("")) fn += "|";
			fn += func.substring(1,en);
		}
		
		UdoParserTerminal text2 = 
				new UdoParserTerminal("text","[^%$,)]+",UdoParserTerminal.REGEX)
					.addParserListener(this);
		UdoParserTerminal text = 
				new UdoParserTerminal("text","[^%$]+",UdoParserTerminal.REGEX)
					.addParserListener(this);
		UdoParserTerminal tagx = 
				new UdoParserTerminal("tag","[%][^%]+[%]",UdoParserTerminal.REGEX)
					.addParserListener(this);

		UdoParserList func = 
				new UdoParserList("func");
		
		UdoParserChoice funcparm = 
				new UdoParserChoice("token2")
					.addToken(func)
					.addToken(tagx)
					.addToken(text2);
		func = func
				.addString("$")
				.addRegex(fn,"funcname",this)
				.addString("(")
				.addToken(new UdoParserList("funcparms") 
								.addToken(funcparm,UdoParserRule.Repeat.ZEROORONE)
								.addToken(new UdoParserList("funcparmlist")
												.addString(",","newparm",this)
												.addToken(funcparm)
										,UdoParserRule.Repeat.ZEROORMORE)
							)
				.addString(")","funcend",this);
							
		root = new UdoParserList("root")
			.addToken(new UdoParserChoice("token")
						.addToken(func)
						.addToken(tagx)
						.addToken(text)
					,UdoParserRule.Repeat.ZEROORMORE)
			.addRegex(".+",UdoParserRule.Repeat.ZEROORONE,"text",this);		
	}
	
	/**
	 * Callback routing for the parser.
	 */
	public void callBack(UdoParserRule rule, String text) {
		
		String ruleName=rule.ruleName;
		
		if(ruleName.equals("funcname")){
			FormatterFunction f =new FormatterFunction(text.replaceAll("^\\$|\\($", ""));
			stack.add(f);
		} else  if(ruleName.equals("newparm")) {
			stack.peek().addParm();
		} else if(ruleName.equals("funcend")) {
			FormatToken t=stack.pop();
			if(stack.size()>0) {
				stack.peek().addData(t);
			} else {
				tokens.add(t);
			}
		} else  if(ruleName.equals("tag")) {
			if(stack.size()>0) { 
				stack.peek().addData(new FormatTag(text.replaceAll("^%|%$", "")));						
			} else  {
				tokens.add(new FormatTag(text.replaceAll("^%|%$", "")));
			}
		} else {
			text=text.replaceAll("&comma;",",");
			text=text.replaceAll("&cp;",")");
			text=text.replaceAll("&nl;","\n");
			if(stack.size()>0) {
				stack.peek().addData(new FormatText(text));
			} else  {
				tokens.add(new FormatText(text));
			}
		}
	}
	
	/**
	 * Clears the tokens and the stack and invokes the parser.
	 * @param 	text - text to be parsed.
	 * @return 	return code from the parser.
	 */
	public int parse(String text){
		tokens.clear();
		stack.clear();
   		return parser.parse(text, root);

	}
	
	/**
	 * Applies the parsed tokens to a given {@link ModelSong}.
	 * @param 	item - the library item.
	 * @return 	the formatted string.
	 */
	public String toString(ModelSong item) {
		String out="";
		for (FormatToken t : tokens) {
			out+=t.toString(item);
		}
		return out;
	}
/*
	public String[] toArray(LibraryItem item, String tag) {
		int i=0;
		for (FormatToken t : this) {
			if(t.getClass()==FormatTag.class) i++;
		}
		String [] out = new String[i];
		String text = item.meta.get(tag);
		if(text!=null) {
			i=0;
			FormatTag lastTag=null;
			for (FormatToken t : this) {
				if(t.getClass()==FormatTag.class){
					lastTag=(FormatTag)t;
				} else {
					String txt = ((FormatText)t).text;
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
*/

	/**
	 * Adds a token to the list.
	 * @param 	token - token to be added.
	 */
	public void addToken(FormatToken token) {
		tokens.add(token);
	}

	/**
	 * Gets the number of tokens currently in the list.
	 * @return	number of tokens.
	 */
	public int getTokenCount() {
		return tokens.size();
	}

}
