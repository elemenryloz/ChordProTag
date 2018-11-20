package cpt.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;

import model.UdoMvcModelEvent;
import view.UdoMvcView;
import view.UdoMvcViewEventListener;
import view.action.UdoMvcViewAction;
import view.filedialog.UdoFileDialog;
import view.menu.UdoMvcViewMenuBar;
import view.options.UdoMvcViewOptionsDialog;
import view.options.UdoMvcViewOptionsDlgItem;
import cpt.control.Control;
import cpt.model.ModelSong;
import cpt.view.browser.Browser;
import cpt.view.browser.BrowserTable;
import cpt.view.browser.BrowserTableModel;
import cpt.view.browser.options.columns.BrowserOptionsColumnsDlg;
import cpt.view.browser.options.color.BrowserOptionsColorDlg;
import cpt.view.browser.options.content.BrowserOptionsContentDlg;
import cpt.view.editor.Editor;
import cpt.view.editor.EditorOptionsColorDlg;
import cpt.view.editor.EditorOptionsContentDlg;
import cpt.view.filerenamer.FileRenamer;
import cpt.view.help.Help;
import cpt.view.odt.OdtExporter;
import cpt.view.odt.OdtOptionsColorDlg;
import cpt.view.odt.OdtOptionsContentDlg;
import cpt.view.tagchanger.TagChanger;

public class View extends UdoMvcView {
	
	private static final long serialVersionUID = 1L;

	public static final String OPT_TITLE = "view.title";
	public static final String OPT_BOUNDS = "view.bounds";
	
	Browser browser;
	BrowserTable bptable;
	BrowserTableModel bpmodel;
	OdtExporter odt;
	public static UdoMvcViewOptionsDialog options;
	public static UdoMvcViewOptionsDlgItem bocd;
	
	private JTabbedPane tabs;
	private HashMap<ModelSong,Integer> openTabs;

	private UdoMvcViewMenuBar menubar;

	private Editor editor;

	private int helpOpen=-1;

	
	
	public View(){
		options=addOptions();
		options.setListener(new ViewOptionsChangedHandler(this));
		
/*
		UdoMvcViewToolBarItem possibleTools[] = {
			new UdoMvcViewToolBarItem(tbOpen),
			new UdoMvcViewToolBarItem(tbAdd),
			new UdoMvcViewToolBarItem(tbSaveChanged),
			new UdoMvcViewToolBarItem(tbSaveSelected),
			new UdoMvcViewToolBarItem(tbExportOdt),
			new UdoMvcViewToolBarItem(tbExit),
			new UdoMvcViewToolBarItem(tbUndo),
			new UdoMvcViewToolBarItem(tbRedo),
			new UdoMvcViewToolBarItem(tbCut),
			new UdoMvcViewToolBarItem(tbCopy),
			new UdoMvcViewToolBarItem(tbPaste),
//			new UdoMvcViewToolBarItem(tbSelectAll),
//			new UdoMvcViewToolBarItem(tbSelectNone),
//			new UdoMvcViewToolBarItem(tbConvert),
//			new UdoMvcViewToolBarItem(tbConvert2),
			new UdoMvcViewToolBarItem(tbOptions),
			new UdoMvcViewToolBarItem(tbHelp),
//			new UdoMvcViewToolBarItem(tbAbout),
		};		
		options.add(new ViewToolbarOptionsDlg("Toolbar", possibleTools));
	*/
		tabs = new JTabbedPane();
		add(tabs);
		
		createBrowser();
		setJMenuBar(menubar=browser.getMenubar());

		openTabs=new HashMap<ModelSong,Integer>();		
		createEditor();
		
        
		createStatusBar();
		pack();
	}

