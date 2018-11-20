package cpt.view.editor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import view.filtertree.FilterTree;
import view.options.UdoMvcViewOptionsDlgItem;


public class EditorOptionsContentDlg extends UdoMvcViewOptionsDlgItem {

	static final String OPT_NEW_TEXT = "lib.new.text";
	
	private JTextArea taNewText;
	
	public EditorOptionsContentDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){

		JPanel dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
		Box box = Box.createVerticalBox();
		
		JLabel l = new JLabel("New file content:");
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(l);
		box.add( new JLabel(" "));
		taNewText = new JTextArea();
		taNewText.setText(options.get(OPT_NEW_TEXT));
		taNewText.setAlignmentX(Component.LEFT_ALIGNMENT);
		taNewText.addKeyListener(new KL());
		taNewText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        box.add(taNewText);
		box.add( new JLabel(" "));
		
		
        box.add(Box.createVerticalGlue());
        dlg.add(box,BorderLayout.CENTER);
		
		return dlg;
	}
	
	class KL extends KeyAdapter {
		
		@Override
		public void keyTyped(final KeyEvent ke) {
			super.keyTyped(ke);
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){ 
					String key = "";
					String val = "";
					JTextArea src = (JTextArea)ke.getSource(); 
					if( ke.getSource()==taNewText) {
						key=OPT_NEW_TEXT;
						val = src.getText();
					}
					if(!key.equals("")) options.put(key,val); 
				}
			});
	    }
	}	

	@Override
	public void setDefaults(Properties prop){
		String s=OPT_NEW_TEXT, v="#{start_of_meta}\n#{title:new}\n#{artist:new}\n#{end_of_meta}\n";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		saveOptions();
	}
}
