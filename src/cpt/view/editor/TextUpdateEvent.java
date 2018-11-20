package cpt.view.editor;

import java.awt.Component;

public class TextUpdateEvent {
	
	public Component source;
	
	public TextUpdateEvent (Component source){
		this.source=source;
	}
}
