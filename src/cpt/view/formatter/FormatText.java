package cpt.view.formatter;
import cpt.model.ModelSong;

public class FormatText extends FormatToken {

	public String text;
	
	public FormatText(String text){
		this.text=text;
	}
	
	@Override
	public String toString(ModelSong item) {
		return text;
	}
}