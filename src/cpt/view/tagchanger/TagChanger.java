package cpt.view.tagchanger;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import parser.UdoParser;
import parser.UdoParserChoice;
import parser.UdoParserList;
import parser.UdoParserListener;
import parser.UdoParserRule;
import parser.UdoParserTerminal;
import cpt.model.ModelSong;
import cpt.view.View;
import cpt.view.browser.BrowserTable;
import cpt.view.formatter.FormatterFunction;
import cpt.view.formatter.FormatTag;
import cpt.view.formatter.FormatText;
import cpt.view.formatter.FormatToken;
import cpt.view.formatter.Formatter;

public class TagChanger extends JDialog {
	private static final long serialVersionUID = 1L;

	static Stack<FormatterFunction> stack = new Stack<FormatterFunction>();
	static Formatter tree; 
	final static String[] FMT_FUNCTIONS = {
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
	

	
	static UdoParser parser=new UdoParser();
	private UdoParserList tmplRoot=new UdoParserList();
	private UdoParserList fmtRoot=new UdoParserList();

    private JLabel lTitle=new JLabel("<html><b>Select source format string and parsing template</b></html>");
    private JLabel lFormat=new JLabel("Source format string");
    private JTextField eFmt=new JTextField(30);
    private JLabel lTemplate=new JLabel("Parsing template");
    private JTextField eTemplate=new JTextField(30);
    private JTable tPreview=new JTable(){
    	@Override
    	public String getToolTipText(MouseEvent e){
    		Point p = e.getPoint();
    		int row = rowAtPoint(p);
    		int mr=bptable.getRowSorter().convertRowIndexToModel(items[row]);
    		ModelSong item = bptable.getModel().getLib().get(mr);
    		String tip = "<html><b>filename: </b>"+item.meta.get("_file_name")+"<br><b>source string: </b>"+item.tmpstr+"</html>";
    		return tip;
    	}
    };
    private	DefaultTableModel model=new DefaultTableModel();
    private int[] items;
    private JButton bOK;
	private View view;
	
	private Vector<String> anchors = new Vector<String>();
	private Vector<String> vars = new Vector<String>();

    private BrowserTable bptable;

	public TagChanger(View view, String title){
		super();
		this.view=view;
		

		bptable = view.getBrowserTable();

		bptable.editingCanceled(null);
		
		this.setTitle(title);
        this.setModal(true);
        this.setLayout(new GridBagLayout());
        
        int a=0;
        int b=0;
        int d=3;
        int e=d+4;
        GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=0;
		c.insets=new Insets(5,5,5,5);
		c.gridx=a;
		c.gridy=b;
		c.gridheight=1;
		c.gridwidth=e;
		lTitle.setOpaque(true);
		lTitle.setBackground(Color.WHITE);
        this.add(lTitle,c);
        

        Box box = Box.createVerticalBox();
        box.add(lFormat);
        JPanel p=new JPanel();
        
        
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenu menu = new JMenu("tags");
		popupMenu.add(menu);
		JMenu menui = new JMenu("file info");
		menu.add(menui);
		items=bptable.getSelectedRows();
		String[] usedKeys = bptable.getModel().getUsedKeys(items);
		for(final String s: usedKeys){
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					int pos = eFmt.getCaretPosition();
		            try {
		            	if(eFmt.getSelectedText()!=null) {
		            		eFmt.getDocument().remove(eFmt.getSelectionStart(), eFmt.getSelectionEnd()-eFmt.getSelectionStart()+1);
		            	}
		                eFmt.getDocument().insertString(pos, "%"+s+"%", null);
		            } catch(Exception ex) {
		            }
		            eFmt.requestFocus();
		            parse0();
				}		    	
			});
			if(s.startsWith("_")) menui.add(menuItem);
			else menu.add(menuItem);
		}
		menu = new JMenu("functions");
		popupMenu.add(menu);
		for(final String s: FMT_FUNCTIONS){
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					int st = s.indexOf("(");
					int en = s.indexOf(")");
					int pos = eFmt.getCaretPosition();
		            try {
		            	if(eFmt.getSelectedText()!=null) {
		            		eFmt.getDocument().remove(eFmt.getSelectionStart(), eFmt.getSelectionEnd()-eFmt.getSelectionStart()+1);
		            	}
		                eFmt.getDocument().insertString(pos, s, null);
		            } catch(Exception ex) {
		            }
		            eFmt.requestFocus();
		            eFmt.setSelectionStart(pos+st+1);
		            eFmt.setSelectionEnd(pos+en);
		            
				}		    	
			});
			menu.add(menuItem);
		}
        
        final JButton bFmt=new JButton(">");
        bFmt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
		          popupMenu.show(bFmt, bFmt.getHeight(), bFmt.getWidth()/2);
			}
		});
        bFmt.setMaximumSize(new Dimension(Integer.MAX_VALUE, bFmt.getPreferredSize().height));
        p.setLayout(new BorderLayout());
        p.add(eFmt,BorderLayout.CENTER);
        p.add(bFmt,BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
        eFmt.setMaximumSize(new Dimension(Integer.MAX_VALUE, eFmt.getPreferredSize().height));
        eFmt.addKeyListener(new FmtEntryListener());
        box.add(p);

        box.add(lTemplate);
        eTemplate.setMaximumSize(new Dimension(Integer.MAX_VALUE, eTemplate.getPreferredSize().height));
        eTemplate.addKeyListener(new FmtEntryListener());
        box.add(eTemplate);
    	box.add(Box.createVerticalGlue());  
    	tPreview.setModel(model);
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,box,new JScrollPane(tPreview));
		c.gridx=a;
		c.gridy=++b;
		c.gridheight=1;
		c.gridwidth=e;		
        this.add(sp,c);
        
		c.gridx=a;
		c.gridy=++b;
		c.gridheight=1;
		c.gridwidth=e;		
        this.add(new JSeparator(JSeparator.HORIZONTAL),c);

        b++;
        c.weightx=1;
        for(int n=d; n<e-1; n++) {
			c.gridx=++a;
			c.gridy=b;
			c.gridheight=1;
			c.gridwidth=1;		
	        this.add(new JLabel(""),c);
        }
        bOK=new JButton("OK");
        bOK.setEnabled(false);
        bOK.setMnemonic(KeyEvent.VK_O);
        bOK.addActionListener(new OKButtonListener());
        c.weightx=0;
		c.gridx=++a;
		c.gridy=b;
		c.gridheight=1;
		c.gridwidth=1;		
        this.add(bOK,c);
        JButton button=new JButton("Cancel");
        button.setMnemonic(KeyEvent.VK_ESCAPE);
        button.addActionListener(new CancelButtonListener());
		c.gridx=++a;
		c.gridy=b;
		c.gridheight=1;
		c.gridwidth=1;		
        this.add(button,c);
        button=new JButton("Help");
        button.setMnemonic(KeyEvent.VK_F1);
        button.addActionListener(new HelpButtonListener());
		c.gridx=++a;
		c.gridy=b;
		c.gridheight=1;
		c.gridwidth=1;		
        this.add(button,c);		
        
        this.pack();
