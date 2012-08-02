/**
 * Copyright (C) 2012 Enterprise System Solutions (P) Ltd. All rights reserved.
 *
 * This file is part of DATA Gen. http://testdatagen.sourceforge.net/
 *
 * DATA Gen is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DATA Gen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
