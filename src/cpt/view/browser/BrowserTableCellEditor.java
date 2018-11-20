package cpt.view.browser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import cpt.model.ModelSong;

public class BrowserTableCellEditor extends DefaultCellEditor implements ActionListener, TableCellEditor {

	final static int[] EMPTY_ARRAY = {};
	
	JDialog dia;
	JTextArea ta;
	JButton button,ok,cancel;
	JTable table;
	int row,col;
	String val;
    int selectedRows[] = {};
    int rowToEdit;
	int columnToEdit;
	    

	
	public BrowserTableCellEditor() {
		super(new JTextField());
		button = new JButton();
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setActionCommand("EDIT");
	    button.addActionListener(this);		
		dia = new JDialog();
		dia.setTitle("Edit Tag");
		dia.addWindowListener(new WindowAdapter(){


			@Override
			public void windowClosing(WindowEvent arg0) {
	            fireEditingStopped();
			}
			
		});
		dia.setLayout(new BorderLayout());
		dia.add(ta=new JTextArea(),BorderLayout.CENTER);
		JPanel p=new JPanel();
		p.add(ok = new JButton("OK"));
		p.add(cancel = new JButton("CANCEL"));
		dia.add(p,BorderLayout.SOUTH);
		ok.addActionListener(this);
		cancel.addActionListener(this);
        dia.setSize(200,200);
        this.setClickCountToStart(2);
	}
	
	@Override
	public Object getCellEditorValue() {
		return table.getValueAt(row, col); 
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.table=table;
		this.row=row;
		this.col=column;
		this.val=""+value;
		ta.setText(""+value);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("EDIT".equals(e.getActionCommand())) {
			selectedRows = table.getSelectedRows();
			rowToEdit =  table.getEditingRow();
			columnToEdit = table.getEditingColumn();
			Point p = button.getLocationOnScreen();
	        dia.setLocation(p.x, p.y + button.getSize().height);
            dia.setVisible(true);

            //Make the renderer reappear.
            button.setText(val); 
    		button.setBackground(Color.GREEN);
        } else if ("OK".equals(e.getActionCommand())) {
            dia.setVisible(false);
//            table.setValueAt(ta.getText(), row, col); 
            int mrow = table.convertRowIndexToModel( row );
            int mcol = table.convertColumnIndexToModel( col );
            BrowserTableModel model = (BrowserTableModel) table.getModel();
            Vector<ModelSong> lib = model.getLib();
            ModelSong item = lib.get(mrow);
            item.meta.put(model.getColumn(mcol).getIdentifier(),ta.getText());
            item.setModified(true);
            model.fireTableRowsUpdated(mrow, mrow);
            fireEditingStopped();

	    } else if ("CANCEL".equals(e.getActionCommand())) {
            dia.setVisible(false);
	        selectedRows=EMPTY_ARRAY;
	        this.fireEditingCanceled();
	    }
	}

}
