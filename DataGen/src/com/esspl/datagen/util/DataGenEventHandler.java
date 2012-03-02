package com.esspl.datagen.util;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.data.DataFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

/**
 * @author Tapas
 *
 * This class separates the ValueChangeEvent logics from main Application class.
 */
public class DataGenEventHandler {

	private static final Logger log = Logger.getLogger(DataGenEventHandler.class);
	private DataFactory df = new DataFactory();
	
	public void onChangeSelect(ValueChangeEvent event){
		log.debug("DataGenEventHandler - onChangeSelect() method called");
    	Select sel = (Select)event.getProperty();
    	if(sel.getCaption().equals("DataType")){
    		onChangeDataType(event, sel);
    	}else if(sel.getCaption().equals("Format")){
    		onChangeFormat(event, sel);
    	}
    }
    
    public void onChangeDataType(ValueChangeEvent event, Select select){
    	log.debug("DataGenEventHandler - onChangeDataType() method start");
		int rowId = (Integer)select.getData();
		DataGenApplication tdg = (DataGenApplication)select.getApplication();
        Item item = tdg.listing.getItem(rowId);
        Select formatSel = (Select)item.getItemProperty("Format").getValue();
        formatSel.removeAllItems();
        formatSel.setEnabled(true);
        
        Label exampleSel = (Label)item.getItemProperty("Examples").getValue();
        exampleSel.setValue("NA");
        
        HorizontalLayout addBar = (HorizontalLayout)item.getItemProperty("Additional Data").getValue();
        addBar.removeAllComponents();
        addBar.setMargin(false);
        addBar.addComponent(new Label("NA"));
        
        if(event.getProperty().getValue() == null){
        	formatSel.setValue(null);
        	formatSel.setEnabled(false);
        	return;
    	}
    	if(event.getProperty().getValue().equals("Name")){
    		for(String f : DataGenConstant.NAME_FORMATS){
    	        formatSel.addItem(f);
    		}
    		formatSel.setNullSelectionAllowed(false);
    		formatSel.setValue("First_Name Last_Name");
    	}else if(event.getProperty().getValue().equals("Date")){
    		for(String f : DataGenConstant.DATE_FORMATS){
    			formatSel.addItem(f);
    		}
    		formatSel.setNullSelectionAllowed(false);
    		formatSel.setValue("dd/MM/yyyy");
    	}else if(event.getProperty().getValue().equals("Phone/Fax")){
    		for(String f : DataGenConstant.PHONE_FORMATS){
    			formatSel.addItem(f);
    		}
    		formatSel.setNullSelectionAllowed(false);
    		formatSel.setValue("India");
    	}else if(event.getProperty().getValue().equals("Postal/Zip")){
    		for(String f : DataGenConstant.ZIP_FORMATS){
    			formatSel.addItem(f);
    		}
    		formatSel.setNullSelectionAllowed(false);
    		formatSel.setValue("India");
    	}else if(event.getProperty().getValue().equals("State/Provience/County")){
    		for(String f : DataGenConstant.STATE_FORMATS){
    			formatSel.addItem(f);
    		}
    		formatSel.setNullSelectionAllowed(false);
    		formatSel.setValue("Full");
    	}else if(event.getProperty().getValue().equals("Random Text")){
    		exampleSel.setValue(df.getRandomText(6));
    		addTextFields(addBar, "Min Length", "Max Length");
    	}else if(event.getProperty().getValue().equals("Fixed Text")){
    		exampleSel.setValue(df.getRandomWord());
    		addChkTextField(addBar, "Text", "Is Number");
    	}else if(event.getProperty().getValue().equals("Number Range")){
    		exampleSel.setValue(df.getNumberBetween(10, 1000));
    		addTextFields(addBar, "Start Number", "End Number");
    	}else if(event.getProperty().getValue().equals("Alphanumeric")){
    		exampleSel.setValue("SD0358");
    		addTextFields(addBar, "Starting Text", "Total Length");
    	}else if(event.getProperty().getValue().equals("Title")){//Examples start
    		exampleSel.setValue(df.getPrefix());
    	}else if(event.getProperty().getValue().equals("Email")){
    		exampleSel.setValue(df.getEmailAddress());
    	}else if(event.getProperty().getValue().equals("Street Address")){
    		exampleSel.setValue(df.getStreetName());
    	}else if(event.getProperty().getValue().equals("City")){
    		exampleSel.setValue(df.getCity());
    	}else if(event.getProperty().getValue().equals("Country")){
    		exampleSel.setValue(df.getCountry());
    	}else if(event.getProperty().getValue().equals("Incremental Number")){
    		exampleSel.setValue("1, 2, 3, 4, 5..");
    		addSingleTextField(addBar, "Starting From");
    	}else if(event.getProperty().getValue().equals("Marital Status")){
    		exampleSel.setValue(df.getStatus());
    	}else if(event.getProperty().getValue().equals("Department Name")){
    		exampleSel.setValue(df.getBusinessType());
    	}else if(event.getProperty().getValue().equals("Company Name")){
    		exampleSel.setValue(df.getCompanyName());
    	}else if(event.getProperty().getValue().equals("Boolean Flag")){
    		exampleSel.setValue(df.getBooleanFlag());
    	}else if(event.getProperty().getValue().equals("Passport Number")){
    		exampleSel.setValue(df.getPassportNumber());
    	}
    	
    	//If there are no formats to show, then disable it
    	if(formatSel.getItemIds().size() == 0){
    		formatSel.setEnabled(false);
    	}
    	log.debug("DataGenEventHandler - onChangeDataType() method end");
    }
    
