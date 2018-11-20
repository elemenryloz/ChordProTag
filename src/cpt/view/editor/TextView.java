package cpt.view.editor;

import java.util.Vector;

import javax.swing.JPanel;

public class TextView extends JPanel {

	private static final long serialVersionUID = 1L;

	Vector<TextUpdateListener> listeners = new Vector<TextUpdateListener>();
	
	public TextView() {
		super();
	}
	
	public void addTextUpdateListener(TextUpdateListener comp) {
		listeners.add(comp);
	}
	
	public void fireTextChange() {
		for(TextUpdateListener comp : listeners){
			comp.textUpdated(new TextUpdateEvent(this));
		}
		
	}
	
}
