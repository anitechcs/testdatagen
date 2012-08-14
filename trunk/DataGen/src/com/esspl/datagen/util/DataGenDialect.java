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
package com.esspl.datagen.util;

import org.apache.log4j.Logger;

/**
 * @author Tapas
 *
 */
public class DataGenDialect {

	private static final Logger log = Logger.getLogger(DataGenDialect.class);
	
	private String databaseName = "";
	
	/**
	 * This is the parameterised constructor for 
	 * instantiating Dialect class by giving particular database name.
	 * If value is blank It will take the selected database name in Generator tab to 
	 * instantiate the Dialect.
	 * 
	 * @param dbName
	 */
	public DataGenDialect(String dbName){
		log.debug("SqlDataGenerator - DataGenDialect(String dbName) - Instantiating Dialect fro DB : " + dbName);
		this.databaseName = dbName;
		
	}
	
	/**
	 * This method will return column datatype for string fields in Database agnostic manner
	 * 
	 */
	public String getStringDataType(){
		//Call the overloaded method with default value 100
		return getStringDataType(100);
	}
	
	/**
	 * This is the overloaded method which will return column datatype for string fields in 
	 * Database agnostic manner by taking size
	 * 
	 */
	public String getStringDataType(int size){
		//If database is blank return blank. 
		if(databaseName.equals("")){
			log.info("DataGenDialect - getStringDataType() - Database name is blank! Please select one.");
			return "";
		}
		
		String stringDataType = "";
		if(databaseName.equals("Sql Server")){
			stringDataType = "VARCHAR(" + size + ")";
		}else{
			stringDataType = "VARCHAR2(" + size + ")";
		}
		
		return stringDataType;
	}
	
	/**
	 * This method will return column datatype for number fields in Database agnostic manner
	 * 
	 */
	public String getNumberDataType(){
		//Call the overloaded method with default value 10
		return getNumberDataType(10);
	}
	
	/**
	 * This is the overloaded method which will return column datatype for number fields in 
	 * Database agnostic manner by taking size
	 * 
	 */
	public String getNumberDataType(int size){
		//If database is blank return blank. 
		if(databaseName.equals("")){
			log.info("DataGenDialect - getNumberDataType() - Database name is blank! Please select one.");
			return "";
		}
		
		String numberDataType = "";
		if(databaseName.equals("Sql Server")){
			numberDataType = "INT";
		}else{
			numberDataType = "NUMBER( " + size + " )";
		}
		
		return numberDataType;
	}
	
	/**
	 * This method will return column datatype for date fields in Database agnostic manner
	 * 
	 */
	public String getDateDataType(){
		//If database is blank return blank. 
		if(databaseName.equals("")){
			log.info("DataGenDialect - getDateDataType() - Database name is blank! Please select one.");
			return "";
		}
		
		String dateDataType = "";
		if(databaseName.equals("Sql Server")){
			dateDataType = "DATETIME";
		}else{
			dateDataType = "DATE";
		}
		
		return dateDataType;
	}
	
	/**
	 * This method will return column datatype for char fields in Database agnostic manner
	 * 
	 */
	public String getCharDataType(){
		//If database is blank return blank. 
		if(databaseName.equals("")){
			log.info("DataGenDialect - getCharDataType() - Database name is blank! Please select one.");
			return "";
		}
		
		//This logic needs to be re-looked
		String charDataType = "";
		if(databaseName.equals("Sql Server")){
			charDataType = "CHAR";
		}else{
			charDataType = "CHAR(1)";
		}
		
		return charDataType;
	}
	
}
