package com.deeplinkengine.common;

import java.util.ArrayList;
import java.util.List;

public class HotelResult extends Hotel{

	static HotelResult example;
	static {
		// TODO: Keep updated!
		example = new HotelResult();
		
		example.id = "hotelid";
		example.name = "Loremp ipsum hotel - *****";
		example.desc = "Loremp ipsum hotel. ";
		example.cat = 5;
		
		example.addImage("hotel", "http://dummy.server/images/hotel_image1.jpg");
		example.addImage("bar", "http://dummy.server/images/hotel_image2.jpg");
		example.addImage("pool", "http://dummy.server/images/hotel_image3.jpg");
		
		Occupancy occ = new Occupancy((byte)1, (byte)2, (byte)0, (byte)0);
		example.addPrice(occ, "Double", "Half-board", "30,00€");
		example.addPrice(occ, "Double", "Full-board", "60,00€");
		example.addPrice(occ, "Single", "Half-board", "28,00€");
	}
	/**
	 * Returns an example!
	 * @return
	 */
	public static HotelResult getAnExample(){
		return example;
	} 
	
	public List<PricedRoom> prices;
	
	/**
	 * Contains the original HTML code
	 */
	public String html;
	
	public HotelResult addPrice(Occupancy occ, String roomType, String boardType, String price){
		if(prices == null)
			prices = new ArrayList<PricedRoom>();
		PricedRoom room = new PricedRoom(occ, price, roomType, boardType);
		prices.add(room);
		return this;
	}
}
