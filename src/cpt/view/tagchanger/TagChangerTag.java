package cpt.view.tagchanger;
import cpt.model.ModelSong;

class TagChangerTag extends TagChangerToken {

	public String name;
	
	public TagChangerTag(String name){
		this.name=name;
	}
	
	@Override
	public String toString(ModelSong item) {
		return item.meta.get(name);
	}
}