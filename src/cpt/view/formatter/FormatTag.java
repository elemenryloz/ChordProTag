package cpt.view.formatter;
import cpt.model.ModelSong;

public class FormatTag extends FormatToken {

	public String name;
	
	public FormatTag(String name){
		this.name=name;
	}
	
	@Override
	public String toString(ModelSong item) {
		return item.meta.get(name);
	}
}