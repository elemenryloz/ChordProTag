package cpt.view.filerenamer;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Stack;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import parser.UdoParser;
import parser.UdoParserChoice;
import parser.UdoParserList;
import parser.UdoParserListener;
import parser.UdoParserRule;
import parser.UdoParserTerminal;
import cpt.model.ModelSong;
import cpt.view.View;
import cpt.view.browser.BrowserTable;
import cpt.view.browser.BrowserTableModel;
import cpt.view.formatter.FormatterFunction;
import cpt.view.formatter.FormatTag;
import cpt.view.formatter.FormatText;
import cpt.view.formatter.FormatToken;
import cpt.view.formatter.Formatter;

public class FileRenamer extends JDialog {

	static FileRenamer inst;
	static Formatter tree; 

	
	private View view;
	
    
    private JTextField eFmt=new JTextField(30);
    private JTable tPreview=new JTable();
    private	DefaultTableModel model=new DefaultTableModel();
    private int[] items = {};
    private JButton bOK=new JButton("OK");
	private FmtEntryListener fmtListener;
	private String mode="_file_name";

	public FileRenamer(View view, String title){
		super();

		this.view=view;
		
		final BrowserTable bptable = view.getBrowserTable();
    	bptable.editingCanceled(null);
    	items=bptable.getSelectedRows();
    	String[] usedKeys = bptable.getModel().getUsedKeys(items);

		this.setTitle(title);
        this.setModal(true);
        this.setLayout(new BorderLayout());

		Box box = Box.createVerticalBox();
        box.add(new JLabel(" "));
        
        // mode
        ActionListener modeListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mode=e.getActionCommand();
	            fmtListener.keyTyped(null);
			}
        	
        };
        ButtonGroup bg = new ButtonGroup();
        Box pMode = Box.createHorizontalBox();
        JRadioButton bFileName=new JRadioButton("File name ");
        bFileName.setSelected(true);
        bFileName.addActionListener(modeListener);
        bFileName.setActionCommand("_file_name");
        pMode.add(bFileName);
		bg.add(bFileName);
		JRadioButton bFileNameEx=new JRadioButton("File extension ");
        bFileNameEx.addActionListener(modeListener);
        bFileNameEx.setActionCommand("_file_extension");
        pMode.add(bFileNameEx);
		bg.add(bFileNameEx);
		JRadioButton bFilePath=new JRadioButton("File path ");
        bFilePath.addActionListener(modeListener);
        bFilePath.setActionCommand("_file_path");
        pMode.add(bFilePath);
		bg.add(bFilePath);
		JRadioButton bDirPath=new JRadioButton("Directory path ");
        bDirPath.addActionListener(modeListener);
        bDirPath.setActionCommand("_file_directory_path");
        pMode.add(bDirPath);
		bg.add(bDirPath);
		pMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(pMode);
        box.add(new JLabel(" "));
        
        // format
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenu menu = new JMenu("tags");
		popupMenu.add(menu);
		JMenu menui = new JMenu("file info");
		menu.add(menui);
		for(final String s: usedKeys){
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					int pos = eFmt.getCaretPosition();
		            try {
		            	if(eFmt.getSelectedText()!=null) {
		            		eFmt.getDocument().remove(eFmt.getSelectionStart(), eFmt.getSelectionEnd()-eFmt.getSelectionStart()+1);
		            	}
		                eFmt.getDocument().insertString(pos, "%"+s+"%", null);
		            } catch(Exception ex) {
		            }
		            eFmt.requestFocus();
		            fmtListener.keyTyped(null);
				}		    	
			});
			if(s.startsWith("_")) menui.add(menuItem);
			else menu.add(menuItem);
		}
		menu = new JMenu("functions");
		popupMenu.add(menu);
		for(final String s: FormatterFunction.FMT_FUNCTIONS){
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					int st = s.indexOf("(");
					int en = s.indexOf(")");
					int pos = eFmt.getCaretPosition();
		            try {
		            	if(eFmt.getSelectedText()!=null) {
		            		eFmt.getDocument().remove(eFmt.getSelectionStart(), eFmt.getSelectionEnd()-eFmt.getSelectionStart()+1);
		            	}
		                eFmt.getDocument().insertString(pos, s, null);
		            } catch(Exception ex) {
		            }
		            eFmt.requestFocus();
		            eFmt.setSelectionStart(pos+st+1);
		            eFmt.setSelectionEnd(pos+en);
		            fmtListener.keyTyped(null);
				}		    	
			});
			menu.add(menuItem);
		}
		
		JMenuItem menuItem = new JMenuItem("file system object...");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				if(fc.showDialog(null, "Select")==JFileChooser.APPROVE_OPTION){
					int pos = eFmt.getCaretPosition();
		            try {
		            	if(eFmt.getSelectedText()!=null) {
		            		eFmt.getDocument().remove(eFmt.getSelectionStart(), eFmt.getSelectionEnd()-eFmt.getSelectionStart()+1);
		            	}
		                eFmt.getDocument().insertString(pos, fc.getSelectedFile().getAbsolutePath(), null);
		            } catch(Exception ex) {
		            }
		            eFmt.requestFocus();
		            fmtListener.keyTyped(null);
				}
			}		    	
		});
		popupMenu.add(menuItem);
        final JButton bFmt=new JButton(">");
        bFmt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
		          popupMenu.show(bFmt, bFmt.getHeight(), bFmt.getWidth()/2);
			}
		});
        bFmt.setMaximumSize(new Dimension(Integer.MAX_VALUE, bFmt.getPreferredSize().height));
        JPanel p=new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Format string: "),BorderLayout.WEST);
        p.add(eFmt,BorderLayout.CENTER);
        p.add(bFmt,BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        eFmt.setMaximumSize(new Dimension(Integer.MAX_VALUE, eFmt.getPreferredSize().height));
        eFmt.addKeyListener(fmtListener=new FmtEntryListener());
		box.add(p);
        box.add(new JLabel(" "));
		
		// preview
    	model.addColumn("Old file");
    	model.addColumn("New file");
    	for (final int row: items){
			ModelSong item = bptable.getModel().getLib().get(row);
			model.addRow( new String[]{item.meta.get(mode),""});
    	}
    	tPreview.setModel(model);
    	JScrollPane sp = new JScrollPane(tPreview);
    	sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(sp);
        box.add(new JSeparator(JSeparator.HORIZONTAL));
        add(box,BorderLayout.CENTER);
        
        box = Box.createHorizontalBox();
        bOK.setEnabled(false);
        bOK.setMnemonic(KeyEvent.VK_O);
        bOK.addActionListener(new OKButtonListener());
        box.add(bOK);
        JButton button=new JButton("Cancel");
        button.setMnemonic(KeyEvent.VK_ESCAPE);
        button.addActionListener(new CancelButtonListener());
        box.add(button);
        button=new JButton("Help");
        button.setMnemonic(KeyEvent.VK_F1);
        button.addActionListener(new HelpButtonListener());
        box.add(button);		
        add(box,BorderLayout.SOUTH);
        
        this.pack();
//        setLocationRelativeTo(Control.frame);
        FileRenamer.inst = this;
        
   		tree = new Formatter();

	}

	class FmtEntryListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent e) {
			if(e!=null) super.keyPressed(e);
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					bOK.setEnabled(eFmt.getText().length()>0);
       	    		tree.parse(eFmt.getText());
		        	int size = items.length;
		        	if(size>0) { 
			        	model.setRowCount(0);
						BrowserTable bptable = view.getBrowserTable();
			        	for (final int row: items){
		       				ModelSong item = bptable.getModel().getLib().get(row);
		       	        	String newVal = tree.toString(item);
							model.addRow( new String[]{item.meta.get(mode),newVal});
			        	}
		        	}
				}
			});
		}
    }

	class OKButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
       		int rc = JOptionPane.showConfirmDialog(null, "rename "+items.length+ " files?","Converter",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
       		if(rc!=JOptionPane.OK_OPTION) return;
        	
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
					BrowserTable bptable = view.getBrowserTable();
            		BrowserTableModel bpmodel =(BrowserTableModel)bptable.getModel();
	            	int row=0;
	            	for (int bprow: items){
	                    int mrow = bptable.convertRowIndexToModel( bprow );
	                    ModelSong item = bpmodel.getLib().get(mrow);
	            		String val=(String) tPreview.getValueAt(row, 1);
	            		if(mode.equals("_file_name")){
	            			if (item.meta.get("_file_directory_path")!=null)
	            				val=item.meta.get("_file_directory_path")+File.separator+val;
	            			val+="."+item.meta.get("_file_extension");
	            		} else if(mode.equals("_file_extension")){
	            			val = item.meta.get("_file_name")+"."+val; 
	            			if (item.meta.get("_file_directory_path")!=null)
	            				val=item.meta.get("_file_directory_path")+File.separator+val;
	            		} else if(mode.equals("_file_directory_path")){
	            			val+=File.separator+item.meta.get("_file_name_ex"); 
	            		} 
	            		item.rename(new File(val).toPath());
	                    bpmodel.fireTableRowsUpdated(mrow, mrow);
	    		        row++;
	            	}
		        	setVisible(false);
	            }
	        });		        
			
        }
    }
 
    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	setVisible(false);
        }
    }
    
    class HelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        }
    }
 
}
