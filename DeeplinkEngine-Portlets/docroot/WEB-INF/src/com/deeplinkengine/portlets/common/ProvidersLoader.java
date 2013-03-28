package com.deeplinkengine.portlets.common;

import java.util.Arrays;
import java.util.List;
import com.deeplinkengine.providers.Provider;
import com.deeplinkengine.providers.booking.BookingProvider;
import com.deeplinkengine.providers.hotelopia.HotelopiaProvider;

/**
 * Allows to access to the product providers loaded in the application
 * @author GBNS
 *
 */
public class ProvidersLoader {
	
	static HotelopiaProvider hotelopia = new HotelopiaProvider();
	static BookingProvider booking = new BookingProvider();
	
	public static List<String> getProviders(){
		// FIXME: Dynamic load?
		return Arrays.asList( new String[] { HotelopiaProvider.name, BookingProvider.name});
	}
	
	public static Provider getProviderInstance(String provider, String version){
		// FIXME: version??
		if(provider.equals(HotelopiaProvider.name))
			return hotelopia;
		if(provider.equals(BookingProvider.name))
				return booking;
		return null;
	}

}
