package cpt.view.browser;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;

import cpt.model.ModelSong;


public class BrowserTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	JLabel label=(JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	String val = (String) value;
		int mrow = table.convertRowIndexToModel( row );
        BrowserTableModel model = (BrowserTableModel) table.getModel();
        ModelSong item = model.getLib().get(mrow);
    	if(val!=null) {
    		int i=val.indexOf("\n");
            if (i!=-1){
            	Border border=label.getBorder();
            	Font fn = label.getFont();
            	label = new JLabel(){
            		@Override
            		public void paint(Graphics g){
            			super.paint(g);
            			Graphics2D g2=(Graphics2D)g; 
            			Stroke s=new BasicStroke(2.0f);
						g2.setStroke(s);
						g2.setColor(Color.BLUE);
						int w=getWidth();
						int h=getHeight();
						int l=h/2;
						int l2=h/4;
						g.fillRect(w-l, 0, l, l);
						g2.setColor(Color.WHITE);
            			g.drawLine(w-l, l2, w, l2);
            			g.drawLine(w-l2, 0, w-l2, l);
            		}
            	};
            	label.setOpaque(true);
            	label.setFont(fn);
            	label.setBorder(border);
	            label.setText(val.substring(0,i));
	            label.setToolTipText("<html>"+val.replaceAll("\\n","<br>")+"</html>");
            } else {
	            label.setToolTipText(val);
            }
    	}
        if(!isSelected){
        	label.setForeground(item.isModified() ? Color.RED : table.getForeground() );
        	if(item.isOpen()) label.setBackground(Color.YELLOW);
        	else if(item.isAdded()) label.setBackground(new Color(255,220,220));
        	else label.setBackground(row % 2 == 0 ? new Color(0,0,0,30) : new Color(0,0,0,0) );
        }
        return label;
    }
}
