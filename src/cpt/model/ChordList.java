package cpt.model;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.Vector;

import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.simple.TextDocument;

/**
 * ChordList class
 * 
 * Manages a list of chords. 
 * Multiple variations of a chord name can be stored.
 */
public class ChordList extends HashMap<String,Chord> {
	
	/**
	 * add
	 *
	 * Adds a single definition to the end of the vector. 
	 * If the given chord is not yet in the list it adds a new vector with the
	 * specified definition as the first element.
	 *
	 * @param chord is either a chord object to add or a chord name
	 * @param definition	is the chordpro define statement of the chord variation 
	 * @param isUserChord is true if the definition comes from the chordpro file
	 */
	public void add(String name, String definition, boolean isUserChord){
		
		Chord chord = this.getChord(name);
		if(definition!=null) {
			if(isUserChord) definition=definition.replace("define","udefine");
			chord.addDefinition(definition);
		}
	}
	
	/**
	 * insert
	 *
	 * Adds a single definition to the start of the vector. 
	 * If the given chord is not yet in the list it adds a new vector with the
	 * specified definition as the first element.
	 *
	 * @param chord is either a chord object to add or a chord name
	 * @param definition	is the chordpro define statement of the chord variation 
	 * @param isUserChord is true if the definition comes from the chordpro file
	 */
	public void insert(String name, String definition, boolean isUserChord){

		Chord chord = this.getChord(name);
		if(definition!=null) {
			if(isUserChord) definition=definition.replace("define","udefine");
			chord.insertDefinition(definition);
		}
	}
	
	/**
	 * getChord
	 *
	 * Returns the vector for the given chord.
	 * If the given chord is not yet in the list it adds a new vector to the hash map.
	 *
	 * @param name is the name of the chord for which to return the vector
	 * @return the chord
	 */
	public Chord getChord(String name) {
		// check whether we have that chord already
		if (this.get(name)==null){
			// no definition yet, so add new vector to hash map
			this.put(name,new Chord(name,false));
		}
		return this.get(name);
	}
	
}