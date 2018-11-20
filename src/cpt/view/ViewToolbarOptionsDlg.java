package cpt.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import view.options.UdoMvcViewOptionsDlgItem;
import view.toolbar.UdoMvcViewToolBar;
import view.toolbar.UdoMvcViewToolBarItem;


public class ViewToolbarOptionsDlg extends UdoMvcViewOptionsDlgItem {

	static final String OPT_CHORD_TITLE_COLOR = "odt.chord.title.color";
	static final String OPT_CHORD_FRET_COLOR = "odt.chord.fret.color";

	private static BufferedImage imgSpace;
	private static UdoMvcViewToolBarItem sep;
	private static UdoMvcViewToolBarItem sp;
	private static UdoMvcViewToolBarItem fl;

	{
		try {
			imgSpace = ImageIO.read(View.class.getResource("/cpt/view/images/space.png"));
			sep=new UdoMvcViewToolBarItem(imgSpace.getScaledInstance(10, 24, Image.SCALE_SMOOTH),null);
			sp=new UdoMvcViewToolBarItem(imgSpace.getScaledInstance(24, 24, Image.SCALE_SMOOTH),null);
			fl=new UdoMvcViewToolBarItem(imgSpace.getScaledInstance(24, 24, Image.SCALE_SMOOTH),null);
			sep.setName("SEPARATOR");
			sep.setToolTipText("Separator");		
			sp.setName("SPACE");
			sp.setToolTipText("Space");
			fl.setName("FLEX");
			fl.setToolTipText("Flexible Space");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	
	
	
	Vector<Component> v = new Vector<Component>();
	
	JTextField tf;
	private JPanel ft;
	
	private UdoMvcViewToolBarItem[] possibleTools;
	private JToolBar fakeToolbar;
	
	public ViewToolbarOptionsDlg(String displayName, UdoMvcViewToolBarItem[] possibleTools) {
		super(displayName);
		this.possibleTools = possibleTools;
	}

	public JPanel getDlg(){

		JPanel dlg = new JPanel();
        dlg.setLayout(new BorderLayout());
        
        
		ft = new JPanel();
		for(UdoMvcViewToolBarItem c: possibleTools){
			ft.add(c);
		}
		ft.add(sep);
		ft.add(sp);
		ft.add(fl);
		dlg.add(new JScrollPane(ft),BorderLayout.CENTER);

		fakeToolbar = new FakeTB();
		for(Component c: v){
			fakeToolbar.add(c);
		}
		fakeToolbar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 24));
		dlg.add(fakeToolbar,BorderLayout.NORTH);
		
		return dlg;
		
	}
	class FakeTB extends JToolBar implements DropTargetListener {
		private DropTarget dropTarget;
		private Component tempComp;

        public FakeTB() {
        	dropTarget = new DropTarget(this, DnDConstants.ACTION_MOVE, this, true, null);
        	setFloatable(false);
        }

		@Override
		public void dragEnter(DropTargetDragEvent e) {
			dragOver(e);
		}

		@Override
		public void dragExit(DropTargetEvent arg0) {
			if(tempComp!=null) { 
				remove(tempComp);
				resize(getWidth(),getHeight());
				tempComp=null;
			}
		}

		@Override
		public void dragOver(DropTargetDragEvent e) {
			
			UdoMvcViewToolBarItem tbitem = null;
			try {
				tbitem = (UdoMvcViewToolBarItem) e.getTransferable().getTransferData(UdoMvcViewToolBarItem.myDataFlavor);
			} catch (Exception e1) {
				e.rejectDrag();
				return;
			}
			
/*			if(tbitem.isOnToolbar() || e.getDropAction()!=DnDConstants.ACTION_MOVE) {
				return;
			}
	*/		e.acceptDrag(DnDConstants.ACTION_MOVE);
			if(tempComp==null) {
				tempComp = tbitem.clone();
				((UdoMvcViewToolBarItem)tempComp).setOnToolbar(true);
				add(tempComp);
				resize(getWidth(),Math.max(tempComp.getHeight(),getHeight()));
			}
			Point p = e.getLocation();
			p = SwingUtilities.convertPoint(((DropTarget)e.getSource()).getComponent(),	p, FakeTB.this );			
			Component c=getComponentAt(p);
			if(c!=this){
				if(!tempComp.equals(c)){
					remove(tempComp);
					int ix=getComponentIndex(c);
					Vector<Component> v= new Vector<Component>();
					Component comp;
					for(int cc=this.getComponentCount()-1; cc>=ix; cc--){
						comp=getComponent(cc);
						v.add(comp);
						remove(comp);
					}
					add(tempComp);
					for(int cc=v.size()-1; cc>=0; cc--){
						add(v.elementAt(cc));
					}
				}
			} else  {
				remove(tempComp);
				add(tempComp);
			}
			revalidate();
			repaint();
		}

		@Override
		public void resize(int x, int y){
			
			Insets i = getMargin();
			int left = x-i.left-i.right-10;
			
			int numFlex=0;
			for (Component c: getComponents()){
				if(c.getName().equals("FLEX")){
					if (c.isVisible()) numFlex++;
				} else {
					int cx = c.getWidth();
					left-=cx;
				}
			}
			if(numFlex>0){
				int fx=left/numFlex;
				for (Component c: getComponents()){
					if(c.getName().equals("FLEX")){
						c.setMinimumSize(new Dimension(fx,y));
						c.setPreferredSize(new Dimension(fx,y));
						c.setMaximumSize(new Dimension(fx,y));
					}
				}
			}
			revalidate();
			repaint();
			layout();
		}
		
		@Override
		public void drop(DropTargetDropEvent e) {
			tempComp=null;
			e.acceptDrop(DnDConstants.ACTION_COPY);
			e.dropComplete(true);
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent arg0) {
		}
	}
	
	@Override
	public void setDefaults(Properties prop){
		
		UdoMvcViewToolBar toolbar=this.getView().getToolbar();
		v.clear();
		for (Component c: toolbar.getComponents()){
			for( UdoMvcViewToolBarItem i: possibleTools){
				if(i.getComponent()==c){
					v.add(i);
					break;
				}
			}
			if(c instanceof JToolBar.Separator) {
				c.setName("SEPARATOR");
				v.add(sep.clone());
			}
		}
		
		
		/*
		String s = OPT_CHORD_TITLE_COLOR; Color c = Color.RED;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		s = OPT_CHORD_FRET_COLOR; c = Color.BLUE;
		if (!prop.containsKey(s)) prop.setProperty(s, "#"+Integer.toHexString(c.getRGB()).substring(2)); options.put(s, prop.getProperty(s));
		saveOptions();
		*/
	}
	
}