	public void setOptionsDefaults(Properties prop){
		String s = OPT_TITLE;
		String f = "ChordproTagger";
		if(prop.getProperty(s)==null) prop.setProperty(s, f);
		setTitle(prop.getProperty(s));
		String opt=prop.getProperty(OPT_BOUNDS);
		if(opt!=null){
			String[] bounds=opt.split(",");
			setBounds(Integer.parseInt(bounds[0]),Integer.parseInt(bounds[1]),Integer.parseInt(bounds[2]),Integer.parseInt(bounds[3]));
		}
		options.setDefaults(prop);
		options.saveOptions();
	}
	
	private void createOdtExporter() {
		odt=new OdtExporter();
		UdoMvcViewOptionsDlgItem n = new UdoMvcViewOptionsDlgItem("ODT Export");
		UdoMvcViewOptionsDlgItem n1 = new OdtOptionsContentDlg("Content");
        n.add(n1);
		n1 = new OdtOptionsColorDlg("Color & Fonts");
        n.add(n1);
        options.add(n);
	}

	private void createBrowser() {
		browser = new Browser(this);
		bptable = Browser.table;
		bpmodel=bptable.getModel();
		tabs.add("Browser",browser);
        UdoMvcViewOptionsDlgItem n = new UdoMvcViewOptionsDlgItem("Browser");
        UdoMvcViewOptionsDlgItem n1 = new BrowserOptionsContentDlg("Content");
        bocd = n1;
        n.add(n1);
        n1 = new BrowserOptionsColumnsDlg("Columns");
        bocd = n1;
        n.add(n1);
        n1 = new BrowserOptionsColorDlg("Colors & Fonts");
        n.add(n1);
        options.add(n);
        
		createOdtExporter();

    }

	private void createEditor() {
        UdoMvcViewOptionsDlgItem n = new UdoMvcViewOptionsDlgItem("Editor");
        UdoMvcViewOptionsDlgItem n1 = new EditorOptionsContentDlg("Content");
        n.add(n1);
        n1 = new EditorOptionsColorDlg("Colors & Fonts");
        n.add(n1);
        options.add(n);
    }

	public View(UdoMvcViewEventListener listener) {
		super();
		setListener(listener);
	}

	public void setListener(Control listener) {
		super.setListener(listener);
		browser.setListener(listener);
		odt.setListener(listener);
	}

	private void createStatusBar() {
		addStatusBar();
		addStatus("Select File - Open to begin ...", false);
	}

	public File openDirectory(String lastdir) {
		File dir=null;
		UdoFileDialog chooser = new UdoFileDialog(UdoFileDialog.DIRECTORIES_ONLY);
		chooser.setWarnExistingFile(false);
		if(lastdir!=null) chooser.setCurrentDirectory(new File(lastdir));
        int rc = chooser.showDialog(null, "Open directory");
        if (rc == UdoFileDialog.APPROVE_OPTION) {
        	dir=chooser.getSelectedFile();
        }
        return dir;
	}

	public UdoMvcViewOptionsDialog getOpions() {
		return options;
	}

	public BrowserTable getBrowserTable() {
		return bptable;
	}

	public UdoMvcViewMenuBar getMenubar() {
		return menubar;
	}
	
	public OdtExporter getOdt(){
		return odt;
	}

	public void showAboutDlg() {
//		JOptionPane.showMessageDialog(null, "<html>ChordProTag<br>Version 0.1<br>4.4.2014 14:00</html>", View.MENU_HELP_ABOUT, 1);
	}

	public void showOptionsDlg() {
		options.setVisible(true);
	}

	public Browser getBrowser() {
		return browser;
	}

	public Editor getEditor() {
		return editor;
	}
	
