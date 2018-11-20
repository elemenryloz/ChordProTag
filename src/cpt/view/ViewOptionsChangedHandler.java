package cpt.view;

import java.util.Properties;

import cpt.view.browser.Browser;
import cpt.view.editor.Editor;
import view.UdoMvcViewEvent;
import view.UdoMvcViewEventListener;

public class ViewOptionsChangedHandler implements UdoMvcViewEventListener {

	View view;
	
	public ViewOptionsChangedHandler(View view){
		this.view=view;
	}
	
	@Override
	public void handleViewEvent(UdoMvcViewEvent e) {
		if(e.type==UdoMvcViewEvent.OPTIONS){
			Editor.setOptions((Properties)e.source);
			view.getOdt().setOptions((Properties)e.source);
			Browser.setOptions((Properties)e.source);
			view.getListener().handleViewEvent(e);
			
		}
	}
}
