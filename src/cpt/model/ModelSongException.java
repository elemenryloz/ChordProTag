package cpt.model;

import java.util.Vector;

public class ModelSongException extends Exception {

	Vector<ModelToken> tokens = new Vector<>();
	
	public ModelSongException(String message) {
		super(message);
	}

	public Vector<ModelToken> getTokens() {
		return tokens;
	}
	
	public void setTokens(Vector<ModelToken> tokens) {
		this.tokens.clear();
		if(tokens != null) this.tokens.addAll(tokens);
	}
}
