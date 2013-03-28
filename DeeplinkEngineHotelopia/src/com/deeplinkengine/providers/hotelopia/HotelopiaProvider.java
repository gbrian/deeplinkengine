package com.deeplinkengine.providers.hotelopia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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

public class HotelopiaProvider implements Provider {

	public static final String name = "Hotelopia";
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
		Connection conn = Jsoup.connect("http://www.hotelopia.es/results.aspx")
				.data("idMarket", "29", "language", "170", "DayIn",
						search.getTwoDigitDay(true), "MonthIn",
						search.getTwoDigitMonth(true), "YearIn",
						search.getYear(true), "DayOut",
						search.getTwoDigitDay(false), "MonthOut",
						search.getTwoDigitMonth(false), "YearOut",
						search.getYear(true), "coddest",
						firstDestination(search.destination));
		// Occupancy
		for (int c = 0, num = 1; c != search.occupancy.size(); ++c, ++num) {
			Occupancy occ = search.occupancy.get(c);
			conn.data("nro", num + "", "nroom" + num, String.format("%d;%d;%d",
					occ.rooms, occ.adults, occ.children));
		}
		// Request!
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
			for (Element e : doc.select("#hotels .hotel")) {
				try {
					HotelResult data = new HotelResult();
					data.html = e.outerHtml();
					data.addImage("hotel", e.select(".sImg").first()
							.attr("src"));
					data.id = e.select("div.favHotel").first().attr("id")
							.split("-")[1];
					data.name = e.select(".hotel_title h3 a").first()
							.attr("title");
					data.desc = e.select(".hotel_description").text();

					// Price!
					// per room jQuery(jQuery('td.price')[0]).text()
					// suggested jQuery('span.price').text()
					data.addPrice(search.occupancy.get(0), "", "",
							e.select("span.price").text());

					hotels.add(data);
				} catch (Exception ex) {
					ex.printStackTrace();
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
	 * Maps the engine destination code with the Hotelopia one
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
	
	String getDestinationSuggest(SuggestSettings settings,
			ProviderSettings providerSettings) throws Exception {
		// Params 
				String url = "http://www.hotelopia.es/TaskManager/fts.ashx?"+
						"&lang="+ settings.language + 
						"&aid=" + "" + Calendar.getInstance().get(Calendar.MILLISECOND) + 
						"&dl=5&zl=5&hl=5&q=" + settings.term;
				InputStream input = new URL(url).openStream();
				Reader reader = new InputStreamReader(input, "UTF-8");
				
				JsonParser parser = new JsonParser();
				JsonElement re = parser.parse(reader);
				JsonArray data = re.getAsJsonArray();
				
				SuggestList res = new SuggestList();
				for(JsonElement jel: data){
					Suggest s = new Suggest();
					JsonObject o = jel.getAsJsonObject();
				 	s.value = s.label = o.get("Name")
				 					.getAsString()
				 					.replaceAll("<\\/?strong>", "")
				 					+ 
				 					o.get("ParentName")
				 					.getAsString()
				 					.replaceAll("<\\/?strong>", "");
				 	s.data =  o.toString().replaceAll("\\\"", "\"");
				 	res.add(s);
				}
				return res.toString();
	}
}