//        setLocationRelativeTo(Control.frame);

        makeFmtRoot();
        makeTmplRoot();
	}
	
	private void makeFmtRoot(){
		UdoParserListener listener = new UdoParserListener(){

			
			@Override
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
						tree.addToken(t);
					}
				} else  if(ruleName.equals("tag")) {
					if(stack.size()>0) { 
						stack.peek().addData(new FormatTag(text.replaceAll("^%|%$", "")));						
					} else  {
						tree.addToken(new FormatTag(text.replaceAll("^%|%$", "")));
					}
				} else {
					text=text.replaceAll("&comma;",",");
					text=text.replaceAll("&cp;",")");
					text=text.replaceAll("&nl;","\n");
					if(stack.size()>0) {
						stack.peek().addData(new FormatText(text));
					} else  {
						tree.addToken(new FormatText(text));
					}
				}
				
				
			}
		};

		String fn = "";
		for(int i=0; i<FMT_FUNCTIONS.length; i++){
			int en = FMT_FUNCTIONS[i].indexOf("(");
			if(!fn.equals("")) fn += "|";
			fn += FMT_FUNCTIONS[i].substring(1,en);
		}
		
		UdoParserTerminal text2 = 
				new UdoParserTerminal("text","[^%$,)]+",UdoParserTerminal.REGEX)
					.addParserListener(listener);
		UdoParserTerminal text = 
				new UdoParserTerminal("text","[^%$]+",UdoParserTerminal.REGEX)
					.addParserListener(listener);
		UdoParserTerminal tagx = 
				new UdoParserTerminal("tag","[%][^%]+[%]",UdoParserTerminal.REGEX)
					.addParserListener(listener);

		UdoParserList func = 
				new UdoParserList("func");
		
		UdoParserChoice funcparm = 
				new UdoParserChoice("token2")
					.addToken(func)
					.addToken(tagx)
					.addToken(text2);
		func = func
				.addString("$")
				.addRegex(fn,"funcname",listener)
				.addString("(")
				.addToken(new UdoParserList("funcparms") 
								.addToken(funcparm,UdoParserRule.Repeat.ZEROORONE)
								.addToken(new UdoParserList("funcparmlist")
												.addString(",","newparm",listener)
												.addToken(funcparm)
										,UdoParserRule.Repeat.ZEROORMORE)
							)
				.addString(")","funcend",listener);
							
		fmtRoot = new UdoParserList("root")
			.addToken(new UdoParserChoice("token")
						.addToken(func)
						.addToken(tagx)
						.addToken(text)
					,UdoParserRule.Repeat.ZEROORMORE)
			.addRegex(".+",UdoParserRule.Repeat.ZEROORONE,"text",listener);


	}	
	
	private void makeTmplRoot(){
		UdoParserListener listener = new UdoParserListener(){

			
			@Override
			public void callBack(UdoParserRule rule, String text) {
				
				String ruleName=rule.ruleName;

				if(ruleName.equals("start")) {
					anchors.clear();
					vars.clear();
					vars.add("");
				} else if(ruleName.equals("end")) {
					anchors.add("$");
				} else if(ruleName.equals("num_anchor")) {
					if(text.matches("^\\d.*")) text="="+text;
					anchors.add(text.trim());
					vars.add("");
				} else if(ruleName.equals("lit")) {
					anchors.add("\""+text);
					vars.add("");
				} else if(ruleName.equals("var_list")) {
					vars.set(vars.size()-1,text.trim());
					String vars[]=text.split(" +");
					for(String v: vars){
						String c=v.replace("%","");
						if(!c.equals(".") && model.findColumn(c)==-1) model.addColumn(c);
					}
				}
			}
		};

		UdoParserTerminal lit = new UdoParserTerminal("lit","[^%(-+=]+ +",UdoParserTerminal.REGEX)
		.addParserListener(listener);

		UdoParserTerminal lit2 = new UdoParserTerminal("lit","\"[^\"]*\" +",UdoParserTerminal.REGEX)
		.addParserListener(listener);

		UdoParserTerminal var = new UdoParserTerminal("var","([%][^%]+[%]|\\.) +",UdoParserTerminal.REGEX);

		UdoParserTerminal num_anchor = new UdoParserTerminal("num_anchor","[+-=]?[0-9]+ +",UdoParserTerminal.REGEX)
			.addParserListener(listener);

		UdoParserChoice anchor = new UdoParserChoice("anchor")
			.addToken(num_anchor)
			.addToken(lit)
			.addToken(lit2);
			
		UdoParserList var_list = new UdoParserList("var_list")
			.addToken(var,UdoParserRule.Repeat.ONEORMORE)
			.addParserListener(listener);
		
		UdoParserChoice pattern = new UdoParserChoice("pattern")
			.addToken(var_list)
			.addToken(anchor);
		
		tmplRoot = new UdoParserList("root")
			.addRegex("^","start",listener)
			.addToken(pattern,UdoParserRule.Repeat.ZEROORMORE)
			.addRegex("$","end",listener);
	}

	protected void parse0(){
		
    	model.setRowCount(0);
		BrowserTable bptable = view.getBrowserTable();
		
		// build source format tree
       	stack.clear();
   		tree = new Formatter();
       	String fmt = eFmt.getText();
   		int rc = parser.parse(fmt, fmtRoot);
		if(rc!=0) {
			eFmt.requestFocus();
			eFmt.setBackground(Color.RED);
			bOK.setEnabled(false);
			return;
		}
		
		// get parsing template
		String tmpl = eTemplate.getText().trim()+" ";
		if(tmpl.equals(" ")) return;
		rc=parser.parse(tmpl, tmplRoot);
		if(rc!=0){
			eTemplate.requestFocus();
			eTemplate.setBackground(Color.RED);
			bOK.setEnabled(false);
			return;
		}
		
		eTemplate.setBackground(Color.WHITE);
		bOK.setEnabled(true);
		
    	for (final int row: items){
    		int mr=bptable.getRowSorter().convertRowIndexToModel(row);
    		ModelSong item = bptable.getModel().getLib().get(mr);
    		String newVal = tree.toString(item);
    		item.tmpstr=newVal;
			parse(newVal);
    	}
	}

	private void parse(String text){
//		System.out.println("anchors=" + anchors);
//		System.out.println("vars=" + vars);

		HashMap<String,String> map = new HashMap<String,String>();
		int endPos = text.length();
		int lastPos = 0;
		for(int a=0; a<anchors.size(); a++){
			String a2=anchors.get(a);
			int nextPos=0,savePos=-1,dataLen=0;
			if( a2.startsWith("+")){
				nextPos=lastPos+Integer.parseInt(a2);
				if(nextPos>endPos) nextPos=endPos;
				if(nextPos==lastPos) {
					savePos=nextPos;
    				nextPos=endPos;
				} 
				dataLen=nextPos-lastPos;
			} else if( a2.startsWith("-")){
				nextPos=lastPos+Integer.parseInt(a2);
				if(nextPos<0) nextPos=0;
				savePos=nextPos;
				nextPos=endPos;
				dataLen=nextPos-lastPos;
			} else if( a2.startsWith("=")){
				nextPos=Integer.parseInt(a2.substring(1))-1;
				if(nextPos>endPos) nextPos=endPos;
				if(nextPos<0) nextPos=0;
				if(nextPos<=lastPos) {
					savePos=nextPos;
    				nextPos=endPos;
				}
				dataLen=nextPos-lastPos;
			} else if( a2.startsWith("\"")){
				String find = a2.substring(1,a2.length()-1).replaceAll("^\"|\"$", "");
				nextPos=text.indexOf(find, lastPos);
				if (nextPos==-1){
					nextPos=endPos;
    				dataLen=nextPos-lastPos;
				} else {
    				dataLen=nextPos-lastPos;
					nextPos+=find.length();
				}
			} else if( a2.startsWith("$")){
				nextPos=endPos;
				dataLen=nextPos-lastPos;
			}
			
			String data = text.substring(lastPos,lastPos+dataLen);
			String vv[] = vars.get(a).split(" +");
			for (int v=0; v<vv.length; v++ ){
				if (vv[v].equals("")) continue;
				String token;
				if (v==vv.length-1) token=data;
				else {
					data=data.replaceAll("^ +", "");
					int p=data.indexOf(" ");
					if(p==-1){
						token=data;
						data="";
					} else {
						token = data.substring(0,p);
						data=data.substring(p+1);
					}
				}
				map.put(vv[v].replace("%",""), token);
			}
			if(savePos==-1) lastPos=nextPos;
			else lastPos=savePos;
		}
		Vector<String> o = new Vector<String>();
		for(int c=0; c<model.getColumnCount(); c++){
			o.add(map.get(model.getColumnName(c)));
		}
		model.addRow( o.toArray());
	}

	
	class TargetActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
		        	model.setRowCount(0);
		        	parse0();
				}
			});
		}
    }

	class FmtEntryListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
		        	model.setRowCount(0);
		        	model.setColumnCount(0);
		        	parse0();
				}
				
			});
		}

    }

	class OKButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	if(model.findColumn("_file_name")!=-1){
        		int rc = JOptionPane.showConfirmDialog(null, "rename "+items.length+ " files?","Converter",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
        		if(rc!=JOptionPane.OK_OPTION) return;
        	}
	        SwingUtilities.invokeLater(new Runnable() {

				@Override
	            public void run() {
					int tr=0;
	            	for (int row: items){
	            		Vector<String> rowData = (Vector<String>) model.getDataVector().get(tr);
	            		int mr=bptable.getRowSorter().convertRowIndexToModel(row);
	            		ModelSong item = bptable.getModel().getLib().get(mr);
	            		for(int c=0; c<model.getColumnCount(); c++){
	            			String col=model.getColumnName(c);
	            			String val=rowData.get(c);
	            			item.meta.put(col,val);
	            			System.out.println(col+"="+val);
	            		}
	                    item.setModified(true);
	                    bptable.getModel().fireTableRowsUpdated(mr, mr);
	                    tr++;
	            	}
	            }
	        });		        
			
        }
    }
 
    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	setVisible(false);
        }
    }
    
    class HelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        }
    }
 
}
