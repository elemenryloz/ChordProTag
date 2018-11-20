package cpt.view.browser.options.color;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import view.filtertree.FilterTree;
import view.font.UdoMvcViewFontChooser;
import view.options.UdoMvcViewOptionsDlgItem;


public class BrowserOptionsColorDlg extends UdoMvcViewOptionsDlgItem {
	private static final long serialVersionUID = 1L;
	
	public final static String OPT_BACKGROUND_COLOR = "browser.backgroundColor";
	public final static String OPT_FOREGROUND_COLOR = "browser.foregroundColor";
	public final static String OPT_CHANGED_COLOR = "browser.changed.foregroundColor";
	public final static String OPT_OPEN_COLOR = "browser.open.backgroundColor";
	public final static String OPT_FONT = "browser.font";
	public final static String OPT_STATUS_BACKGROUND_COLOR = "browser.status.backgroundColor";
	public final static String OPT_STATUS_FOREGROUND_COLOR = "browser.status.foregroundColor";
	public final static String OPT_STATUS_FONT = "browser.status.font";

	JTextField tf;
	private FilterTree ft;
	private ColorItem selectedEntry;
	
	private JColorChooser colorSample=new JColorChooser();
	private UdoMvcViewFontChooser fontSample;
	private JPanel dlg;
	
	public BrowserOptionsColorDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){
		
		dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
		ColorItem r =  new ColorItem("root",null,null);
		ColorItem n =  new ColorItem("Browser",null,null);
		ColorItem n1 =  new ColorItem("Background color",OPT_BACKGROUND_COLOR,null);
		n.add(n1);
		n1 =  new ColorItem("Foreground color and font",OPT_FOREGROUND_COLOR,OPT_FONT);
		String f[] = ((String)options.get(OPT_FONT)).split(",");
		n1.font = new Font(f[0],Integer.parseInt(f[1]),Integer.parseInt(f[2]));
		n.add(n1);
		n1 =  new ColorItem("Foreground color for changed items",OPT_CHANGED_COLOR,null);
		n.add(n1);
		n1 =  new ColorItem("Background Color for open items",OPT_OPEN_COLOR,null);
		n.add(n1);
		r.add(n);
		n =  new ColorItem("Status",null,null);
		n1 =  new ColorItem("Background color",OPT_STATUS_BACKGROUND_COLOR,null);
		n.add(n1);
		n1 =  new ColorItem("Foreground color and font",OPT_STATUS_FOREGROUND_COLOR,OPT_STATUS_FONT);
		n.add(n1);
		r.add(n);
		ft = new FilterTree(r);
		ft.getTree().addTreeSelectionListener(new TL());
	    ft.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    ft.getTree().setRootVisible(false);
		dlg.add(ft,BorderLayout.CENTER);

		colorSample.setPreviewPanel(new JPanel());
		colorSample.setMaximumSize(colorSample.getMaximumSize());
		/*
		AbstractColorChooserPanel panels[] = colorSample.getChooserPanels();
		for (int i = 1; i < panels.length; i ++) {
		    colorSample.removeChooserPanel(panels[i]);
		}
		*/
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
	
	@Override
	public void setDefaults(Properties prop){
		String s = OPT_BACKGROUND_COLOR; Color c = Color.WHITE;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_FOREGROUND_COLOR; c = Color.BLACK;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_CHANGED_COLOR; c = Color.RED;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_OPEN_COLOR; c = Color.YELLOW;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_STATUS_BACKGROUND_COLOR; c = Color.LIGHT_GRAY;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_STATUS_FOREGROUND_COLOR; c = Color.BLACK;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_FONT; String f = "Arial,0,14";
		if (!prop.containsKey(s)) prop.setProperty(s, f); options.put(s, prop.getProperty(s));
		s = OPT_STATUS_FONT; f = "Arial,0,14";
		if (!prop.containsKey(s)) prop.setProperty(s, f); options.put(s, prop.getProperty(s));
		saveOptions();
	}
	
    /** A listener shared by the text field and add button. */
    class TL implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
		   	selectedEntry=(ColorItem) ft.getTree().getLastSelectedPathComponent();
		   	if(selectedEntry.foregroundColor!=null) colorSample.setColor(selectedEntry.foregroundColor);
		   	colorSample.setVisible(selectedEntry.foregroundColor!=null);
		   	if(selectedEntry.font!=null) fontSample.setSelectedFont(selectedEntry.font);
		   	fontSample.setVisible(selectedEntry.font!=null);
		}	
    }
 
}
