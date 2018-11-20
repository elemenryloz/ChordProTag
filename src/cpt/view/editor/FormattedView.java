package cpt.view.editor;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.odftoolkit.odfdom.dom.attribute.fo.FoMinWidthAttribute;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;

import cpt.model.Chord;
import cpt.model.ModelSong;
import cpt.model.ModelSongException;
import cpt.model.ModelToken;


public class FormattedView extends TextView {

	private static final long serialVersionUID = 1L;
	static MessageFormat head = new MessageFormat("");
    static MessageFormat foot = new MessageFormat("");

    JEditorPane ed1;
	String incomment, inchorus, intab;
	private String chordRow;
	private String lyricsRow;
	
	public FormattedView() {
		super();
        ed1=new JEditorPane();
        ed1.setEditable(false);
        
     // add some styles to the html
        HTMLEditorKit kit = new HTMLEditorKit();
        ed1.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        
        
        styleSheet.addRule(".chords { font-weight: bold;"
        		+ "color: "+Editor.oChordColor+"; font:"+Editor.oChordFont+";"
        		+ "}");
        styleSheet.addRule(".chords-tab { font-weight: bold;"
        		+ "color: "+Editor.oChordColorTab+"; "
        		+ "font-family: \"Courier New\", Courier, monospace; font-style: normal; margin-top: 0;	margin-bottom: 0;"
        		+"}");
        styleSheet.addRule(".chords-chorus { font-weight: bold;"
        		+ "color: "+Editor.oChordColorChorus+"; background-color: #ffffdd; font-style: normal;"
        		+"}");
        styleSheet.addRule(".chords-chorus-tab { font-weight: bold;"
        		+ "color: "+Editor.oChordColorChorusTab+"; background-color: #ffffdd; font-style: normal;"
        		+ "font-family: \"Courier New\", Courier, monospace; font-style: normal; margin-top: 0;	margin-bottom: 0;"
        		+"}");
        
        
        styleSheet.addRule(".lyrics {"
        		+ "color: black;"
        		+"}");
        styleSheet.addRule(".lyrics-tab {"
        		+ "color: black;"
        		+ "font-family: \"Courier New\", Courier, monospace; font-style: normal; margin-top: 0;	margin-bottom: 0;"
        		+"}");
        styleSheet.addRule(".lyrics-chorus {"
        		+ "color: black;"
        		+ "color: navy; background-color: #ffffdd; font-style: normal;"
        		+"}");
        styleSheet.addRule(".lyrics-chorus-tab {"
        		+ "color: navy; background-color: #ffffdd; font-style: normal;"
        		+ "font-family: \"Courier New\", Courier, monospace; font-style: normal; margin-top: 0;	margin-bottom: 0;"
        		+"}");
        
        
        styleSheet.addRule(".comment { background-color: #eeeeee;"
        		+ "}");
        styleSheet.addRule(".comment-italic { background-color: #eeeeee;"
        		+ "font-style: italic;"
        		+ "}");
        styleSheet.addRule(".comment-box { background-color: #eeeeee;"
        		+ "border: 1px solid;"
        		+ "}");
        
		setLayout(new BorderLayout());
		add(new JScrollPane(ed1),BorderLayout.CENTER);

	}
	
	public void setText(ModelSong song) {
		
	    Vector<ModelToken> tokens=null;
		try {
			tokens = song.parseText();
		} catch (ModelSongException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			tokens = e.getTokens();
		}
		int crds=0;
		
		incomment = "";
		inchorus = "";
		intab = "";
	    
   		String html="<html><head><title>Simple Page</title></head><body>";

		for ( int i = 0; i < tokens.size(); i++) {
			ModelToken token = tokens.get(i);
			switch(token.name) {
				case "C":
				case "CI":
				case "CB":
					incomment = "comment";
					if(token.name.equals("CI")) incomment += "-italic";
					else if(token.name.equals("CB")) incomment += "-box";
					html+="<p><table class=\"" + incomment + "\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr>";
				    break;
				case "/C":
					incomment = "";
					html+="<td style=\"width: 100%; visibility: hidden;\"></td></tr></table></p>";
					break;
				case "SOC":
					inchorus = "-chorus";
				    break;
				case "EOC":
					inchorus = "";
				    break;
				case "SOT":
					intab = "-tab";
					break;
				case "EOT":
					intab = "";
					break;
				case "NL":
					chordRow = "";
					crds=0;
					lyricsRow = "";
					break;
				case "EOL":
					html+="<table cellspacing=\"0\" cellpadding=\"0\">"
					+ (crds>0 ? "<tr>" + chordRow + "</tr>" : "")
					+ "<tr>" + lyricsRow + "</tr></table>";
					break;
				case "CHORD":
					String crd = token.value;
					if(incomment.length()>0){
						html+="<td class=\"chords\" style=\"padding-left: 0; padding-right: 0;\">" + crd.trim() + "</td>";
					} else  {
						if(crd.length()>0) crds++; 
						chordRow+="<td style=\"padding-right: 4pt;\" class=\"chords" + inchorus + intab + "\">" + crd.trim() + "</td>";
					}
					break;
				case "TEXT":
					String txt=token.value;
					if(incomment.length()>0){
						if (!txt.equals(" ")) html+="<td  style=\"padding-left: 0; padding-right: 0;\">"+txt.replaceAll(" ", "&nbsp;") +"</td>";
					} else {
						lyricsRow+="<td class=\"lyrics" + inchorus + intab + "\">" + txt.replaceAll(" ", "&nbsp;")  + "</td>";
					}
					break;
				default:
			}
		}		
        html+="</body></html>";
//        System.out.println(html);
        final Rectangle orig = ed1.getVisibleRect();

		ed1.setText(html);
		ed1.setContentType("text/html");
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				ed1.scrollRectToVisible(orig);
				
				/*
				 PrinterJob pj = PrinterJob.getPrinterJob();
			        if(pj.printDialog()) {
			            pj.setPrintable(ed1.getPrintable(head, foot));
			            try {
							pj.print();
						} catch (PrinterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            System.out.println("done .............. ");
			        }
			        */
			}
		});
	}
	

}
