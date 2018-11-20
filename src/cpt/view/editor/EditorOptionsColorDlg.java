package cpt.view.editor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import view.filtertree.FilterTree;
import view.font.UdoMvcViewFontChooser;
import view.options.UdoMvcViewOptionsDlgItem;


public class EditorOptionsColorDlg extends UdoMvcViewOptionsDlgItem {

	static final String OPT_CHORD_COLOR = "editor.chord.color";
	static final String OPT_CHORD_FONT = "editor.chord.font";
	static final String OPT_CHORD_TAB_COLOR = "editor.chord.tab.color";
	static final String OPT_CHORD_CHORUS_COLOR = "editor.chord.chorus.color";
	static final String OPT_CHORD_CHORUS_TAB_COLOR = "editor.chord.chorus.tab.color";
	
	JTextField tf;
	private FilterTree ft;
	private JButton editColor;
	private ColorItem selectedEntry;
	private JButton editFont;
	
	private JColorChooser colorSample=new JColorChooser();
	private UdoMvcViewFontChooser fontSample;
	
	
	public EditorOptionsColorDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){

		JPanel dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
		ColorItem r =  new ColorItem("root",null,null);
		ColorItem n =  new ColorItem("Chords",null,null);
		ColorItem n1 =  new ColorItem("Title",OPT_CHORD_COLOR,OPT_CHORD_FONT);
		n.add(n1);
		n1 =  new ColorItem("Title in Tab",OPT_CHORD_TAB_COLOR,null);
		n.add(n1);
		n1 =  new ColorItem("Title in Chorus",OPT_CHORD_CHORUS_COLOR,null);
		n.add(n1);
		n1 =  new ColorItem("Title in Tab in Chorus",OPT_CHORD_CHORUS_TAB_COLOR,null);
		n.add(n1);
		r.add(n);
		ft = new FilterTree(r);
		ft.getTree().addTreeSelectionListener(new TL());
	    ft.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    ft.getTree().setRootVisible(false);
		dlg.add(ft,BorderLayout.CENTER);

		colorSample.getSelectionModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				selectedEntry.foregroundColor=colorSample.getColor();
				options.put(selectedEntry.keycolor, "#"+Integer.toHexString(selectedEntry.foregroundColor.getRGB()).substring(2));
				ft.repaint();
			}
			
		});
		colorSample.getSelectionModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				if(selectedEntry.foregroundColor!=null){
					selectedEntry.foregroundColor=colorSample.getColor();
					options.put(selectedEntry.keycolor, "#"+Integer.toHexString(selectedEntry.foregroundColor.getRGB()).substring(2));
					ft.repaint();
				}
			}
			
		});
		
		fontSample = new UdoMvcViewFontChooser(false);
		fontSample.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(selectedEntry.font!=null){
					selectedEntry.font = fontSample.getSelectedFont();
					System.out.println(selectedEntry.font.getStyle());
					options.put(selectedEntry.keyfont, selectedEntry.font.getFamily()+","+selectedEntry.font.getStyle()+","+selectedEntry.font.getSize());
					ft.repaint();
				}
			}
			
		});

		Box pp=Box.createVerticalBox();
		pp.add(colorSample);
		pp.add(fontSample);
		dlg.add(pp,BorderLayout.SOUTH);

		return dlg;
	}
	
	private final class AL implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent ae) {
			if( ae.getSource()==editColor) {
				int s[]=ft.getTree().getSelectionRows();
				Color selectedColor = JColorChooser.showDialog(null, "Pick A Color - "+selectedEntry, selectedEntry.foregroundColor);
				if (selectedColor!=null){
					selectedEntry.foregroundColor=selectedColor;
				}
			} else if( ae.getSource()==editFont) {
				int s[]=ft.getTree().getSelectionRows();
				UdoMvcViewFontChooser fc = new UdoMvcViewFontChooser();
				if(fc.showDialog(null)==UdoMvcViewFontChooser.OK_OPTION){
					selectedEntry.font=fc.getSelectedFont();
				};
			} 
			ft.repaint();
		}
	}

	@Override
	public void setDefaults(Properties prop){
		String s = OPT_CHORD_COLOR; Color c = Color.BLACK;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_CHORD_FONT; String f = "Arial,0,14";
		if (!prop.containsKey(s)) prop.setProperty(s, f); options.put(s, prop.getProperty(s));
		s = OPT_CHORD_TAB_COLOR; c = Color.BLACK;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_CHORD_CHORUS_COLOR; c = Color.decode("#000080");
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_CHORD_CHORUS_TAB_COLOR; c = Color.decode("#000080");
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		saveOptions();
	}
	
    /** A listener shared by the text field and add button. */
    class TL implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
		   	selectedEntry=(ColorItem) ft.getTree().getLastSelectedPathComponent();
		   	colorSample.setColor(selectedEntry.foregroundColor);
		   	if(selectedEntry.font!=null) fontSample.setSelectedFont(selectedEntry.font);
		   	fontSample.setVisible(selectedEntry.font!=null);
		}	
    }
}