	public File newFile() {
		
    	UdoFileDialog chooser = new UdoFileDialog("Chorpro Document (*.crd, *chorpro)", "crd", "chopro");
    	String last = (String) ((Control)getListener()).getModel().getOptions().get("lib.lastdir");
    	if(last!=null) chooser.setSelectedFile(new File(last+"/."));
        int rc = chooser.showDialog(null, "New File");
        if (rc == UdoFileDialog.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;
	}
	

	public void editItems(int[] rows) {
		for(int row: rows){
			editItem(row,false);
		}
	}
	
	public void editItem(int row, boolean isNew) {
		ModelSong i=bpmodel.getLib().get(row);
		if(!i.isOpen()){
			Editor e = new Editor(this,i);
			String t=i.meta.get("_file_name");
			tabs.add(t, e);
	        tabs.setTabComponentAt(tabs.getTabCount()-1, new ButtonTabComponent(tabs,i));
	        tabs.setSelectedIndex(tabs.getTabCount()-1);
	        openTabs.put(i, new Integer(tabs.getTabCount()-1));
		} else {
	        tabs.setSelectedIndex(openTabs.get(i));
		}
    }

	public boolean deleteItems(int[] rows) {
		
		if( JOptionPane.showConfirmDialog(this, "Sure to delete "+rows.length+" songs?","Warning",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
			Arrays.sort(rows);
			for(int row=rows.length-1; row>=0; row--){
				ModelSong i=bpmodel.getLib().get(rows[row]);
				if(i.isOpen()) tabs.remove(openTabs.get(i));
				bpmodel.getLib().remove(i);
			}
			bpmodel.fireTableDataChanged();
			return true;
		}
		return false;
    }

	private class ButtonTabComponent extends JPanel {
	    private final JTabbedPane pane;
	    private ModelSong item;
		 
	    public ButtonTabComponent(final JTabbedPane pane, ModelSong i) {
	        //unset default FlowLayout' gaps
	        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
	        if (pane == null) {
	            throw new NullPointerException("TabbedPane is null");
	        }
	        this.pane = pane;
	        setOpaque(false);
	         
	        //make JLabel read titles from JTabbedPane
	        JLabel label = new JLabel() {
	            public String getText() {
	                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
	                if (i != -1) {
	                    return pane.getTitleAt(i);
	                }
	                return null;
	            }
	        };
	         
	        add(label);
	        //add more space between the label and the button
	        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
	        //tab button
	        JButton button = new TabButton();
	        add(button);
	        //add more space to the top of the component
	        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	        
	        item=i;
	        if(i!=null) i.setOpen(true);
	    }
		 
	    private class TabButton extends JButton implements ActionListener {
	        public TabButton() {
	            int size = 17;
	            setPreferredSize(new Dimension(size, size));
	            setToolTipText("close this tab");
	            //Make the button looks the same for all Laf's
	            setUI(new BasicButtonUI());
	            //Make it transparent
	            setContentAreaFilled(false);
	            //No need to be focusable
	            setFocusable(false);
	            setBorder(BorderFactory.createEtchedBorder());
	            setBorderPainted(false);
	            //Making nice rollover effect
	            //we use the same listener for all buttons
	            addMouseListener(buttonMouseListener);
	            setRolloverEnabled(true);
	            //Close the proper tab by clicking the button
	            addActionListener(this);
	        }
	 
	        public void actionPerformed(ActionEvent e) {
	            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
	            if (i != -1) {
	                if(item!=null){
			        	Editor ed=(Editor)pane.getComponentAt(i);
		                openTabs.remove(item);
		                item.setOpen(false);
	                } else {
	                	helpOpen=-1;
	                }
	                pane.remove(i);
	            }
	        }
	 
	        //we don't want to update UI for this button
	        public void updateUI() {
	        }
	 
	        //paint the cross
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g.create();
	            //shift the image for pressed buttons
	            if (getModel().isPressed()) {
	                g2.translate(1, 1);
	            }
	            g2.setStroke(new BasicStroke(2));
	            g2.setColor(Color.BLACK);
	            if (getModel().isRollover()) {
	                g2.setColor(Color.MAGENTA);
	            }
	            int delta = 6;
	            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
	            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
	            g2.dispose();
	        }
	    }
	 
	    private final MouseListener buttonMouseListener = new MouseAdapter() {
	        public void mouseEntered(MouseEvent e) {
	            Component component = e.getComponent();
	            if (component instanceof AbstractButton) {
	                AbstractButton button = (AbstractButton) component;
	                button.setBorderPainted(true);
	            }
	        }
	 
	        public void mouseExited(MouseEvent e) {
	            Component component = e.getComponent();
	            if (component instanceof AbstractButton) {
	                AbstractButton button = (AbstractButton) component;
	                button.setBorderPainted(false);
	            }
	        }
	    };
	}
	
	public void setLib(Vector<ModelSong> mlib) {
		Vector<ModelSong> lib = bpmodel.getLib();
		Pattern patFilter = getBrowser().getFilterPattern();
		lib.clear();
		for( ModelSong item: mlib){
			// get matcher if filter is set
			Matcher m=null;
			if(patFilter!=null && !item.isAdded() && !item.isOpen()) {
			    m=patFilter.matcher(item.allText);
			}
			if(m==null || m.matches()){
				lib.add(item);
			}
		}		
		bpmodel.fireTableDataChanged();
		addStatus( "Loading files ... done", true );
		if (lib.size()>0) {
			for(UdoMvcViewAction m: browser.whenLoadedEnabled) {
				m.setEnabled(true);
			}
		}		
	}

	
	@Override
	public void handleModelEvent(UdoMvcModelEvent e) {
		if (e.getType()==UdoMvcModelEvent.LOAD){
			if(e.getData()!=null) addStatus( "Loading files ... "+((Integer)e.getData())+"%", true );
			else setLib((Vector<ModelSong>) e.getSource());
		} else if (e.getType()==UdoMvcModelEvent.SAVE){
			if(e.getData()!=null) addStatus( "Saving file ... "+((ModelSong)e.getData()).meta.get("_file_path"), true );
			else bpmodel.fireTableDataChanged(); 
		}
	}
	
	public int[] getSelectedItems(){
    	bptable.editingCanceled(null);
		int s[] = bptable.getSelectedRows();
		for(int i=0; i<s.length; i++){
			s[i]=bptable.convertRowIndexToModel(s[i]);
		}
		return s;
	}
	

	public void update(){
    	bpmodel.fireTableDataChanged();
	}

	public void exportODT() {
		DefaultListModel<ModelSong> lm = odt.getModel();
	    lm.clear();
		int[] sel = bptable.getSelectedRows();
		for(int i: sel){
			int r= bptable.convertRowIndexToModel( i );
    		lm.addElement(bpmodel.getLib().get(r));
		}
		odt.setVisible(true);
	}

	
	/**
	 * main method 
	 * @param args this program ignores all arguments
	 */
	public static void main(String args[]) {
		
		// set the look and feel
		try {
			/*
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			*/
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e2) {
				JOptionPane.showOptionDialog(null, e.toString(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
			}
		}

		// create the control instance and add to frame 
		
		View v=new View();
		Control cntl=new Control(v);
	}

	public void renameFiles() {
		new FileRenamer(this, Browser.MENU_TOOLS_RENAME).setVisible(true);
	}

	public void changeTags() {
		new TagChanger(this, Browser.MENU_TOOLS_TAG_TO_TAG).setVisible(true);
	}

	public void showHelpDlg() {
		if (helpOpen==-1){
			Help e = new Help(this);
			String t="*Help";
			tabs.add(t,e);
	        tabs.setTabComponentAt(tabs.getTabCount()-1, new ButtonTabComponent(tabs,null));
	        tabs.setSelectedIndex(helpOpen=tabs.getTabCount()-1);
		} else {
	        tabs.setSelectedIndex(helpOpen);
		} 
	}

	public void addFile(ModelSong i) {
		final Vector<ModelSong> lib = bpmodel.getLib();
		int ix=lib.indexOf(i);
		if (ix==-1){
			lib.add(i);
			bpmodel.fireTableDataChanged();
			ix=lib.size()-1;
		}
		editItem( ix,true);
		addStatus( "New file created.", true );
		for(UdoMvcViewAction m: getBrowser().whenLoadedEnabled) {
			m.setEnabled(true);
		}
	}

}