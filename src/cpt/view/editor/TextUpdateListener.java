package cpt.view.editor;

/**
 * The listener interface for receiving text update  events. 
 * The class that is interested in processing a text update event implements this interface, 
 * and the object created with that class is registered with a component, using the component's addtextUpdateListener method. 
 * When the text update event occurs, that object's textUpdated method is invoked.
 * <p>	
 * @author udo
 *
 */
public interface TextUpdateListener {
	
	/**
	 * Indicates that a text has been updated.
	 * <p>
	 * @param evt - the text update event
	 */
	abstract public void textUpdated(TextUpdateEvent evt); 

}
