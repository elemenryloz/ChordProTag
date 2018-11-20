package cpt.view.browser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class BrowserTableRowSorter extends TableRowSorter< TableModel > {
	
	public BrowserTableRowSorter(TableModel model){
		super(model);
	}
	
	@Override
	public void toggleSortOrder(int column) {
		if(column >= 0 && column < getModelWrapper().getColumnCount() && isSortable(column)) {
			List< SortKey > keys = new ArrayList< SortKey >(getSortKeys());
			if(!keys.isEmpty()) {
				SortKey sortKey = keys.get(0);
				if(sortKey.getColumn()==column && sortKey.getSortOrder()==SortOrder.DESCENDING) {
					setSortKeys(null);
					return;
				}
			}
	    }
	    super.toggleSortOrder(column);
	}
}
