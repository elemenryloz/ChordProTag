package cpt.view.odt;
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


public class OdtOptionsContentDlg extends UdoMvcViewOptionsDlgItem {

	static final String OPT_TITLE_DEFAULT = "odt.songbook.title";
	static final String OPT_VOICINGS_SIZE = "odt.voicings.size";
	static final String OPT_VOICINGS_LOC = "odt.voicings.location";
	static final String OPT_VOICINGS_COLS = "odt.voicings.columns";
	static final String OPT_MARGIN_TOP = "odt.margin.top";
	static final String OPT_MARGIN_BOTTOM = "odt.margin.bottom";
	static final String OPT_MARGIN_LEFT = "odt.margin.left";
	static final String OPT_MARGIN_RIGHT = "odt.margin.right";
	
	JTextField tf;
	private JRadioButton rbVoicingLoc1,rbVoicingLoc2,rbVoicingLoc3;
	private JTextField tfVoicingSize, tfVoicingCols, tfTitleDefault, tfLeft, tfRight, tfTop, tfBottom;
	private FilterTree ft;
	
	public OdtOptionsContentDlg(String displayName) {
		super(displayName);
	}

	public JPanel getDlg(){

		JPanel dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
		ButtonGroup bg = new ButtonGroup();
		Box box = Box.createVerticalBox();
		
		JLabel l = new JLabel("Default Songbook Title:");
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(l);
		box.add( new JLabel(" "));
		tfTitleDefault = new JTextField();
		tfTitleDefault.addActionListener(new AL());
		tfTitleDefault.setText(options.get(OPT_TITLE_DEFAULT));
		tfTitleDefault.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfTitleDefault.addKeyListener(new KL());
		tfTitleDefault.setMaximumSize(new Dimension(Integer.MAX_VALUE, tfTitleDefault.getPreferredSize().height));
        box.add(tfTitleDefault);
		box.add( new JLabel(" "));
		
		
		l = new JLabel("Voicings:");
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(l);
		box.add( new JLabel(" "));
		JPanel vp=new JPanel();
		vp.setLayout(new BorderLayout());
		rbVoicingLoc1 = new JRadioButton("on every song - width: ");
		rbVoicingLoc1.addActionListener(new AL());
		bg.add(rbVoicingLoc1);
		tfVoicingSize = new JTextField(2);
		tfVoicingSize.setText(options.get(OPT_VOICINGS_SIZE));
		tfVoicingSize.addKeyListener(new KL());
		vp.add(rbVoicingLoc1,BorderLayout.WEST);
		vp.add(tfVoicingSize,BorderLayout.CENTER);
		vp.add(new JLabel(" cm"),BorderLayout.EAST);
		vp.setMaximumSize(vp.getPreferredSize());
		vp.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(vp);
		
		rbVoicingLoc2 = new JRadioButton("at the end of the songbook in ");
		rbVoicingLoc2.addActionListener(new AL());
		bg.add(rbVoicingLoc2);
		vp=new JPanel();
		vp.setLayout(new BorderLayout());
		vp.add(rbVoicingLoc2,BorderLayout.WEST);
		tfVoicingCols = new JTextField(2);
		tfVoicingCols.setText(options.get(OPT_VOICINGS_COLS));
		tfVoicingCols.addKeyListener(new KL());
		vp.add(tfVoicingCols,BorderLayout.CENTER);
		vp.add(new JLabel(" column(s)"),BorderLayout.EAST);
		vp.setMaximumSize(vp.getPreferredSize());
		vp.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(vp);
        
		rbVoicingLoc3 = new JRadioButton("no voicings at all");
		rbVoicingLoc3.addActionListener(new AL());
		bg.add(rbVoicingLoc3);
		rbVoicingLoc3.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(rbVoicingLoc3);
		box.add( new JLabel(" "));
        
        // page margins
		tfLeft = new JTextField(2);
		tfLeft.setText(options.get(OPT_MARGIN_LEFT));
		tfLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfLeft.addKeyListener(new KL());
		tfRight = new JTextField(2);
		tfRight.setText(options.get(OPT_MARGIN_RIGHT));
		tfRight.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfRight.addKeyListener(new KL());
		tfTop = new JTextField(2);
		tfTop.setText(options.get(OPT_MARGIN_TOP));
		tfTop.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfTop.addKeyListener(new KL());
		tfBottom = new JTextField(2);
		tfBottom.setText(options.get(OPT_MARGIN_BOTTOM));
		tfBottom.setAlignmentX(Component.LEFT_ALIGNMENT);
		tfBottom.addKeyListener(new KL());
		l = new JLabel("Page margins:");
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(l);
		box.add( new JLabel(" "));
		vp=new JPanel();
		vp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0;
		vp.add(new JLabel("left:"),c); c.gridy++;
		vp.add(new JLabel("right:"),c); c.gridy++;
		vp.add(new JLabel("top:"),c); c.gridy++;
		vp.add(new JLabel("bottom:"),c); c.gridy++;
		c.gridx++; c.gridy = 0;
		vp.add(tfLeft,c); c.gridy++;
		vp.add(tfRight,c); c.gridy++;
		vp.add(tfTop,c); c.gridy++;
		vp.add(tfBottom,c); c.gridy++;
		c.gridx++; c.gridy = 0;
		vp.add(new JLabel(" cm"),c); c.gridy++;
		vp.add(new JLabel(" cm"),c); c.gridy++;
		vp.add(new JLabel(" cm"),c); c.gridy++;
		vp.add(new JLabel(" cm"),c); c.gridy++;
		vp.setMaximumSize(vp.getPreferredSize());
		vp.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(vp);
		
        box.add(Box.createVerticalGlue());
        dlg.add(box,BorderLayout.CENTER);
		
		String vv=options.get(OPT_VOICINGS_LOC);
		int v = (vv==null ? 0 : new Integer(vv));
		if (v<2) rbVoicingLoc1.setSelected(true);
		else if (v==2) rbVoicingLoc2.setSelected(true);
		else if (v==3) rbVoicingLoc3.setSelected(true);
		
		
		return dlg;
	}
	
