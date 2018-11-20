package cpt.view.editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import cpt.model.ModelSong;
import cpt.view.browser.options.color.BrowserOptionsColorDlg;
import cpt.view.odt.OdtOptionsColorDlg;
import view.UdoMvcView;

public class Editor extends JPanel implements TextUpdateListener {
	private static final long serialVersionUID = 1L;

	static String oChordColor,oChordColorTab,oChordColorChorus,oChordColorChorusTab;
	static String oChordFont;
	
	private UdoMvcView view;
	private ModelSong item;
	
	private cpt.view.editor.PlainView plain;
	private cpt.view.editor.ChordProView chordPro;
	private cpt.view.editor.FormattedView formatted;
	
	private boolean opening=true;

	public Editor(UdoMvcView view, ModelSong item) {
		super();
		this.view=view;
		this.item=item;

		setLayout(new BorderLayout());

        plain = new PlainView();
        chordPro = new ChordProView();
//        chordPro.addTextUpdateListener(this);
        formatted = new FormattedView();
//        formatted.addTextUpdateListener(this);
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, plain, chordPro);
        sp.setResizeWeight(0.5);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, formatted);
        sp2.setResizeWeight(0.5);
        add(sp2);
        
        plain.formatFromChordPro(item.allText);
        plain.addTextUpdateListener(this);
    }

	@Override
	public void textUpdated(TextUpdateEvent evt) {
		if(evt.source==plain) {
	        try {
	        	String text=plain.toChordPro();
	            final Point orig = chordPro.getScrollPoint();
	            chordPro.setText(text);
                item.setText(text);
	    		SwingUtilities.invokeLater(new Runnable(){

	    			@Override
	    			public void run() {
	    				chordPro.setScrollPoint(orig);
	    			}
	    		});
	            formatted.setText(item);
	            if(!opening){
	            	item.setModified(true);
	            /*
	            	changed=true;
			        setTitle(TITLE+" - "+oldpath+" *");
			        */
	            }
	            opening=false; 
	        } catch(Throwable e) {
	        	e.printStackTrace();
	        }
		}
	}
	
	public String getPlainText(){
		return plain.getText();
	}

	public static void setOptions(Properties props) {
		oChordColor = props.get(EditorOptionsColorDlg.OPT_CHORD_COLOR).toString();
		HashMap<String,String> style=new HashMap<String,String>();
		style.put(""+Font.BOLD,"bold");
		style.put(""+Font.PLAIN,"normal");
		style.put(""+Font.ITALIC,"italic");
		style.put(""+(Font.BOLD+Font.ITALIC),"bold italic");
		String f[]=((String) props.get(EditorOptionsColorDlg.OPT_CHORD_FONT)).split(",");
		oChordFont = style.get(f[1])+" "+f[2]+"pt "+ f[0];
		oChordColorTab = props.get(EditorOptionsColorDlg.OPT_CHORD_TAB_COLOR).toString();
		oChordColorChorus = props.get(EditorOptionsColorDlg.OPT_CHORD_CHORUS_COLOR).toString();
		oChordColorChorusTab = props.get(EditorOptionsColorDlg.OPT_CHORD_CHORUS_TAB_COLOR).toString();
	}

}
