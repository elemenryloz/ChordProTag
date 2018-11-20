package cpt.view.browser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import cpt.model.ModelSong;

public class BrowserTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Vector<ModelSong> lib = new Vector<ModelSong>();
	private List<BrowserTableColumn> cols;

	public BrowserTableModel(){
		super();
		cols=new ArrayList();
	}
	
	public Vector<ModelSong> getLib() {
		return lib;
	}

	public String[] getUsedKeys(int[] items){
    	HashMap<String, Boolean> tag=new HashMap<String, Boolean>();
    	if(items==null){
    		items=new int[lib.size()];
    		for (int i=0; i<lib.size(); i++){
    			items[i]=i;
    		};
    	}
    	for (int i: items){
			ModelSong item = lib.get(i);
			for (String t: item.meta.keySet()) {
				if(tag.get(t)==null) tag.put((String)t, true);
			}
    	}
    	List<String> sortedKeys=new ArrayList<String>(tag.keySet());
   		Collections.sort(sortedKeys);
		return (String[])sortedKeys.toArray(new String[0]);
	}
	
	public void addColumn(int columnNumber, BrowserTableColumn column) {
		cols.add(columnNumber, column);
//		fireTableStructureChanged();
	}

	public void addColumn(BrowserTableColumn column) {
		cols.add(column);
//		fireTableStructureChanged();
	}

	public void removeColumn(int columnNumber) {
		cols.remove(columnNumber);
//		fireTableStructureChanged();
	}

	public void removeColumn(BrowserTableColumn column) {
		cols.remove(column);
//		fireTableStructureChanged();
	}
	
	public void removeAllColumns() {
		cols.clear();
//		fireTableStructureChanged();
	}
	
	public BrowserTableColumn getColumn(int index) {
		return cols.get(index);
	}
	
	
	@Override public boolean isCellEditable(int row, int column) { return !((String)((BrowserTableColumn)cols.get(column)).getIdentifier()).startsWith("_"); }

	@Override public int getColumnCount() {	return cols.size(); }

	@Override public String getColumnName(int col) { return cols.get(col).getHeaderValue().toString(); }

	@Override public int getRowCount() { return lib.size(); }

	@Override public Object getValueAt(int row, int col) { 
		if(row>=lib.size() || col >= cols.size()) return null;
		BrowserTableColumn c = cols.get(col);
		String key=(String) c.getIdentifier();
		ModelSong item = lib.get(row);
		Object v = item.meta.get(key);
		return v;
	}
}