	private final class AL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			String key="";
			String val="0";
			if(ae.getSource()==rbVoicingLoc1){
				key=OPT_VOICINGS_LOC;
				val="1";
			} else if(ae.getSource()==rbVoicingLoc2) {
				key=OPT_VOICINGS_LOC;
				val="2";
			} else if(ae.getSource()==rbVoicingLoc3) {
				key=OPT_VOICINGS_LOC;
				val="3";
			}
			final String k=key;
			final String v=val;
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){ options.put(k,v); }
			});
		}
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
					if(src==tfVoicingCols)  {
						key=OPT_VOICINGS_COLS;
						val = src.getText();
					} else if( ke.getSource()==tfVoicingSize) {
						key=OPT_VOICINGS_SIZE;
						val = src.getText();
					} else if( ke.getSource()==tfTitleDefault) {
						key=OPT_TITLE_DEFAULT;
						val = src.getText();
					} else if( ke.getSource()==tfLeft) {
						key=OPT_MARGIN_LEFT;
						val = src.getText();
					} else if( ke.getSource()==tfRight) {
						key=OPT_MARGIN_RIGHT;
						val = src.getText();
					} else if( ke.getSource()==tfTop) {
						key=OPT_MARGIN_TOP;
						val = src.getText();
					} else if( ke.getSource()==tfBottom) {
						key=OPT_MARGIN_BOTTOM;
						val = src.getText();
					}
					if(!key.equals("")) options.put(key,val); 
				}
			});
	    }
	}	

	@Override
	public void setDefaults(Properties prop){
		String s=OPT_VOICINGS_SIZE, v="2.0";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_VOICINGS_LOC; v="1";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_VOICINGS_COLS; v="3";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_TITLE_DEFAULT; v="my Songbook";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_MARGIN_LEFT; v="1.9";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_MARGIN_RIGHT; v="0.5";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_MARGIN_TOP; v="1.0";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		s=OPT_MARGIN_BOTTOM; v="1.0";
		if (!prop.containsKey(s)) prop.setProperty(s,v); options.put(s, prop.getProperty(s));
		saveOptions();
	}
	
    /** A listener shared by the text field and add button. */
    class TL implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
		   	DefaultMutableTreeNode n=(DefaultMutableTreeNode) ft.getTree().getLastSelectedPathComponent();
		}	
    }
 
	
}
