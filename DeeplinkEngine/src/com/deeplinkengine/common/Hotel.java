package com.deeplinkengine.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an hotel product
 */
public class Hotel extends Product {

	public String id;

	public String name;

	public String desc;

	public int cat;

	public List<Image> images;

	public Hotel() {
		super(ProductType.HOTEL, "1.0");

	}
	
	public Image addImage(String type, String src){
		if(images == null)
			images = new ArrayList<Image>();
		Image i = new Image(type, src);
		images.add(i);
		return i;
	}
}