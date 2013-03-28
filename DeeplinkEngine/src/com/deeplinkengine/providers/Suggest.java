package com.deeplinkengine.providers;

public class Suggest {

	public String label;
	public String value;
	public Object data;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{\"label\":\"" + label + "\", \"value\":\"" + value + "\", \"data\":"+data+"}";
	}
}

