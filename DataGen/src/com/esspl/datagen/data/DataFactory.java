package com.esspl.datagen.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.esspl.datagen.data.impl.DefaultAddressDataValues;
import com.esspl.datagen.data.impl.DefaultContentDataValues;
import com.esspl.datagen.data.impl.DefaultNameDataValues;

/**
 * Class that provides a number of methods for generating test data through
 * helper components. These components implement interfaces that provide an
 * interface to accessing the test data. Components can be replaced with other
 * components to allow more suitable data to be used.
 *
 * @author Tapas
 *
 */

public final class DataFactory {
	
	private static final Logger log = Logger.getLogger(DataFactory.class);
	private static Random random = new Random();//93285

	private NameDataValues nameDataValues = new DefaultNameDataValues();
	private AddressDataValues addressDataValues = new DefaultAddressDataValues();
	private ContentDataValues contentDataValues = new DefaultContentDataValues();
	
	/**
	 * Returns a random item from a list of items.
	 *
	 * @param <T>
	 *            Item type in the list and to return
	 * @param items
	 *            List of items to choose from
	 * @return Item from the list
	 */
	public <T> T getItem(List<T> items) {
		log.debug("DataFactory - getItem() method called");
		return getItem(items, 100, null);
	}

	/**
	 * Returns a random item from a list of items or the null depending on the
	 * probability parameter. The probability determines the chance (in %) of
	 * returning an item off the list versus null.
	 *
	 * @param <T>
	 *            Item type in the list and to return
	 * @param items
	 *            List of items to choose from
	 * @param probability
	 *            chance (in %, 100 being guaranteed) of returning an item from
	 *            the list
	 * @return Item from the list or null if the probability test fails.
	 */
	public <T> T getItem(List<T> items, int probability) {
		log.debug("DataFactory - getItem() method called");
		return getItem(items, probability, null);
	}

	/**
	 * Returns a random item from a list of items or the defaultItem depending
	 * on the probability parameter. The probability determines the chance (in
	 * %) of returning an item off the list versus the default value.
	 *
	 * @param <T>
	 *            Item type in the list and to return
	 * @param items
	 *            List of items to choose from
	 * @param probability
	 *            chance (in %, 100 being guaranteed) of returning an item from
	 *            the list
	 * @param defaultItem
	 *            value to return if the probability test fails
	 * @return Item from the list or the default value
	 */
	public <T> T getItem(List<T> items, int probability, T defaultItem) {
		log.debug("DataFactory - getItem() method called");
		if (items == null) {
			throw new IllegalArgumentException("Item list cannot be null");
		}
		if (items.isEmpty()) {
			throw new IllegalArgumentException("Item list cannot be empty");
		}

		return chance(probability) ? items.get(random.nextInt(items.size()))
				: defaultItem;
	}

	/**
	 * Returns a random item from an array of items
	 *
	 * @param <T>
	 *            Array item type and the type to return
	 * @param items
	 *            Array of items to choose from
	 * @return Item from the array
	 */
	public <T> T getItem(T[] items) {
		log.debug("DataFactory - getItem() method called");
		return getItem(items, 100, null);
	}

	/**
	 * Returns a random item from an array of items or null depending on the
	 * probability parameter. The probability determines the chance (in %) of
	 * returning an item from the array versus null.
	 *
	 * @param <T>
	 *            Array item type and the type to return
	 * @param items
	 *            Array of items to choose from
	 * @param probability
	 *            chance (in %, 100 being guaranteed) of returning an item from
	 *            the array
	 * @return Item from the array or the default value
	 */
	public <T> T getItem(T[] items, int probability) {
		log.debug("DataFactory - getItem() method called");
		return getItem(items, probability, null);
	}

