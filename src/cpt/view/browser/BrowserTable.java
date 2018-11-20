package cpt.view.browser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import view.UdoMvcViewEventListener;
import cpt.control.Control;
import cpt.view.View;

/**
 * Represents the browser table
 * @author Udo
 *
 */
public class BrowserTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	private Control listener;
	private BrowserTableModel model;
	
	/**
	 * Constructor. creates the browser table
	 */
    public BrowserTable() {
		super(new BrowserTableModel());
		model = getModel();

		setAutoCreateColumnsFromModel(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		setAutoCreateRowSorter(true);
		setRowSorter(new BrowserTableRowSorter(model));
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		        if(listener!=null) listener.updateMenuStatus();
		    }
		});
		
	    setDefaultRenderer(Object.class, new BrowserTableCellRenderer());
	    setDefaultEditor(Object.class, new BrowserTableCellEditor());

	    
		// create a popup menu for the table header
	    // add one item to brig up the column customization dialog
		final JPopupMenu popupMenu = new JPopupMenu();
		final String INSERT_CMD = "Customize Columns...";		
		JMenuItem menuItem = new JMenuItem(INSERT_CMD);
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				View.options.selectDlg(View.bocd);
				View.options.setVisible(true);
			}		    	
		});
		popupMenu.add(menuItem);
		
		// add a mouse listener to the header to bring up the popup menu
		getTableHeader().addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) { performHeaderAction( e ); }
			public void mouseReleased(MouseEvent e) { performHeaderAction( e ); }
			public void mouseClicked(MouseEvent e) { performHeaderAction( e ); }
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			private void performHeaderAction(MouseEvent e) { 
				if (e.isPopupTrigger()){ 
					popupMenu.show(getTableHeader(), e.getX(), e.getY()); }
				}
		});
		
		// add a mouse listener to the table body to bring up a row popup or to edit selected song(s)
	    addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) { performRowAction( e ); }
			public void mouseReleased(MouseEvent e) { performRowAction( e ); }
			public void mouseClicked(MouseEvent e) { performRowAction( e ); }
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			private void performRowAction(MouseEvent e) {
	        	if(e.isPopupTrigger()) {
	                JTable source = (JTable)e.getSource();
	                int row = source.rowAtPoint( e.getPoint() );
	                int column = source.columnAtPoint( e.getPoint() );
	     
	                if (! source.isRowSelected(row))
	                    source.changeSelection(row, column, false, false);
	                
	                listener.getView().getBrowser().createEditPopup().show(e.getComponent(), e.getX(), e.getY());
	            } else if(e.getClickCount()==2){
	                JTable source = (JTable)e.getSource();
	                int row = source.rowAtPoint( e.getPoint() );
	                listener.getView().editItem(convertRowIndexToModel(row),false);
	        	}
			}
		});
	}

    @Override
	public BrowserTableModel getModel() {
		return (BrowserTableModel) super.getModel();
	}

    public void setListener(Control listener){
		this.listener = listener;
    }
    
    public UdoMvcViewEventListener getListener(){
    	return listener;
    }

}
