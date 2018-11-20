package cpt.view.editor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class PlainView extends TextView implements ActionListener, CaretListener, DocumentListener {

	private static final long serialVersionUID = 1L;

	PlainText text;
    Vector<JRadioButton> r = new Vector<JRadioButton>();
    int dot=0;
    JTextArea ta;
    JTextField tax;
    Timer timer;
    JScrollPane scroll;

	private JButton buttonUp;
	private JButton buttonDown;
	private JButton buttonSearch;

	private JButton buttonFind;
	private JButton buttonRepAndFind;
	private JButton buttonReplace;
	private JButton buttonRepAll;

	private JTextField tfind;

	private JTextField treplace;
	
	public PlainView() {
		super();
		
		ta=new JTextArea();
		ta.setLineWrap(false);
		ta.setFont(new Font("monospaced",Font.PLAIN,14));
		ta.setWrapStyleWord(true);
		ta.setEditable(true);
//		ta.getDocument().addDocumentListener(this);
//		ta.addCaretListener(this);
		ta.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke("ctrl shift O"));
//		ta.getDocument().addUndoableEditListener(new UndoListener());
		
		
		Document doc = ta.getDocument();
		if (doc instanceof AbstractDocument) {
		    ((AbstractDocument) doc).setDocumentFilter(new DocumentFilter(){
		    	
		    	public void insertString(FilterBypass fb, int offs, String str, AttributeSet attr) 
		    			throws BadLocationException {
		    		super.insertString(fb, offs, str.replaceAll("\\t", "        "), attr);
		    	};
		    	
		    	public void replace(FilterBypass fb, int offs, int len, String str, AttributeSet attr) 
		    			throws BadLocationException {
		    		super.replace(fb, offs, len, str.replaceAll("\\t", "        "), attr);
		    	};
		    });
		} 
		

		setLayout(new BorderLayout());
		scroll=new JScrollPane(ta);
		add(scroll,BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        ButtonGroup gruppe = new ButtonGroup();
        String s[] = {"empty", "chords only", "chords over text", "text", "tag", "comment" };
        for(int n=0; n<s.length; n++) {
            JRadioButton b = new JRadioButton(s[n]);
            b.setActionCommand(String.valueOf(n));
            b.addActionListener(this);
            r.add(b);
            gruppe.add(b);
            panel.add(b);
        }
        
        tax = new JTextField();
        tax.setText("A B C D E F G NC");
        tax.setMaximumSize(new Dimension(Integer.MAX_VALUE, tax.getPreferredSize().height));
        tax.addActionListener(this);
    	Box box = Box.createVerticalBox();
        box.add(new JLabel(" "));
        box.add(new JLabel(" List of strings to be "));
        box.add(new JLabel(" recognized as chord starts: "));
    	box.add(tax);
    	buttonUp = new JButton("Transpose up");
    	buttonUp .setAlignmentX(LEFT_ALIGNMENT);
    	buttonUp .addActionListener(this);
    	box.add(buttonUp );
    	buttonDown = new JButton("Transpose down");
    	buttonDown.setAlignmentX(LEFT_ALIGNMENT);
    	buttonDown.addActionListener(this);
    	box.add(buttonDown);
    	buttonSearch = new JButton("Search/Replace");
    	buttonSearch.setAlignmentX(LEFT_ALIGNMENT);
    	buttonSearch.addActionListener(this);
    	box.add(buttonSearch);
    	box.add(Box.createVerticalGlue());
        panel.add(box);
        
		add(panel,BorderLayout.WEST);
		
		final PlainView self=this;
		
    	timer = new Timer(3000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
		    	SwingUtilities.invokeLater(new Updater(self));
			}
    		
    	});
	}
	
	public String getText() {
		return ta.getText();
	}

	public void setText(String text) {
		ta.getDocument().removeDocumentListener(this);
		text=text.replaceAll("\\t", "        ");
		text=text.replaceAll("[^\\P{Cntrl}\\n]", "?");
		text=text.replaceAll("(^|\\n)([\\s]*)[\\n]", "$1{c}\n");
		ta.setText(text);
		ta.setCaretPosition(0);
		ta.getDocument().addDocumentListener(this);
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				scroll.getViewport().setViewPosition(new Point(0,0));
			}
		});
    	SwingUtilities.invokeLater(new Updater(this));

	}
	
    public void formatFromChordPro(String text) {
        String v[] = text.split("\n");
        String t = "";
        for(int n=0; n<v.length; n++){
        	if(v[n].startsWith("#")) {
        		t+=v[n]+"\n";
        	} else if(v[n].startsWith("{")) {
        		t+=v[n]+"\n";
        	} else {
        		String tc = "";
        		String tt = "";
        		String[] c = v[n].split("\\[");
        		for ( int i=0; i<c.length; i++) {
        			if (i == 0 && c[i].equals("")) continue;
        			String[] cc = c[i].split("\\]");
        			String crd = "";
        			String txt = "";
        			if (cc.length==2) {
        				crd = cc[0];
        				txt = cc[1];
        			} else if (c[i].endsWith("]")) {
        				crd = cc[0];
        			} else {
        				txt = cc[0];
        			}
        			
        			if(crd.length()>0) crd=crd+" ";
                    for(int x=crd.length(); x<txt.length(); x++) crd+=" ";
                    for(int x=txt.length(); x<crd.length(); x++) txt+=" ";
            		tc+=crd;
              		tt+=txt;
        		}        		
              	if(tc.trim().length()>0) t+=tc+"\n";
              	if(tt.trim().length()>0) t+=tt+"\n";
           	} 
        }
        setText(t);
    }
		

    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource()==buttonUp) {
    		transpose(1);
    	} else if(e.getSource()==buttonDown) {
    		transpose(-1);
    	} else if(e.getSource()==buttonSearch) {
    		
			buttonSearch.setEnabled(false);
    		
    		JPanel p = new JPanel();
    		p.setLayout(new GridBagLayout());
    		GridBagConstraints c = new GridBagConstraints();
    		c.anchor=GridBagConstraints.WEST;
    		c.insets=new Insets(5,5,5,5);
    		c.gridx=0;
    		c.gridy=0;
    		JLabel l = new JLabel("Find: ", SwingConstants.LEFT);
    		p.add(l,c);
    		c.gridy=1;
    		l = new JLabel("Replace with: ", SwingConstants.LEFT);
    		p.add(l,c);
    		tfind=new JTextField(30);
    		tfind.addKeyListener(new KeyAdapter(){
    			public void keyReleased(KeyEvent e){
    				super.keyReleased(e);
    				int len=tfind.getText().length();
    				buttonFind.setEnabled(len>0);
    				buttonReplace.setEnabled(false);
    				buttonRepAndFind.setEnabled(false);
    				buttonRepAll.setEnabled(false);
    			}
    		});
    		Border border = BorderFactory.createEtchedBorder();
    		tfind.setBorder(border);
    		c.gridx=1;
    		c.gridy=0;
    		p.add(tfind,c);
    		c.gridy=1;
    		treplace = new JTextField(30);
    		treplace.setBorder(border);
    		treplace.setEditable(true);
    		treplace.setPreferredSize(new Dimension(tfind.getPreferredSize().width,treplace.getPreferredSize().height));
    		p.add(treplace,c);
    		JPanel pp = new JPanel();
    		pp.setLayout(new BorderLayout());
    		pp.add(new JLabel("Enter title and tag:"),BorderLayout.NORTH);
    		pp.add(p,BorderLayout.CENTER);
    		
    		Box box=Box.createHorizontalBox();
        	buttonFind = new JButton("Find");
        	buttonFind.setEnabled(false);
        	buttonFind.addActionListener(this);
        	box.add(buttonFind);
        	buttonRepAndFind = new JButton("Replace/Find");
        	buttonRepAndFind.setEnabled(false);
        	buttonRepAndFind.addActionListener(this);
        	box.add(buttonRepAndFind);
        	buttonReplace = new JButton("Replace");
        	buttonReplace.setEnabled(false);
        	buttonReplace.addActionListener(this);
        	box.add(buttonReplace);
        	buttonRepAll = new JButton("Replace all");
        	buttonRepAll.setEnabled(false);
        	buttonRepAll.addActionListener(this);
        	box.add(buttonRepAll);
    		
    		c.gridx=0;
    		c.gridy=2;
    		c.gridwidth=2;
    		p.add(box,c);
    		
    		JDialog dlg = new JDialog();
    		dlg.setContentPane(p);
    		dlg.pack();
    		dlg.setLocationRelativeTo(this);
    		dlg.setAlwaysOnTop(true);
    		dlg.addWindowListener(new WindowAdapter(){
    			@Override
    			public void windowClosing(WindowEvent arg0) {
    				buttonSearch.setEnabled(true);
    			}
    		});
    		dlg.setVisible(true);
    	} else if(e.getSource()==buttonFind) {
    		doFind(true);
    	} else if(e.getSource()==buttonRepAndFind) {
			doReplace();
    		doFind(true);
    	} else if(e.getSource()==buttonReplace) {
			doReplace();
			buttonReplace.setEnabled(false);
			buttonRepAndFind.setEnabled(false);
			buttonRepAll.setEnabled(false);
    	} else if(e.getSource()==buttonRepAll) {
    		int x=0;
    		do {
    			doReplace();
    			x++;
    		} while (doFind(false));
			JOptionPane.showMessageDialog(this, x+" ocurrences changed");
    	} else if(e.getSource()==tax) {
    		SwingUtilities.invokeLater(new Updater(this));
        } else {
	        int l=text.getLine(dot);
	        int t=Integer.valueOf(e.getActionCommand());
	        text.setType(l, t);
		    text.toChordPro(); 
    	}
    };
	
	private void doReplace() {
		String replace = treplace.getText();
		ta.replaceSelection(replace);
	}

	private boolean doFind(boolean msg) {
		int cpos = ta.getCaretPosition();
		String t = getText().substring(cpos);
		String f = tfind.getText();
		int pos = t.indexOf(f);
		buttonReplace.setEnabled(pos!=-1);
		buttonRepAndFind.setEnabled(pos!=-1);
		buttonRepAll.setEnabled(pos!=-1);
		if(pos!=-1){
			int npos=cpos+pos;
			ta.setCaretPosition(npos);
			ta.setSelectionStart(npos);
			ta.setSelectionEnd(npos+f.length());
			ta.requestFocus();
		} else {
			if(msg) JOptionPane.showMessageDialog(this, "not found!");
		}
		return pos!=-1;
	}

	public void transpose(int tv) {
		
		String cp=text.toChordPro();
		String newText="";
		
		String[] c = cp.split("\\[");
		for ( int i=0; i<c.length; i++) {
			if (i == 0 && c[i].equals("")) continue;
			String[] cc = c[i].split("\\]");
			String crd = null;
			String txt = null;
			if (cc.length==2) {
				crd = cc[0];
				txt = cc[1];
			} else if (c[i].substring(c[i].length() - 1).equals("]")) {
				crd = cc[0];
			} else {
				txt = cc[0];
			}
			if( crd!=null) {
				crd=doTranspose(crd,tv);
				newText+="["+crd+"]";
			}
			if( txt!=null){
				newText+=txt;
			}
		}
		this.formatFromChordPro(newText);
	}
	
	private String doTranspose(String chord, int n) {
		String names = "ABCDEFG";
		if (n == 0)	return chord;
		
		char c0=chord.charAt(0);
		String c1=chord.length()>1?chord.substring(1,2):"";
		String cx=chord.length()>2?chord.substring(2):"";
		if (n > 0) {
			if (c1.equals("b")) {
				chord=c0+cx;
			} else if (c1.equals("#")) {
				int p = names.indexOf(c0) + 1;
				if (p > names.length() - 1) p = 0;
				chord = names.substring(p, p + 1)+cx;
			} else {
				if (c0 == 'E')
					chord="F"+c1+cx;
				else if (c0 == 'B')
					chord="C"+c1+cx;
				else
					chord=""+c0+"#"+c1+cx;
			}
			chord = doTranspose(chord, n - 1);
		} else {
			if (c1.equals("#")) {
				chord=c0+cx;
			} else if (c1.equals("b")) {
				int p = names.indexOf(c0) - 1;
				if (p < 0) p = names.length() - 1;
				chord = names.substring(p, p + 1)+cx;
			} else {
				if (c0 == 'F')
					chord="E"+c1+cx;
				else if (c0 == 'C')
					chord="B"+c1+cx;
				else
					chord=""+c0+"b"+c1+cx;
			}
			chord = doTranspose(chord, n + 1);
		}
		return chord;
	}	    
    
    @Override
    public void caretUpdate(CaretEvent e) {
    	if (text==null) return;
        dot=e.getDot();
        int l=text.getLine(dot);
        if (l==-1) l=0;
        int t=text.getType(l);
        r.get(t).setSelected(true);
    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
    	timer.restart();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
    	timer.restart();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
    	timer.restart();
    }

	public void format() {
		text.toChordPro();
	}


	class Updater implements Runnable {
		
		PlainView plain;
		
		public Updater(PlainView plain){
			this.plain=plain;
		}
		
		@Override
		public void run() {
			
		    plain.text=new PlainText(plain.ta.getText(), plain.getChords());
	        int l=plain.text.getLine(plain.ta.getCaretPosition());
	        if (l==-1) l=0;
	        int t=plain.text.getType(l);
	        plain.r.get(t).setSelected(true);
	        
			plain.fireTextChange();
			
	        plain.timer.stop();
			
		}
	}


	public String[] getChords() {
		return tax.getText().split(" ");
	}
	
	public String toChordPro() {
		if(text==null) return "";
		return text.toChordPro();
	}

	public void defaultFocus() {
		ta.requestFocusInWindow();
	}


}
