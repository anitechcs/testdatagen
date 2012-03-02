package com.esspl.datagen.generator.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.GeneratorBean;
import com.esspl.datagen.data.DataFactory;
import com.esspl.datagen.generator.Generator;
import com.vaadin.data.Item;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

/**
 * @author Tapas
 *
 */
public class SqlDataGenerator implements Generator{
	
	private static final Logger log = Logger.getLogger(SqlDataGenerator.class);
    private StringBuilder sbCreate = new StringBuilder();
    private StringBuilder sbInsert = new StringBuilder();
    
    private String tableName = "";
    private String database = "";
    private boolean isCreateScriptRequired = false;
    
    private String lineSeparator = "";
    
    /**
     * Actual data generation logic for SQL present here
     */
	@Override
	public String generate(DataGenApplication dataGenApplication, ArrayList<GeneratorBean> rowList) {
		log.debug("SqlDataGenerator - generate() method start");
		sbCreate.setLength(0);
		sbInsert.setLength(0);
		int maxRows = Integer.parseInt(dataGenApplication.resultNum.getValue().toString());
		tableName = dataGenApplication.tblName.getValue().toString();
		database = dataGenApplication.database.getValue().toString();
		isCreateScriptRequired = Boolean.valueOf(dataGenApplication.createQuery.getValue().toString());
		
		if(database.equals("Sql Server")){
			lineSeparator = "\nGO";
		}else{
			lineSeparator = ";";
		}
			
        //If generate create script is enabled then create script generatd
		if(isCreateScriptRequired){
			generateCreateData(rowList);
		}

        DataFactory df = new DataFactory();
        for(int row=0;row<maxRows;row++){
        	StringBuilder sbColumnNames = new StringBuilder();
        	sbColumnNames.append("(");
        	StringBuilder sb = new StringBuilder();
        	for(GeneratorBean generatorBean : rowList){
        		String data = "";
        		String dataType = generatorBean.getDataType();
        		int iid = generatorBean.getId();
    	        Item item = dataGenApplication.listing.getItem(iid);
    	        Select selFormat = (Select)(item.getItemProperty("Format").getValue());
        		if(dataType.equalsIgnoreCase("name")){
        			data = "'"+df.getName(selFormat.getValue().toString())+"'";
        		}else if(dataType.equalsIgnoreCase("email")){
        			data = "'"+df.getEmailAddress()+"'";
        		}else if(dataType.equalsIgnoreCase("date")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			PopupDateField startDate = (PopupDateField)hLayout.getComponent(0);
        			PopupDateField endDate = (PopupDateField)hLayout.getComponent(1);
        			SimpleDateFormat sdf = new SimpleDateFormat(selFormat.getValue().toString());
        			String formatedStatDate = (startDate.getValue() == null || startDate.getValue().toString().equals(""))?"":sdf.format(startDate.getValue()).toString();
        			String formatedEndDate = (endDate.getValue() == null || endDate.getValue().toString().equals(""))?"":sdf.format(endDate.getValue()).toString();
        			data = "to_date('"+df.getDate(selFormat.getValue().toString(), formatedStatDate, formatedEndDate)+"', '"+selFormat.getValue().toString()+"')";
        		}else if(dataType.equalsIgnoreCase("city")){
        			data = "'"+df.getCity()+"'";
        		}else if(dataType.equalsIgnoreCase("state/provience/county")){
        			data = "'"+df.getState(selFormat.getValue().toString())+"'";
        		}else if(dataType.equalsIgnoreCase("postal/zip")){
        			data = df.getZipCode(selFormat.getValue().toString());
        		}else if(dataType.equalsIgnoreCase("street address")){
        			data = "'"+df.getStreetName()+"'";
        		}else if(dataType.equalsIgnoreCase("title")){
        			data = "'"+df.getPrefix()+"'";
        		}else if(dataType.equalsIgnoreCase("phone/fax")){
        			data = df.getPhoneNumber(selFormat.getValue().toString());
        		}else if(dataType.equalsIgnoreCase("country")){
        			data = "'"+df.getCountry()+"'";
        		}else if(dataType.equalsIgnoreCase("random text")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField minLengthField = (TextField)hLayout.getComponent(0);
        			TextField maxLengthField = (TextField)hLayout.getComponent(1);
        			int minLength = (minLengthField.getValue() == null || minLengthField.getValue().toString().equals(""))?3:Integer.parseInt(minLengthField.getValue().toString());
        			int maxLength = (maxLengthField.getValue() == null || maxLengthField.getValue().toString().equals(""))?10:Integer.parseInt(maxLengthField.getValue().toString());
        			data = "'"+df.getRandomText(minLength, maxLength)+"'";
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
        			data = "'"+df.getAlphaNumericText(startingText, length)+"'";
        		}else if(dataType.equalsIgnoreCase("maratial status")){
        			data = "'"+df.getStatus()+"'";
        		}else if(dataType.equalsIgnoreCase("department name")){
        			data = "'"+df.getBusinessType()+"'";
        		}else if(dataType.equalsIgnoreCase("company name")){
        			data = "'"+df.getCompanyName()+"'";
        		}else if(dataType.equalsIgnoreCase("fixed text")){
        			HorizontalLayout hLayout = (HorizontalLayout)(item.getItemProperty("Additional Data").getValue());
        			TextField textField = (TextField)hLayout.getComponent(0);
        			String fixedText = (textField.getValue() == null || textField.getValue().toString().equals(""))?"":textField.getValue().toString();
        			
        			CheckBox cb = (CheckBox)hLayout.getComponent(1);
        			if(cb.getValue() != null && Boolean.valueOf(cb.getValue().toString())){
        				data = fixedText;
        			}else{
        				data = "'"+fixedText+"'";
        			}
        		}else if(dataType.equalsIgnoreCase("boolean flag")){
        			data = "'"+df.getBooleanFlag()+"'";
        		}else if(dataType.equalsIgnoreCase("passport number")){
        			data = "'"+df.getPassportNumber()+"'";
        		}
        		
        		if(sb.length() > 0) sb.append(", ");
        		sb.append(data);
        		
        		if(sbColumnNames.length() > 1) sbColumnNames.append(", ");
        		sbColumnNames.append(generatorBean.getColumnName());
        	}
        	sbColumnNames.append(")");
        	addResultData("INSERT INTO "+tableName+sbColumnNames.toString()+" VALUES("+sb.toString()+")");
        }
        
        log.debug("SqlDataGenerator - generate() method end");
		return getCreateData()+getResultData();
	}
	
