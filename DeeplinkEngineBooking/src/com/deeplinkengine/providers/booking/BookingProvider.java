package com.deeplinkengine.providers.booking;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.deeplinkengine.common.HotelResult;
import com.deeplinkengine.common.Occupancy;
import com.deeplinkengine.common.ProductType;
import com.deeplinkengine.common.SearchSettings;
import com.deeplinkengine.providers.Provider;
import com.deeplinkengine.providers.ProviderSettings;
import com.deeplinkengine.providers.Suggest;
import com.deeplinkengine.providers.SuggestList;
import com.deeplinkengine.providers.SuggestSettings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BookingProvider implements Provider {

	public static final String name = "Booking";
	public static final String version = "1.0";
	public static final ProductType[] products = new ProductType[] { ProductType.HOTEL };

	@Override
	public String doHotelSearch(SearchSettings search,
			ProviderSettings providerSettings) {
		try {
			return searchHotels(search, providerSettings);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public List<ProductType> getProducts() {
		return Arrays.asList(products);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public String searchHotels(SearchSettings search,
			ProviderSettings providerSettings) throws IOException {
		Connection conn = Jsoup.connect(
				"http://www.booking.com/searchresults.html").data(
				"src",
				"index",
				// "error_url",
				// "http%3A%2F%2Fwww.booking.com%2Findex.es.html%3Fsid%3D578d125ad7f32d8feab908cf8666b14b%3Bdcid%3D1%3B",
				// "dcid", "1",
				// "sid", "578d125ad7f32d8feab908cf8666b14b",
				// "si", "ai%2Cco%2Cci%2Cre%2Cdi",
				// "ss", "Madrid%2C+Espa%C3%B1a",
				"checkin_monthday", search.getTwoDigitDay(true),
				"checkin_year_month",
				search.getYear(true) + "-" + search.getTwoDigitMonth(true),
				"checkout_monthday", search.getTwoDigitDay(false),
				"checkout_year_month",
				search.getYear(true) + "-" + search.getTwoDigitMonth(true),
				"dest_type", "city", "dest_id",
				firstDestination(search.destination) // "-390625"
				// "ac_pageview_id", "119b88ba44c00085"
				// "ppt", "none",
				);
		// Occupancy
		for (int c = 0, num = 1; c != search.occupancy.size(); ++c, ++num) {
			Occupancy occ = search.occupancy.get(c);
			conn.data("org_nr_rooms", num + "", "org_nr_adults", occ.adults
					+ "", "org_nr_children", occ.children + ""
			// FIXME: Check this params
			// ,"group_adults", "2", "group_children", "0"
			);
		}
		conn.header("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152 Safari/537.22")
				.timeout(10 * 1000);
		Document doc = conn.get();

		// Check error
		Element elError = doc.select("#errorMessage").first();
		if (elError != null) {
			System.out.println("Error : " + elError.text());
		} else {
			ArrayList<HotelResult> hotels = new ArrayList<HotelResult>();
			/*
			 * jQuery('.hotellist tr') .map(function(){ var i =
			 * jQuery(this).find('img.hotel')[0]; return {id:
			 * this.childNodes[1].id, img: i, name: i, desc:
			 * jQuery(this).find('.hotel_desc').text() }})
			 */
			for (Element e : doc.select(".hotellist tr")) {
				try {
					Element img = e.select("img.hotel").first();
					if (img != null) {
						HotelResult data = new HotelResult();
						data.html = e.outerHtml();
						data.addImage("HOTEL", img.attr("src"));
						data.id = e.childNode(1).attr("id").split("_")[1];
						data.name = e.select(".hotel_name_link").text();
						data.desc = e.select(".hotel_desc").text();
						
						// Price
						// jQuery(jQuery('.roomrow')[0]).find('.roomName').text()
						// jQuery(jQuery('.roomrow')[0]).find('.price').text()
						// FIXME: check occupancy
						data.addPrice(search.occupancy.get(0), e.select(".roomrow .roomName").first().text(), null, e.select(".roomrow .price").first().text());
						
						hotels.add(data);
					}
				} catch (Exception ex) {
					System.out.println("No result " + e);
				}
			}
			assert (hotels.size() != 0) : "No hay hoteles en la respuesta \n"
					+ doc.outerHtml();
			Gson gson = new Gson();
			return gson.toJson(hotels);
		}
		return null;
	}

	/**
	 * Maps the engine destination code with the Booking one
	 * 
	 * @param destination
	 * @return
	 */
	protected String firstDestination(String destination) {
		// FIXME: mapDestination
		return destination;
	}

	/**
	 * Maps a destination code with the provider's one/ones
	 * 
	 * @param destination
	 * @return
	 */
	public List<String> mapDestination(String destination) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String doDestinationSuggest(SuggestSettings settings,
			ProviderSettings providerSettings) {
		
		try {
			return getDestinationSuggest(settings, providerSettings);
		} catch (Exception e) {
			// FIXME Log suggested error
			e.printStackTrace();
		}
		return "[]";
	}
	
	private String getDestinationSuggest(SuggestSettings settings,
			ProviderSettings providerSettings) throws Exception {
		// Params 
		String url = "http://www.booking.com/autocomplete?"+
				"&lang="+ settings.language + 
				"&aid=" + "" + Calendar.getInstance().get(Calendar.MILLISECOND) + 
				"&e_sadt="+ "1" +
				"&term=" + settings.term;
		InputStream input = new URL(url).openStream();
		Reader reader = new InputStreamReader(input, "UTF-8");
		
		JsonParser parser = new JsonParser();
		JsonElement re = parser.parse(reader);
		JsonObject response = re.getAsJsonObject();
		
		SuggestList res = new SuggestList();
		for(Map.Entry<String,JsonElement> entry: response.entrySet()){
			JsonArray data = entry.getValue().getAsJsonArray();
			for(JsonElement jel: data){
				Suggest s = new Suggest();
				JsonObject o = jel.getAsJsonObject();
			 	s.value = s.label = o.get("label").getAsString();
			 	s.data =  o.toString().replaceAll("\\\"", "\"");
			 	res.add(s);
			}
		}
		return res.toString();
	}
}
