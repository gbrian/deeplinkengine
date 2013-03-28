package com.deeplinkengine.providers;

import java.util.List;

import com.deeplinkengine.common.ProductType;
import com.deeplinkengine.common.SearchSettings;

public interface Provider {

	/**
	 * Makes an hotel search
	 * @return
	 */
	String doHotelSearch(SearchSettings search, ProviderSettings providerSettings);
	
	/**
	 * Returns a set of suggested destinations
	 * @param settings
	 * @param providerSettings
	 * @return
	 */
	String doDestinationSuggest(SuggestSettings settings, ProviderSettings providerSettings);

	/**
	 * Maps a destination code with the provider's one/ones
	 * @param destination
	 * @return
	 */
	List<String> mapDestination(String destination);
	
	/**
	 * Returns the provider name
	 * @return
	 */
	String getName();
	
	/**
	 * Returns the provider version
	 * @return
	 */
	String getVersion();
	
	
	/**
	 * Returns a list of products
	 * @return
	 */
	List<ProductType> getProducts();
}
