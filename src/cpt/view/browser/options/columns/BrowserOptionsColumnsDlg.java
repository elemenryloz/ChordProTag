package cpt.view.browser.options.columns
;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import view.options.UdoMvcViewOptionsDlgItem;
import cpt.view.browser.Browser;

public class BrowserOptionsColumnsDlg extends UdoMvcViewOptionsDlgItem {

	private static final long serialVersionUID = 1L;
	public final static String OPT_COLUMN_PRESETS = "browser.column.presets";
	public final static String OPT_COLUMN_SETTINGS = "browser.column.settings";

	private JButton loadButton, saveButton, deleteButton;
	private JComboBox<String> presets;

	private JPanel dlg;


	TreeMap<String,String> presetMap;
	private BrowserOptionsColumnsTable colTable;
    
	public BrowserOptionsColumnsDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){
    
		dlg=super.getDlg();
        dlg.setLayout(new BorderLayout());
        
		Box box = Box.createVerticalBox();
		box.setBorder(new EmptyBorder(10,10,10,10));
		
        box.add(colTable);

        
        box.add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE,sep.getPreferredSize().height));
        box.add(sep);
        box.add(Box.createVerticalStrut(10));
        
        
        Box p=Box.createHorizontalBox();
        JLabel lab = new JLabel("Presets: ");
		p.add(lab);
		presets = new JComboBox<String>();
        presets.setMaximumSize(new Dimension(100,presets.getPreferredSize().height));
        presets.setEditable(true);
        final JTextComponent tc=(JTextComponent)presets.getEditor().getEditorComponent();
        tc.addCaretListener(new CaretListener() {
            private String lastText;

            @Override
            public void caretUpdate(CaretEvent e) {
                String text = tc.getText();
                if (!text.equals(lastText)) {
                    lastText = text;
                    int ix=((DefaultComboBoxModel)presets.getModel()).getIndexOf(text);
                    loadButton.setEnabled(ix!=-1);
                    deleteButton.setEnabled(ix!=-1 && presets.getItemCount()>1);
                }
            }
        });
        colTable.setPresets(presets);
        lab.setLabelFor(presets);
        lab.setDisplayedMnemonic(KeyEvent.VK_P);
		p.add(presets);
        loadButton=new JButton("Load");
        loadButton.setMnemonic(KeyEvent.VK_L);
        loadButton.addActionListener(new ButtonListener());
		p.add(loadButton);
        saveButton=new JButton("Save");
        saveButton.setMnemonic(KeyEvent.VK_S);
        saveButton.addActionListener(new ButtonListener());
		p.add(saveButton);
        deleteButton=new JButton("Delete");
        deleteButton.setMnemonic(KeyEvent.VK_L);
        deleteButton.addActionListener(new ButtonListener());
		p.add(deleteButton);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE,presets.getPreferredSize().height));
        box.add(p);
        
		dlg.add(box);

		oldOptions.clear();
		oldOptions.put(OPT_COLUMN_PRESETS, Browser.oColumnPresets);
		oldOptions.put(OPT_COLUMN_SETTINGS, Browser.oColumnSettings);

		
		setDlgData();
        
		return dlg;
	}
	

	private void setDlgData(){
		presetMap = new TreeMap<String,String>(getPresets());
		presets.removeAllItems();
		for (String s : presetMap.keySet()) {
			presets.addItem(s);
		}
        String curPreset=Browser.oColumnSettings.split(":")[0];
        presets.setSelectedItem(curPreset.equals("")?null:curPreset);
//        loadPreset();
	}

	private HashMap<String,String> getPresets(){
		String[] ss = Browser.oColumnPresets.split(",");
		HashMap<String,String> map= new HashMap<String,String>();
		for( String s: ss){
			String[] cc = s.split(":");
			map.put(cc[0], s);
		}
		return map;
	}

	
    /** A listener shared by the text field and add button. */
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
            if (e.getSource()==loadButton) {
            	loadPreset();
            } else if (e.getSource()==saveButton) {
            	savePreset();
            } else if (e.getSource()==deleteButton) {
            	deletePreset();
            }

        }
    }
    
	@Override
	public void setDefaults(Properties prop){
		String s = OPT_COLUMN_PRESETS; String f = "Default:"
				+ "Directory:75:_file_directory:"
				+ "Directory Path:75:_file_directory_path:"
				+ "File Name:75:_file_name:"
				+ "File Extension:75:_file_extension:"
				+ "File Path:75:_file_path:"
				+ "Created:75:_file_created:"
				+ "Last Modified:75:_file_modified:"
				+ "Last Accessed:75:_file_accessed:"
				+ "File Size:75:_file_size:"
				+ "ChordPro Title:75:\u00A7title:"
				+ "ChordPro Subtitle:75:\u00A7subtitle:"
				+ "Artist:75:artist:"
				+ "Title:75:title";
		if (!prop.containsKey(s)) prop.setProperty(s, f); options.put(s, prop.getProperty(s));
		s = OPT_COLUMN_SETTINGS; f = "Default";
		if (!prop.containsKey(s)) prop.setProperty(s, f); options.put(s, prop.getProperty(s));
		saveOptions();

		colTable = new BrowserOptionsColumnsTable(dlg);
		String[] ss = options.get(OPT_COLUMN_PRESETS).split(",");
		HashMap<String,String> map= new HashMap<String,String>();
		for( String p: ss){
			String[] cc = p.split(":");
			map.put(cc[0], p);
		}
		colTable.setColumns(map.get(options.get(OPT_COLUMN_SETTINGS)));
		
	}
	
	@Override
	public Properties cancelOptions() {
		
		options.clear();
		options.putAll(oldOptions);
		presetMap = new TreeMap<String,String>(getPresets());
		colTable.setColumns(presetMap.get(options.get(OPT_COLUMN_SETTINGS)));
		setDlgData();
		return saveOptions();
	}

	private void deletePreset() {
		int selIx = presets.getSelectedIndex();
		if(selIx==-1){
		    Toolkit.getDefaultToolkit().beep();
		    return;
		}
		
		String sel=""+presets.getEditor().getItem();
		presetMap.remove(sel);
		presets.removeItemAt(selIx);
		presets.setSelectedItem("");
		
		options.put(OPT_COLUMN_PRESETS, Browser.oColumnPresets.replaceAll(sel+"[^,]*(,|$)", ""));

	}

	private void savePreset() {
		if(presets.getSelectedIndex()!=-1){
		    if(JOptionPane.showConfirmDialog(dlg, presets.getEditor().getItem()+" already exists!\nDo you want to override it?", "Save Preset", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)==JOptionPane.CANCEL_OPTION){
		    	return;
		    }
		}
		//name:head1:200:tag1:Head2:100:tag2
		String newPreset=""+presets.getEditor().getItem();
		
		String setting=newPreset+colTable.getColumns();
		String settings="";
		presetMap.put(newPreset, setting);
		presets.removeAllItems();
		for( String s: presetMap.keySet()){
			settings+=","+presetMap.get(s);
			presets.addItem(s);
		}
		options.put(OPT_COLUMN_PRESETS, settings.substring(1));

		presets.setSelectedItem(newPreset);
		loadPreset();
	}

	private void loadPreset() {
		if(presets.getSelectedIndex()==-1){
		    Toolkit.getDefaultToolkit().beep();
		    return;
		}
		String preset = (String) presets.getSelectedItem();
		options.put(OPT_COLUMN_SETTINGS, preset);
		colTable.setColumns(presetMap.get(preset));
	}
	
	
}
