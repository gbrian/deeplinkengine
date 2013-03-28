package com.deeplinkengine.common;

public class Occupancy {
	public byte rooms;
	public byte adults;
	public byte children;
	public byte babies;

	public Occupancy(byte rooms, byte adults, byte children, byte babies) {
		this.rooms = rooms;
		this.adults = adults;
		this.children = children;
		this.babies = babies;
	}

	public Occupancy() {
	}
}