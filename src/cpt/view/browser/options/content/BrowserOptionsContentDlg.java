package cpt.view.browser.options.content;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import view.filtertree.FilterTree;
import view.options.UdoMvcViewOptionsDlgItem;


public class BrowserOptionsContentDlg extends UdoMvcViewOptionsDlgItem {

	public static final String OPT_DIRS = "browser.dirs";
	
	JTextField tf;
	
	public BrowserOptionsContentDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){

		JPanel dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
		Box box = Box.createVerticalBox();
		
		JLabel l = new JLabel("Directories:");
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(l);
		box.add( new JLabel(" "));
		tf = new JTextField();
		tf.setText(options.get(OPT_DIRS));
		tf.setAlignmentX(Component.LEFT_ALIGNMENT);
		tf.addKeyListener(new KL());
		tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, tf.getPreferredSize().height));
        box.add(tf);

		
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
					JTextField src = (JTextField)ke.getSource(); 
					if(src==tf)  {
						key=OPT_DIRS;
						val = src.getText();
					}
					if(!key.equals("")) options.put(key,val); 
				}
			});
	    }
	}	

	@Override
	public void setDefaults(Properties prop){
		String s=OPT_DIRS, v="";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		saveOptions();
	}
	
	
}
