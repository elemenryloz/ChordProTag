package cpt.view.browser.options.columns;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import cpt.view.browser.Browser;
import cpt.view.browser.BrowserTableColumn;
import cpt.view.browser.BrowserTableModel;

public class BrowserOptionsColumnsTable extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTable tCols;
	private TabelModel mCols;
	private JButton addButton, deleteButton, upButton, downButton;
    private BrowserTableModel mBrowserTable;
    private TableColumnModel mBrowserTableColumns;
    private JComponent dlg;
    private JComboBox presets;
	private ArrayList<TableColumn> omCols;

	public void setPresets(JComboBox presets){
		this.presets=presets;
	}
	
	BrowserOptionsColumnsTable(JComponent dlg){
		this.dlg=dlg;
		this.mBrowserTable=Browser.table.getModel();
		this.mBrowserTableColumns=Browser.table.getColumnModel();
		
		setLayout(new BorderLayout());
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
        mCols = new TabelModel();
        tCols = new JTable(mCols);
        tCols.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tCols.setDefaultRenderer(Object.class, new CellRenderer());
        tCols.setDefaultEditor(Object.class, new CellEditor());
        tCols.getSelectionModel().addListSelectionListener(new SelectionListener());
        JScrollPane sp = new JScrollPane(tCols);
        sp.getViewport().setBackground(Color.WHITE);
      //  sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(sp,BorderLayout.CENTER);
        
		Box p=Box.createHorizontalBox();
		addButton=new JButton("Add");
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.addActionListener(this);
		p.add(addButton);
        deleteButton=new JButton("Remove");
        deleteButton.setMnemonic(KeyEvent.VK_R);
        deleteButton.addActionListener(this);
		p.add(deleteButton);
        upButton=new JButton("Move up");
        upButton.setMnemonic(KeyEvent.VK_U);
        upButton.addActionListener(this);
		p.add(upButton);
        downButton=new JButton("Move down");
        downButton.setMnemonic(KeyEvent.VK_D);
        downButton.addActionListener(this);
		p.add(downButton);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(p, BorderLayout.SOUTH);
	}
	@Override
    public void actionPerformed(ActionEvent e) {
    	
        ListSelectionModel lsm = tCols.getSelectionModel();
        final int firstSelected = lsm.getMinSelectionIndex();
        final int lastSelected = lsm.getMaxSelectionIndex();
        int size = mCols.getRowCount();

        if (e.getSource()==addButton) {
			addColumns(lastSelected, size);
        } else if (e.getSource()==deleteButton) {
            deleteColumns(firstSelected, lastSelected);
        } else if (e.getSource()==upButton) {
            moveColumnsUp(lsm, firstSelected, lastSelected);
        } else if (e.getSource()==downButton) {
            moveColumnsDown(lsm, firstSelected, lastSelected);
        }

    }

	
    private void addColumns(final int lastSelected, int size) {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		p.add(new JLabel("Ttle: "),c);
		c.gridy=1;
		p.add(new JLabel("Tag: "),c);
		JTextField title=new JTextField(20);
		Border border = BorderFactory.createEtchedBorder();
		title.setBorder(border);
		c.gridx=1;
		c.gridy=0;
		p.add(title,c);
		c.gridy=1;
		JComboBox<String> tag = new JComboBox<String>();
		tag.setBorder(border);
		tag.setEditable(true);
		tag.setPreferredSize(new Dimension(title.getPreferredSize().width,tag.getPreferredSize().width));
		tag.removeAllItems();
		for (String t: mBrowserTable.getUsedKeys(null)) {
			tag.addItem(t);
		}

		p.add(tag,c);
		JPanel pp = new JPanel();
		pp.setLayout(new BorderLayout());
		pp.add(new JLabel("Enter title and tag:"),BorderLayout.NORTH);
		pp.add(p,BorderLayout.CENTER);
		if(JOptionPane.showConfirmDialog( dlg, p, "New Column", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
		    int newIx = lastSelected==-1 ? size-1 : lastSelected+1;
		    String titleText=title.getText().trim();
		    String tagText=((String)tag.getSelectedItem()).trim();
		    
		    if(tagText.length()>0){
		    	if(titleText.length()==0) titleText=tagText;
		        BrowserTableColumn col = new BrowserTableColumn(tagText, titleText);
		        mCols.addColumn(col,newIx);
//		        presets.setSelectedItem(null);
		    } else {
		        Toolkit.getDefaultToolkit().beep();
		    }
		}
	}


	private void deleteColumns(final int firstSelected, final int lastSelected) {
		for(int i=firstSelected; i<=lastSelected; i++){
			mCols.deleteColumn(firstSelected);
		}
		if(mCols.getRowCount()==0) deleteButton.setEnabled(false);
//        presets.setSelectedItem(null);
		
	}


	private void moveColumnsDown(ListSelectionModel lsm, final int firstSelected, final int lastSelected) {
		int moveTo = lastSelected+1;
		for (int moveMe=lastSelected; moveMe>=firstSelected; moveMe--) {    
			mBrowserTableColumns.moveColumn(moveMe, moveTo);
			moveTo--;
		}
		lsm.setSelectionInterval(firstSelected+1, lastSelected+1);
		Rectangle rect = tCols.getCellRect(lastSelected+1, 0, true);
		tCols.scrollRectToVisible(rect);
//        presets.setSelectedItem(null);
	}


	private void moveColumnsUp(ListSelectionModel lsm, final int firstSelected,
			final int lastSelected) {
		int moveTo = firstSelected-1;
		for (int moveMe=firstSelected; moveMe<=lastSelected; moveMe++) {    
		    mBrowserTableColumns.moveColumn(moveMe,moveTo);
			moveTo++;
		}
		lsm.setSelectionInterval(firstSelected-1, lastSelected-1);
		Rectangle rect = tCols.getCellRect(firstSelected-1, 0, true);
		tCols.scrollRectToVisible(rect);
//        presets.setSelectedItem(null);
	}


	class TabelModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		public void addColumn(BrowserTableColumn col, int newIndex){
    		addColumn(col, newIndex, 100);
    	}
    	
    	public void addColumn(BrowserTableColumn col, int newIndex, int width){
        	mBrowserTable.addColumn(col);
        	TableColumn tcol = new TableColumn(mBrowserTable.getColumnCount()-1);
        	tcol.setHeaderValue(col.getHeaderValue());
        	tcol.setPreferredWidth(width);
        	mBrowserTableColumns.addColumn(tcol);
        	mBrowserTableColumns.moveColumn(mBrowserTableColumns.getColumnCount()-1, newIndex);
            mCols.fireTableRowsInserted(newIndex, newIndex);
            tCols.setRowSelectionInterval(newIndex, newIndex);
    	}

    	public TableColumn deleteColumn(int col){
    		TableColumn tcol = mBrowserTableColumns.getColumn(col);
        	mBrowserTableColumns.removeColumn(tcol);
            int size = mBrowserTableColumns.getColumnCount();
            if(size>0){
    	        if (col == size) tCols.setRowSelectionInterval(col-1, col-1);
    	        else tCols.setRowSelectionInterval(col,col);
            }
            tCols.repaint();
            return tcol;
    	}
    	
    	
		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return mBrowserTableColumns.getColumnCount();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (omCols==null) {
				omCols = new ArrayList<TableColumn>();
		    	for( int c1=0; c1<mBrowserTableColumns.getColumnCount(); c1++) {
		    		TableColumn tcol = mBrowserTableColumns.getColumn(c1);
		    		TableColumn tcolo = new TableColumn(tcol.getModelIndex());
		    		tcolo.setHeaderValue(tcol.getHeaderValue());
		    		tcolo.setPreferredWidth(tcol.getWidth());
		    		omCols.add(tcolo);
		    	}
		        tCols.getSelectionModel().setSelectionInterval(0, 0);
			}

			int r=Browser.table.convertColumnIndexToModel(row);
//			TableColumn tcol=mBrowserTable.getColumn(row);
			BrowserTableColumn col = mBrowserTable.getColumn(r);
			if(column==0) return col.getHeaderValue();
			if(column==1) return col.getIdentifier();
			return Browser.table.getColumn(col.getHeaderValue()).getPreferredWidth(); 
		}

		@Override
    	public void setValueAt(Object value, int row, int column){
        	BrowserTableColumn col;
    		if(column==0){
            	col=new BrowserTableColumn(""+mCols.getValueAt(row, 1), ""+value, (int)mCols.getValueAt(row, 2));
    		} else if(column==1){
            	col=new BrowserTableColumn(""+value,""+mCols.getValueAt(row, 0), (int)mCols.getValueAt(row, 2));
    		} else {
            	col=new BrowserTableColumn(""+mCols.getValueAt(row, 1),""+mCols.getValueAt(row, 0),new Integer((String)value));
    		}
        	TableColumn tcol=mCols.deleteColumn(row);
        	mCols.addColumn(col,row,col.getPreferredWidth());
    	}
    	
		@Override
		public String getColumnName(int col) {
		    return new String[]{"Column title", "Chordpro Tag", "Width"}[col];
		}
		
		@Override public boolean isCellEditable(int row, int column) { return true; }

	};
	
    class CellEditor extends DefaultCellEditor {

    	private static final long serialVersionUID = 1L;
		
    	String val;
    	JTextField title=null;
    	JComboBox<String> tag;
    	int row,col;
    	
    	public CellEditor() {
    		super(new JTextField());
    		
    		tag = new JComboBox<String>(mBrowserTable.getUsedKeys(null));
    		tag.setEditable(true);
            this.setClickCountToStart(2);
    	}
    	
    	
    	@Override
    	public Object getCellEditorValue() {
    		if(col==0) return title.getText();
    		return tag.getEditor().getItem();
    	}

    	@Override
    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    		this.row=row;
    		this.col=column;
    		if(col==0){
    			title=(JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
    			title.setBackground(Color.red);
//    			title.setText(""+value);
    			return title;
    		} else {
	    		tag.setSelectedItem(""+value);
	    		return tag;
    		}
    	}
    }
    
    class CellRenderer extends DefaultTableCellRenderer{

    	private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable list, Object value, boolean isSelected, boolean hasFocus, int col, int row) {
			JLabel component=(JLabel) super.getTableCellRendererComponent(list,value,isSelected,hasFocus,col,row);
			component.setText(value+"");
			return component;
		}
    	
    }
    
    class SelectionListener implements ListSelectionListener {
    	
    	@Override
    	public void valueChanged(ListSelectionEvent e) {
    		if (e.getValueIsAdjusting() == false) {

    			int[] selCols = tCols.getSelectedRows();
    			deleteButton.setEnabled(selCols.length>0);
    			upButton.setEnabled(selCols.length>0 && !tCols.isRowSelected(0));
    			downButton.setEnabled(selCols.length>0 && !tCols.isRowSelected(mCols.getRowCount()-1));
            }
    	 }	
    }

	public String getColumns() {
		String columns="";
		for(int c=0; c<mBrowserTableColumns.getColumnCount(); c++){
			TableColumn tcol=mBrowserTableColumns.getColumn(c);
			int mi=tcol.getModelIndex();
			columns+=":"+tcol.getHeaderValue()+":"+tcol.getWidth()+":"+mBrowserTable.getColumn(mi).getIdentifier();
		}
		return columns;
	}
	public void setColumns(String columns) {
		while (mBrowserTableColumns.getColumnCount()>0){
			mCols.deleteColumn(0);
		}
		String[] cols=columns.split(":");
		if(presets!=null) presets.setSelectedItem(cols[0]);
		for( int c=1; c<cols.length; c+=3) {
			BrowserTableColumn col = new BrowserTableColumn(cols[c+2],cols[c]);
			mCols.addColumn(col,mBrowserTableColumns.getColumnCount(),Integer.parseInt(cols[c+1]));
		}
		tCols.getSelectionModel().setSelectionInterval(0, 0);
		mCols.fireTableStructureChanged();
	}

	public void cancelOptions(){
   		Browser.table.setColumnModel(new DefaultTableColumnModel());
    	for(int t=0; t<omCols.size(); t++){
    		TableColumn tcol = omCols.get(t);
        	Browser.table.addColumn(tcol);
    	}
    	omCols=null;
		
	}

}
