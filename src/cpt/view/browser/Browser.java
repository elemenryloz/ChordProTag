package cpt.view.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import view.action.UdoMvcViewAction;
import view.menu.UdoMvcViewMenu;
import view.menu.UdoMvcViewMenuBar;
import view.toolbar.UdoMvcViewToolBar;
import cpt.control.Control;
import cpt.view.View;
import cpt.view.browser.options.color.BrowserOptionsColorDlg;
import cpt.view.browser.options.columns.BrowserOptionsColumnsDlg;

/**
 * Represents the browser component
 * @author Udo
 *
 */
public class Browser extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public final static String MENU_FILE = "File";
	public final static String MENU_FILE_NEW = "New File...";
	public final static String MENU_FILE_EDIT = "Edit selected";
	public final static String MENU_FILE_DELETE = "Delete selected";
	public final static String MENU_FILE_OPEN = "Open directory...";
	public final static String MENU_FILE_ADD = "Add directory...";
	public final static String MENU_FILE_SAVE_CHANGED = "Save changed";
	public final static String MENU_FILE_SAVE_SELECTED = "Save selected";
	public final static String MENU_FILE_EXPORT_ODT = "Export selected as ODT...";
	public final static String MENU_FILE_EXIT = "Exit";
	public final static String MENU_EDIT = "Edit";
	public final static String MENU_EDIT_UNDO = "Undo";
	public final static String MENU_EDIT_REDO = "Redo";
	public final static String MENU_EDIT_CUT = "Cut";
	public final static String MENU_EDIT_COPY = "Copy";
	public final static String MENU_EDIT_PASTE = "Paste";
	public final static String MENU_EDIT_SELECT_ALL = "Select all";
	public final static String MENU_EDIT_SELECT_NONE = "Select none";
	public final static String MENU_TOOLS = "Tools";
	public final static String MENU_TOOLS_RENAME = "Rename...";
	public final static String MENU_TOOLS_TAG_TO_TAG = "Tag to tag...";
	public final static String MENU_TOOLS_OPTIONS = "Options...";
	public final static String MENU_Help = "Help";
	public final static String MENU_HELP_HELP = "Help...";
	public final static String MENU_HELP_ABOUT = "About...";
	
	
	public static BrowserTable table;
    private JScrollPane pLists;
    private JTextField tfFilter;
	private Pattern patFilter;
    
    
    private Control listener;
    static private View view;
	public static String oColumnPresets;
	public static String oColumnSettings;
    
	private UdoMvcViewAction actionNew, actionEdit, actionDelete, actionOpen, actionAdd, actionSaveChanged, actionSaveSelected, actionExportOdt, actionExit;
	private UdoMvcViewAction actionUndo, actionRedo, actionCut, actionCopy, actionPaste, actionSelectAll, actionSelectNone;
	private UdoMvcViewAction actionConvert, actionConvert2,actionOptions;
	private UdoMvcViewAction actionHelp,actionAbout;
	
	private JButton tbNew, tbEdit, tbDelete, tbOpen, tbAdd, tbSaveChanged, tbSaveSelected, tbExportOdt, tbExit;
	private JButton tbUndo, tbRedo, tbCut, tbCopy, tbPaste, tbSelectAll, tbSelectNone;
	private JButton tbConvert, tbConvert2,tbOptions;
	private JButton tbHelp,tbAbout;
	
	private UdoMvcViewMenuBar menubar;
	
	public UdoMvcViewAction[] alwaysEnabled, whenLoadedEnabled, zeroSelectedEnabled, oneSelectedEnabled, multipleSelectedEnabled;
	private UdoMvcViewToolBar toolbar;
    
	/**
	 * Constructor. creates the components of the browser pane
	 */
    public Browser(View view) {
		super();
		Browser.view=view;
		
		// create components
		setLayout(new BorderLayout());
		
		actionNew=new UdoMvcViewAction(view, MENU_FILE_NEW, "/cpt/view/images/new_file.png", "Create a new file", KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		actionEdit=new UdoMvcViewAction(view, MENU_FILE_EDIT, "/cpt/view/images/edit_file.png", "Edit all selected files", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		actionDelete=new UdoMvcViewAction(view, MENU_FILE_DELETE, "/cpt/view/images/delete_file.png", "Delete all selected files", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		actionOpen=new UdoMvcViewAction(view, MENU_FILE_OPEN, "/cpt/view/images/open_dir.png", "Clear list and open a new directory", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		actionAdd=new UdoMvcViewAction(view, MENU_FILE_ADD, "/cpt/view/images/add_dir.png", "Add a directory to the existing list");
		actionSaveChanged=new UdoMvcViewAction(view, MENU_FILE_SAVE_CHANGED, "/cpt/view/images/save_changed.png", "Save all files that have been changed", KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		actionSaveSelected=new UdoMvcViewAction(view, MENU_FILE_SAVE_SELECTED, "/cpt/view/images/save_selected.png", "Save all selected files");
		actionExportOdt=new UdoMvcViewAction(view, MENU_FILE_EXPORT_ODT, "/cpt/view/images/export_odt.png", "Export all selected songs to an ODT document", KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		actionExit=new UdoMvcViewAction(view, MENU_FILE_EXIT, "/cpt/view/images/exit.png", "Exit this application", KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));

		actionUndo=new UdoMvcViewAction(view, MENU_EDIT_UNDO, "/cpt/view/images/undo.png", "Undo last operation", KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		actionRedo=new UdoMvcViewAction(view, MENU_EDIT_REDO, "/cpt/view/images/redo.png", "Redo last operation", KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		actionCut=new UdoMvcViewAction(view, MENU_EDIT_CUT, "/cpt/view/images/cut.png", "Cut contents and store in clipboard", KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		actionCopy=new UdoMvcViewAction(view, MENU_EDIT_COPY, "/cpt/view/images/copy.png", "Copy contents and store in clipboard", KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		actionPaste=new UdoMvcViewAction(view, MENU_EDIT_PASTE, "/cpt/view/images/paste.png", "paste contents from clipboard", KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		actionSelectAll=new UdoMvcViewAction(view, MENU_EDIT_SELECT_ALL, (BufferedImage)null, "Select complete contents", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		actionSelectNone=new UdoMvcViewAction(view, MENU_EDIT_SELECT_NONE, (BufferedImage)null, "Deselect complete contents", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
		
		actionConvert=new UdoMvcViewAction(view, MENU_TOOLS_RENAME, "/cpt/view/images/rename.png", "Convert tags of the selected song", KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK+ActionEvent.SHIFT_MASK));
		actionConvert2=new UdoMvcViewAction(view, MENU_TOOLS_TAG_TO_TAG, "/cpt/view/images/tag.png", "Convert2 tags of the selected song", KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK+ActionEvent.SHIFT_MASK));
		actionOptions=new UdoMvcViewAction(view, MENU_TOOLS_OPTIONS, "/cpt/view/images/options.png", "Show the \"Options\" dialog", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK+ActionEvent.SHIFT_MASK));
		
		actionHelp=new UdoMvcViewAction(view, MENU_HELP_HELP, "/cpt/view/images/help.png", "Request help", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		actionAbout=new UdoMvcViewAction(view, MENU_HELP_ABOUT, (BufferedImage)null, "Show the \"About\" dialog", null);

		createMenu();
		toolbar=new UdoMvcViewToolBar(view);
		toolbar.setOpaque(true);
		toolbar.setBackground(Color.decode("#eeeeff"));
		
		tbNew=toolbar.addItem(actionNew);
		tbEdit=toolbar.addItem(actionEdit);
		tbDelete=toolbar.addItem(actionDelete);
		tbOpen=toolbar.addItem(actionOpen);
		tbAdd=toolbar.addItem(actionAdd);toolbar.remove(tbAdd);
		tbSaveChanged=toolbar.addItem(actionSaveChanged);
		tbSaveSelected=toolbar.addItem(actionSaveSelected);toolbar.remove(tbSaveSelected);
		tbExportOdt=toolbar.addItem(actionExportOdt);
		tbExit=toolbar.addItem(actionExit);
		toolbar.addSeparator();
		tbUndo=toolbar.addItem(actionUndo);
		tbRedo=toolbar.addItem(actionRedo);
		tbCut=toolbar.addItem(actionCut);
		tbCopy=toolbar.addItem(actionCopy);
		tbPaste=toolbar.addItem(actionPaste);
		tbSelectAll=toolbar.addItem(actionSelectAll);toolbar.remove(tbSelectAll);
		tbSelectNone=toolbar.addItem(actionSelectNone);toolbar.remove(tbSelectNone);
		toolbar.addSeparator();
		tbConvert=toolbar.addItem(actionConvert);toolbar.remove(tbConvert);
		tbConvert2=toolbar.addItem(actionConvert2);toolbar.remove(tbConvert2);
		tbOptions=toolbar.addItem(actionOptions);
		toolbar.addSeparator();
		toolbar.add(createFilterPane());
		toolbar.addSeparator();
		toolbar.add(Box.createHorizontalGlue());
		tbHelp=toolbar.addItem(actionHelp);
		tbAbout=toolbar.addItem(actionAbout);toolbar.remove(tbAbout);
		
		add(toolbar,BorderLayout.NORTH);
		
		table = new BrowserTable();
		InputMap im = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		Object actionKey=new Object();
		im.put((KeyStroke) actionEdit.getValue(Action.ACCELERATOR_KEY),actionKey);
		table.getActionMap().put(actionKey, null);
		pLists = new JScrollPane(table);
		pLists.getViewport().setBackground(Color.BLACK);
		add(pLists,BorderLayout.CENTER);
	}
    
    public void setListener(Control listener){
    	this.listener = listener;
    	table.setListener(listener);
    }
    
    public UdoMvcViewMenuBar getMenubar(){
    	return menubar;
    }
    
    public Pattern getFilterPattern(){
    	return patFilter;
    }
    
	private void createMenu(){
		menubar = new UdoMvcViewMenuBar(view);
		createFileMenu(menubar);
		createEditMenu(menubar);
		createToolsMenu(menubar);
		createHelpMenu(menubar);
		alwaysEnabled = new UdoMvcViewAction[]{ actionNew, actionOpen, actionExit, actionOptions, actionHelp, actionAbout};
		whenLoadedEnabled = new UdoMvcViewAction[]{ actionAdd, actionSaveChanged, actionSelectAll };
		zeroSelectedEnabled = new UdoMvcViewAction[]{ };
		oneSelectedEnabled = new UdoMvcViewAction[]{ actionEdit, actionDelete, actionSaveSelected, actionExportOdt, actionConvert, actionConvert2, actionCut, actionCopy, actionSelectNone};
		multipleSelectedEnabled = new UdoMvcViewAction[]{ actionEdit, actionDelete, actionSaveSelected, actionExportOdt, actionSelectNone, actionConvert, actionConvert2 };
		menubar.setAllMenuItemsEnabled(false);
		for(UdoMvcViewAction m: alwaysEnabled) {
			m.setEnabled(true);
		}
	}
	
	private void createFileMenu(UdoMvcViewMenuBar menubar){
		UdoMvcViewMenu menu = menubar.addMenu(MENU_FILE);
		menu.addMenuItem(actionNew);
		menu.addMenuItem(actionEdit);
		menu.addMenuItem(actionDelete);
		menu.addSeparator();
		menu.addMenuItem(actionOpen);
		menu.addMenuItem(actionAdd);
		menu.addSeparator();
		menu.addMenuItem(actionSaveChanged);
		menu.addMenuItem(actionSaveSelected);
		menu.addSeparator();
		menu.addMenuItem(actionExportOdt);
		menu.addSeparator();
		menu.addMenuItem(actionExit);
	}
	
	private void createEditMenu(UdoMvcViewMenuBar menubar){
		UdoMvcViewMenu menu=menubar.addMenu(MENU_EDIT);
		menu.addMenuItem(actionUndo);
		menu.addMenuItem(actionRedo);
		menu.addSeparator();
		menu.addMenuItem(actionCut);
		menu.addMenuItem(actionCopy);
		menu.addMenuItem(actionPaste);
		menu.addSeparator();
		menu.addMenuItem(actionSelectAll);
		menu.addMenuItem(actionSelectNone);
	}

	public JPopupMenu createEditPopup(){
		JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem(actionNew));
		popup.add(new JMenuItem(actionEdit));
		popup.add(new JMenuItem(actionDelete));
		popup.addSeparator();
		popup.add(new JMenuItem(actionConvert));
		popup.add(new JMenuItem(actionConvert2));
		popup.addSeparator();
		popup.add(new JMenuItem(actionCut));
		popup.add(new JMenuItem(actionCopy));
		popup.add(new JMenuItem(actionPaste));
		popup.addSeparator();
		popup.add(new JMenuItem(actionSelectAll));
		popup.add(new JMenuItem(actionSelectNone));
		return popup;
	}

	private void createToolsMenu(UdoMvcViewMenuBar menubar){
		UdoMvcViewMenu menu=menubar.addMenu("Tools");
		menu.addMenuItem(actionConvert);
		menu.addMenuItem(actionConvert2);
		menu.addSeparator();
		menu.addMenuItem(actionOptions);
	}

	private void createHelpMenu(UdoMvcViewMenuBar menubar){
		UdoMvcViewMenu menu = menubar.addMenu("Help");
		menu.addMenuItem(actionHelp);
		menu.addSeparator();
		menu.addMenuItem(actionAbout);
	}
    
	/**
	 * Creates the filter panel
	 * @return filter panel
	 */
    public JPanel createFilterPane() {

	    // create the filter label and textfield
	    JLabel lFilter=new JLabel("Filter:");
	    lFilter.setOpaque(true);
	    lFilter.setBackground(toolbar.getBackground());
	    tfFilter=new JTextField(20);
	    tfFilter.setOpaque(true);
	    tfFilter.setBackground(Color.WHITE);
	    tfFilter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==tfFilter){
					String text=tfFilter.getText();
					if ((text.length()==0)) patFilter=null;
					else {
						if(!text.startsWith("/")){
							text=".*"+text+".*";
							patFilter = Pattern.compile(text,Pattern.MULTILINE + Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
						}
						else {
							text=text.replaceAll("^/|/$","");
							patFilter = Pattern.compile(text, Pattern.MULTILINE + Pattern.DOTALL );
						}
					}
					listener.getView().setLib(listener.getModel().getLib());
				} 
			}
	    	
	    });
	    JPanel pButtons;
	    pButtons=new JPanel(new BorderLayout());
	    pButtons.add(lFilter,BorderLayout.WEST);
	    pButtons.add(tfFilter, BorderLayout.CENTER);
	    return pButtons;
	}
	
	public static void setOptions(Properties props) {
		
		Browser browser = view.getBrowser();
		
		Color c=Color.decode((String) props.get(BrowserOptionsColorDlg.OPT_BACKGROUND_COLOR));
		browser.setBackground(c);
		browser.pLists.getViewport().setBackground(c);
		Browser.table.setBackground(c);

		c=Color.decode((String) props.get(BrowserOptionsColorDlg.OPT_FOREGROUND_COLOR));
		browser.setForeground(c);
		Browser.table.setForeground(c);
		
		String f[]=((String) props.get(BrowserOptionsColorDlg.OPT_FONT)).split(",");
		Font font = new Font(f[0],Integer.parseInt(f[1]),Integer.parseInt(f[2]));
		Browser.table.setFont(font);
		
		// get metrics from the graphics
		FontMetrics metrics = Browser.table.getGraphics().getFontMetrics(font);
		// get the height of a line of text in this
		// font and render context
		int hgt = metrics.getHeight();
		Browser.table.setRowHeight(hgt);
		
		oColumnPresets=(String) props.get(BrowserOptionsColumnsDlg.OPT_COLUMN_PRESETS);
		oColumnSettings=(String) props.get(BrowserOptionsColumnsDlg.OPT_COLUMN_SETTINGS);
	}
}