	/**
	 * Returns a random item from an array of items or the defaultItem depending
	 * on the probability parameter. The probability determines the chance (in
	 * %) of returning an item from the array versus the default value.
	 *
	 * @param <T>
	 *            Array item type and the type to return
	 * @param items
	 *            Array of items to choose from
	 * @param probability
	 *            chance (in %, 100 being guaranteed) of returning an item from
	 *            the array
	 * @param defaultItem
	 *            value to return if the probability test fails
	 * @return Item from the array or the default value
	 */
	public <T> T getItem(T[] items, int probability, T defaultItem) {
		log.debug("DataFactory - getItem() method called");
		if (items == null) {
			throw new IllegalArgumentException("Item array cannot be null");
		}
		if (items.length == 0) {
			throw new IllegalArgumentException("Item array cannot be empty");
		}
		return chance(probability) ? items[random.nextInt(items.length)]
				: defaultItem;
	}

	/**
	 * @return A random first name
	 */
	public String getFirstName() {
		log.debug("DataFactory - getFirstName() method called");
		return getItem(nameDataValues.getFirstNames());
	}
	
	/**
	 * @return A random last name
	 */
	public String getLastName() {
		log.debug("DataFactory - getLastName() method called");
		return getItem(nameDataValues.getLastNames());
	}

	/**
	 * Take a format and Returns a combination of firstname, lastname and surname values in one string
	 *
	 * @return Name
	 */
	public String getName(String format) {
		log.debug("DataFactory - getName() method called");
		StringBuilder sbName = new StringBuilder();
		if(format.equals("First_Name Last_Name")){
			sbName.append(getItem(nameDataValues.getFirstNames())).append(" ").append(getItem(nameDataValues.getLastNames()));
		}else if(format.equals("First_Name")){
			sbName.append(getItem(nameDataValues.getFirstNames()));
		}else if(format.equals("Sur_Name Last_Name")){
			sbName.append(getItem(nameDataValues.getPrefixes())).append(" ").append(getItem(nameDataValues.getLastNames()));
		}else if(format.equals("Sur_Name First_Name")){
			sbName.append(getItem(nameDataValues.getPrefixes())).append(" ").append(getItem(nameDataValues.getFirstNames()));
		}else{
			sbName.append(getItem(nameDataValues.getPrefixes())).append(" ").append(getItem(nameDataValues.getFirstNames())).append(" ").append(getItem(nameDataValues.getLastNames()));
		}
		
		return sbName.toString();
	}

	/**
	 * @return A random street name
	 */
	public String getStreetName() {
		log.debug("DataFactory - getStreetName() method called");
		return getItem(addressDataValues.getStreetNames());
	}

	/**
	 * @return A random street suffix
	 */
	public String getStreetSuffix() {
		log.debug("DataFactory - getStreetSuffix() method called");
		return getItem(addressDataValues.getAddressSuffixes());
	}

	/**
	 * Generates a random city value
	 *
	 * @return City as a string
	 */
	public String getCity() {
		log.debug("DataFactory - getCity() method called");
		return getItem(addressDataValues.getCities());
	}
	
	/**
	 * Generates a random country value
	 *
	 * @return City as a string
	 */
	public String getCountry() {
		log.debug("DataFactory - getCountry() method called");
		return getItem(addressDataValues.getCountries());
	}

	/**
	 * Generates an address value consisting of house number, street name and
	 * street suffix. i.e. <code>543 Larkhill Road</code>
	 *
	 * @return Address as a string
	 */
	public String getAddress() {
		log.debug("DataFactory - getAddress() method called");
		int num = 404 + random.nextInt(1400);
		return num + " " + getStreetName() + " " + getStreetSuffix();
	}

	/**
	 * Generates line 2 for a street address (usually an Apt. or Suite #).
	 * Returns null if the probabilty test fails.
	 *
	 * @param probability
	 *            Chance as % of have a value returned instead of null.
	 * @return Street address line two or null if the probability test fails
	 */
	public String getAddressLine2(int probability) {
		log.debug("DataFactory - getAddressLine2() method called");
		return getAddressLine2(probability, null);
	}

