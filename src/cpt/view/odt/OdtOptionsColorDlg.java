package cpt.view.odt;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

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


public class OdtOptionsColorDlg extends UdoMvcViewOptionsDlgItem {

	static final String OPT_CHORD_TITLE_COLOR = "odt.chord.title.color";
	static final String OPT_CHORD_FRET_COLOR = "odt.chord.fret.color";
	
	JTextField tf;
	private FilterTree ft;
	private JButton editColor;
	private ColorItem selectedEntry;
	private JButton editFont;
	
	private JColorChooser colorSample=new JColorChooser();
	
	public OdtOptionsColorDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){

		JPanel dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
		ColorItem r =  new ColorItem("root",null,null);
		ColorItem n =  new ColorItem("Chords",null,null);
		ColorItem n1 =  new ColorItem("Title",OPT_CHORD_TITLE_COLOR,null);
		n.add(n1);
		n1 =  new ColorItem("Fret Number",OPT_CHORD_FRET_COLOR,null);
		n.add(n1);
		r.add(n);
		n =  new ColorItem("Song",null,null);
		n1 =  new ColorItem("Title",null,null);
		n.add(n1);
		n1 =  new ColorItem("Lyrics",null,null);
		n.add(n1);
		n1 =  new ColorItem("Comments",null,null);
		n.add(n1);
		n1 =  new ColorItem("Tabs",null,null);
		n.add(n1);
		r.add(n);
		n =  new ColorItem("Voicings",null,null);
		n1 =  new ColorItem("Chord Title",null,null);
		n.add(n1);
		n1 =  new ColorItem("Fret Number",null,null);
		n.add(n1);
		r.add(n);
		n =  new ColorItem("Indexes",null,null);
		n1 =  new ColorItem("Title",null,null);
		n.add(n1);
		n1 =  new ColorItem("Entry",null,null);
		n.add(n1);
		r.add(n);
		ft = new FilterTree(r);
		ft.getTree().addTreeSelectionListener(new TL());
	    ft.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    ft.getTree().setRootVisible(false);
		dlg.add(ft,BorderLayout.CENTER);

		JPanel p = new JPanel();
		editColor=new JButton("Edit Color...");
		editColor.addActionListener(new AL());
		p.add(editColor);
		editFont=new JButton("Edit Font...");
		editFont.addActionListener(new AL());
		p.add(editFont);
		dlg.add(p,BorderLayout.EAST);
		
		colorSample.getSelectionModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				selectedEntry.foregroundColor=colorSample.getColor();
				ft.repaint();
			}
			
		});
		dlg.add(colorSample,BorderLayout.SOUTH);

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
		String s = OPT_CHORD_TITLE_COLOR; Color c = Color.RED;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_CHORD_FRET_COLOR; c = Color.BLUE;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		saveOptions();
	}
	
    /** A listener shared by the text field and add button. */
    class TL implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
		   	selectedEntry=(ColorItem) ft.getTree().getLastSelectedPathComponent();
		   	colorSample.setColor(selectedEntry.foregroundColor);
		}	
    }
}
