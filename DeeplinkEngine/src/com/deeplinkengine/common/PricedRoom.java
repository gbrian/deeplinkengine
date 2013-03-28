package com.deeplinkengine.common;

public class PricedRoom extends Room {
	
	public String totalPrice;
	
	public Occupancy occupancy;
	
	public PricedRoom(){
	}
	
	public PricedRoom(Occupancy occ, String price, String roomType, String boardType){
		this.occupancy = occ;
		this.type = roomType;
		this.board = new Board();
		this.board.type = boardType;
		this.totalPrice = price;
	}
}
