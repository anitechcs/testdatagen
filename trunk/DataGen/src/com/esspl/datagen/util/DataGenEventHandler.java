package com.esspl.datagen.util;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
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
        
        Label exampleSel = (Label)item.getItemProperty("Examples").getValue();
        exampleSel.setValue("NA");
        
        HorizontalLayout addBar = (HorizontalLayout)item.getItemProperty("Additional Data").getValue();
        addBar.removeAllComponents();
        addBar.setMargin(false);
        addBar.addComponent(new Label("NA"));
        
        if(event.getProperty().getValue() == null){
        	formatSel.setValue(null);
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
    		exampleSel.setValue("though");
    		addTextFields(addBar, "Min Length", "Max Length");
    	}else if(event.getProperty().getValue().equals("Number Range")){
    		exampleSel.setValue("40");
    		addTextFields(addBar, "Start Number", "End Number");
    	}else if(event.getProperty().getValue().equals("Alphanumeric")){
    		exampleSel.setValue("SD0358");
    		addTextFields(addBar, "Starting Text", "Total Length");
    	}else if(event.getProperty().getValue().equals("Title")){//Examples start
    		exampleSel.setValue("Mr.");
    	}else if(event.getProperty().getValue().equals("Email")){
    		exampleSel.setValue("ansh@gmail.com");
    	}else if(event.getProperty().getValue().equals("Street Address")){
    		exampleSel.setValue("Infocity");
    	}else if(event.getProperty().getValue().equals("City")){
    		exampleSel.setValue("Bhubaneswar");
    	}else if(event.getProperty().getValue().equals("Country")){
    		exampleSel.setValue("India");
    	}else if(event.getProperty().getValue().equals("Incremental Number")){
    		exampleSel.setValue("1, 2, 3, 4, 5..");
    	}else if(event.getProperty().getValue().equals("Maratial Status")){
    		exampleSel.setValue("Single");
    	}else if(event.getProperty().getValue().equals("Department Name")){
    		exampleSel.setValue("IT");
    	}else if(event.getProperty().getValue().equals("Company Name")){
    		exampleSel.setValue("Google");
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
    		exampleSel.setValue("Tapas Jena");
    	}else if(event.getProperty().getValue().equals("First_Name")){
    		exampleSel.setValue("Tapas");
    	}else if(event.getProperty().getValue().equals("Sur_Name Last_Name")){
    		exampleSel.setValue("Mr. Jena");
    	}else if(event.getProperty().getValue().equals("Sur_Name First_Name")){
    		exampleSel.setValue("Mr. Tapas");
    	}else if(event.getProperty().getValue().equals("Sur_Name First_Name Last_Name")){
    		exampleSel.setValue("Mr. Tapas Jena");
    	}else if(event.getProperty().getValue().equals("MM/dd/yyyy")){//Date Examples start
    		exampleSel.setValue("06/05/2012");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("dd/MM/yyyy")){
    		exampleSel.setValue("05/06/2011");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("yyyy-MM-dd")){
    		exampleSel.setValue("2012-06-05");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("MMM dd, yyyy")){
    		exampleSel.setValue("Jun 5, 2012");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("dd MMM, yy")){
    		exampleSel.setValue("05 Jun, 12");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("yyyy-MM-dd HH:mm:ss")){
    		exampleSel.setValue("2012-06-05 02:35:12");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("MM/dd/yyyy HH:mm:ss")){
    		exampleSel.setValue("06/05/2011 02:25:45");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("MM.dd.yyyy")){
    		exampleSel.setValue("06.05.2011");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("dd.MM.yyyy")){
    		exampleSel.setValue("05.06.2012");
    		addDateFields(select, addBar);
    	}else if(event.getProperty().getValue().equals("India") && dataSel.getValue().equals("Phone/Fax")){//Phone Examples start
    		exampleSel.setValue("91983360616");
    	}else if(event.getProperty().getValue().equals("USA/Canada") && dataSel.getValue().equals("Phone/Fax")){
    		exampleSel.setValue("1-800-555-5555");
    	}else if(event.getProperty().getValue().equals("UK") && dataSel.getValue().equals("Phone/Fax")){
    		exampleSel.setValue("(020) 7222 1234");
    	}else if(event.getProperty().getValue().equals("India") && dataSel.getValue().equals("Postal/Zip")){//Zip Examples start
    		exampleSel.setValue("752026");
    	}else if(event.getProperty().getValue().equals("Canada") && dataSel.getValue().equals("Postal/Zip")){
    		exampleSel.setValue("V3H 1Z7");
    	}else if(event.getProperty().getValue().equals("USA") && dataSel.getValue().equals("Postal/Zip")){
    		exampleSel.setValue("52079");
    	}else if(event.getProperty().getValue().equals("Full")){//State Examples start
    		exampleSel.setValue("Los Angeles");
    	}else if(event.getProperty().getValue().equals("Sort")){
    		exampleSel.setValue("LA");
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
    	first.setWidth("97px");
    	
    	TextField second = new TextField();
    	second.setInputPrompt(promptText2);
    	second.setWidth("97px");
    	
    	addBar.removeAllComponents();
        addBar.setSpacing(true);
        addBar.addComponent(first);
        addBar.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        addBar.addComponent(second);
        addBar.setComponentAlignment(second, Alignment.MIDDLE_LEFT);
        log.debug("DataGenEventHandler - addTextFields() method end");
    }
}