    public void onChangeFormat(ValueChangeEvent event, Select select){
    	log.debug("DataGenEventHandler - onChangeFormat() method start");
    	int rowId = (Integer)select.getData();
    	DataGenApplication tdg = (DataGenApplication)select.getApplication();
        Item item = tdg.listing.getItem(rowId);
        Label exampleSel = (Label)item.getItemProperty("Examples").getValue();
        Select dataSel = (Select)item.getItemProperty("Data Type").getValue();
        HorizontalLayout addBar = (HorizontalLayout)item.getItemProperty("Additional Data").getValue();
        addBar.setMargin(false);
        
        if(event.getProperty().getValue() == null){
        	exampleSel.setValue("NA");
        	return;
    	}
        //Name examples
    	if(event.getProperty().getValue().equals("First_Name Last_Name")){
    		exampleSel.setValue(df.getName("First_Name Last_Name"));
    	}else if(event.getProperty().getValue().equals("First_Name")){
    		exampleSel.setValue(df.getName("First_Name"));
    	}else if(event.getProperty().getValue().equals("Last_Name")){
    		exampleSel.setValue(df.getName("Last_Name"));
    	}else if(event.getProperty().getValue().equals("Sur_Name Last_Name")){
    		exampleSel.setValue(df.getName("Sur_Name Last_Name"));
    	}else if(event.getProperty().getValue().equals("Sur_Name First_Name")){
    		exampleSel.setValue(df.getName("Sur_Name First_Name"));
    	}else if(event.getProperty().getValue().equals("Sur_Name First_Name Last_Name")){
    		exampleSel.setValue(df.getName("Sur_Name First_Name Last_Name"));
    	}else if(event.getProperty().getValue().equals("MM/dd/yyyy")){//Date Examples start
    		exampleSel.setValue(df.getDate("MM/dd/yyyy", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("dd/MM/yyyy")){
    		exampleSel.setValue(df.getDate("dd/MM/yyyy", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("yyyy-MM-dd")){
    		exampleSel.setValue(df.getDate("yyyy-MM-dd", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("MMM dd, yyyy")){
    		exampleSel.setValue(df.getDate("MMM dd, yyyy", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("dd MMM, yy")){
    		exampleSel.setValue(df.getDate("dd MMM, yy", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("yyyy-MM-dd HH:mm:ss")){
    		exampleSel.setValue(df.getDate("yyyy-MM-dd HH:mm:ss", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("MM/dd/yyyy HH:mm:ss")){
    		exampleSel.setValue(df.getDate("MM/dd/yyyy HH:mm:ss", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("MM.dd.yyyy")){
    		exampleSel.setValue(df.getDate("MM.dd.yyyy", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("dd.MM.yyyy")){
    		exampleSel.setValue(df.getDate("dd.MM.yyyy", "", ""));
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("India") && dataSel.getValue().equals("Phone/Fax")){//Phone Examples start
    		exampleSel.setValue(df.getPhoneNumber("India"));
    	}else if(event.getProperty().getValue().equals("USA/Canada") && dataSel.getValue().equals("Phone/Fax")){
    		exampleSel.setValue(df.getPhoneNumber("USA/Canada"));
    	}else if(event.getProperty().getValue().equals("UK") && dataSel.getValue().equals("Phone/Fax")){
    		exampleSel.setValue(df.getPhoneNumber("UK"));
    	}else if(event.getProperty().getValue().equals("India") && dataSel.getValue().equals("Postal/Zip")){//Zip Examples start
    		exampleSel.setValue(df.getZipCode("India"));
    	}else if(event.getProperty().getValue().equals("Canada") && dataSel.getValue().equals("Postal/Zip")){
    		exampleSel.setValue(df.getZipCode("Canada"));
    	}else if(event.getProperty().getValue().equals("USA") && dataSel.getValue().equals("Postal/Zip")){
    		exampleSel.setValue(df.getZipCode("USA"));
    	}else if(event.getProperty().getValue().equals("Full")){//State Examples start
    		exampleSel.setValue(df.getState("Full"));
    	}else if(event.getProperty().getValue().equals("Sort")){
    		exampleSel.setValue(df.getState("Sort"));
    	}
    	log.debug("DataGenEventHandler - onChangeFormat() method end");
    }
    
    public void addDateFields(Select select, HorizontalLayout addBar){
    	log.debug("DataGenEventHandler - addDateFields() method start");
    	PopupDateField startDate = new PopupDateField();
        startDate.setInputPrompt("Start date");
        startDate.setDateFormat(select.getValue().toString());
        startDate.setResolution(PopupDateField.RESOLUTION_DAY);
        startDate.setWidth("120px");
        startDate.setLenient(true);
        
        PopupDateField endDate = new PopupDateField();
        endDate.setInputPrompt("End date");
        endDate.setDateFormat(select.getValue().toString());
        endDate.setResolution(PopupDateField.RESOLUTION_DAY);
        endDate.setWidth("120px");
        endDate.setLenient(true);
        
        addBar.removeAllComponents();
        addBar.setSpacing(true);
        addBar.addComponent(startDate);
        addBar.setComponentAlignment(startDate, Alignment.MIDDLE_LEFT);
        addBar.addComponent(endDate);
        addBar.setComponentAlignment(endDate, Alignment.MIDDLE_LEFT);
        log.debug("DataGenEventHandler - addDateFields() method end");
    }
    
    public void addTextFields(HorizontalLayout addBar, String promptText1, String promptText2){
    	log.debug("DataGenEventHandler - addTextFields() method start");
    	TextField first = new TextField();
    	first.setInputPrompt(promptText1);
    	first.setWidth("95px");
    	first.setImmediate(true);
    	if(promptText1.endsWith("Length") || promptText1.endsWith("Number") || promptText1.equals("Starting From")){
    		first.addValidator(new IntegerValidator(promptText1+" must be an Integer"));
    	}
    	
    	TextField second = new TextField();
    	second.setInputPrompt(promptText2);
    	second.setWidth("95px");
    	second.setImmediate(true);
    	if(promptText2.endsWith("Length") || promptText2.endsWith("Number") || promptText2.equals("Starting From")){
    		second.addValidator(new IntegerValidator(promptText2+" must be an Integer"));
    	}
    	
    	addBar.removeAllComponents();
        addBar.setSpacing(true);
        addBar.addComponent(first);
        addBar.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        addBar.addComponent(second);
        addBar.setComponentAlignment(second, Alignment.MIDDLE_LEFT);
        log.debug("DataGenEventHandler - addTextFields() method end");
    }
    
    public void addSingleTextField(HorizontalLayout addBar, String promptText1){
    	log.debug("DataGenEventHandler - addSingleTextField() method start");
    	TextField first = new TextField();
    	first.setInputPrompt(promptText1);
    	first.setWidth("95px");
    	first.setImmediate(true);
    	if(promptText1.endsWith("Length") || promptText1.endsWith("Number") || promptText1.equals("Starting From")){
    		first.addValidator(new IntegerValidator(promptText1+" must be an Integer"));
    	}
    	
    	addBar.removeAllComponents();
        addBar.setSpacing(true);
        addBar.addComponent(first);
        addBar.setComponentAlignment(first, Alignment.MIDDLE_CENTER);
        log.debug("DataGenEventHandler - addSingleTextField() method end");
    }
    
    public void addChkTextField(HorizontalLayout addBar, String promptText1, String promptText2){
    	log.debug("DataGenEventHandler - addSingleTextField() method start");
    	TextField first = new TextField();
    	first.setInputPrompt(promptText1);
    	first.setWidth("95px");
    	first.setImmediate(true);
    	if(promptText1.endsWith("Length") || promptText1.endsWith("Number") || promptText1.equals("Starting From")){
    		first.addValidator(new IntegerValidator(promptText1+" must be an Integer"));
    	}
    	
    	CheckBox cb = new CheckBox(promptText2);
    	
    	addBar.removeAllComponents();
        addBar.setSpacing(true);
        addBar.addComponent(first);
        addBar.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        addBar.addComponent(cb);
        addBar.setComponentAlignment(cb, Alignment.MIDDLE_LEFT);
        log.debug("DataGenEventHandler - addSingleTextField() method end");
    }
}
