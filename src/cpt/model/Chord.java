package cpt.model;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.odftoolkit.odfdom.dom.element.draw.DrawCircleElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawLineElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawTextBoxElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSElement;
import org.odftoolkit.simple.TextDocument;

import cpt.view.odt.OdtExporter;


/**
 * Chord class
 * 
 *  manages a chord with different voicings. 
 */ 
public class Chord {
	
	static String names = "ABCDEFG";
	static HashMap<String,String[]> defaultDefs = new HashMap<String,String[]>();
	{
		defaultDefs.put("A", new String[]{ "{define A base-fret 1 frets X 0 2 2 2 0}",
				"{define A base-fret 1 frets 0 0 2 2 2 0}",
				"{define A base-fret 2 frets 0 3 X 1 4 0}",
				"{define A base-fret 5 frets 1 3 3 2 1 1}",
				"{define A base-fret 4 frets X 1 4 X X 2}" });
		defaultDefs.put("A#", new String[]{ "{define A# base-fret 1 frets X 1 3 3 3 1}"  });
		defaultDefs.put("A#+", new String[]{ "{define A#+ base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("A#4", new String[]{ "{define A#4 base-fret 1 frets X X 3 3 4 1}"  });
		defaultDefs.put("A#5", new String[]{ "{define A#5 base-fret 1 frets X 0 3 2 2 1}",
			"{define A#5 base-fret 1 frets X 0 X 2 2 1}"  });
		defaultDefs.put("A#7", new String[]{ "{define A#7 base-fret 3 frets X X 1 1 1 2}"  });
		defaultDefs.put("A#dim", new String[]{ "{define A#dim base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("A#m", new String[]{ "{define A#m base-fret 1 frets X 1 3 3 2 1}"  });
		defaultDefs.put("A#m7", new String[]{ "{define A#m7 base-fret 1 frets X 1 3 1 2 1}"  });
		defaultDefs.put("A#maj", new String[]{ "{define A#maj base-fret 1 frets X 1 3 3 3 1}"  });
		defaultDefs.put("A#maj7", new String[]{ "{define A#maj7 base-fret 1 frets X 1 3 2 3 X}"  });
		defaultDefs.put("A#min", new String[]{ "{define A#min base-fret 1 frets X 1 3 3 2 1}"  });
		defaultDefs.put("A#sus", new String[]{ "{define A#sus base-fret 1 frets X X 3 3 4 1}"  });
		defaultDefs.put("A#sus4", new String[]{ "{define A#sus4 base-fret 1 frets X X 3 3 4 1}"  });
		defaultDefs.put("A+", new String[]{ "{define A+ base-fret 1 frets X 0 3 2 2 1}"  });
		defaultDefs.put("A/Ab", new String[]{ "{define A/Ab base-fret 1 frets X 0 2 1 2 0}"  });
		defaultDefs.put("A/B", new String[]{ "{define A/B base-fret 1 frets 0 0 2 4 2 0}",
			"{define A/B base-fret 5 frets X 0 3 2 0 0}"  });
		defaultDefs.put("A/C#", new String[]{ "{define A/C# base-fret 1 frets 0 2 2 2 3 X}"  });
		defaultDefs.put("A/D", new String[]{ "{define A/D base-fret 1 frets X X 0 0 2 2}",
			"{define A/D base-fret 1 frets X 0 0 2 2 0}",
			"{define A/D base-fret 1 frets X X 0 2 2 0}",
			"{define A/D base-fret 5 frets X X 0 2 1 1}",
			"{define A/D base-fret 9 frets X X 0 1 2 1}"  });
		defaultDefs.put("A/F#", new String[]{ "{define A/F# base-fret 1 frets 2 0 2 2 2 0}"  });
		defaultDefs.put("A/G", new String[]{ "{define A/G base-fret 1 frets 3 X 2 2 2 0}",
			"{define A/G base-fret 1 frets X 0 2 0 2 0}",
			"{define A/G base-fret 1 frets X 0 2 2 2 3}"  });
		defaultDefs.put("A/G#", new String[]{ "{define A/G# base-fret 1 frets 4 0 2 2 2 0}"  });
		defaultDefs.put("A/Gb", new String[]{ "{define A/Gb base-fret 1 frets 0 0 2 2 2 2}",
			"{define A/Gb base-fret 1 frets 0 X 4 2 2 0}",
			"{define A/Gb base-fret 1 frets 2 X 2 2 2 0}",
			"{define A/Gb base-fret 1 frets X 0 4 2 2 0}",
			"{define A/Gb base-fret 1 frets X X 2 2 2 2}"  });
		defaultDefs.put("A11", new String[]{ "{define A11 base-fret 1 frets X 4 2 4 3 3}"  });
		defaultDefs.put("A13", new String[]{ "{define A13 base-fret 5 frets X 0 1 2 3 1}"  });
		defaultDefs.put("A4", new String[]{ "{define A4 base-fret 1 frets 0 0 2 2 0 0}"  });
		defaultDefs.put("A5", new String[]{ "{define A5 base-fret 5 frets 1 3 3 X X 1}",
			"{define A5 base-fret 1 frets X 0 2 2 X 0}",
			"{define A5 base-fret 5 frets 1 3 3 X X 0}"  });
		defaultDefs.put("A6", new String[]{ "{define A6 base-fret 1 frets X X 2 2 2 2}",
			"{define A6 base-fret 1 frets 0 0 2 2 2 2}",
			"{define A6 base-fret 1 frets 0 X 4 2 2 0}",
			"{define A6 base-fret 1 frets 2 X 2 2 2 0}",
			"{define A6 base-fret 1 frets X 0 4 2 2 0}"  });
		defaultDefs.put("A6/7", new String[]{ "{define A6/7 base-fret 1 frets 0 0 2 0 2 2}"  });
		defaultDefs.put("A6/7sus", new String[]{ "{define A6/7sus base-fret 3 frets 3 3 2 0 1 0}",
			"{define A6/7sus base-fret 1 frets X 0 2 0 3 2}"  });
		defaultDefs.put("A7", new String[]{ "{define A7 base-fret 1 frets X 0 2 0 2 0}",
			"{define A7 base-fret 1 frets 3 X 2 2 2 0}",
			"{define A7 base-fret 1 frets X 0 2 2 2 3}"  });
		defaultDefs.put("A7(#5)", new String[]{ "{define A7(#5) base-fret 1 frets 1 0 3 0 2 1}"  });
		defaultDefs.put("A7(9+)", new String[]{ "{define A7(9+) base-fret 1 frets X 2 2 2 2 3}"  });
		defaultDefs.put("A7+", new String[]{ "{define A7+ base-fret 1 frets X X 3 2 2 1}"  });
		defaultDefs.put("A7/add11", new String[]{ "{define A7/add11 base-fret 1 frets X 0 0 0 2 0}"  });
		defaultDefs.put("A7sus4", new String[]{ "{define A7sus4 base-fret 1 frets 0 0 2 0 3 0}",
			"{define A7sus4 base-fret 1 frets X 0 2 0 3 0}",
			"{define A7sus4 base-fret 1 frets X 0 2 0 3 3}",
			"{define A7sus4 base-fret 1 frets X 0 2 2 3 3}",
			"{define A7sus4 base-fret 3 frets 3 X 0 0 1 0}",
			"{define A7sus4 base-fret 1 frets X 0 0 0 X 0}"  });
		defaultDefs.put("A9", new String[]{ "{define A9 base-fret 1 frets X 0 2 1 0 0}"  });
		defaultDefs.put("A9sus", new String[]{ "{define A9sus base-fret 1 frets X 0 2 1 0 0}"  });
		defaultDefs.put("Aadd9", new String[]{ "{define Aadd9 base-fret 1 frets 0 0 2 4 2 0}",
			"{define Aadd9 base-fret 6 frets X 0 2 1 0 0}"  });
		defaultDefs.put("Aaug/D", new String[]{ "{define Aaug/D base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Aaug/G", new String[]{ "{define Aaug/G base-fret 1 frets 1 0 3 0 2 1}"  });
		defaultDefs.put("Ab", new String[]{ "{define Ab base-fret 4 frets 1 3 3 2 1 1}"  });
		defaultDefs.put("Ab#5", new String[]{ "{define Ab#5 base-fret 1 frets X 3 2 1 1 0}"  });
		defaultDefs.put("Ab+", new String[]{ "{define Ab+ base-fret 1 frets X X 2 1 1 0}"  });
		defaultDefs.put("Ab/A", new String[]{ "{define Ab/A base-fret 1 frets X X 1 2 1 4}"  });
		defaultDefs.put("Ab/F", new String[]{ "{define Ab/F base-fret 8 frets X 1 3 1 2 1}",
			"{define Ab/F base-fret 1 frets X X 1 1 1 1}"  });
		defaultDefs.put("Ab/Gb", new String[]{ "{define Ab/Gb base-fret 1 frets X X 1 1 1 2}",
			"{define Ab/Gb base-fret 4 frets X X 1 2 1 1}"  });
		defaultDefs.put("Ab11", new String[]{ "{define Ab11 base-fret 4 frets 1 3 1 3 1 1}"  });
		defaultDefs.put("Ab4", new String[]{ "{define Ab4 base-fret 1 frets X X 1 1 2 4}"  });
		defaultDefs.put("Ab5", new String[]{ "{define Ab5 base-fret 4 frets 1 3 3 X X 1}"  });
		defaultDefs.put("Ab6", new String[]{ "{define Ab6 base-fret 8 frets X 1 3 1 2 1}",
			"{define Ab6 base-fret 1 frets X X 1 1 1 1}"  });
		defaultDefs.put("Ab7", new String[]{ "{define Ab7 base-fret 1 frets X X 1 1 1 2}",
			"{define Ab7 base-fret 4 frets X X 1 2 1 1}"  });
		defaultDefs.put("Abdim", new String[]{ "{define Abdim base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("Abdim/E", new String[]{ "{define Abdim/E base-fret 1 frets 0 2 0 1 0 0}",
			"{define Abdim/E base-fret 1 frets 0 2 2 1 3 0}",
			"{define Abdim/E base-fret 1 frets X 2 0 1 3 0}",
			"{define Abdim/E base-fret 1 frets X X 0 1 0 0}"  });
		defaultDefs.put("Abdim/Eb", new String[]{ "{define Abdim/Eb base-fret 1 frets X X 0 4 4 4}"  });
		defaultDefs.put("Abdim/F", new String[]{ "{define Abdim/F base-fret 1 frets X 2 0 1 0 1}",
			"{define Abdim/F base-fret 1 frets X X 0 1 0 1}",
			"{define Abdim/F base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Abdim7", new String[]{ "{define Abdim7 base-fret 1 frets X 2 0 1 0 1}",
			"{define Abdim7 base-fret 1 frets X X 0 1 0 1}",
			"{define Abdim7 base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Abm", new String[]{ "{define Abm base-fret 4 frets 1 3 3 1 1 1}",
			"{define Abm base-fret 4 frets X X 3 1 1 1}"  });
		defaultDefs.put("Abm/D", new String[]{ "{define Abm/D base-fret 1 frets X X 0 4 4 4}"  });
		defaultDefs.put("Abm/E", new String[]{ "{define Abm/E base-fret 1 frets 0 2 1 1 0 0}",
			"{define Abm/E base-fret 4 frets 0 X 3 1 1 0}",
			"{define Abm/E base-fret 1 frets X X 1 1 0 0}"  });
		defaultDefs.put("Abm/Gb", new String[]{ "{define Abm/Gb base-fret 1 frets X X 4 4 4 4}"  });
		defaultDefs.put("Abm7", new String[]{ "{define Abm7 base-fret 4 frets X X 1 1 1 1}",
			"{define Abm7 base-fret 1 frets X X 4 4 4 4}"  });
		defaultDefs.put("Abmaj", new String[]{ "{define Abmaj base-fret 4 frets 1 3 3 2 1 1}"  });
		defaultDefs.put("Abmaj7", new String[]{ "{define Abmaj7 base-fret 1 frets X X 1 1 1 3}"  });
		defaultDefs.put("Abmin", new String[]{ "{define Abmin base-fret 4 frets 1 3 3 1 1 1}"  });
		defaultDefs.put("Absus", new String[]{ "{define Absus base-fret 1 frets X X 1 1 2 4}",
			"{define Absus base-fret 4 frets X X 3 3 1 1}"  });
		defaultDefs.put("Absus2/F", new String[]{ "{define Absus2/F base-fret 1 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Absus4", new String[]{ "{define Absus4 base-fret 1 frets X X 1 1 2 4}"  });
		defaultDefs.put("Adim", new String[]{ "{define Adim base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Adim/Ab", new String[]{ "{define Adim/Ab base-fret 1 frets X X 1 2 1 4}"  });
		defaultDefs.put("Adim/E", new String[]{ "{define Adim/E base-fret 1 frets 0 3 X 2 4 0}"  });
		defaultDefs.put("Adim/F", new String[]{ "{define Adim/F base-fret 1 frets X X 1 2 1 1}",
			"{define Adim/F base-fret 3 frets X X 1 3 2 1}"  });
		defaultDefs.put("Adim/G", new String[]{ "{define Adim/G base-fret 1 frets X X 1 2 1 3}"  });
		defaultDefs.put("Adim/Gb", new String[]{ "{define Adim/Gb base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Adim7", new String[]{ "{define Adim7 base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Am", new String[]{ "{define Am base-fret 1 frets X 0 2 2 1 0}",
			"{define Am base-fret 5 frets X 0 3 1 1 1}",
			"{define Am base-fret 1 frets X 3 2 2 1 0}"  });
		defaultDefs.put("Am#7", new String[]{ "{define Am#7 base-fret 1 frets X X 2 1 1 0}"  });
		defaultDefs.put("Am(7#)", new String[]{ "{define Am(7#) base-fret 1 frets X 0 2 2 1 4}"  });
		defaultDefs.put("Am(add9)", new String[]{ "{define Am(add9) base-fret 1 frets 0 2 2 2 1 0}"  });
		defaultDefs.put("Am/B", new String[]{ "{define Am/B base-fret 5 frets 0 0 3 1 0 0}",
			"{define Am/B base-fret 1 frets X 3 2 2 0 0}"  });
		defaultDefs.put("Am/D", new String[]{ "{define Am/D base-fret 1 frets X X 0 2 1 0}",
			"{define Am/D base-fret 5 frets X X 0 1 1 1}"  });
		defaultDefs.put("Am/Eb", new String[]{ "{define Am/Eb base-fret 1 frets 0 3 X 2 4 0}"  });
		defaultDefs.put("Am/F", new String[]{ "{define Am/F base-fret 1 frets 0 0 3 2 1 0}",
			"{define Am/F base-fret 1 frets 1 3 3 2 1 0}",
			"{define Am/F base-fret 1 frets 1 X 2 2 1 0}",
			"{define Am/F base-fret 1 frets X X 2 2 1 1}",
			"{define Am/F base-fret 1 frets X X 3 2 1 0}"  });
		defaultDefs.put("Am/G", new String[]{ "{define Am/G base-fret 1 frets 3 0 2 2 1 0}",
			"{define Am/G base-fret 1 frets 0 0 2 0 1 3}",
			"{define Am/G base-fret 1 frets X 0 2 0 1 0}",
			"{define Am/G base-fret 1 frets X 0 2 2 1 3}",
			"{define Am/G base-fret 5 frets X 0 1 1 1 4}"  });
		defaultDefs.put("Am/Gb", new String[]{ "{define Am/Gb base-fret 1 frets X 0 2 2 1 2}",
			"{define Am/Gb base-fret 1 frets X X 2 2 1 2}"  });
		defaultDefs.put("Am6", new String[]{ "{define Am6 base-fret 1 frets X 0 2 2 1 2}",
			"{define Am6 base-fret 1 frets X X 2 2 1 2}"  });
		defaultDefs.put("Am7", new String[]{ "{define Am7 base-fret 1 frets X 0 2 2 1 3}",
			"{define Am7 base-fret 1 frets 0 0 2 0 1 3}",
			"{define Am7 base-fret 1 frets X 0 2 0 1 0}",
			"{define Am7 base-fret 5 frets X 0 1 1 1 4}"  });
		defaultDefs.put("Am7(b5)", new String[]{ "{define Am7(b5) base-fret 1 frets X X 1 2 1 3}"  });
		defaultDefs.put("Am7/add11", new String[]{ "{define Am7/add11 base-fret 5 frets X 1 3 1 4 0}"  });
		defaultDefs.put("Am7sus4", new String[]{ "{define Am7sus4 base-fret 1 frets 0 0 0 0 3 0}"  });
		defaultDefs.put("Am9", new String[]{ "{define Am9 base-fret 5 frets X 0 1 1 1 3}"  });
		defaultDefs.put("Amadd9", new String[]{ "{define Amadd9 base-fret 1 frets 0 2 2 2 1 0}"  });
		defaultDefs.put("Amaj", new String[]{ "{define Amaj base-fret 1 frets X 0 2 2 2 0}"  });
		defaultDefs.put("Amaj7", new String[]{ "{define Amaj7 base-fret 1 frets X 0 2 1 2 0}"  });
		defaultDefs.put("Amin", new String[]{ "{define Amin base-fret 1 frets X 0 2 2 1 0}"  });
		defaultDefs.put("Amin/maj9", new String[]{ "{define Amin/maj9 base-fret 5 frets X 0 2 1 1 3}"  });
		defaultDefs.put("Asus", new String[]{ "{define Asus base-fret 1 frets X X 2 2 3 0}",
			"{define Asus base-fret 1 frets 0 0 2 2 3 0}",
			"{define Asus base-fret 1 frets X 0 2 2 3 0}",
			"{define Asus base-fret 5 frets 1 1 3 3 X 0}",
			"{define Asus base-fret 1 frets X 0 0 2 3 0}"  });
		defaultDefs.put("Asus2", new String[]{ "{define Asus2 base-fret 1 frets 0 0 2 2 0 0}",
			"{define Asus2 base-fret 1 frets 0 0 2 4 0 0}",
			"{define Asus2 base-fret 1 frets 0 2 2 2 0 0}",
			"{define Asus2 base-fret 1 frets X 0 2 2 0 0}",
			"{define Asus2 base-fret 1 frets X X 2 2 0 0}"  });
		defaultDefs.put("Asus2/Ab", new String[]{ "{define Asus2/Ab base-fret 1 frets X 0 2 1 0 0}"  });
		defaultDefs.put("Asus2/C", new String[]{ "{define Asus2/C base-fret 5 frets 0 0 3 1 0 0}",
			"{define Asus2/C base-fret 1 frets X 3 2 2 0 0}"  });
		defaultDefs.put("Asus2/D", new String[]{ "{define Asus2/D base-fret 1 frets 0 2 0 2 0 0}",
			"{define Asus2/D base-fret 1 frets X 2 0 2 3 0}"  });
		defaultDefs.put("Asus2/Db", new String[]{ "{define Asus2/Db base-fret 1 frets 0 0 2 4 2 0}",
			"{define Asus2/Db base-fret 6 frets X 0 2 1 0 0}"  });
		defaultDefs.put("Asus2/Eb", new String[]{ "{define Asus2/Eb base-fret 1 frets X 2 1 2 0 0}"  });
		defaultDefs.put("Asus2/F", new String[]{ "{define Asus2/F base-fret 1 frets 0 0 3 2 0 0}"  });
		defaultDefs.put("Asus2/G", new String[]{ "{define Asus2/G base-fret 1 frets X 0 2 0 0 0}",
			"{define Asus2/G base-fret 4 frets X 0 2 1 2 0}"  });
		defaultDefs.put("Asus2/Gb", new String[]{ "{define Asus2/Gb base-fret 1 frets X 0 4 4 0 0}",
			"{define Asus2/Gb base-fret 2 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Asus4", new String[]{ "{define Asus4 base-fret 1 frets X X 2 2 3 0}"  });
		defaultDefs.put("Asus4/Ab", new String[]{ "{define Asus4/Ab base-fret 1 frets 4 X 0 2 3 0}"  });
		defaultDefs.put("Asus4/B", new String[]{ "{define Asus4/B base-fret 1 frets 0 2 0 2 0 0}"  });
		defaultDefs.put("Asus4/Bb", new String[]{ "{define Asus4/Bb base-fret 1 frets 0 1 X 2 3 0}"  });
		defaultDefs.put("Asus4/C", new String[]{ "{define Asus4/C base-fret 1 frets X X 0 2 1 0}",
			"{define Asus4/C base-fret 5 frets X X 0 1 1 1}"  });
		defaultDefs.put("Asus4/Db", new String[]{ "{define Asus4/Db base-fret 1 frets X 0 0 2 2 0}",
			"{define Asus4/Db base-fret 1 frets X X 0 2 2 0}",
			"{define Asus4/Db base-fret 5 frets X X 0 2 1 1}",
			"{define Asus4/Db base-fret 9 frets X X 0 1 2 1}"  });
		defaultDefs.put("Asus4/F", new String[]{ "{define Asus4/F base-fret 6 frets X X 2 2 1 0}"  });
		defaultDefs.put("Asus4/G", new String[]{ "{define Asus4/G base-fret 1 frets X 0 2 0 3 0}",
			"{define Asus4/G base-fret 1 frets X 0 2 0 3 3}",
			"{define Asus4/G base-fret 1 frets X 0 2 2 3 3}",
			"{define Asus4/G base-fret 1 frets X 0 0 0 X 0}"  });
		defaultDefs.put("Asus4/Gb", new String[]{ "{define Asus4/Gb base-fret 1 frets 0 0 0 2 3 2}",
			"{define Asus4/Gb base-fret 1 frets 0 0 4 2 3 0}",
			"{define Asus4/Gb base-fret 1 frets 2 X 0 2 3 0}",
			"{define Asus4/Gb base-fret 1 frets X 0 2 2 3 2}",
			"{define Asus4/Gb base-fret 1 frets X X 2 2 3 2}",
			"{define Asus4/Gb base-fret 2 frets X 4 3 1 2 0}",
			"{define Asus4/Gb base-fret 7 frets X 3 1 1 X 0}"  });		

		defaultDefs.put("B", new String[]{ "{define B base-fret 1 frets X 2 4 4 4 2}"  });
		defaultDefs.put("B#5", new String[]{ "{define B#5 base-fret 1 frets 3 2 1 0 0 3}",
			"{define B#5 base-fret 1 frets 3 X 1 0 0 3}"  });
		defaultDefs.put("B(addE)", new String[]{ "{define B(addE) base-fret 1 frets X 2 4 4 0 0}"  });
		defaultDefs.put("B+", new String[]{ "{define B+ base-fret 1 frets X X 1 0 0 4}"  });
		defaultDefs.put("B/A", new String[]{ "{define B/A base-fret 1 frets 2 X 1 2 0 2}",
			"{define B/A base-fret 1 frets X 0 1 2 0 2}",
			"{define B/A base-fret 1 frets X 2 1 2 0 2}",
			"{define B/A base-fret 1 frets X 2 4 2 4 2}"  });
		defaultDefs.put("B/Ab", new String[]{ "{define B/Ab base-fret 1 frets X X 4 4 4 4}"  });
		defaultDefs.put("B/E", new String[]{ "{define B/E base-fret 1 frets X 2 2 4 4 2}",
			"{define B/E base-fret 1 frets X X 4 4 4 0}"  });
		defaultDefs.put("B/F#", new String[]{ "{define B/F# base-fret 2 frets 0 2 2 2 0 0}"  });
		defaultDefs.put("B11", new String[]{ "{define B11 base-fret 7 frets 1 3 3 2 0 0}"  });
		defaultDefs.put("B11/13", new String[]{ "{define B11/13 base-fret 2 frets X 1 1 1 1 3}"  });
		defaultDefs.put("B13", new String[]{ "{define B13 base-fret 1 frets X 2 1 2 0 4}"  });
		defaultDefs.put("B4", new String[]{ "{define B4 base-fret 2 frets X X 3 3 4 1}"  });
		defaultDefs.put("B5", new String[]{ "{define B5 base-fret 1 frets X 2 4 4 X 2}"  });
		defaultDefs.put("B6", new String[]{ "{define B6 base-fret 1 frets X X 4 4 4 4}"  });
		defaultDefs.put("B7", new String[]{ "{define B7 base-fret 1 frets 0 2 1 2 0 2}",
			"{define B7 base-fret 1 frets 2 X 1 2 0 2}",
			"{define B7 base-fret 1 frets X 0 1 2 0 2}",
			"{define B7 base-fret 1 frets X 2 1 2 0 2}",
			"{define B7 base-fret 1 frets X 2 4 2 4 2}"  });
		defaultDefs.put("B7#9", new String[]{ "{define B7#9 base-fret 1 frets X 2 1 2 3 X}"  });
		defaultDefs.put("B7(#9)", new String[]{ "{define B7(#9) base-fret 1 frets X 2 1 2 3 X}"  });
		defaultDefs.put("B7+", new String[]{ "{define B7+ base-fret 1 frets X 2 1 2 0 3}"  });
		defaultDefs.put("B7+5", new String[]{ "{define B7+5 base-fret 1 frets X 2 1 2 0 3}"  });
		defaultDefs.put("B7/add11", new String[]{ "{define B7/add11 base-fret 1 frets 0 0 4 4 4 0}",
			"{define B7/add11 base-fret 1 frets 0 2 1 2 0 2}"  });
		defaultDefs.put("B7sus4", new String[]{ "{define B7sus4 base-fret 1 frets X 0 4 4 0 0}",
			"{define B7sus4 base-fret 2 frets X 1 3 1 4 1}"  });
		defaultDefs.put("B9", new String[]{ "{define B9 base-fret 7 frets 1 3 1 2 1 3}"  });
		defaultDefs.put("BaddE", new String[]{ "{define BaddE base-fret 1 frets X 2 4 4 0 0}"  });
		defaultDefs.put("BaddE/F#", new String[]{ "{define BaddE/F# base-fret 1 frets 2 X 4 4 0 0}"  });
		defaultDefs.put("Baug/E", new String[]{ "{define Baug/E base-fret 1 frets 3 X 1 0 0 0}",
			"{define Baug/E base-fret 1 frets X X 1 0 0 0}"  });
		defaultDefs.put("Bb", new String[]{ "{define Bb base-fret 1 frets X 1 3 3 3 1}",
			"{define Bb base-fret 1 frets 1 1 3 3 3 1}",
			"{define Bb base-fret 1 frets X X 0 3 3 1}"  });
		defaultDefs.put("Bb#5", new String[]{ "{define Bb#5 base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("Bb+", new String[]{ "{define Bb+ base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("Bb/A", new String[]{ "{define Bb/A base-fret 1 frets 1 1 3 2 3 1}"  });
		defaultDefs.put("Bb/Ab", new String[]{ "{define Bb/Ab base-fret 1 frets X 1 3 1 3 1}",
			"{define Bb/Ab base-fret 1 frets X X 3 3 3 4}"  });
		defaultDefs.put("Bb/Db", new String[]{ "{define Bb/Db base-fret 6 frets X X 0 1 1 1}"  });
		defaultDefs.put("Bb/E", new String[]{ "{define Bb/E base-fret 1 frets X 1 3 3 3 0}"  });
		defaultDefs.put("Bb/G", new String[]{ "{define Bb/G base-fret 3 frets 1 3 1 1 1 1}",
			"{define Bb/G base-fret 1 frets X X 3 3 3 3}"  });
		defaultDefs.put("Bb11", new String[]{ "{define Bb11 base-fret 6 frets 1 3 1 3 4 1}"  });
		defaultDefs.put("Bb4", new String[]{ "{define Bb4 base-fret 1 frets X X 3 3 4 1}"  });
		defaultDefs.put("Bb5", new String[]{ "{define Bb5 base-fret 6 frets 1 3 3 X X 1}"  });
		defaultDefs.put("Bb6", new String[]{ "{define Bb6 base-fret 1 frets X X 3 3 3 3}",
			"{define Bb6 base-fret 3 frets 1 3 1 1 1 1}"  });
		defaultDefs.put("Bb6/add9", new String[]{ "{define Bb6/add9 base-fret 1 frets X 3 3 3 3 3}"  });
		defaultDefs.put("Bb7", new String[]{ "{define Bb7 base-fret 3 frets X X 1 1 1 2}",
			"{define Bb7 base-fret 1 frets X 1 3 1 3 1}",
			"{define Bb7 base-fret 1 frets X X 3 3 3 4}"  });
		defaultDefs.put("Bb7sus4", new String[]{ "{define Bb7sus4 base-fret 1 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Bb9", new String[]{ "{define Bb9 base-fret 6 frets 1 3 1 2 1 3}"  });
		defaultDefs.put("Bbadd#11", new String[]{ "{define Bbadd#11 base-fret 1 frets X 1 3 3 3 0}"  });
		defaultDefs.put("Bbaug/E", new String[]{ "{define Bbaug/E base-fret 1 frets 2 X 4 3 3 0}"  });
		defaultDefs.put("Bbb5", new String[]{ "{define Bbb5 base-fret 1 frets X X 0 3 X 0}"  });
		defaultDefs.put("Bbdim", new String[]{ "{define Bbdim base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Bbdim/C", new String[]{ "{define Bbdim/C base-fret 1 frets X 3 X 3 2 0}"  });
		defaultDefs.put("Bbdim/D", new String[]{ "{define Bbdim/D base-fret 1 frets X X 0 3 2 0}"  });
		defaultDefs.put("Bbdim/G", new String[]{ "{define Bbdim/G base-fret 1 frets X 1 2 0 2 0}",
			"{define Bbdim/G base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Bbdim/Gb", new String[]{ "{define Bbdim/Gb base-fret 1 frets 2 4 2 3 2 2}",
			"{define Bbdim/Gb base-fret 1 frets X X 4 3 2 0}"  });
		defaultDefs.put("Bbdim7", new String[]{ "{define Bbdim7 base-fret 1 frets X 1 2 0 2 0}",
			"{define Bbdim7 base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Bbm", new String[]{ "{define Bbm base-fret 1 frets X 1 3 3 2 1}",
			"{define Bbm base-fret 1 frets 1 1 3 3 2 1}"  });
		defaultDefs.put("Bbm/Ab", new String[]{ "{define Bbm/Ab base-fret 1 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Bbm/D", new String[]{ "{define Bbm/D base-fret 6 frets X X 0 1 1 1}"  });
		defaultDefs.put("Bbm/Gb", new String[]{ "{define Bbm/Gb base-fret 1 frets X X 3 3 2 2}"  });
		defaultDefs.put("Bbm6", new String[]{ "{define Bbm6 base-fret 2 frets X X 3 3 2 3}"  });
		defaultDefs.put("Bbm7", new String[]{ "{define Bbm7 base-fret 1 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Bbm9", new String[]{ "{define Bbm9 base-fret 6 frets X X X 1 1 3}"  });
		defaultDefs.put("Bbmaj", new String[]{ "{define Bbmaj base-fret 1 frets X 1 3 3 3 1}"  });
		defaultDefs.put("Bbmaj7", new String[]{ "{define Bbmaj7 base-fret 1 frets X 1 3 2 3 X}",
			"{define Bbmaj7 base-fret 1 frets 1 1 3 2 3 1}"  });
		defaultDefs.put("Bbmaj9", new String[]{ "{define Bbmaj9 base-fret 3 frets X 1 1 1 1 3}"  });
		defaultDefs.put("Bbmin", new String[]{ "{define Bbmin base-fret 1 frets X 1 3 3 2 1}"  });
		defaultDefs.put("Bbsus", new String[]{ "{define Bbsus base-fret 1 frets X X 3 3 4 1}"  });
		defaultDefs.put("Bbsus2", new String[]{ "{define Bbsus2 base-fret 1 frets X X 3 3 1 1}"  });
		defaultDefs.put("Bbsus2/G", new String[]{ "{define Bbsus2/G base-fret 3 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Bbsus4", new String[]{ "{define Bbsus4 base-fret 1 frets X X 3 3 4 1}"  });
		defaultDefs.put("Bbsus4/Ab", new String[]{ "{define Bbsus4/Ab base-fret 1 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Bdim", new String[]{ "{define Bdim base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("Bdim/A", new String[]{ "{define Bdim/A base-fret 1 frets 1 2 3 2 3 1}",
			"{define Bdim/A base-fret 1 frets X 2 0 2 0 1}",
			"{define Bdim/A base-fret 1 frets X X 0 2 0 1}"  });
		defaultDefs.put("Bdim/Ab", new String[]{ "{define Bdim/Ab base-fret 1 frets X 2 0 1 0 1}",
			"{define Bdim/Ab base-fret 1 frets X X 0 1 0 1}",
			"{define Bdim/Ab base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Bdim/G", new String[]{ "{define Bdim/G base-fret 1 frets 1 X 0 0 0 3}",
			"{define Bdim/G base-fret 1 frets 3 2 0 0 0 1}",
			"{define Bdim/G base-fret 1 frets X X 0 0 0 1}"  });
		defaultDefs.put("Bdim7", new String[]{ "{define Bdim7 base-fret 1 frets X 2 0 1 0 1}",
			"{define Bdim7 base-fret 1 frets X X 0 1 0 1}",
			"{define Bdim7 base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Bm", new String[]{ "{define Bm base-fret 1 frets X 2 4 4 3 2}",
			"{define Bm base-fret 1 frets 2 2 4 4 3 2}",
			"{define Bm base-fret 1 frets X X 0 4 3 2}"  });
		defaultDefs.put("Bm(maj7)", new String[]{ "{define Bm(maj7) base-fret 1 frets X 1 4 4 3 X}"  });
		defaultDefs.put("Bm(sus9)", new String[]{ "{define Bm(sus9) base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("Bm/A", new String[]{ "{define Bm/A base-fret 1 frets X 0 4 4 3 2}",
			"{define Bm/A base-fret 1 frets X 2 0 2 0 2}",
			"{define Bm/A base-fret 1 frets X 2 0 2 3 2}",
			"{define Bm/A base-fret 1 frets X 2 4 2 3 2}",
			"{define Bm/A base-fret 1 frets X X 0 2 0 2}"  });
		defaultDefs.put("Bm/G", new String[]{ "{define Bm/G base-fret 1 frets 2 2 0 0 0 3}",
			"{define Bm/G base-fret 1 frets 2 2 0 0 3 3}",
			"{define Bm/G base-fret 1 frets 3 2 0 0 0 2}",
			"{define Bm/G base-fret 1 frets X X 4 4 3 3}"  });
		defaultDefs.put("Bm6", new String[]{ "{define Bm6 base-fret 1 frets X X 4 4 3 4}"  });
		defaultDefs.put("Bm7", new String[]{ "{define Bm7 base-fret 2 frets X 1 3 1 2 1}",
			"{define Bm7 base-fret 1 frets X 0 4 4 3 2}",
			"{define Bm7 base-fret 1 frets X 2 0 2 0 2}",
			"{define Bm7 base-fret 1 frets X 2 0 2 3 2}",
			"{define Bm7 base-fret 1 frets X 2 4 2 3 2}",
			"{define Bm7 base-fret 1 frets X X 0 2 0 2}"  });
		defaultDefs.put("Bm7(b5)", new String[]{ "{define Bm7(b5) base-fret 1 frets 1 2 3 2 3 1}",
			"{define Bm7(b5) base-fret 1 frets X 2 0 2 0 1}",
			"{define Bm7(b5) base-fret 1 frets X X 0 2 0 1}"  });
		defaultDefs.put("Bm7/add11", new String[]{ "{define Bm7/add11 base-fret 1 frets 0 0 2 4 3 2}",
			"{define Bm7/add11 base-fret 1 frets 0 2 0 2 0 2}"  });
		defaultDefs.put("Bm7b5", new String[]{ "{define Bm7b5 base-fret 1 frets 1 2 4 2 3 1}"  });
		defaultDefs.put("Bmaj", new String[]{ "{define Bmaj base-fret 1 frets X 2 4 3 4 X}"  });
		defaultDefs.put("Bmaj7", new String[]{ "{define Bmaj7 base-fret 1 frets X 2 4 3 4 X}"  });
		defaultDefs.put("Bmaj7/#11", new String[]{ "{define Bmaj7/#11 base-fret 1 frets X 2 3 3 4 2}"  });
		defaultDefs.put("Bmin", new String[]{ "{define Bmin base-fret 1 frets X 2 4 4 3 2}"  });
		defaultDefs.put("Bmmaj7", new String[]{ "{define Bmmaj7 base-fret 1 frets X 1 4 4 3 X}"  });
		defaultDefs.put("Bmsus9", new String[]{ "{define Bmsus9 base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("Bsus", new String[]{ "{define Bsus base-fret 2 frets X X 3 3 4 1}",
			"{define Bsus base-fret 7 frets 1 3 3 X X 0}",
			"{define Bsus base-fret 1 frets X 2 4 4 X 0}"  });
		defaultDefs.put("Bsus2", new String[]{ "{define Bsus2 base-fret 1 frets X 4 4 4 X 2}",
			"{define Bsus2 base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("Bsus2/E", new String[]{ "{define Bsus2/E base-fret 1 frets X 4 4 4 X 0}"  });
		defaultDefs.put("Bsus4", new String[]{ "{define Bsus4 base-fret 2 frets X X 3 3 4 1}"  });
		defaultDefs.put("Bsus4/A", new String[]{ "{define Bsus4/A base-fret 1 frets X 0 4 4 0 0}",
			"{define Bsus4/A base-fret 2 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Bsus4/Ab", new String[]{ "{define Bsus4/Ab base-fret 1 frets 0 2 2 1 0 2}",
			"{define Bsus4/Ab base-fret 1 frets 0 X 4 1 0 0}",
			"{define Bsus4/Ab base-fret 1 frets 2 2 2 1 0 0}"  });
		defaultDefs.put("Bsus4/Db", new String[]{ "{define Bsus4/Db base-fret 1 frets X 4 4 4 X 0}"  });
		defaultDefs.put("Bsus4/Eb", new String[]{ "{define Bsus4/Eb base-fret 1 frets X 2 2 4 4 2}",
			"{define Bsus4/Eb base-fret 1 frets X X 4 4 4 0}"  });
		defaultDefs.put("Bsus4/G", new String[]{ "{define Bsus4/G base-fret 1 frets 0 2 2 0 0 2}",
			"{define Bsus4/G base-fret 1 frets 0 2 4 0 0 0}",
			"{define Bsus4/G base-fret 1 frets 0 X 4 0 0 0}",
			"{define Bsus4/G base-fret 1 frets 2 2 2 0 0 0}"  });
		defaultDefs.put("C", new String[]{ "{define C base-fret 1 frets X 3 2 0 1 0}",
			"{define C base-fret 1 frets 0 3 2 0 1 0}",
			"{define C base-fret 3 frets 0 1 3 3 3 1}",
			"{define C base-fret 1 frets 3 3 2 0 1 0}",
			"{define C base-fret 1 frets 3 X 2 0 1 0}",
			"{define C base-fret 3 frets X 1 3 3 3 0}"  });
		defaultDefs.put("C#", new String[]{ "{define C# base-fret 1 frets X X 3 1 2 1}"  });
		defaultDefs.put("C#(add9)", new String[]{ "{define C#(add9) base-fret 4 frets X 1 3 3 1 1}"  });
		defaultDefs.put("C#+", new String[]{ "{define C#+ base-fret 1 frets X X 3 2 2 1}"  });
		defaultDefs.put("C#4", new String[]{ "{define C#4 base-fret 4 frets X X 3 3 4 1}"  });
		defaultDefs.put("C#5", new String[]{ "{define C#5 base-fret 1 frets X 3 2 1 1 0}"  });
		defaultDefs.put("C#7", new String[]{ "{define C#7 base-fret 1 frets X X 3 4 2 4}"  });
		defaultDefs.put("C#7(b5)", new String[]{ "{define C#7(b5) base-fret 1 frets X 2 1 2 1 2}"  });
		defaultDefs.put("C#add9", new String[]{ "{define C#add9 base-fret 4 frets X 1 3 3 1 1}"  });
		defaultDefs.put("C#dim", new String[]{ "{define C#dim base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("C#m", new String[]{ "{define C#m base-fret 1 frets X X 2 1 2 0}"  });
		defaultDefs.put("C#m7", new String[]{ "{define C#m7 base-fret 1 frets X X 2 4 2 4}"  });
		defaultDefs.put("C#maj", new String[]{ "{define C#maj base-fret 1 frets X 4 3 1 1 1}"  });
		defaultDefs.put("C#maj7", new String[]{ "{define C#maj7 base-fret 1 frets X 4 3 1 1 1}"  });
		defaultDefs.put("C#min", new String[]{ "{define C#min base-fret 1 frets X X 2 1 2 0}"  });
		defaultDefs.put("C#sus", new String[]{ "{define C#sus base-fret 4 frets X X 3 3 4 1}"  });
		defaultDefs.put("C#sus4", new String[]{ "{define C#sus4 base-fret 4 frets X X 3 3 4 1}"  });
		defaultDefs.put("C(add9)", new String[]{ "{define C(add9) base-fret 1 frets X 3 2 0 3 0}"  });
		defaultDefs.put("C(addD)", new String[]{ "{define C(addD) base-fret 1 frets X 3 2 0 3 0}"  });
		defaultDefs.put("C+", new String[]{ "{define C+ base-fret 1 frets X X 2 1 1 0}"  });
		defaultDefs.put("C/A", new String[]{ "{define C/A base-fret 1 frets 0 0 2 0 1 3}",
			"{define C/A base-fret 1 frets X 0 2 0 1 0}",
			"{define C/A base-fret 1 frets X 0 2 2 1 3}",
			"{define C/A base-fret 5 frets X 0 1 1 1 4}"  });
		defaultDefs.put("C/B", new String[]{ "{define C/B base-fret 1 frets X 2 2 0 1 0}",
			"{define C/B base-fret 1 frets 0 3 2 0 0 0}",
			"{define C/B base-fret 3 frets X 1 3 2 4 1}"  });
		defaultDefs.put("C/Bb", new String[]{ "{define C/Bb base-fret 3 frets X 1 3 1 3 1}"  });
		defaultDefs.put("C/D", new String[]{ "{define C/D base-fret 1 frets 3 X 0 0 1 0}",
			"{define C/D base-fret 1 frets X 3 0 0 1 0}",
			"{define C/D base-fret 1 frets X 3 2 0 3 0}",
			"{define C/D base-fret 1 frets X 3 2 0 3 3}",
			"{define C/D base-fret 1 frets X X 0 0 1 0}",
			"{define C/D base-fret 3 frets X X 0 3 3 1}",
			"{define C/D base-fret 10 frets X 1 3 3 4 0}",
			"{define C/D base-fret 5 frets X 1 1 1 X 0}",
			"{define C/D base-fret 1 frets 0 1 0 0 X X}"  });
		defaultDefs.put("C/F", new String[]{ "{define C/F base-fret 1 frets X 3 3 0 1 0}",
			"{define C/F base-fret 1 frets X X 3 0 1 0}"  });
		defaultDefs.put("C/G", new String[]{ "{define C/G base-fret 1 frets 0 1 0 2 3 3}"  });
		defaultDefs.put("C11", new String[]{ "{define C11 base-fret 3 frets X 1 3 1 4 1}"  });
		defaultDefs.put("C3", new String[]{ "{define C3 base-fret 3 frets X 1 3 3 2 1}"  });
		defaultDefs.put("C4", new String[]{ "{define C4 base-fret 1 frets X X 3 0 1 3}"  });
		defaultDefs.put("C5", new String[]{ "{define C5 base-fret 3 frets X 1 3 3 X 1}"  });
		defaultDefs.put("C6", new String[]{ "{define C6 base-fret 1 frets X 0 2 2 1 3}",
			"{define C6 base-fret 1 frets 0 0 2 0 1 3}",
			"{define C6 base-fret 1 frets X 0 2 0 1 0}",
			"{define C6 base-fret 5 frets X 0 1 1 1 4}"  });
		defaultDefs.put("C6/add9", new String[]{ "{define C6/add9 base-fret 5 frets X 1 3 1 4 0}"  });
		defaultDefs.put("C7", new String[]{ "{define C7 base-fret 1 frets 0 3 2 3 1 0}",
			"{define C7 base-fret 3 frets X 1 3 1 3 1}"  });
		defaultDefs.put("C75", new String[]{ "{define C75 base-fret 3 frets 3 X 2 3 1 X}"  });
		defaultDefs.put("C7sus4", new String[]{ "{define C7sus4 base-fret 3 frets X 1 3 1 4 1}"  });
		defaultDefs.put("C9", new String[]{ "{define C9 base-fret 8 frets 1 3 1 2 1 3}"  });
		defaultDefs.put("C9(11)", new String[]{ "{define C9(11) base-fret 1 frets X 3 3 3 3 X}"  });
		defaultDefs.put("C9(b5)", new String[]{ "{define C9(b5) base-fret 1 frets 0 3 X 3 3 2}"  });
		defaultDefs.put("Cadd2/B", new String[]{ "{define Cadd2/B base-fret 1 frets X 2 0 0 1 0}"  });
		defaultDefs.put("Cadd9", new String[]{ "{define Cadd9 base-fret 1 frets X 3 2 0 3 0}",
			"{define Cadd9 base-fret 1 frets 3 X 0 0 1 0}",
			"{define Cadd9 base-fret 1 frets X 3 0 0 1 0}",
			"{define Cadd9 base-fret 1 frets X 3 2 0 3 3}",
			"{define Cadd9 base-fret 1 frets X X 0 0 1 0}",
			"{define Cadd9 base-fret 3 frets X X 0 3 3 1}",
			"{define Cadd9 base-fret 10 frets X 1 3 3 4 0}",
			"{define Cadd9 base-fret 5 frets X 1 1 1 X 0}"  });
		defaultDefs.put("CaddD", new String[]{ "{define CaddD base-fret 1 frets X 3 2 0 3 0}"  });
		defaultDefs.put("Cb5", new String[]{ "{define Cb5 base-fret 4 frets X X 1 2 X 0}"  });
		defaultDefs.put("Cdim", new String[]{ "{define Cdim base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Cdim/A", new String[]{ "{define Cdim/A base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Cdim/Ab", new String[]{ "{define Cdim/Ab base-fret 1 frets X X 1 1 1 2}",
			"{define Cdim/Ab base-fret 4 frets X X 1 2 1 1}"  });
		defaultDefs.put("Cdim/D", new String[]{ "{define Cdim/D base-fret 2 frets X 4 3 4 3 1}"  });
		defaultDefs.put("Cdim7", new String[]{ "{define Cdim7 base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Cm", new String[]{ "{define Cm base-fret 3 frets X 1 3 3 2 1}",
			"{define Cm base-fret 3 frets X X 3 3 2 1}"  });
		defaultDefs.put("Cm/A", new String[]{ "{define Cm/A base-fret 1 frets X X 1 2 1 3}"  });
		defaultDefs.put("Cm/Bb", new String[]{ "{define Cm/Bb base-fret 3 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Cm11", new String[]{ "{define Cm11 base-fret 3 frets X 1 3 1 4 X}"  });
		defaultDefs.put("Cm6", new String[]{ "{define Cm6 base-fret 1 frets X X 1 2 1 3}"  });
		defaultDefs.put("Cm7", new String[]{ "{define Cm7 base-fret 3 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Cmaj", new String[]{ "{define Cmaj base-fret 1 frets 0 3 2 0 1 0}"  });
		defaultDefs.put("Cmaj7", new String[]{ "{define Cmaj7 base-fret 1 frets X 3 2 0 0 0}",
			"{define Cmaj7 base-fret 1 frets 0 3 2 0 0 0}",
			"{define Cmaj7 base-fret 1 frets X 2 2 0 1 0}",
			"{define Cmaj7 base-fret 3 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Cmaj9", new String[]{ "{define Cmaj9 base-fret 1 frets X 3 0 0 0 0}"  });
		defaultDefs.put("Cmin", new String[]{ "{define Cmin base-fret 3 frets X 1 3 3 2 1}"  });
		defaultDefs.put("Csus", new String[]{ "{define Csus base-fret 1 frets X X 3 0 1 3}",
			"{define Csus base-fret 1 frets X 3 3 0 1 1}",
			"{define Csus base-fret 1 frets X X 3 0 1 1}"  });
		defaultDefs.put("Csus2", new String[]{ "{define Csus2 base-fret 1 frets X 3 0 0 1 X}",
			"{define Csus2 base-fret 3 frets X 3 3 3 X 1}",
			"{define Csus2 base-fret 1 frets X 3 0 0 3 3}",
			"{define Csus2 base-fret 3 frets X 1 3 3 1 1}"  });
		defaultDefs.put("Csus2/A", new String[]{ "{define Csus2/A base-fret 1 frets X X 0 2 1 3}"  });
		defaultDefs.put("Csus2/B", new String[]{ "{define Csus2/B base-fret 1 frets 3 3 0 0 0 3}",
			"{define Csus2/B base-fret 1 frets X 3 0 0 0 3}"  });
		defaultDefs.put("Csus2/E", new String[]{ "{define Csus2/E base-fret 1 frets 3 X 0 0 1 0}",
			"{define Csus2/E base-fret 1 frets X 3 0 0 1 0}",
			"{define Csus2/E base-fret 1 frets X 3 2 0 3 0}",
			"{define Csus2/E base-fret 1 frets X 3 2 0 3 3}",
			"{define Csus2/E base-fret 1 frets X X 0 0 1 0}",
			"{define Csus2/E base-fret 3 frets X X 0 3 3 1}",
			"{define Csus2/E base-fret 10 frets X 1 3 3 4 0}",
			"{define Csus2/E base-fret 5 frets X 1 1 1 X 0}"  });
		defaultDefs.put("Csus2/F", new String[]{ "{define Csus2/F base-fret 1 frets 3 3 0 0 1 1}"  });
		defaultDefs.put("Csus4", new String[]{ "{define Csus4 base-fret 1 frets X X 3 0 1 3}"  });
		defaultDefs.put("Csus4/A", new String[]{ "{define Csus4/A base-fret 1 frets 3 X 3 2 1 1}",
			"{define Csus4/A base-fret 1 frets X X 3 2 1 3}"  });
		defaultDefs.put("Csus4/B", new String[]{ "{define Csus4/B base-fret 1 frets X 3 3 0 0 3}"  });
		defaultDefs.put("Csus4/Bb", new String[]{ "{define Csus4/Bb base-fret 3 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Csus4/D", new String[]{ "{define Csus4/D base-fret 1 frets 3 3 0 0 1 1}"  });
		defaultDefs.put("Csus4/E", new String[]{ "{define Csus4/E base-fret 1 frets X 3 3 0 1 0}",
			"{define Csus4/E base-fret 1 frets X X 3 0 1 0}"  });
		defaultDefs.put("Csus9", new String[]{ "{define Csus9 base-fret 7 frets X X 4 1 2 4}"  });
		defaultDefs.put("D", new String[]{ "{define D base-fret 1 frets X X 0 2 3 2}",
			"{define D base-fret 2 frets X 4 3 1 2 1}",
			"{define D base-fret 1 frets 2 0 0 2 3 2}",
			"{define D base-fret 1 frets X 0 0 2 3 2}",
			"{define D base-fret 1 frets X 0 4 2 3 2}",
			"{define D base-fret 5 frets X X 0 3 3 1}"  });
		defaultDefs.put("D#", new String[]{ "{define D# base-fret 3 frets X X 3 1 2 1}"  });
		defaultDefs.put("D#+", new String[]{ "{define D#+ base-fret 1 frets X X 1 0 0 4}"  });
		defaultDefs.put("D#4", new String[]{ "{define D#4 base-fret 1 frets X X 1 3 4 4}"  });
		defaultDefs.put("D#5", new String[]{ "{define D#5 base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("D#7", new String[]{ "{define D#7 base-fret 1 frets X X 1 3 2 3}"  });
		defaultDefs.put("D#dim", new String[]{ "{define D#dim base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("D#m", new String[]{ "{define D#m base-fret 1 frets X X 4 3 4 2}"  });
		defaultDefs.put("D#m7", new String[]{ "{define D#m7 base-fret 1 frets X X 1 3 2 2}"  });
		defaultDefs.put("D#maj", new String[]{ "{define D#maj base-fret 3 frets X X 3 1 2 1}"  });
		defaultDefs.put("D#maj7", new String[]{ "{define D#maj7 base-fret 1 frets X X 1 3 3 3}"  });
		defaultDefs.put("D#min", new String[]{ "{define D#min base-fret 1 frets X X 4 3 4 2}"  });
		defaultDefs.put("D#sus", new String[]{ "{define D#sus base-fret 1 frets X X 1 3 4 4}"  });
		defaultDefs.put("D#sus4", new String[]{ "{define D#sus4 base-fret 1 frets X X 1 3 4 4}"  });
		defaultDefs.put("D(add9)", new String[]{ "{define D(add9) base-fret 1 frets 0 0 0 2 3 2}"  });
		defaultDefs.put("D+", new String[]{ "{define D+ base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("D/A", new String[]{ "{define D/A base-fret 1 frets X 0 0 2 3 2}"  });
		defaultDefs.put("D/B", new String[]{ "{define D/B base-fret 1 frets X 2 0 2 3 2}",
			"{define D/B base-fret 2 frets X 0 3 3 2 1}",
			"{define D/B base-fret 1 frets X 2 0 2 0 2}",
			"{define D/B base-fret 1 frets X 2 4 2 3 2}",
			"{define D/B base-fret 1 frets X X 0 2 0 2}"  });
		defaultDefs.put("D/C", new String[]{ "{define D/C base-fret 1 frets X 3 0 2 3 2}",
			"{define D/C base-fret 1 frets X 0 0 2 1 2}",
			"{define D/C base-fret 1 frets X 3 X 2 3 2}"  });
		defaultDefs.put("D/C#", new String[]{ "{define D/C# base-fret 1 frets X 4 0 2 3 2}"  });
		defaultDefs.put("D/Db", new String[]{ "{define D/Db base-fret 1 frets X X 0 2 2 2}"  });
		defaultDefs.put("D/E", new String[]{ "{define D/E base-fret 7 frets X 1 1 1 1 X}",
			"{define D/E base-fret 1 frets 0 0 0 2 3 2}",
			"{define D/E base-fret 1 frets 0 0 4 2 3 0}",
			"{define D/E base-fret 1 frets 2 X 0 2 3 0}",
			"{define D/E base-fret 1 frets X 0 2 2 3 2}",
			"{define D/E base-fret 1 frets X X 2 2 3 2}",
			"{define D/E base-fret 2 frets X 4 3 1 2 0}",
			"{define D/E base-fret 7 frets X 3 3 1 X 0}"  });
		defaultDefs.put("D/F#", new String[]{ "{define D/F# base-fret 1 frets 2 X 0 2 3 2}"  });
		defaultDefs.put("D/G", new String[]{ "{define D/G base-fret 1 frets 3 X 0 2 3 2}",
			"{define D/G base-fret 3 frets 3 X 2 0 1 3}"  });
		defaultDefs.put("D11", new String[]{ "{define D11 base-fret 1 frets 3 0 0 2 1 0}"  });
		defaultDefs.put("D4", new String[]{ "{define D4 base-fret 1 frets X X 0 2 3 3}"  });
		defaultDefs.put("D5", new String[]{ "{define D5 base-fret 5 frets 1 1 3 3 X 1}",
			"{define D5 base-fret 2 frets X 0 0 1 2 4}"  });
		defaultDefs.put("D5/E", new String[]{ "{define D5/E base-fret 7 frets 0 1 1 1 X X}"  });
		defaultDefs.put("D6", new String[]{ "{define D6 base-fret 1 frets X 0 0 2 0 2}",
			"{define D6 base-fret 1 frets X 0 4 4 3 2}",
			"{define D6 base-fret 1 frets X 2 0 2 0 2}",
			"{define D6 base-fret 1 frets X 2 4 2 3 2}",
			"{define D6 base-fret 1 frets X X 0 2 0 2}"  });
		defaultDefs.put("D6/add9", new String[]{ "{define D6/add9 base-fret 1 frets 0 0 2 4 3 2}",
			"{define D6/add9 base-fret 7 frets 4 1 1 1 1 1}"  });
		defaultDefs.put("D6/9", new String[]{ "{define D6/9 base-fret 1 frets X X 0 2 0 0}",
			"{define D6/9 base-fret 4 frets X 2 1 1 2 2}",
			"{define D6/9 base-fret 7 frets 4 1 1 3 1 1}",
			"{define D6/9 base-fret 9 frets 2 1 1 1 2 X}"  });
		defaultDefs.put("D7", new String[]{ "{define D7 base-fret 1 frets X X 0 2 1 2}",
			"{define D7 base-fret 1 frets X 0 0 2 1 2}",
			"{define D7 base-fret 1 frets X 3 X 2 3 2}",
			"{define D7 base-fret 5 frets X 1 3 1 3 1}"  });
		defaultDefs.put("D7#9", new String[]{ "{define D7#9 base-fret 4 frets X 2 1 2 3 3}"  });
		defaultDefs.put("D7(#9)", new String[]{ "{define D7(#9) base-fret 4 frets X 2 1 2 3 3}"  });
		defaultDefs.put("D7sus2", new String[]{ "{define D7sus2 base-fret 1 frets X 0 0 2 1 0}"  });
		defaultDefs.put("D7sus4", new String[]{ "{define D7sus4 base-fret 1 frets X 0 0 2 1 3}",
			"{define D7sus4 base-fret 1 frets X X 0 2 1 3}"  });
		defaultDefs.put("D9", new String[]{ "{define D9 base-fret 10 frets 1 3 1 2 1 3}",
			"{define D9 base-fret 1 frets 0 0 0 2 1 2}",
			"{define D9 base-fret 1 frets 2 X 0 2 1 0}",
			"{define D9 base-fret 5 frets X 1 3 1 3 0}"  });
		defaultDefs.put("D9(#5)", new String[]{ "{define D9(#5) base-fret 1 frets 0 3 X 3 3 2}"  });
		defaultDefs.put("D9(add6)", new String[]{ "{define D9(add6) base-fret 10 frets 1 3 3 2 0 0}"  });
		defaultDefs.put("D9add6", new String[]{ "{define D9add6 base-fret 10 frets 1 3 3 2 0 0}"  });
		defaultDefs.put("Dadd9", new String[]{ "{define Dadd9 base-fret 1 frets 0 0 0 2 3 2}",
			"{define Dadd9 base-fret 1 frets 0 0 4 2 3 0}",
			"{define Dadd9 base-fret 1 frets 2 X 0 2 3 0}",
			"{define Dadd9 base-fret 1 frets X 0 2 2 3 2}",
			"{define Dadd9 base-fret 1 frets X X 2 2 3 2}",
			"{define Dadd9 base-fret 2 frets X 4 3 1 2 0}",
			"{define Dadd9 base-fret 7 frets X 3 1 1 X 0}"  });
		defaultDefs.put("Daug/E", new String[]{ "{define Daug/E base-fret 1 frets 2 X 4 3 3 0}"  });
		defaultDefs.put("Db", new String[]{ "{define Db base-fret 1 frets X X 3 1 2 1}",
			"{define Db base-fret 4 frets 1 1 3 3 3 1}",
			"{define Db base-fret 1 frets X 4 3 1 2 1}",
			"{define Db base-fret 4 frets X 1 3 3 3 1}",
			"{define Db base-fret 4 frets X X 3 3 3 1}"  });
		defaultDefs.put("Db#5", new String[]{ "{define Db#5 base-fret 1 frets X 0 3 2 2 1}",
			"{define Db#5 base-fret 1 frets X 0 X 2 2 1}"  });
		defaultDefs.put("Db+", new String[]{ "{define Db+ base-fret 1 frets X X 3 2 2 1}"  });
		defaultDefs.put("Db/B", new String[]{ "{define Db/B base-fret 1 frets X 4 3 4 0 4}"  });
		defaultDefs.put("Db/Bb", new String[]{ "{define Db/Bb base-fret 1 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Db/C", new String[]{ "{define Db/C base-fret 1 frets X 3 3 1 2 1}",
			"{define Db/C base-fret 4 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Db5", new String[]{ "{define Db5 base-fret 4 frets X 1 3 3 X 1}"  });
		defaultDefs.put("Db6", new String[]{ "{define Db6 base-fret 1 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Db7", new String[]{ "{define Db7 base-fret 1 frets X X 3 4 2 4}",
			"{define Db7 base-fret 1 frets X 4 3 4 0 4}"  });
		defaultDefs.put("Dbaug/D", new String[]{ "{define Dbaug/D base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Dbaug/G", new String[]{ "{define Dbaug/G base-fret 1 frets 1 0 3 0 2 1}"  });
		defaultDefs.put("Dbb5", new String[]{ "{define Dbb5 base-fret 1 frets X X 3 0 2 1}"  });
		defaultDefs.put("Dbdim", new String[]{ "{define Dbdim base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Dbdim/A", new String[]{ "{define Dbdim/A base-fret 1 frets 3 X 2 2 2 0}",
			"{define Dbdim/A base-fret 1 frets X 0 2 0 2 0}",
			"{define Dbdim/A base-fret 1 frets X 0 2 2 2 3}"  });
		defaultDefs.put("Dbdim/B", new String[]{ "{define Dbdim/B base-fret 1 frets 0 2 2 0 2 0}"  });
		defaultDefs.put("Dbdim/Bb", new String[]{ "{define Dbdim/Bb base-fret 1 frets X 1 2 0 2 0}",
			"{define Dbdim/Bb base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Dbdim/D", new String[]{ "{define Dbdim/D base-fret 1 frets 3 X 0 0 2 0}",
			"{define Dbdim/D base-fret 1 frets X X 0 0 2 0}"  });
		defaultDefs.put("Dbdim7", new String[]{ "{define Dbdim7 base-fret 1 frets X 1 2 0 2 0}",
			"{define Dbdim7 base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Dbm", new String[]{ "{define Dbm base-fret 1 frets X X 2 1 2 0}",
			"{define Dbm base-fret 4 frets X 1 3 3 2 1}",
			"{define Dbm base-fret 4 frets X 1 3 3 X 0}"  });
		defaultDefs.put("Dbm/A", new String[]{ "{define Dbm/A base-fret 1 frets X 0 2 1 2 0}"  });
		defaultDefs.put("Dbm/B", new String[]{ "{define Dbm/B base-fret 1 frets 0 2 2 1 2 0}",
			"{define Dbm/B base-fret 4 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Dbm7", new String[]{ "{define Dbm7 base-fret 1 frets X X 2 4 2 4}",
			"{define Dbm7 base-fret 1 frets 0 2 2 1 2 0}",
			"{define Dbm7 base-fret 4 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Dbm7(b5)", new String[]{ "{define Dbm7(b5) base-fret 1 frets 0 2 2 0 2 0}"  });
		defaultDefs.put("Dbmaj", new String[]{ "{define Dbmaj base-fret 1 frets X X 3 1 2 1}"  });
		defaultDefs.put("Dbmaj7", new String[]{ "{define Dbmaj7 base-fret 1 frets X 4 3 1 1 1}",
			"{define Dbmaj7 base-fret 1 frets X 3 3 1 2 1}",
			"{define Dbmaj7 base-fret 4 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Dbmin", new String[]{ "{define Dbmin base-fret 1 frets X X 2 1 2 0}"  });
		defaultDefs.put("Dbsus", new String[]{ "{define Dbsus base-fret 4 frets X X 3 3 4 1}"  });
		defaultDefs.put("Dbsus2", new String[]{ "{define Dbsus2 base-fret 4 frets X X 3 3 1 1}"  });
		defaultDefs.put("Dbsus4", new String[]{ "{define Dbsus4 base-fret 4 frets X X 3 3 4 1}"  });
		defaultDefs.put("Dbsus4/Bb", new String[]{ "{define Dbsus4/Bb base-fret 1 frets X X 4 3 2 4}"  });
		defaultDefs.put("Ddim", new String[]{ "{define Ddim base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("Ddim/B", new String[]{ "{define Ddim/B base-fret 1 frets X 2 0 1 0 1}",
			"{define Ddim/B base-fret 1 frets X X 0 1 0 1}",
			"{define Ddim/B base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Ddim/Bb", new String[]{ "{define Ddim/Bb base-fret 1 frets X 1 3 1 3 1}",
			"{define Ddim/Bb base-fret 1 frets X X 3 3 3 4}"  });
		defaultDefs.put("Ddim/C", new String[]{ "{define Ddim/C base-fret 1 frets X X 0 1 1 1}"  });
		defaultDefs.put("Ddim7", new String[]{ "{define Ddim7 base-fret 1 frets X 2 0 1 0 1}",
			"{define Ddim7 base-fret 1 frets X X 0 1 0 1}",
			"{define Ddim7 base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Dm", new String[]{ "{define Dm base-fret 1 frets X X 0 2 3 1}",
			"{define Dm base-fret 1 frets X 0 0 2 3 1}",
			"{define Dm base-fret 7 frets 1 1 3 3 2 1}"  });
		defaultDefs.put("Dm#5", new String[]{ "{define Dm#5 base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("Dm#7", new String[]{ "{define Dm#7 base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Dm(#5)", new String[]{ "{define Dm(#5) base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("Dm(#7)", new String[]{ "{define Dm(#7) base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Dm/A", new String[]{ "{define Dm/A base-fret 1 frets X 0 0 2 3 1}"  });
		defaultDefs.put("Dm/B", new String[]{ "{define Dm/B base-fret 1 frets X 2 0 2 3 1}",
			"{define Dm/B base-fret 1 frets 1 2 3 2 3 1}",
			"{define Dm/B base-fret 1 frets X 2 0 2 0 1}",
			"{define Dm/B base-fret 1 frets X X 0 2 0 1}"  });
		defaultDefs.put("Dm/Bb", new String[]{ "{define Dm/Bb base-fret 1 frets 1 1 3 2 3 1}"  });
		defaultDefs.put("Dm/C", new String[]{ "{define Dm/C base-fret 1 frets X 3 0 2 3 1}",
			"{define Dm/C base-fret 5 frets X 1 3 1 2 1}",
			"{define Dm/C base-fret 1 frets X X 0 2 1 1}",
			"{define Dm/C base-fret 5 frets X X 0 1 2 1}"  });
		defaultDefs.put("Dm/C#", new String[]{ "{define Dm/C# base-fret 1 frets X 4 0 2 3 1}"  });
		defaultDefs.put("Dm/Db", new String[]{ "{define Dm/Db base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Dm/E", new String[]{ "{define Dm/E base-fret 6 frets X X 2 2 1 0}"  });
		defaultDefs.put("Dm6", new String[]{ "{define Dm6 base-fret 1 frets X 2 0 2 0 1}",
			"{define Dm6 base-fret 1 frets X X 0 2 0 1}"  });
		defaultDefs.put("Dm6(5b)", new String[]{ "{define Dm6(5b) base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("Dm7", new String[]{ "{define Dm7 base-fret 1 frets X X 0 2 1 1}",
			"{define Dm7 base-fret 5 frets X 1 3 1 2 1}",
			"{define Dm7 base-fret 5 frets X X 0 1 2 1}"  });
		defaultDefs.put("Dm7(b5)", new String[]{ "{define Dm7(b5) base-fret 1 frets X X 0 1 1 1}"  });
		defaultDefs.put("Dm7/add11", new String[]{ "{define Dm7/add11 base-fret 1 frets 3 X 0 2 1 1}"  });
		defaultDefs.put("Dm9", new String[]{ "{define Dm9 base-fret 1 frets X X 3 2 1 0}"  });
		defaultDefs.put("Dmaj", new String[]{ "{define Dmaj base-fret 1 frets X X 0 2 3 2}"  });
		defaultDefs.put("Dmaj7", new String[]{ "{define Dmaj7 base-fret 1 frets X X 0 2 2 2}",
			"{define Dmaj7 base-fret 14 frets X X 0 1 1 1}"  });
		defaultDefs.put("Dmin", new String[]{ "{define Dmin base-fret 1 frets X X 0 2 3 1}"  });
		defaultDefs.put("Dmin/maj7", new String[]{ "{define Dmin/maj7 base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Dsus", new String[]{ "{define Dsus base-fret 1 frets X X 0 2 3 3}",
			"{define Dsus base-fret 3 frets 3 X 0 0 1 3}",
			"{define Dsus base-fret 1 frets 3 0 0 0 3 3}",
			"{define Dsus base-fret 1 frets X 0 0 0 3 3}"  });
		defaultDefs.put("Dsus2", new String[]{ "{define Dsus2 base-fret 1 frets 0 0 0 2 3 0}",
			"{define Dsus2 base-fret 5 frets 1 1 3 3 X 0}",
			"{define Dsus2 base-fret 1 frets X 0 0 2 3 0}",
			"{define Dsus2 base-fret 1 frets 0 0 2 2 3 0}",
			"{define Dsus2 base-fret 1 frets X 0 2 2 3 0}",
			"{define Dsus2 base-fret 1 frets X X 0 2 3 0}"  });
		defaultDefs.put("Dsus2/Ab", new String[]{ "{define Dsus2/Ab base-fret 1 frets 4 X 0 2 3 0}"  });
		defaultDefs.put("Dsus2/B", new String[]{ "{define Dsus2/B base-fret 1 frets 0 2 0 2 0 0}",
			"{define Dsus2/B base-fret 1 frets X 2 0 2 3 0}"  });
		defaultDefs.put("Dsus2/Bb", new String[]{ "{define Dsus2/Bb base-fret 1 frets 0 1 X 2 3 0}"  });
		defaultDefs.put("Dsus2/C", new String[]{ "{define Dsus2/C base-fret 1 frets X X 0 2 1 0}",
			"{define Dsus2/C base-fret 5 frets X X 0 1 1 1}"  });
		defaultDefs.put("Dsus2/Db", new String[]{ "{define Dsus2/Db base-fret 1 frets X 0 0 2 2 0}",
			"{define Dsus2/Db base-fret 1 frets X X 0 2 2 0}",
			"{define Dsus2/Db base-fret 5 frets X X 0 2 1 1}",
			"{define Dsus2/Db base-fret 9 frets X X 0 1 2 1}"  });
		defaultDefs.put("Dsus2/F", new String[]{ "{define Dsus2/F base-fret 6 frets X X 2 2 1 0}"  });
		defaultDefs.put("Dsus2/G", new String[]{ "{define Dsus2/G base-fret 1 frets X 0 2 0 3 0}",
			"{define Dsus2/G base-fret 1 frets X 0 2 0 3 3}",
			"{define Dsus2/G base-fret 1 frets X 0 2 2 3 3}",
			"{define Dsus2/G base-fret 3 frets 3 X 0 0 1 0}",
			"{define Dsus2/G base-fret 1 frets X 0 0 0 X 0}"  });
		defaultDefs.put("Dsus2/Gb", new String[]{ "{define Dsus2/Gb base-fret 1 frets 0 0 0 2 3 2}",
			"{define Dsus2/Gb base-fret 1 frets 0 0 4 2 3 0}",
			"{define Dsus2/Gb base-fret 1 frets 2 X 0 2 3 0}",
			"{define Dsus2/Gb base-fret 1 frets X 0 2 2 3 2}",
			"{define Dsus2/Gb base-fret 1 frets X X 2 2 3 2}",
			"{define Dsus2/Gb base-fret 2 frets X 4 3 1 2 0}",
			"{define Dsus2/Gb base-fret 7 frets X 3 1 1 X 0}"  });
		defaultDefs.put("Dsus4", new String[]{ "{define Dsus4 base-fret 1 frets X X 0 2 3 3}"  });
		defaultDefs.put("Dsus4/B", new String[]{ "{define Dsus4/B base-fret 1 frets 3 0 0 0 0 3}",
			"{define Dsus4/B base-fret 1 frets 3 2 0 2 0 3}"  });
		defaultDefs.put("Dsus4/C", new String[]{ "{define Dsus4/C base-fret 1 frets X X 0 2 1 3}"  });
		defaultDefs.put("Dsus4/E", new String[]{ "{define Dsus4/E base-fret 1 frets X 0 2 0 3 0}",
			"{define Dsus4/E base-fret 1 frets X 0 2 0 3 3}",
			"{define Dsus4/E base-fret 1 frets X 0 2 2 3 3}",
			"{define Dsus4/E base-fret 3 frets 3 X 0 0 1 0}",
			"{define Dsus4/E base-fret 1 frets X 0 0 0 X 0}"  });
		defaultDefs.put("Dsus4/Gb", new String[]{ "{define Dsus4/Gb base-fret 3 frets 3 X 2 0 1 3}",
			"{define Dsus4/Gb base-fret 1 frets 3 X 0 2 3 2}"  });
		defaultDefs.put("E", new String[]{ "{define E base-fret 1 frets 0 2 2 1 0 0}",
			"{define E base-fret 4 frets X 4 3 1 2 0}"  });
		defaultDefs.put("E#5", new String[]{ "{define E#5 base-fret 1 frets X 3 2 1 1 0}"  });
		defaultDefs.put("E+", new String[]{ "{define E+ base-fret 1 frets X X 2 1 1 0}"  });
		defaultDefs.put("E/A", new String[]{ "{define E/A base-fret 1 frets X 0 2 1 0 0}"  });
		defaultDefs.put("E/D", new String[]{ "{define E/D base-fret 1 frets 0 2 0 1 0 0}",
			"{define E/D base-fret 1 frets 0 2 2 1 3 0}",
			"{define E/D base-fret 1 frets X 2 0 1 3 0}",
			"{define E/D base-fret 1 frets X X 0 1 0 0}"  });
		defaultDefs.put("E/Db", new String[]{ "{define E/Db base-fret 1 frets 0 2 2 1 2 0}",
			"{define E/Db base-fret 4 frets X 1 3 1 2 1}"  });
		defaultDefs.put("E/Eb", new String[]{ "{define E/Eb base-fret 1 frets 0 2 1 1 0 0}",
			"{define E/Eb base-fret 4 frets 0 X 3 1 1 0}",
			"{define E/Eb base-fret 1 frets X X 1 1 0 0}"  });
		defaultDefs.put("E/Gb", new String[]{ "{define E/Gb base-fret 1 frets 0 2 2 1 0 2}",
			"{define E/Gb base-fret 1 frets 0 X 4 1 0 0}",
			"{define E/Gb base-fret 1 frets 2 2 2 1 0 0}"  });
		defaultDefs.put("E11", new String[]{ "{define E11 base-fret 1 frets 1 1 1 1 2 2}"  });
		defaultDefs.put("E11/b9", new String[]{ "{define E11/b9 base-fret 1 frets 0 0 3 4 3 4}"  });
		defaultDefs.put("E5", new String[]{ "{define E5 base-fret 7 frets 0 1 3 3 X X}",
			"{define E5 base-fret 1 frets 0 2 X X X 0}",
			"{define E5 base-fret 7 frets X 1 3 3 X 0}"  });
		defaultDefs.put("E6", new String[]{ "{define E6 base-fret 9 frets X X 3 3 3 3}",
			"{define E6 base-fret 1 frets 0 2 2 1 2 0}",
			"{define E6 base-fret 4 frets X 1 3 1 2 1}"  });
		defaultDefs.put("E7", new String[]{ "{define E7 base-fret 1 frets 0 2 2 1 3 0}",
			"{define E7 base-fret 1 frets 0 2 0 1 0 0}",
			"{define E7 base-fret 1 frets X 2 0 1 3 0}",
			"{define E7 base-fret 1 frets X X 0 1 0 0}"  });
		defaultDefs.put("E7#9", new String[]{ "{define E7#9 base-fret 1 frets 0 2 2 1 3 3}"  });
		defaultDefs.put("E7(#9)", new String[]{ "{define E7(#9) base-fret 1 frets 0 2 2 1 3 3}"  });
		defaultDefs.put("E7(11)", new String[]{ "{define E7(11) base-fret 1 frets 0 2 2 2 3 0}"  });
		defaultDefs.put("E7(5b)", new String[]{ "{define E7(5b) base-fret 1 frets X 1 0 1 3 0}"  });
		defaultDefs.put("E7(b9)", new String[]{ "{define E7(b9) base-fret 1 frets 0 2 0 1 3 2}"  });
		defaultDefs.put("E7/add11", new String[]{ "{define E7/add11 base-fret 1 frets X 0 0 1 0 0}"  });
		defaultDefs.put("E7/b9(b5)", new String[]{ "{define E7/b9(b5) base-fret 1 frets 0 1 3 1 3 1}"  });
		defaultDefs.put("E7b9", new String[]{ "{define E7b9 base-fret 1 frets 0 2 0 1 3 2}"  });
		defaultDefs.put("E7sus4", new String[]{ "{define E7sus4 base-fret 1 frets 0 2 0 2 0 0}"  });
		defaultDefs.put("E9", new String[]{ "{define E9 base-fret 1 frets 1 3 1 2 1 3}",
			"{define E9 base-fret 1 frets 0 2 0 1 0 2}",
			"{define E9 base-fret 1 frets 2 2 0 1 0 0}"  });
		defaultDefs.put("Eadd9", new String[]{ "{define Eadd9 base-fret 1 frets 0 2 2 1 0 2}",
			"{define Eadd9 base-fret 1 frets 0 X 4 1 0 0}",
			"{define Eadd9 base-fret 1 frets 2 2 2 1 0 0}"  });
		defaultDefs.put("Eb", new String[]{ "{define Eb base-fret 3 frets X X 3 1 2 1}",
			"{define Eb base-fret 1 frets X 1 1 3 4 3}",
			"{define Eb base-fret 1 frets X X 1 3 4 3}"  });
		defaultDefs.put("Eb#5", new String[]{ "{define Eb#5 base-fret 1 frets 3 2 1 0 0 3}",
			"{define Eb#5 base-fret 1 frets 3 X 1 0 0 3}"  });
		defaultDefs.put("Eb(add9)", new String[]{ "{define Eb(add9) base-fret 1 frets X 1 1 3 4 1}"  });
		defaultDefs.put("Eb+", new String[]{ "{define Eb+ base-fret 1 frets X X 1 0 0 4}"  });
		defaultDefs.put("Eb/C", new String[]{ "{define Eb/C base-fret 3 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Eb/D", new String[]{ "{define Eb/D base-fret 6 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Eb/Db", new String[]{ "{define Eb/Db base-fret 1 frets X 1 1 3 2 3}",
			"{define Eb/Db base-fret 6 frets X 1 3 1 3 1}",
			"{define Eb/Db base-fret 1 frets X X 1 3 2 3}"  });
		defaultDefs.put("Eb/E", new String[]{ "{define Eb/E base-fret 3 frets X X 3 1 2 0}"  });
		defaultDefs.put("Eb4", new String[]{ "{define Eb4 base-fret 1 frets X X 1 3 4 4}"  });
		defaultDefs.put("Eb5", new String[]{ "{define Eb5 base-fret 6 frets X 1 3 3 X 1}"  });
		defaultDefs.put("Eb6", new String[]{ "{define Eb6 base-fret 3 frets X 1 3 1 2 1}"  });
		defaultDefs.put("Eb7", new String[]{ "{define Eb7 base-fret 1 frets X X 1 3 2 3}",
			"{define Eb7 base-fret 1 frets X 1 1 3 2 3}",
			"{define Eb7 base-fret 6 frets X 1 3 1 3 1}"  });
		defaultDefs.put("Ebadd9", new String[]{ "{define Ebadd9 base-fret 1 frets X 1 1 3 4 1}"  });
		defaultDefs.put("Ebaug/E", new String[]{ "{define Ebaug/E base-fret 1 frets 3 X 1 0 0 0}",
			"{define Ebaug/E base-fret 1 frets X X 1 0 0 0}"  });
		defaultDefs.put("Ebdim", new String[]{ "{define Ebdim base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Ebdim/B", new String[]{ "{define Ebdim/B base-fret 1 frets 2 X 1 2 0 2}",
			"{define Ebdim/B base-fret 1 frets X 0 1 2 0 2}",
			"{define Ebdim/B base-fret 1 frets X 2 1 2 0 2}",
			"{define Ebdim/B base-fret 1 frets X 2 4 2 4 2}"  });
		defaultDefs.put("Ebdim/C", new String[]{ "{define Ebdim/C base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Ebdim7", new String[]{ "{define Ebdim7 base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Ebm", new String[]{ "{define Ebm base-fret 1 frets X X 4 3 4 2}"  });
		defaultDefs.put("Ebm/Db", new String[]{ "{define Ebm/Db base-fret 1 frets X X 1 3 2 2}"  });
		defaultDefs.put("Ebm7", new String[]{ "{define Ebm7 base-fret 1 frets X X 1 3 2 2}"  });
		defaultDefs.put("Ebmaj", new String[]{ "{define Ebmaj base-fret 1 frets X X 1 3 3 3}"  });
		defaultDefs.put("Ebmaj7", new String[]{ "{define Ebmaj7 base-fret 1 frets X X 1 3 3 3}",
			"{define Ebmaj7 base-fret 6 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Ebmin", new String[]{ "{define Ebmin base-fret 1 frets X X 4 3 4 2}"  });
		defaultDefs.put("Ebsus", new String[]{ "{define Ebsus base-fret 1 frets X X 1 3 4 4}"  });
		defaultDefs.put("Ebsus2/Ab", new String[]{ "{define Ebsus2/Ab base-fret 1 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Ebsus4", new String[]{ "{define Ebsus4 base-fret 1 frets X X 1 3 4 4}"  });
		defaultDefs.put("Ebsus4/F", new String[]{ "{define Ebsus4/F base-fret 1 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Edim", new String[]{ "{define Edim base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Edim/C", new String[]{ "{define Edim/C base-fret 3 frets X 1 3 1 3 1}"  });
		defaultDefs.put("Edim/D", new String[]{ "{define Edim/D base-fret 1 frets 3 X 0 3 3 0}"  });
		defaultDefs.put("Edim/Db", new String[]{ "{define Edim/Db base-fret 1 frets X 1 2 0 2 0}",
			"{define Edim/Db base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Edim/Eb", new String[]{ "{define Edim/Eb base-fret 3 frets X X 3 1 2 0}"  });
		defaultDefs.put("Edim7", new String[]{ "{define Edim7 base-fret 1 frets X 1 2 0 2 0}",
			"{define Edim7 base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Em", new String[]{ "{define Em base-fret 1 frets 0 2 2 0 0 0}",
			"{define Em base-fret 1 frets 3 X 2 0 0 0}",
			"{define Em base-fret 2 frets X 1 4 X X 0}"  });
		defaultDefs.put("Em(add9)", new String[]{ "{define Em(add9) base-fret 1 frets 0 2 4 0 0 0}"  });
		defaultDefs.put("Em(sus4)", new String[]{ "{define Em(sus4) base-fret 1 frets 0 0 2 0 0 0}"  });
		defaultDefs.put("Em/A", new String[]{ "{define Em/A base-fret 1 frets 3 X 2 2 0 0}",
			"{define Em/A base-fret 1 frets X 0 2 0 0 0}",
			"{define Em/A base-fret 4 frets X 0 2 1 2 0}"  });
		defaultDefs.put("Em/B", new String[]{ "{define Em/B base-fret 1 frets X 2 2 0 0 0}"  });
		defaultDefs.put("Em/C", new String[]{ "{define Em/C base-fret 1 frets 0 3 2 0 0 0}",
			"{define Em/C base-fret 1 frets X 2 2 0 1 0}",
			"{define Em/C base-fret 3 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Em/D", new String[]{ "{define Em/D base-fret 1 frets X X 0 0 0 0}",
			"{define Em/D base-fret 1 frets 0 2 0 0 0 0}",
			"{define Em/D base-fret 1 frets 0 2 0 0 3 0}",
			"{define Em/D base-fret 1 frets 0 2 2 0 3 0}",
			"{define Em/D base-fret 1 frets 0 2 2 0 3 3}",
			"{define Em/D,. base-fret 1 frets 0 X 0 0 0 0}",
			"{define Em/D base-fret 12 frets X X 0 1 1 1}",
			"{define Em/D base-fret 7 frets X X 0 3 2 1}",
			"{define Em/D base-fret 1 frets X X 2 4 3 3}",
			"{define Em/D base-fret 10 frets X 1 3 3 3 0}"  });
		defaultDefs.put("Em/Db", new String[]{ "{define Em/Db base-fret 1 frets 0 2 2 0 2 0}"  });
		defaultDefs.put("Em/Eb", new String[]{ "{define Em/Eb base-fret 1 frets 3 X 1 0 0 0}",
			"{define Em/Eb base-fret 1 frets X X 1 0 0 0}"  });
		defaultDefs.put("Em/Gb", new String[]{ "{define Em/Gb base-fret 1 frets 0 2 2 0 0 2}",
			"{define Em/Gb base-fret 1 frets 0 2 4 0 0 0}",
			"{define Em/Gb base-fret 1 frets 0 X 4 0 0 0}",
			"{define Em/Gb base-fret 1 frets 2 2 2 0 0 0}"  });
		defaultDefs.put("Em6", new String[]{ "{define Em6 base-fret 1 frets 0 2 2 0 2 0}"  });
		defaultDefs.put("Em7", new String[]{ "{define Em7 base-fret 1 frets 0 2 2 0 3 0}",
			"{define Em7 base-fret 1 frets 0 2 0 0 0 0}",
			"{define Em7 base-fret 1 frets 0 2 0 0 3 0}",
			"{define Em7 base-fret 1 frets 0 2 2 0 3 3}",
			"{define Em7 base-fret 1 frets X X 0 0 0 0}",
			"{define Em7 base-fret 12 frets X X 0 1 1 1}",
			"{define Em7 base-fret 7 frets X X 0 3 2 1}",
			"{define Em7 base-fret 1 frets X X 2 4 3 3}",
			"{define Em7 base-fret 1 frets 0 X 0 0 0 0}",
			"{define Em7 base-fret 10 frets X 1 3 3 3 0}"  });
		defaultDefs.put("Em7(b5)", new String[]{ "{define Em7(b5) base-fret 1 frets 3 X 0 3 3 0}"  });
		defaultDefs.put("Em7/D", new String[]{ "{define Em7/D base-fret 1 frets X X 0 0 0 0}"  });
		defaultDefs.put("Em7/add11", new String[]{ "{define Em7/add11 base-fret 1 frets 0 0 0 0 0 0}",
			"{define Em7/add11 base-fret 1 frets 0 0 0 0 0 3}",
			"{define Em7/add11 base-fret 1 frets 3 X 0 2 0 0}"  });
		defaultDefs.put("Em9", new String[]{ "{define Em9 base-fret 1 frets 0 2 0 0 0 2}",
			"{define Em9 base-fret 1 frets 0 2 0 0 3 2}",
			"{define Em9 base-fret 1 frets 2 2 0 0 0 0}"  });
		defaultDefs.put("Emadd9", new String[]{ "{define Emadd9 base-fret 1 frets 0 2 4 0 0 0}"  });
		defaultDefs.put("Emaj", new String[]{ "{define Emaj base-fret 1 frets 0 2 2 1 0 0}"  });
		defaultDefs.put("Emaj7", new String[]{ "{define Emaj7 base-fret 1 frets 0 2 1 1 0 X}",
			"{define Emaj7 base-fret 1 frets 0 2 1 1 0 0}",
			"{define Emaj7 base-fret 4 frets 0 X 3 1 1 0}",
			"{define Emaj7 base-fret 1 frets X X 1 1 0 0}"  });
		defaultDefs.put("Emaj9", new String[]{ "{define Emaj9 base-fret 1 frets 0 2 1 1 0 2}",
			"{define Emaj9 base-fret 1 frets 4 X 4 4 4 0}"  });
		defaultDefs.put("Emin", new String[]{ "{define Emin base-fret 1 frets 0 2 2 0 0 0}"  });
		defaultDefs.put("Emin/maj7", new String[]{ "{define Emin/maj7 base-fret 1 frets 3 X 1 0 0 0}",
			"{define Emin/maj7 base-fret 1 frets X X 1 0 0 0}"  });
		defaultDefs.put("Emin/maj9", new String[]{ "{define Emin/maj9 base-fret 4 frets 0 3 1 0 0 0}"  });
		defaultDefs.put("Emsus4", new String[]{ "{define Emsus4 base-fret 1 frets 0 0 2 0 0 0}"  });
		defaultDefs.put("Esus", new String[]{ "{define Esus base-fret 1 frets 0 2 2 2 0 0}",
			"{define Esus base-fret 1 frets 0 0 2 2 0 0}",
			"{define Esus base-fret 1 frets 0 0 2 4 0 0}",
			"{define Esus base-fret 1 frets X 0 2 2 0 0}",
			"{define Esus base-fret 1 frets X X 2 2 0 0}"  });
		defaultDefs.put("Esus2", new String[]{ "{define Esus2 base-fret 7 frets 1 3 3 X X 0}",
			"{define Esus2 base-fret 1 frets X 2 4 4 X 0}"  });
		defaultDefs.put("Esus2/A", new String[]{ "{define Esus2/A base-fret 1 frets X 0 4 4 0 0}",
			"{define Esus2/A base-fret 2 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Esus2/Ab", new String[]{ "{define Esus2/Ab base-fret 1 frets 0 2 2 1 0 2}",
			"{define Esus2/Ab base-fret 1 frets 0 X 4 1 0 0}",
			"{define Esus2/Ab base-fret 1 frets 2 2 2 1 0 0}"  });
		defaultDefs.put("Esus2/Db", new String[]{ "{define Esus2/Db base-fret 1 frets X 4 4 4 X 0}"  });
		defaultDefs.put("Esus2/Eb", new String[]{ "{define Esus2/Eb base-fret 1 frets X 2 2 4 4 2}",
			"{define Esus2/Eb base-fret 1 frets X X 4 4 4 0}"  });
		defaultDefs.put("Esus2/G", new String[]{ "{define Esus2/G base-fret 1 frets 0 2 2 0 0 2}",
			"{define Esus2/G base-fret 1 frets 0 2 4 0 0 0}",
			"{define Esus2/G base-fret 1 frets 0 X 4 0 0 0}",
			"{define Esus2/G base-fret 1 frets 2 2 2 0 0 0}"  });
		defaultDefs.put("Esus4", new String[]{ "{define Esus4 base-fret 1 frets 0 2 2 2 0 0}"  });
		defaultDefs.put("Esus4/Ab", new String[]{ "{define Esus4/Ab base-fret 1 frets X 0 2 1 0 0}"  });
		defaultDefs.put("Esus4/C", new String[]{ "{define Esus4/C base-fret 5 frets 0 0 3 1 0 0}",
			"{define Esus4/C base-fret 1 frets X 3 2 2 0 0}"  });
		defaultDefs.put("Esus4/D", new String[]{ "{define Esus4/D base-fret 1 frets 0 2 0 2 0 0}",
			"{define Esus4/D base-fret 1 frets X 2 0 2 3 0}"  });
		defaultDefs.put("Esus4/Db", new String[]{ "{define Esus4/Db base-fret 1 frets 0 0 2 4 2 0}",
			"{define Esus4/Db base-fret 6 frets X 0 2 1 0 0}"  });
		defaultDefs.put("Esus4/Eb", new String[]{ "{define Esus4/Eb base-fret 1 frets X 2 1 2 0 0}"  });
		defaultDefs.put("Esus4/F", new String[]{ "{define Esus4/F base-fret 1 frets 0 0 3 2 0 0}"  });
		defaultDefs.put("Esus4/G", new String[]{ "{define Esus4/G base-fret 1 frets 3 X 2 2 0 0}",
			"{define Esus4/G base-fret 1 frets X 0 2 0 0 0}",
			"{define Esus4/G base-fret 4 frets X 0 2 1 2 0}"  });
		defaultDefs.put("Esus4/Gb", new String[]{ "{define Esus4/Gb base-fret 1 frets X 0 4 4 0 0}",
			"{define Esus4/Gb base-fret 2 frets X 1 3 1 4 1}"  });
		defaultDefs.put("F", new String[]{ "{define F base-fret 1 frets 1 3 3 2 1 1}",
			"{define F base-fret 1 frets X 0 3 2 1 1}",
			"{define F base-fret 1 frets X 3 3 2 1 1}",
			"{define F base-fret 1 frets X X 3 2 1 1}"  });
		defaultDefs.put("F#", new String[]{ "{define F# base-fret 1 frets 2 4 4 3 2 2}"  });
		defaultDefs.put("F#+", new String[]{ "{define F#+ base-fret 1 frets X X 4 3 3 2}"  });
		defaultDefs.put("F#/E", new String[]{ "{define F#/E base-fret 1 frets 0 4 4 3 2 2}"  });
		defaultDefs.put("F#11", new String[]{ "{define F#11 base-fret 1 frets 2 4 2 4 2 2}"  });
		defaultDefs.put("F#4", new String[]{ "{define F#4 base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("F#5", new String[]{ "{define F#5 base-fret 1 frets X 0 3 2 2 1}",
			"{define F#5 base-fret 1 frets X 0 X 2 2 1}"  });
		defaultDefs.put("F#7", new String[]{ "{define F#7 base-fret 1 frets X X 4 3 2 0}"  });
		defaultDefs.put("F#9", new String[]{ "{define F#9 base-fret 1 frets X 1 2 1 2 2}"  });
		defaultDefs.put("F#dim", new String[]{ "{define F#dim base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("F#m", new String[]{ "{define F#m base-fret 1 frets 2 4 4 2 2 2}"  });
		defaultDefs.put("F#m/C#m", new String[]{ "{define F#m/C#m base-fret 1 frets X X 4 2 2 2}"  });
		defaultDefs.put("F#m6", new String[]{ "{define F#m6 base-fret 1 frets X X 1 2 2 2}"  });
		defaultDefs.put("F#m7", new String[]{ "{define F#m7 base-fret 1 frets X X 2 2 2 2}"  });
		defaultDefs.put("F#m7-5", new String[]{ "{define F#m7-5 base-fret 2 frets 1 0 2 3 3 3}"  });
		defaultDefs.put("F#maj", new String[]{ "{define F#maj base-fret 1 frets 2 4 4 3 2 2}"  });
		defaultDefs.put("F#maj7", new String[]{ "{define F#maj7 base-fret 1 frets X X 4 3 2 1}"  });
		defaultDefs.put("F#min", new String[]{ "{define F#min base-fret 1 frets 2 4 4 2 2 2}"  });
		defaultDefs.put("F#sus", new String[]{ "{define F#sus base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("F#sus4", new String[]{ "{define F#sus4 base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("F(add9)", new String[]{ "{define F(add9) base-fret 1 frets 3 0 3 2 1 1}"  });
		defaultDefs.put("F+", new String[]{ "{define F+ base-fret 1 frets X X 3 2 2 1}"  });
		defaultDefs.put("F+7+11", new String[]{ "{define F+7+11 base-fret 1 frets 1 3 3 2 0 0}"  });
		defaultDefs.put("F/A", new String[]{ "{define F/A base-fret 1 frets X 0 3 2 1 1}"  });
		defaultDefs.put("F/C", new String[]{ "{define F/C base-fret 1 frets X X 3 2 1 1}"  });
		defaultDefs.put("F/D", new String[]{ "{define F/D base-fret 1 frets X X 0 2 1 1}",
			"{define F/D base-fret 5 frets X 1 3 1 2 1}",
			"{define F/D base-fret 5 frets X X 0 1 2 1}"  });
		defaultDefs.put("F/E", new String[]{ "{define F/E base-fret 1 frets 0 0 3 2 1 0}",
			"{define F/E base-fret 1 frets 1 3 3 2 1 0}",
			"{define F/E base-fret 1 frets 1 X 2 2 1 0}",
			"{define F/E base-fret 1 frets X X 2 2 1 1}",
			"{define F/E base-fret 1 frets X X 3 2 1 0}"  });
		defaultDefs.put("F/Eb", new String[]{ "{define F/Eb base-fret 1 frets X X 1 2 1 1}",
			"{define F/Eb base-fret 3 frets X X 1 3 2 3}"  });
		defaultDefs.put("F/G", new String[]{ "{define F/G base-fret 1 frets 3 3 3 2 1 1}",
			"{define F/G base-fret 1 frets 3 X 3 2 1 1}",
			"{define F/G base-fret 1 frets X X 3 2 1 3}"  });
		defaultDefs.put("F11", new String[]{ "{define F11 base-fret 1 frets 1 3 1 3 1 1}"  });
		defaultDefs.put("F4", new String[]{ "{define F4 base-fret 1 frets X X 3 3 1 1}"  });
		defaultDefs.put("F5", new String[]{ "{define F5 base-fret 1 frets 1 3 3 X X 1}"  });
		defaultDefs.put("F6", new String[]{ "{define F6 base-fret 1 frets X 3 3 2 3 X}",
			"{define F6 base-fret 5 frets X 1 3 1 2 1}",
			"{define F6 base-fret 1 frets X X 0 2 1 1}",
			"{define F6 base-fret 5 frets X X 0 1 2 1}"  });
		defaultDefs.put("F6/add9", new String[]{ "{define F6/add9 base-fret 1 frets 3 X 0 2 1 1}"  });
		defaultDefs.put("F7", new String[]{ "{define F7 base-fret 1 frets 1 3 1 2 1 1}",
			"{define F7 base-fret 1 frets X X 1 2 1 1}",
			"{define F7 base-fret 3 frets X X 1 3 2 3}"  });
		defaultDefs.put("F7/A", new String[]{ "{define F7/A base-fret 1 frets X 0 3 0 1 1}"  });
		defaultDefs.put("F9", new String[]{ "{define F9 base-fret 1 frets 2 4 2 3 2 4}"  });
		defaultDefs.put("Fadd9", new String[]{ "{define Fadd9 base-fret 1 frets 3 0 3 2 1 1}",
			"{define Fadd9 base-fret 1 frets 3 X 3 2 1 1}",
			"{define Fadd9 base-fret 1 frets X X 3 2 1 3}"  });
		defaultDefs.put("FaddG", new String[]{ "{define FaddG base-fret 1 frets 1 X 3 2 1 3}"  });
		defaultDefs.put("Faug/D", new String[]{ "{define Faug/D base-fret 1 frets X X 0 2 2 1}"  });
		defaultDefs.put("Faug/G", new String[]{ "{define Faug/G base-fret 1 frets 1 0 3 0 2 1}"  });
		defaultDefs.put("Fdim", new String[]{ "{define Fdim base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("Fdim/D", new String[]{ "{define Fdim/D base-fret 1 frets X 2 0 1 0 1}",
			"{define Fdim/D base-fret 1 frets X X 0 1 0 1}",
			"{define Fdim/D base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Fdim/Db", new String[]{ "{define Fdim/Db base-fret 1 frets X 4 3 4 0 4}"  });
		defaultDefs.put("Fdim7", new String[]{ "{define Fdim7 base-fret 1 frets X 2 0 1 0 1}",
			"{define Fdim7 base-fret 1 frets X X 0 1 0 1}",
			"{define Fdim7 base-fret 1 frets X X 3 4 3 4}"  });
		defaultDefs.put("Fm", new String[]{ "{define Fm base-fret 1 frets 1 3 3 1 1 1}",
			"{define Fm base-fret 1 frets X 3 3 1 1 1}",
			"{define Fm base-fret 1 frets X X 3 1 1 1}"  });
		defaultDefs.put("Fm/D", new String[]{ "{define Fm/D base-fret 1 frets X X 0 1 1 1}"  });
		defaultDefs.put("Fm/Db", new String[]{ "{define Fm/Db base-fret 1 frets X 3 3 1 2 1}",
			"{define Fm/Db base-fret 4 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Fm/Eb", new String[]{ "{define Fm/Eb base-fret 8 frets X 1 3 1 2 1}",
			"{define Fm/Eb base-fret 1 frets X X 1 1 1 1}"  });
		defaultDefs.put("Fm6", new String[]{ "{define Fm6 base-fret 1 frets X X 0 1 1 1}"  });
		defaultDefs.put("Fm7", new String[]{ "{define Fm7 base-fret 1 frets 1 3 1 1 1 1}",
			"{define Fm7 base-fret 8 frets X 1 3 1 2 1}",
			"{define Fm7 base-fret 1 frets X X 1 1 1 1}"  });
		defaultDefs.put("Fmaj", new String[]{ "{define Fmaj base-fret 1 frets 1 3 3 2 1 1}"  });
		defaultDefs.put("Fmaj7", new String[]{ "{define Fmaj7 base-fret 1 frets X 3 3 2 1 0}",
			"{define Fmaj7 base-fret 1 frets 0 0 3 2 1 0}",
			"{define Fmaj7 base-fret 1 frets 1 3 3 2 1 0}",
			"{define Fmaj7 base-fret 1 frets 1 X 2 2 1 0}",
			"{define Fmaj7 base-fret 1 frets X X 2 2 1 1}",
			"{define Fmaj7 base-fret 1 frets X X 3 2 1 0}"  });
		defaultDefs.put("Fmaj7(+5)", new String[]{ "{define Fmaj7(+5) base-fret 1 frets X X 3 2 2 0}"  });
		defaultDefs.put("Fmaj7/#11", new String[]{ "{define Fmaj7/#11 base-fret 1 frets 0 2 3 2 1 0}",
			"{define Fmaj7/#11 base-fret 1 frets 1 3 3 2 0 0}"  });
		defaultDefs.put("Fmaj7/A", new String[]{ "{define Fmaj7/A base-fret 1 frets X 0 3 2 1 0}"  });
		defaultDefs.put("Fmaj7/C", new String[]{ "{define Fmaj7/C base-fret 1 frets X 3 3 2 1 0}"  });
		defaultDefs.put("Fmaj9", new String[]{ "{define Fmaj9 base-fret 1 frets 0 0 3 0 1 3}"  });
		defaultDefs.put("Fmin", new String[]{ "{define Fmin base-fret 1 frets 1 3 3 1 1 1}"  });
		defaultDefs.put("Fmmaj7", new String[]{ "{define Fmmaj7 base-fret 1 frets X 3 3 1 1 0}"  });
		defaultDefs.put("Fsus", new String[]{ "{define Fsus base-fret 1 frets X X 3 3 1 1}"  });
		defaultDefs.put("Fsus2", new String[]{ "{define Fsus2 base-fret 1 frets X 3 3 0 1 1}",
			"{define Fsus2 base-fret 1 frets X X 3 0 1 1}"  });
		defaultDefs.put("Fsus2/A", new String[]{ "{define Fsus2/A base-fret 1 frets 3 X 3 2 1 1}",
			"{define Fsus2/A base-fret 1 frets X X 3 2 1 3}"  });
		defaultDefs.put("Fsus2/B", new String[]{ "{define Fsus2/B base-fret 1 frets X 3 3 0 0 3}"  });
		defaultDefs.put("Fsus2/Bb", new String[]{ "{define Fsus2/Bb base-fret 3 frets X 1 3 1 4 1}"  });
		defaultDefs.put("Fsus2/D", new String[]{ "{define Fsus2/D base-fret 1 frets 3 3 0 0 1 1}"  });
		defaultDefs.put("Fsus2/E", new String[]{ "{define Fsus2/E base-fret 1 frets X 3 3 0 1 0}",
			"{define Fsus2/E base-fret 1 frets X X 3 0 1 0}"  });
		defaultDefs.put("Fsus4", new String[]{ "{define Fsus4 base-fret 1 frets X X 3 3 1 1}"  });
		defaultDefs.put("Fsus4/G", new String[]{ "{define Fsus4/G base-fret 3 frets X 1 3 1 4 1}"  });
				
		
		defaultDefs.put("G", new String[]{ "{define G base-fret 1 frets 3 2 0 0 0 3}",
			"{define G base-fret 10 frets X 1 3 3 3 1}",
			"{define G base-fret 1 frets 3 2 0 0 3 3}",
			"{define G base-fret 3 frets 1 3 3 2 1 1}",
			"{define G base-fret 1 frets 3 X 0 0 0 3}",
			"{define G base-fret 3 frets X 3 3 2 1 1}",
			"{define G base-fret 1 frets X X 0 4 3 3}",
			"{define G base-fret 7 frets X X 0 1 2 1}"  });
		defaultDefs.put("G#", new String[]{ "{define G# base-fret 4 frets 1 3 3 2 1 1}"  });
		defaultDefs.put("G#+", new String[]{ "{define G#+ base-fret 1 frets X X 2 1 1 0}"  });
		defaultDefs.put("G#4", new String[]{ "{define G#4 base-fret 4 frets 1 3 3 1 1 1}"  });
		defaultDefs.put("G#5", new String[]{ "{define G#5 base-fret 1 frets 3 2 1 0 0 3}",
			"{define G#5 base-fret 1 frets 3 X 1 0 0 3}"  });
		defaultDefs.put("G#7", new String[]{ "{define G#7 base-fret 1 frets X X 1 1 1 2}"  });
		defaultDefs.put("G#dim", new String[]{ "{define G#dim base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("G#m", new String[]{ "{define G#m base-fret 4 frets 1 3 3 1 1 1}"  });
		defaultDefs.put("G#m6", new String[]{ "{define G#m6 base-fret 1 frets X X 1 1 0 1}"  });
		defaultDefs.put("G#m7", new String[]{ "{define G#m7 base-fret 4 frets X X 1 1 1 1}"  });
		defaultDefs.put("G#m9(maj7)", new String[]{ "{define G#m9(maj7) base-fret 1 frets X X 1 3 0 3}"  });
		defaultDefs.put("G#m9maj7", new String[]{ "{define G#m9maj7 base-fret 1 frets X X 1 3 0 3}"  });
		defaultDefs.put("G#maj", new String[]{ "{define G#maj base-fret 4 frets 1 3 3 2 1 1}"  });
		defaultDefs.put("G#maj7", new String[]{ "{define G#maj7 base-fret 1 frets X X 1 1 1 3}"  });
		defaultDefs.put("G#min", new String[]{ "{define G#min base-fret 4 frets 1 3 3 1 1 1}"  });
		defaultDefs.put("G#sus", new String[]{ "{define G#sus base-fret 1 frets X X 1 1 2 4}"  });
		defaultDefs.put("G#sus4", new String[]{ "{define G#sus4 base-fret 1 frets X X 1 1 2 4}"  });
		defaultDefs.put("G(add9)", new String[]{ "{define G(add9) base-fret 3 frets 1 3 X 2 1 3}"  });
		defaultDefs.put("G+", new String[]{ "{define G+ base-fret 1 frets X X 1 0 0 4}"  });
		defaultDefs.put("G/A", new String[]{ "{define G/A base-fret 1 frets X 0 0 0 0 3}",
			"{define G/A base-fret 1 frets 3 0 0 0 0 3}",
			"{define G/A base-fret 1 frets 3 2 0 2 0 3}"  });
		defaultDefs.put("G/B", new String[]{ "{define G/B base-fret 1 frets X 2 0 0 0 3}",
			"{define G/B base-fret 1 frets 3 0 0 0 2 X}"  });
		defaultDefs.put("G/C", new String[]{ "{define G/C base-fret 1 frets 3 3 0 0 0 3}",
			"{define G/C base-fret 1 frets X 3 0 0 0 3}"  });
		defaultDefs.put("G/D", new String[]{ "{define G/D base-fret 4 frets X 2 2 1 0 0}"  });
		defaultDefs.put("G/E", new String[]{ "{define G/E base-fret 1 frets 0 2 0 0 0 0}",
			"{define G/E base-fret 1 frets 0 2 0 0 3 0}",
			"{define G/E base-fret 1 frets 0 2 2 0 3 0}",
			"{define G/E base-fret 1 frets 0 2 2 0 3 3}",
			"{define G/E base-fret 12 frets X X 0 1 1 1}",
			"{define G/E base-fret 7 frets X X 0 3 2 1}",
			"{define G/E base-fret 1 frets X X 2 4 3 3}",
			"{define G/E base-fret 1 frets 0 X 0 0 0 0}",
			"{define G/E base-fret 10 frets X 1 3 3 3 0}"  });
		defaultDefs.put("G/F", new String[]{ "{define G/F base-fret 1 frets 1 X 0 0 0 3}",
			"{define G/F base-fret 1 frets 3 2 0 0 0 1}",
			"{define G/F base-fret 1 frets X X 0 0 0 1}"  });
		defaultDefs.put("G/F#", new String[]{ "{define G/F# base-fret 1 frets 2 2 0 0 0 3}"  });
		defaultDefs.put("G/Gb", new String[]{ "{define G/Gb base-fret 1 frets 2 2 0 0 0 3}",
			"{define G/Gb base-fret 1 frets 2 2 0 0 3 3}",
			"{define G/Gb base-fret 1 frets 3 2 0 0 0 2}",
			"{define G/Gb base-fret 1 frets X X 4 4 3 3}"  });
		defaultDefs.put("G11", new String[]{ "{define G11 base-fret 1 frets 3 X 0 2 1 1}"  });
		defaultDefs.put("G4", new String[]{ "{define G4 base-fret 1 frets X X 0 0 1 3}"  });
		defaultDefs.put("G5", new String[]{ "{define G5 base-fret 3 frets 1 3 3 X X 1}",
			"{define G5 base-fret 1 frets 3 X 0 0 3 3}"  });
		defaultDefs.put("G6", new String[]{ "{define G6 base-fret 1 frets 3 X 0 0 0 0}",
			"{define G6 base-fret 1 frets 0 2 0 0 0 0}",
			"{define G6 base-fret 1 frets 0 2 0 0 3 0}",
			"{define G6 base-fret 1 frets 0 2 2 0 3 0}",
			"{define G6 base-fret 1 frets 0 2 2 0 3 3}",
			"{define G6 base-fret 10 frets X 1 3 3 3 0}",
			"{define G6 base-fret 12 frets X X 0 1 1 1}",
			"{define G6 base-fret 7 frets X X 0 3 2 1}",
			"{define G6 base-fret 1 frets X X 2 4 3 3}",
			"{define G6 base-fret 1 frets 0 X 0 0 0 0}"  });
		defaultDefs.put("G6(sus4)", new String[]{ "{define G6(sus4) base-fret 1 frets 0 2 0 0 1 0}"  });
		defaultDefs.put("G6/add9", new String[]{ "{define G6/add9 base-fret 1 frets 0 0 0 0 0 0}",
			"{define G6/add9 base-fret 1 frets 0 0 0 0 0 3}",
			"{define G6/add9 base-fret 1 frets 3 X 0 2 0 0}"  });
		defaultDefs.put("G6sus4", new String[]{ "{define G6sus4 base-fret 1 frets 0 2 0 0 1 0}"  });
		defaultDefs.put("G7", new String[]{ "{define G7 base-fret 1 frets 3 2 0 0 0 1}",
			"{define G7 base-fret 1 frets 1 X 0 0 0 3}",
			"{define G7 base-fret 1 frets X X 0 0 0 1}"  });
		defaultDefs.put("G7#9", new String[]{ "{define G7#9 base-fret 3 frets 1 3 X 2 4 4}"  });
		defaultDefs.put("G7(#9)", new String[]{ "{define G7(#9) base-fret 3 frets 1 3 X 2 4 4}"  });
		defaultDefs.put("G7(b9)", new String[]{ "{define G7(b9) base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("G7(sus4)", new String[]{ "{define G7(sus4) base-fret 1 frets 3 3 0 0 1 1}"  });
		defaultDefs.put("G7+", new String[]{ "{define G7+ base-fret 1 frets X X 4 3 3 2}"  });
		defaultDefs.put("G7/add11", new String[]{ "{define G7/add11 base-fret 1 frets X 3 0 0 0 1}"  });
		defaultDefs.put("G7b9", new String[]{ "{define G7b9 base-fret 1 frets X X 0 1 0 1}"  });
		defaultDefs.put("G7sus4", new String[]{ "{define G7sus4 base-fret 1 frets 3 3 0 0 1 1}"  });
		defaultDefs.put("G9", new String[]{ "{define G9 base-fret 1 frets 3 X 0 2 0 1}",
			"{define G9 base-fret 1 frets X 0 0 0 0 1}",
			"{define G9 base-fret 1 frets X 2 3 2 3 3}"  });
		defaultDefs.put("G9(11)", new String[]{ "{define G9(11) base-fret 3 frets 1 3 1 3 1 3}"  });
		defaultDefs.put("Gadd9", new String[]{ "{define Gadd9 base-fret 3 frets 1 3 X 2 1 3}",
			"{define Gadd9 base-fret 1 frets 3 0 0 0 0 3}",
			"{define Gadd9 base-fret 1 frets 3 2 0 2 0 3}"  });
		defaultDefs.put("Gaug/E", new String[]{ "{define Gaug/E base-fret 1 frets 3 X 1 0 0 0}",
			"{define Gaug/E base-fret 1 frets X X 1 0 0 0}"  });
		defaultDefs.put("Gb", new String[]{ "{define Gb base-fret 1 frets 2 4 4 3 2 2}",
			"{define Gb base-fret 1 frets X 4 4 3 2 2}",
			"{define Gb base-fret 1 frets X X 4 3 2 2}"  });
		defaultDefs.put("Gb#5", new String[]{ "{define Gb#5 base-fret 1 frets X X 0 3 3 2}"  });
		defaultDefs.put("Gb+", new String[]{ "{define Gb+ base-fret 1 frets X X 4 3 3 2}"  });
		defaultDefs.put("Gb/Ab", new String[]{ "{define Gb/Ab base-fret 1 frets X X 4 3 2 4}"  });
		defaultDefs.put("Gb/E", new String[]{ "{define Gb/E base-fret 1 frets 2 4 2 3 2 2}",
			"{define Gb/E base-fret 1 frets X X 4 3 2 0}"  });
		defaultDefs.put("Gb/Eb", new String[]{ "{define Gb/Eb base-fret 1 frets X X 1 3 2 2}"  });
		defaultDefs.put("Gb/F", new String[]{ "{define Gb/F base-fret 1 frets X X 3 3 2 2}"  });
		defaultDefs.put("Gb6", new String[]{ "{define Gb6 base-fret 1 frets X X 1 3 2 2}"  });
		defaultDefs.put("Gb7", new String[]{ "{define Gb7 base-fret 1 frets X X 4 3 2 0}",
			"{define Gb7 base-fret 1 frets 2 4 2 3 2 2}"  });
		defaultDefs.put("Gb7(#5)", new String[]{ "{define Gb7(#5) base-fret 1 frets 2 X 4 3 3 0}"  });
		defaultDefs.put("Gb7/#9", new String[]{ "{define Gb7/#9 base-fret 1 frets X 0 4 3 2 0}"  });
		defaultDefs.put("Gb7sus4", new String[]{ "{define Gb7sus4 base-fret 1 frets X 4 4 4 X 0}"  });
		defaultDefs.put("Gb9", new String[]{ "{define Gb9 base-fret 1 frets X 1 2 1 2 2}"  });
		defaultDefs.put("Gbadd9", new String[]{ "{define Gbadd9 base-fret 1 frets X X 4 3 2 4}"  });
		defaultDefs.put("Gbaug/E", new String[]{ "{define Gbaug/E base-fret 1 frets 2 X 4 3 3 0}"  });
		defaultDefs.put("Gbdim", new String[]{ "{define Gbdim base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Gbdim/D", new String[]{ "{define Gbdim/D base-fret 1 frets X 0 0 2 1 2}",
			"{define Gbdim/D base-fret 1 frets X 3 X 2 3 2}",
			"{define Gbdim/D base-fret 5 frets X 1 3 1 3 1}"  });
		defaultDefs.put("Gbdim/E", new String[]{ "{define Gbdim/E base-fret 1 frets X 0 2 2 1 2}",
			"{define Gbdim/E base-fret 1 frets X X 2 2 1 2}"  });
		defaultDefs.put("Gbdim/Eb", new String[]{ "{define Gbdim/Eb base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Gbdim7", new String[]{ "{define Gbdim7 base-fret 1 frets X X 1 2 1 2}"  });
		defaultDefs.put("Gbm", new String[]{ "{define Gbm base-fret 1 frets 2 4 4 2 2 2}",
			"{define Gbm base-fret 1 frets X 4 4 2 2 2}",
			"{define Gbm base-fret 1 frets X X 4 2 2 2}"  });
		defaultDefs.put("Gbm/D", new String[]{ "{define Gbm/D base-fret 14 frets X X 0 1 1 1}",
			"{define Gbm/D base-fret 1 frets X X 0 2 2 2}"  });
		defaultDefs.put("Gbm/E", new String[]{ "{define Gbm/E base-fret 1 frets 0 0 2 2 2 2}",
			"{define Gbm/E base-fret 1 frets 0 X 4 2 2 0}",
			"{define Gbm/E base-fret 1 frets 2 X 2 2 2 0}",
			"{define Gbm/E base-fret 1 frets X 0 4 2 2 0}",
			"{define Gbm/E base-fret 1 frets X X 2 2 2 2}"  });
		defaultDefs.put("Gbm7", new String[]{ "{define Gbm7 base-fret 1 frets X X 2 2 2 2}",
			"{define Gbm7 base-fret 1 frets 0 0 2 2 2 2}",
			"{define Gbm7 base-fret 1 frets 0 X 4 2 2 0}",
			"{define Gbm7 base-fret 1 frets 2 X 2 2 2 0}",
			"{define Gbm7 base-fret 1 frets X 0 4 2 2 0}"  });
		defaultDefs.put("Gbm7(b5)", new String[]{ "{define Gbm7(b5) base-fret 1 frets X 0 2 2 1 2}",
			"{define Gbm7(b5) base-fret 1 frets X X 2 2 1 2}"  });
		defaultDefs.put("Gbm7/b9", new String[]{ "{define Gbm7/b9 base-fret 1 frets 0 0 2 0 2 2}"  });
		defaultDefs.put("Gbmaj", new String[]{ "{define Gbmaj base-fret 1 frets 2 4 4 3 2 2}"  });
		defaultDefs.put("Gbmaj7", new String[]{ "{define Gbmaj7 base-fret 1 frets X X 4 3 2 1}",
			"{define Gbmaj7 base-fret 1 frets X X 3 3 2 2}"  });
		defaultDefs.put("Gbmin", new String[]{ "{define Gbmin base-fret 1 frets 2 4 4 2 2 2}"  });
		defaultDefs.put("Gbsus", new String[]{ "{define Gbsus base-fret 1 frets X X 4 4 2 2}",
			"{define Gbsus base-fret 1 frets X 4 4 4 2 2}"  });
		defaultDefs.put("Gbsus2/Bb", new String[]{ "{define Gbsus2/Bb base-fret 1 frets X X 4 3 2 4}"  });
		defaultDefs.put("Gbsus4", new String[]{ "{define Gbsus4 base-fret 1 frets X X 4 4 2 2}"  });
		defaultDefs.put("Gbsus4/E", new String[]{ "{define Gbsus4/E base-fret 1 frets X 4 4 4 X 0}"  });
		defaultDefs.put("Gdim", new String[]{ "{define Gdim base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Gdim/E", new String[]{ "{define Gdim/E base-fret 1 frets X 1 2 0 2 0}",
			"{define Gdim/E base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Gdim/Eb", new String[]{ "{define Gdim/Eb base-fret 1 frets X 1 1 3 2 3}",
			"{define Gdim/Eb base-fret 6 frets X 1 3 1 3 1}",
			"{define Gdim/Eb base-fret 1 frets X X 1 3 2 3}"  });
		defaultDefs.put("Gdim7", new String[]{ "{define Gdim7 base-fret 1 frets X 1 2 0 2 0}",
			"{define Gdim7 base-fret 1 frets X X 2 3 2 3}"  });
		defaultDefs.put("Gm", new String[]{ "{define Gm base-fret 3 frets 1 3 3 1 1 1}",
			"{define Gm base-fret 1 frets X X 0 3 3 3}"  });
		defaultDefs.put("Gm/Bb", new String[]{ "{define Gm/Bb base-fret 4 frets 3 2 2 1 X X}"  });
		defaultDefs.put("Gm/E", new String[]{ "{define Gm/E base-fret 1 frets 3 X 0 3 3 0}"  });
		defaultDefs.put("Gm/Eb", new String[]{ "{define Gm/Eb base-fret 6 frets X 1 3 2 3 1}"  });
		defaultDefs.put("Gm/F", new String[]{ "{define Gm/F base-fret 3 frets 1 3 1 1 1 1}",
			"{define Gm/F base-fret 1 frets X X 3 3 3 3}"  });
		defaultDefs.put("Gm13", new String[]{ "{define Gm13 base-fret 1 frets 0 0 3 3 3 3}"  });
		defaultDefs.put("Gm6", new String[]{ "{define Gm6 base-fret 1 frets X X 2 3 3 3}",
			"{define Gm6 base-fret 1 frets 3 X 0 3 3 0}"  });
		defaultDefs.put("Gm7", new String[]{ "{define Gm7 base-fret 3 frets 1 3 1 1 1 1}",
			"{define Gm7 base-fret 1 frets X X 3 3 3 3}"  });
		defaultDefs.put("Gm7/add11", new String[]{ "{define Gm7/add11 base-fret 1 frets X 3 3 3 3 3}"  });
		defaultDefs.put("Gm9", new String[]{ "{define Gm9 base-fret 3 frets 1 3 1 1 1 3}"  });
		defaultDefs.put("Gmaj", new String[]{ "{define Gmaj base-fret 1 frets 3 2 0 0 0 3}"  });
		defaultDefs.put("Gmaj7", new String[]{ "{define Gmaj7 base-fret 2 frets X X 4 3 2 1}",
			"{define Gmaj7 base-fret 1 frets 2 2 0 0 0 3}",
			"{define Gmaj7 base-fret 1 frets 2 2 0 0 3 3}",
			"{define Gmaj7 base-fret 1 frets 3 2 0 0 0 2}",
			"{define Gmaj7 base-fret 1 frets X X 4 4 3 3}"  });
		defaultDefs.put("Gmaj7sus4", new String[]{ "{define Gmaj7sus4 base-fret 1 frets X X 0 0 1 2}"  });
		defaultDefs.put("Gmaj9", new String[]{ "{define Gmaj9 base-fret 2 frets 1 1 4 1 2 1}"  });
		defaultDefs.put("Gmin", new String[]{ "{define Gmin base-fret 3 frets 1 3 3 1 1 1}"  });
		defaultDefs.put("Gsus", new String[]{ "{define Gsus base-fret 1 frets X X 0 0 1 3}",
			"{define Gsus base-fret 1 frets X 3 0 0 3 3}",
			"{define Gsus base-fret 3 frets X 1 3 3 1 1}",
			"{define Gsus base-fret 3 frets X 3 3 3 1 1}"  });
		defaultDefs.put("Gsus2", new String[]{ "{define Gsus2 base-fret 3 frets 3 X 0 0 1 3}",
			"{define Gsus2 base-fret 1 frets 3 0 0 0 3 3}",
			"{define Gsus2 base-fret 1 frets X 0 0 0 3 3}",
			"{define Gsus2 base-fret 1 frets X X 0 2 3 3}"  });
		defaultDefs.put("Gsus2/B", new String[]{ "{define Gsus2/B base-fret 1 frets 3 0 0 0 0 3}",
			"{define Gsus2/B base-fret 1 frets 3 2 0 2 0 3}"  });
		defaultDefs.put("Gsus2/C", new String[]{ "{define Gsus2/C base-fret 1 frets X X 0 2 1 3}"  });
		defaultDefs.put("Gsus2/E", new String[]{ "{define Gsus2/E base-fret 3 frets 3 0 0 0 1 0}",
			"{define Gsus2/E base-fret 1 frets X 0 2 0 3 0}",
			"{define Gsus2/E base-fret 1 frets X 0 2 0 3 3}",
			"{define Gsus2/E base-fret 1 frets X 0 2 2 3 3}"  });
		defaultDefs.put("Gsus2/Gb", new String[]{ "{define Gsus2/Gb base-fret 3 frets 3 X 2 0 1 3}",
			"{define Gsus2/Gb base-fret 1 frets 3 X 0 2 3 2}"  });
		defaultDefs.put("Gsus4", new String[]{ "{define Gsus4 base-fret 1 frets X X 0 0 1 1}"  });
		defaultDefs.put("Gsus4/A", new String[]{ "{define Gsus4/A base-fret 1 frets X X 0 2 1 3}"  });
		defaultDefs.put("Gsus4/B", new String[]{ "{define Gsus4/B base-fret 1 frets 3 3 0 0 0 3}",
			"{define Gsus4/B base-fret 1 frets X 3 0 0 0 3}"  });
		defaultDefs.put("Gsus4/E", new String[]{ "{define Gsus4/E base-fret 1 frets 3 X 0 0 1 0}",
			"{define Gsus4/E base-fret 1 frets X 3 0 0 1 0}",
			"{define Gsus4/E base-fret 1 frets X 3 2 0 3 3}",
			"{define Gsus4/E base-fret 1 frets X X 0 0 1 0}",
			"{define Gsus4/E base-fret 3 frets X X 0 3 3 1}",
			"{define Gsus4/E base-fret 5 frets X 1 1 1 X 0}",
			"{define Gsus4/E base-fret 10 frets X 1 3 3 4 0}"  });
		defaultDefs.put("Gsus4/F", new String[]{ "{define Gsus4/F base-fret 1 frets 3 3 0 0 1 1}" });
	}
	
	private Vector<String> definitionList;
	String name;
	private int voicing;
	
	/**
	 * constructor
	 *
	 * @param name is the name of the chord
	 * @param nocheck must be false to automatically insert the default definitions 
	 */
	public Chord(String name, boolean nocheck) {
		this.name=name.trim();
		this.setDefinitionList(new Vector<String>());
		if(!nocheck) {
			String[] def = Chord.defaultDefs.get(this.name);
			if (def!=null) this.getDefinitionList().addAll(Arrays.asList(def));
		}
	}
	
	/**
	 * transpose
	 *
	 * recursively transposes the chord name by the given number of steps
	 * 
	 * @param chord is the chord name to be transposed
	 * @param steps is the number of steps to transpose (<0 means down >0 means up)
	 */
	public static String transpose(String chord, int steps) {
		// nothing to do is steps=0
		if (steps == 0) return chord;
		// decompose he cord name into single characters (append null if there is only one)
		String[] b = chord.split("/");
		String[] c = b[0].split("");
		if (steps > 0) {
			// transpose 1 step up
			if (c.length>1 && c[1].equals("b")) {
				// this is flattened already, so remove the b
				c[1] = "";
			} else if (c.length>1 && c[1].equals("#")) {
				// this is sharpened, so need the next note up and remove the #
				int p = Chord.names.indexOf(c[0]) + 1;
				if (p > Chord.names.length() - 1) p = 0;
				c[0] = Chord.names.substring(p, p + 1);
			} else {
				// this is natural, so insert a # (next note for E or B)
				String t = c[0];
				if (t.equals("E")) c[0]="F";
				else if (t.equals("B")) c[0]="C";
				else c=new String[]{ c[0], "#" };
			}
			// reassemble chord name and go to next recursion
			chord = Chord.join(c,"");
			chord = Chord.transpose(chord, steps - 1);
		} else {
			// transpose 1 step down
			if (c[1].equals("#")) {
				// this is sharpened already, so remove the #
				c[1] = "";
			} else if (c[1].equals("b")) {
				// this is flattened, so need the next note down and remove the b
				int p = Chord.names.indexOf(c[0]) - 1;
				if (p < 0) p = Chord.names.length() - 1;
				c[0] = Chord.names.substring(p, p + 1);
				c[1] = "";
			} else {
				// this is natural, so insert a b (next note for F or C)
				String t = c[0];
				if (t.equals("F")) c[0]="E";
				else if (t.equals("C")) c[0]="B";
				else c=new String[]{ c[0], "b" };
			}
			// reassemble chord name and go to next recursion
			chord = Chord.join(c,"");
			chord = Chord.transpose(chord, steps + 1);
		}
		//return transposed chord name
		if (b.length>1) chord=chord+'/'+Chord.transpose(b[1],steps);
		return chord;
	}

	private static String join(String[] c, String delim) {
		String chord="";
		for(int i=0; i<c.length; i++) {
			if(chord.length()>0) chord+=delim;
			chord+=c[i];
		}
		return chord;
	}
	

	/**
	 * addDefinition
	 *
	 * adds a new voicing to the end of the list if it is not yet in the list
	 *
	 * @param definition is the chordpo definition of the voicing
	 */
	public void addDefinition(String definition) {
		if (!this.has(definition)) this.getDefinitionList().add(definition);
	}
	
	/**
	 * insert
	 *
	 * adds a new voicing to the start of the list
	 *
	 * @param definition is the chordpo definition of the voicing
	 */
	public void insertDefinition(String definition) {
		if (!this.has(definition)) this.getDefinitionList().add(0,definition);
	}
	
	/**
	 * has
	 *
	 * checks whether the given voicing is in already the list
	 *
	 * @param definition is the chordpo definition of the voicing
	 * @return true if the voicing is already in the list
	 */
	public boolean has(String definition) {
		for(String d: this.getDefinitionList()) {
			if(d.equals(definition)) return true;
		}
		return false;
	}

	public int getVoicing() {
		return voicing;
	}

	public void setVoicing(int voicing) {
		this.voicing = voicing;
	}

	public Vector<String> getDefinitionList() {
		return definitionList;
	}

	public void setDefinitionList(Vector<String> definitionList) {
		this.definitionList = definitionList;
	}	
}
