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
package com.esspl.datagen.generator.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.GeneratorBean;
import com.esspl.datagen.data.DataFactory;
import com.esspl.datagen.generator.Generator;
import com.vaadin.data.Item;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

/**
 * @author Tapas
 *
 */
public class CsvDataGenerator implements Generator{
	
	private static final Logger log = Logger.getLogger(CsvDataGenerator.class);
	
	private StringBuilder sbCSV = new StringBuilder();
    private String dataDelimeter = ", ";

    /**
     * Actual data generation logic for CSV present here
     */
	@Override
	public String generate(DataGenApplication dataGenApplication, ArrayList<GeneratorBean> rowList) {
		log.debug("CsvDataGenerator - generate() method start");
		sbCSV.setLength(0);
		int maxRows = Integer.parseInt(dataGenApplication.resultNum.getValue().toString());
		if(!dataGenApplication.csvDelimiter.getValue().toString().equals("")){
			dataDelimeter = dataGenApplication.csvDelimiter.getValue().toString();
		}
		
		DataFactory df = new DataFactory();
        for(int row=0;row<maxRows;row++){
        	StringBuilder sb = new StringBuilder();
        	for(GeneratorBean generatorBean : rowList){
        		String data = "";
        		String dataType = generatorBean.getDataType();
        		int iid = generatorBean.getId();
    	        Item item = dataGenApplication.listing.getItem(iid);
    	        Select selFormat = (Select)(item.getItemProperty("Format").getValue());
        		if(dataType.equalsIgnoreCase("name")){
        			data = df.getName(selFormat.getValue().toString());
        		}else if(dataType.equalsIgnoreCase("email")){
        			data = df.getEmailAddress();
        		}else if(dataType.equalsIgnoreCase("date")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			PopupDateField startDate = (PopupDateField)hLayout.getComponent(0);
        			PopupDateField endDate = (PopupDateField)hLayout.getComponent(1);
        			SimpleDateFormat sdf = new SimpleDateFormat(selFormat.getValue().toString());
        			String formatedStatDate = (startDate.getValue() == null || startDate.getValue().toString().equals(""))?"":sdf.format(startDate.getValue()).toString();
        			String formatedEndDate = (endDate.getValue() == null || endDate.getValue().toString().equals(""))?"":sdf.format(endDate.getValue()).toString();
        			data = df.getDate(selFormat.getValue().toString(), formatedStatDate, formatedEndDate);
        		}else if(dataType.equalsIgnoreCase("city")){
        			data = df.getCity();
        		}else if(dataType.equalsIgnoreCase("state/provience/county")){
        			data = df.getState(selFormat.getValue().toString());
        		}else if(dataType.equalsIgnoreCase("postal/zip")){
        			data = df.getZipCode(selFormat.getValue().toString());
        		}else if(dataType.equalsIgnoreCase("street address")){
        			data = df.getStreetName();
        		}else if(dataType.equalsIgnoreCase("title")){
        			data = df.getPrefix();
        		}else if(dataType.equalsIgnoreCase("phone/fax")){
        			data = df.getPhoneNumber(selFormat.getValue().toString());
        		}else if(dataType.equalsIgnoreCase("country")){
        			data = df.getCountry();
        		}else if(dataType.equalsIgnoreCase("random text")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField minLengthField = (TextField)hLayout.getComponent(0);
        			TextField maxLengthField = (TextField)hLayout.getComponent(1);
        			int minLength = (minLengthField.getValue() == null || minLengthField.getValue().toString().equals(""))?3:Integer.parseInt(minLengthField.getValue().toString());
        			int maxLength = (maxLengthField.getValue() == null || maxLengthField.getValue().toString().equals(""))?10:Integer.parseInt(maxLengthField.getValue().toString());
        			data = df.getRandomText(minLength, maxLength);
        		}else if(dataType.equalsIgnoreCase("incremental number")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField startingFrom = (TextField)hLayout.getComponent(0);
        			int startNumber = (startingFrom.getValue() == null || startingFrom.getValue().toString().equals(""))?0:Integer.parseInt(startingFrom.getValue().toString());
        			if(startNumber > 0){
        				data = String.valueOf(row+startNumber);
        			}else{
        				data = String.valueOf(row+1);
        			}
        		}else if(dataType.equalsIgnoreCase("number range")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField minNumberField = (TextField)hLayout.getComponent(0);
        			TextField maxNumberField = (TextField)hLayout.getComponent(1);
        			int minNumber = (minNumberField.getValue() == null || minNumberField.getValue().toString().equals(""))?1:Integer.parseInt(minNumberField.getValue().toString());
        			int maxNumber = (maxNumberField.getValue() == null || maxNumberField.getValue().toString().equals(""))?1000:Integer.parseInt(maxNumberField.getValue().toString());
        			data = String.valueOf(df.getNumberBetween(minNumber, maxNumber));
        		}else if(dataType.equalsIgnoreCase("alphanumeric")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField startingTextField = (TextField)hLayout.getComponent(0);
        			TextField lengthField = (TextField)hLayout.getComponent(1);
        			String startingText = (startingTextField.getValue() == null || startingTextField.getValue().toString().equals(""))?"":startingTextField.getValue().toString();
        			int length = (lengthField.getValue() == null || lengthField.getValue().toString().equals(""))?6:Integer.parseInt(lengthField.getValue().toString());
        			data = df.getAlphaNumericText(startingText, length);
        		}else if(dataType.equalsIgnoreCase("maratial status")){
        			data = df.getStatus();
        		}else if(dataType.equalsIgnoreCase("department name")){
        			data = df.getBusinessType();
        		}else if(dataType.equalsIgnoreCase("company name")){
        			data = df.getCompanyName();
        		}else if(dataType.equalsIgnoreCase("fixed text")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField textField = (TextField)hLayout.getComponent(0);
        			String fixedText = (textField.getValue() == null || textField.getValue().toString().equals(""))?"":textField.getValue().toString();
        			data = fixedText;
        		}else if(dataType.equalsIgnoreCase("boolean flag")){
        			data = df.getBooleanFlag();
        		}else if(dataType.equalsIgnoreCase("passport number")){
        			data = df.getPassportNumber();
        		}
        		
        		if(sb.length() > 0) sb.append(dataDelimeter+" ");
        		sb.append(data);
        		
        	}
        	addResultData(sb.toString());
        }
        log.debug("CsvDataGenerator - generate() method end");
		return getResultData();
	}

	private void addResultData(String csvData){
		sbCSV.append(csvData+"\n");
    }
    
	private String getResultData(){
    	return sbCSV.toString();
    }
    
}
