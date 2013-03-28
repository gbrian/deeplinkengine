package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.deeplinkengine.common.Hotel;
import com.deeplinkengine.common.HotelResult;
import com.deeplinkengine.common.SearchSettings;
import com.deeplinkengine.providers.Suggest;
import com.deeplinkengine.providers.SuggestList;
import com.deeplinkengine.providers.SuggestSettings;
import com.deeplinkengine.providers.booking.BookingProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BookingProviderTest {

	Gson gson = new Gson();
	BookingProvider provider = new BookingProvider();
	SearchSettings search = new SearchSettings(Calendar.getInstance().getTime(), 
			Calendar.getInstance().getTime(), "-390625", (byte)1, (byte)2, (byte)0, (byte)0);
	{
		search.checkout.add(Calendar.MONTH, 2);
		search.checkin.add(Calendar.MONTH, 2);
		search.checkout.add(Calendar.DAY_OF_MONTH, 2);
	}
	
	SuggestSettings suggest = new SuggestSettings("mad", "es");
	
	
	@Test
	public void basicSearch() {
		String res = provider.doHotelSearch(search, null);
		List<HotelResult> hotels = gson.fromJson(res, new TypeToken<List<HotelResult>>(){}.getType());
		
		assertTrue(hotels.size() != 0);
		HotelResult example = hotels.get(0);
		
		assertTrue(example.id.length() != 0);
		assertTrue(example.name.length() != 0);
		assertTrue(example.desc.length() != 0);
		// assertTrue(example.cat);
		
		assertTrue(example.images.size() != 0);
		assertTrue(example.prices.size() != 0);
		
	}

	
	@Test
	public void basicSuggest() {
		String res = null; 
		res = provider.doDestinationSuggest(suggest, null);
		List<Suggest> suggests = gson.fromJson(res, new TypeToken<List<Suggest>>(){}.getType());
		assertTrue(suggests.size() != 0);
		assertTrue(suggests.get(0).data != null);
	}
}
