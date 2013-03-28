package com.deeplinkengine.common;

/**
 * Represents an abastract product
 */
public abstract class Product {

	private ProductType type;
	private String version;

	public ProductType getType() {
		return type;
	}

	public String getVersion() {
		return version;
	}

	protected Product(ProductType type, String version) {
		this.type = type;
		this.version = version;
	}

}