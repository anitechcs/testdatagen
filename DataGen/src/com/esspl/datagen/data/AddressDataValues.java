package com.esspl.datagen.data;

/**
 * @author Tapas
 *
 */
public interface AddressDataValues {
	
	/**
	 * @return Array of street address
	 */
	String[] getStreetNames();

	/**
	 * @return Array of cities
	 */
	String[] getCities();

	/**
	 * Returns a list of address suffixes such as "Lane", "Drive","Parkway"
	 * @return Array of address suffixes
	 */
	String[] getAddressSuffixes();

	/**
	 * @return Array of countries
	 */
	String[] getCountries();
	
	/**
	 * @return Array of states
	 */
	String[] getStates();
	
	/**
	 * @return Array of short states
	 */
	String[] getShortStates();
}