    public void generateCreateData(ArrayList<GeneratorBean> rowList){
    	log.debug("SqlDataGenerator - generateCreateData() method start");
    	StringBuilder sb = new StringBuilder();
    	for(GeneratorBean generatorBean : rowList){
    		String columnType = "";
    		String dataType = generatorBean.getDataType();
    		if(dataType.equalsIgnoreCase("name")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("email")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("date")){
    			if(database.equals("Sql Server")){
    				columnType = "DATETIME";
    			}else{
    				columnType = "DATE";
    			}
    		}else if(dataType.equalsIgnoreCase("city")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("state/provience/county")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("postal/zip")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(10)";
    			}else{
    				columnType = "VARCHAR2(10)";
    			}
    		}else if(dataType.equalsIgnoreCase("street address")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("title")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("phone/fax")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(14)";
    			}else{
    				columnType = "VARCHAR2(14)";
    			}
    		}else if(dataType.equalsIgnoreCase("country")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("random text")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("incremental number")){
    			if(database.equals("Sql Server")){
    				columnType = "INT";
    			}else{
    				columnType = "NUMBER(10)";
    			}
    		}else if(dataType.equalsIgnoreCase("number range")){
    			if(database.equals("Sql Server")){
    				columnType = "INT";
    			}else{
    				columnType = "NUMBER(10)";
    			}
    		}else if(dataType.equalsIgnoreCase("alphanumeric")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("maratial status")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("department name")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("company name")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("fixed text")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(100)";
    			}else{
    				columnType = "VARCHAR2(100)";
    			}
    		}else if(dataType.equalsIgnoreCase("boolean flag")){
    			columnType = "CHAR(1)";
    		}else if(dataType.equalsIgnoreCase("boolean flag")){
    			if(database.equals("Sql Server")){
    				columnType = "VARCHAR(10)";
    			}else{
    				columnType = "VARCHAR2(10)";
    			}
    		}
    		
    		if(sb.length() > 0) sb.append(", \n");
    		sb.append("\t").append(generatorBean.getColumnName()).append("\t").append(columnType).append("\t").append("NULL");
    	}
    	
    	addCreateData("CREATE TABLE "+tableName.toUpperCase()+" (\n"+sb.toString()+"\n)"+lineSeparator);
    	log.debug("SqlDataGenerator - generateCreateData() method end");
    }
    
    public void addCreateData(String createData){
    	sbCreate.append(createData).append("\n\n");
    }
    
    public void addResultData(String insertData){
    	sbInsert.append(insertData+lineSeparator+"\n");
    }
    
    public String getCreateData(){
    	return sbCreate.toString();
    }
    
    public String getResultData(){
    	return sbInsert.toString();
    }

}
