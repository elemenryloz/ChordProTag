package cpt.view.editor;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ChordProView extends TextView {

	private static final long serialVersionUID = 1L;

    public JTextArea ta;
    JScrollPane scroll;

	private ChordproText chopro=new ChordproText("");
	
	public ChordProView() {
		super();
		ta=new JTextArea();
		ta.setLineWrap(false);
		ta.setEditable(false);
		ta.setFont(new Font("monospaced",Font.PLAIN,14));
		setLayout(new BorderLayout());
		scroll=new JScrollPane(ta);
		add(scroll,BorderLayout.CENTER);
	}
	
	public String getText() {
		return ta.getText();
	}

	public void setText(String text) {
		ta.setText(text);
		chopro=new ChordproText(text);
	}

	public Point getScrollPoint() {
		return scroll.getViewport().getViewPosition();
	}

	public void setScrollPoint(Point orig) {
		scroll.getViewport().setViewPosition(orig);
	}

	public String getTitle() {
		return chopro.getTag("title");
	}

	public String getArtist() {
		return chopro.getTag("artist");
	}

}
