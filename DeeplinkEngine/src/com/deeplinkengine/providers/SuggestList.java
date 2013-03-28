package com.deeplinkengine.providers;

import java.util.ArrayList;

public class SuggestList extends ArrayList<Suggest>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for(int c = 0; c != size(); ++c){
			if(c != 0)
				sb.append(",");
			Suggest s = get(c);
			sb.append(s.toString());
			
		}
		sb.append("]");
		return sb.toString();
	}
}