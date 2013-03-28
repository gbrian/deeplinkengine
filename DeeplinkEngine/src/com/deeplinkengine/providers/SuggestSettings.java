package com.deeplinkengine.providers;

public class SuggestSettings {
	
	public String term;
	public String language;
	
	public SuggestSettings(){}
	
	public SuggestSettings(String term, String language) {
		super();
		this.term = term;
		this.language = language;
	}
}