	/**
	 * Generates line 2 for a street address (usually an Apt. or Suite #).
	 * Returns default value if the probabilty test fails.
	 *
	 * @param probability
	 *            Chance as % of have a value returned instead of null.
	 * @param defaultValue
	 *            Value to return if the probability test fails.
	 * @return Street address line two or null if the probability test fails
	 */
	public String getAddressLine2(int probability, String defaultValue) {
		log.debug("DataFactory - getAddressLine2() method called");
		return chance(probability) ? getAddressLine2() : defaultValue;
	}

	/**
	 * Generates line 2 for a street address (usually an Apt. or Suite #).
	 * Returns default value if the probabilty test fails.
	 *
	 * @return Street address line 2
	 */
	public String getAddressLine2() {
		log.debug("DataFactory - getAddressLine2() method called");
		int test = random.nextInt(100);
		if (test < 50) {
			return "Apt #" + 100 + random.nextInt(1000);
		} else {
			return "Suite #" + 100 + random.nextInt(1000);
		}
	}

	/**
	 * Creates a random birthdate within the range of 1955 to 1985
	 *
	 * @return Date representing a birthdate
	 */
	/*public Date getBirthDate() {
		Date base = new Date(0);
		return getDate(base, -365 * 15, 365 * 15);
	}*/

