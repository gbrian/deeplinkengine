package com.deeplinkengine.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchSettings {

	public static final String 	DATE_FORMAT = "yyyy-MM-dd", 
								CHECKIN_PARAM = "i", 
								CHECKOUT_PARAM = "o",
								DESTINATION_PARAM = "d";
	public static DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);; 
	
	public Calendar checkin;
	public Calendar checkout;
	public String destination;
	public List<Occupancy> occupancy;

	public SearchSettings() {
		
	}

	public SearchSettings(Date checkin, Date checkout, String destination,
			byte rooms, byte adults, byte children, byte babies) {
		this.checkin = Calendar.getInstance();
		this.checkin.setTime(checkin);
		this.checkout = Calendar.getInstance();
		this.checkout.setTime(checkout);
		this.destination = destination;
		this.occupancy = new ArrayList<Occupancy>();
		this.occupancy.add(new Occupancy(rooms, adults, children, babies));
	}
	
	public SearchSettings(Date checkin, Date checkout, String destination,
			List<Occupancy> occupancy) {
		this.checkin = Calendar.getInstance();
		this.checkin.setTime(checkin);
		this.checkout = Calendar.getInstance();
		this.checkout.setTime(checkout);
		this.destination = destination;
		this.occupancy = occupancy;
	}
	
	/**
	 * 
	 * @param params
	 */
	public SearchSettings(Map<String, String[]> params) throws Exception{
		try{
			this.checkin = Calendar.getInstance();
			this.checkin.setTime(dateFormat.parse(params.get(CHECKIN_PARAM)[0]));
			this.checkout = Calendar.getInstance();
			this.checkout.setTime(dateFormat.parse(params.get(CHECKOUT_PARAM)[0]));
			this.destination = params.get(DESTINATION_PARAM)[0];
			this.occupancy = new ArrayList<Occupancy>();
			this.occupancy.add(new Occupancy((byte)1, (byte)2, (byte)0, (byte)0));
		}catch(Exception ex){
			// FIXME: What to do...throw new SearchSettingsException bla, bla, bla
			throw ex;
		}
	}

	public String getTwoDigitDay(boolean inout) {
		return String.format("%02d", inout ? checkin.get(Calendar.DAY_OF_MONTH)
				: checkout.get(Calendar.DAY_OF_MONTH));
	}
	
	public String getTwoDigitMonth(boolean inout) {
		return String.format("%02d", inout ? checkin.get(Calendar.MONTH)
				: checkout.get(Calendar.MONTH));
	}
	
	public String getYear(boolean inout) {
		return String.format("%04d", inout ? checkin.get(Calendar.YEAR)
				: checkout.get(Calendar.YEAR));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb
		.append(CHECKIN_PARAM)
		.append("=")
		.append(dateFormat.format(this.checkin.getTime()))
		.append("&")
		.append(CHECKOUT_PARAM)
		.append("=")
		.append(dateFormat.format(this.checkout.getTime()))
		.append("&")
		.append(DESTINATION_PARAM)
		.append("=")
		.append(destination);
		// FIXME: finsih this... keep updated!
		return sb.toString();
	}
}