	/**
	 * Returns a random int value.
	 *
	 * @return random number
	 */
	public int getNumber() {
		log.debug("DataFactory - getNumber() method called");
		return getNumberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Returns a random number between 0 and max
	 *
	 * @param max
	 *            Maximum value of result
	 * @return random number no more than max
	 */
	public int getNumberUpTo(int max) {
		log.debug("DataFactory - getNumberUpTo() method called");
		return getNumberBetween(0, max);
	}

	/**
	 * Returns a number betwen min and max
	 *
	 * @param min
	 *            minimum value of result
	 * @param max
	 *            maximum value of result
	 * @return Random number within range
	 */
	public int getNumberBetween(int min, int max) {
		log.debug("DataFactory - getNumberBetween() method called");
		if (max < min) {
			throw new IllegalArgumentException(String.format(
					"Minimum must be less than baximum (min=%d, max=%d)", min,
					max));
		}

		return min + random.nextInt(max - min);
	}

	/**
	 * Returns a random date between two dates with appropriate format. This method will alter the time
	 * component of the dates
	 *
	 * @author Tapas
	 * 
	 * @param format
	 *            date format
	 * @param minDate
	 *            Minimum date that can be returned
	 * @param maxDate
	 *            Maximum date that can be returned
	 * @return random date between these two dates.
	 */
	public String getDate(String format, String minDate, String maxDate) {
		log.debug("DataFactory - getDate() method called");
		DateFormat formatter = new SimpleDateFormat(format);
		if(minDate == null || minDate.equals("")) {
			minDate = formatter.format(new Date(0L));
		}
		if(maxDate == null || maxDate.equals("")) {
			maxDate = formatter.format(new Date());
		}
		List<Date> dates = new ArrayList<Date>();
		try {
			Date  startDate = (Date)formatter.parse(minDate); 
			Date  endDate = (Date)formatter.parse(maxDate);
			long interval = 24*1000 * 60 * 60; // 1 hour in millis
			long endTime = endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
			    dates.add(new Date(curTime));
			    curTime += interval;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		
		return formatter.format((Date)getItem(dates));
	}

	/**
	 * Returns random text made up of english words of length
	 * <code>length</code>
	 *
	 * @param length
	 *            length of returned string
	 *
	 * @return string made up of actual words with length <code>length</code>
	 */
	public String getRandomText(int length) {
		log.debug("DataFactory - getRandomText() method called");
		return getRandomText(length, length);
	}

	/**
	 * Returns random text made up of english words
	 *
	 * @param minLength
	 *            minimum length of returned string
	 * @param maxLength
	 *            maximum length of returned string
	 * @return string of length between min and max length
	 */
	public String getRandomText(int minLength, int maxLength) {
		log.debug("DataFactory - getRandomText() method called");
		validateMinMaxParams(minLength, maxLength);
		StringBuilder sb = new StringBuilder(maxLength);
		int length = minLength;
		if (maxLength != minLength) {
			length = length + random.nextInt(maxLength - minLength);
		}
		while (length > 0) {
			if (sb.length() != 0) {
				sb.append(" ");
				length--;
			}
			String word = getRandomWord(length);
			sb.append(word);
			length = length - word.length();
		}
		return sb.toString();

	}

	private void validateMinMaxParams(int minLength, int maxLength) {
		log.debug("DataFactory - validateMinMaxParams() method called");
		if (minLength < 0) {
			throw new IllegalArgumentException("Minimum length must be a non-negative number");
		}

		if (maxLength < 0) {
			throw new IllegalArgumentException("Maximum length must be a non-negative number");
		}

		if (maxLength < minLength) {
			throw new IllegalArgumentException(String.format("Minimum length must be less than maximum length (min=%d, max=%d)", minLength, maxLength));
		}
	}

	/**
	 * @return a random character
	 */
	public char getRandomChar() {
		log.debug("DataFactory - getRandomChar() method called");
		return (char) (random.nextInt(26) + 'A');
	}

	/**
	 * Return a string containing <code>length</code> random characters
	 *
	 * @param length
	 *            number of characters to use in the string
	 * @return A string containing <code>length</code> random characters
	 */
	public String getRandomChars(int length) {
		log.debug("DataFactory - getRandomChars() method called");
		return getRandomChars(length, length);
	}

	/**
	 * Return a string containing between <code>length</code> random characters
	 *
	 * @param length
	 *            number of characters to use in the string
	 * @return A string containing <code>length</code> random characters
	 */
	public String getRandomChars(int minLength, int maxLength) {
		log.debug("DataFactory - getRandomChars() method called");
		validateMinMaxParams(minLength, maxLength);
		StringBuilder sb = new StringBuilder(maxLength);

		int length = minLength;
		if (maxLength != minLength) {
			length = length + random.nextInt(maxLength - minLength);
		}
		while (length > 0) {
			sb.append(getRandomChar());
			length--;
		}
		return sb.toString();
	}

	/**
	 * Returns a word of a length between 1 and 10 characters.
	 *
	 * @return A work of max length 10
	 */
	public String getRandomWord() {
		log.debug("DataFactory - getRandomWord() method called");
		return getItem(contentDataValues.getWords());
	}

	/**
	 * Returns a valid word with a length of <code>length</code>
	 * characters.
	 *
	 * @param length
	 *            maximum length of the word
	 * @return a word of a length up to <code>length</code> characters
	 */
	public String getRandomWord(int length) {
		log.debug("DataFactory - getRandomWord() method called");
		return getRandomWord(length, length);
	}

	/**
	 * Returns a valid word with a length of up to <code>length</code>
	 * characters. If the <code>exactLength</code> parameter is set, then the
	 * word will be exactly <code>length</code> characters in length.
	 *
	 * @param length
	 *            maximum length of the returned string
	 * @param exactLength
	 *            indicates if the word should have a length of
	 *            <code>length</code>
	 * @return a string with a length that matches the specified parameters.
	 */
	public String getRandomWord(int length, boolean exactLength) {
		log.debug("DataFactory - getRandomWord() method called");
		if (exactLength) {
			return getRandomWord(length, length);
		}
		return getRandomWord(0, length);
	}

	/**
	 * Returns a valid word based on the length range passed in. The length will
	 * always be between the min and max length range inclusive.
	 *
	 * @param minLength minimum length of the word
	 * @param maxLength maximum length of the word
	 * @return a word of a length between min and max length
	 */
	public String getRandomWord(int minLength, int maxLength) {
		log.debug("DataFactory - getRandomWord() method called");
		validateMinMaxParams(minLength, maxLength);
		// special case if we need a single char
		if (maxLength == 1) {
			if (chance(50)) {
				return "a";
			}
			return "I";
		}

		String value = null;

		// start from random pos and find the first word of the right size
		String[] words = contentDataValues.getWords();
		int pos = random.nextInt(words.length);
		for (int i = 0; i < words.length; i++) {
			int idx = (i + pos) % words.length;
			String test = words[idx];
			if (test.length() >= minLength && test.length() <= maxLength) {
				return test;
			}
		}
		// we haven't a word for this length so generate one
		return getRandomChars(minLength, maxLength);
	}

	/**
	 * Return a random person suffix.
	 *
	 * @return suffix string
	 */
	public String getSuffix() {
		log.debug("DataFactory - getSuffix() method called");
		return getItem(nameDataValues.getSuffixes(), 100);
	}

	/**
	 * Return a random person prefix.
	 *
	 * @return Prefix string
	 */
	public String getPrefix() {
		log.debug("DataFactory - getPrefix() method called");
		return getItem(nameDataValues.getPrefixes(), 100);
	}
	
	/**
	 * Return a random person marital status.
	 *
	 * @return marital status string
	 */
	public String getStatus() {
		log.debug("DataFactory - getStatus() method called");
		return getItem(nameDataValues.getStatuses(), 100);
	}

	/**
	 * Returns a alphanumeric string e.g SD0358
	 *
	 * @param text - the base text
	 *
	 * @return Random alphanumeric string with a fixed length
	 */
	public String getAlphaNumericText(String text, int length) {
		log.debug("DataFactory - getAlphaNumericText() method called");
		StringBuilder result = new StringBuilder();
		if(!text.equals("")){
			result.append(text);
		}else{
			result.append("");
		}
		int numberLength = length - result.length();
		if(numberLength > 0){
			result.append(getNumberText(numberLength));
		}
		
		return result.toString();
	}
	
	/**
	 * Returns a string containing a set of numbers with a fixed number of
	 * digits
	 *
	 * @param digits
	 *            number of digits in the final number
	 * @return Random number as a string with a fixed length
	 */
	public String getNumberText(int digits) {
		log.debug("DataFactory - getNumberText() method called");
		String result = "";
		for (int i = 0; i < digits; i++) {
			result = result + random.nextInt(10);
		}
		return result;
	}

	/**
	 * Generates a random business name by taking a city name and additing a
	 * business onto it.
	 *
	 * @return A random business name
	 */
	public String getBusinessName() {
		log.debug("DataFactory - getBusinessName() method called");
		return getCity() + " " + getItem(contentDataValues.getBusinessTypes());
	}
	
	/**
	 * Return a random business company name
	 *
	 * @return A random company name
	 */
	public String getCompanyName() {
		log.debug("DataFactory - getCompanyName() method called");
		return getItem(contentDataValues.getCompanyNames());
	}
	
	/**
	 * Generates a random business type 
	 *
	 * @return A random business type
	 */
	public String getBusinessType() {
		log.debug("DataFactory - getBusinessType() method called");
		return getItem(contentDataValues.getBusinessTypes());
	}
	
	/**
	 * Generates a random number as phone number
	 * 
	 *
	 * @return A random phone number
	 */
	public String getPhoneNumber(String format) {
		log.debug("DataFactory - getPhoneNumber() method called");
		String phNumber = "";
		if(format.equals("USA/Canada")){
			phNumber = "1-"+getNumberText(3)+"-"+getNumberText(3)+"-"+getNumberText(4);
		}else if(format.equals("UK")){
			phNumber = "("+getNumberText(3)+") "+getNumberText(4)+" "+getNumberText(4);
		}else{
			phNumber = getNumberText(10);
		}
		
		return phNumber;
	}
	
	/**
	 * Generates a random number as phone number
	 *
	 * @return A random zip code
	 */
	public String getZipCode(String format) {
		log.debug("DataFactory - getZipCode() method called");
		String zipCode = "";
		if(format.equals("Canada")){
			zipCode = getRandomChar()+getNumberText(1)+getRandomChar()+" "+getNumberText(1)+getRandomChar()+getNumberText(1);
		}else if(format.equals("USA")){
			zipCode = getNumberText(5);
		}else{
			zipCode = getNumberText(6);
		}
		
		return zipCode;
	}
	
	/**
	 * Return a random state name  
	 *
	 * @return A random state
	 */
	public String getState(String format) {
		log.debug("DataFactory - getState() method called");
		String state = "";
		if(format.equals("Sort")){
			state = getItem(addressDataValues.getShortStates());
		}else{
			state = getItem(addressDataValues.getStates());
		}
		
		return state;
	}

	/**
	 * Generates an email address
	 *
	 * @return an email address
	 */
	public String getEmailAddress() {
		log.debug("DataFactory - getEmailAddress() method called");
		int test = random.nextInt(100);
		String email = "";
		if (test < 50) {
			// name and initial
			email = getFirstName().charAt(0) + getLastName();
		} else {
			// 2 words
			email = getItem(contentDataValues.getWords())
					+ getItem(contentDataValues.getWords());
		}
		if (random.nextInt(100) > 80) {
			email = email + random.nextInt(100);
		}
		email = email + "@" + getItem(contentDataValues.getEmailHosts()) + "."
				+ getItem(contentDataValues.getTlds());
		return email.toLowerCase();
	}
	
	/**
	 * Generates an boolean flag e.g. Y/N
	 *
	 * @return an boolean flag
	 */
	public String getBooleanFlag(){
		log.debug("DataFactory - getBooleanFlag() method called");
		return getItem(contentDataValues.getBooleanFlags());
	}

	/**
	 * Gives you a true/false based on a probability with a random number
	 * generator. Can be used to optionally add elements.
	 *
	 * <pre>
	 * if (DataFactory.chance(70)) {
	 * 	// 70% chance of this code being executed
	 * }
	 * </pre>
	 *
	 * @param chance
	 *            % chance of returning true
	 * @return
	 */
	public boolean chance(int chance) {
		return random.nextInt(100) < chance;
	}

	public NameDataValues getNameDataValues() {
		return nameDataValues;
	}

	/**
	 * Call randomize with a seed value to reset the random number generator. By
	 * using the same seed over different tests, you will should get the same
	 * results out for the same data generation calls.
	 *
	 * @param seed
	 *            Seed value to use to generate random numbers
	 */
	public void randomize(int seed) {
		random = new Random(seed);
	}

	/**
	 * Set this to provide your own list of name data values by passing it a
	 * class that implements the {@link NameDataValues} interface which just
	 * returns the String arrays to use for the test data.
	 *
	 * @param nameDataValues
	 *            Object holding the set of data values to use
	 */
	public void setNameDataValues(NameDataValues nameDataValues) {
		this.nameDataValues = nameDataValues;
	}

	/**
	 * Set this to provide your own list of address data values by passing it a
	 * class that implements the {@link AddressDataValues} interface which just
	 * returns the String arrays to use for the test data.
	 *
	 * @param addressDataValues
	 *            Object holding the set of data values to use
	 */
	public void setAddressDataValues(AddressDataValues addressDataValues) {
		this.addressDataValues = addressDataValues;
	}

	/**
	 * Set this to provide your own list of content data values by passing it a
	 * class that implements the {@link ContentDataValues} interface which just
	 * returns the String arrays to use for the test data.
	 *
	 * @param contentDataValues
	 *            Object holding the set of data values to use
	 */
	public void setContentDataValues(ContentDataValues contentDataValues) {
		this.contentDataValues = contentDataValues;
	}

}
