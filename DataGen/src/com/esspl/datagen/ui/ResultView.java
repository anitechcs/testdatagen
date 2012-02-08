package com.esspl.datagen.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.GeneratorBean;
import com.esspl.datagen.generator.Generator;
import com.esspl.datagen.generator.impl.CsvDataGenerator;
import com.esspl.datagen.generator.impl.ExcelDataGenerator;
import com.esspl.datagen.generator.impl.SqlDataGenerator;
import com.esspl.datagen.generator.impl.XmlDataGenerator;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.DataGenExportUtility;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author Tapas
 */

public class ResultView extends Window {
    
	private static final Logger log = Logger.getLogger(ResultView.class);

    public ResultView(final DataGenApplication dataGenApplication, ArrayList<GeneratorBean> rowList) {
    	log.debug("ResultView constructor start");
        VerticalLayout layout = (VerticalLayout) this.getContent();
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setHeight("500px");
        layout.setWidth("600px");

        Button close = new Button("Close", new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	log.info("ResultView - Close Button clicked");
            	dataGenApplication.getMainWindow().removeWindow(event.getButton().getWindow());
            }
        });
        close.setIcon(DataGenConstant.CLOSE_ICON);
        
        String dataOption = dataGenApplication.generateType.getValue().toString();
        Generator genrator = null;
        if(dataOption.equalsIgnoreCase("xml")){
        	genrator = new XmlDataGenerator();
        }else if(dataOption.equalsIgnoreCase("excel")){
        	genrator = new ExcelDataGenerator();
        }else if(dataOption.equalsIgnoreCase("csv")){
        	genrator = new CsvDataGenerator();
        }else{
        	genrator = new SqlDataGenerator();//Default command is Sql
        }
		 
        //Data generated from respective command class and shown in the modal window
		final TextArea message = new TextArea();
		message.setSizeFull();
		message.setHeight("450px");
		message.setWordwrap(false);
		message.setStyleName("noResizeTextArea");
		message.setValue(genrator.generate(dataGenApplication, rowList));
	    layout.addComponent(message);
	    
	    Button copy = new Button("Copy", new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	log.info("ResultView - Copy Button clicked");
            	StringSelection stringSelection = new StringSelection(message.getValue().toString());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents( stringSelection, null );
            }
        });
	    copy.setIcon(DataGenConstant.COPY_ICON);
	    
	    Button execute = new Button("Execute", new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	log.info("ResultView - Execute Button clicked");
            	dataGenApplication.tabSheet.setSelectedTab(dataGenApplication.executor);
            	dataGenApplication.sqlScript.setValue(message.getValue().toString());
            	dataGenApplication.getMainWindow().removeWindow(event.getButton().getWindow());
            }
        });
	    execute.setIcon(DataGenConstant.EXECUTOR_ICON);
	    
	    Button export = new Button("Export to File", new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	log.info("ResultView - Export to File Button clicked");
            	String dataOption = dataGenApplication.generateType.getValue().toString();
            	DataGenExportUtility resource = null;
				try {
					if(dataOption.equalsIgnoreCase("xml")){
						File tempFile = File.createTempFile("tmp", ".xml");
						BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
						out.write(message.getValue().toString());
						out.close();
						resource = new DataGenExportUtility(dataGenApplication, "data.xml", "text/xml", tempFile);
			        }else if(dataOption.equalsIgnoreCase("excel")){
			        	File tempFile = File.createTempFile("tmp", ".xls");
						//Create contents here, using POI, and write to tempFile
						resource = new DataGenExportUtility(dataGenApplication, "data.xls", "application/vnd.ms-excel", tempFile);
			        }else if(dataOption.equalsIgnoreCase("csv")){
			        	File tempFile = File.createTempFile("tmp", ".csv");
						BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
						out.write(message.getValue().toString());
						out.close();
						resource = new DataGenExportUtility(dataGenApplication, "data.csv", "text/csv", tempFile);
			        }else{
			        	File tempFile = File.createTempFile("tmp", ".sql");
						BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
						out.write(message.getValue().toString());
						out.close();
						resource = new DataGenExportUtility(dataGenApplication, "data.sql", "text/plain", tempFile);
			        }
					getWindow().open(resource, "_self");
				} catch (FileNotFoundException e) {
					log.info("ResultView - Export to File Error - "+e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					log.info("ResultView - Export to File Error - "+e.getMessage());
					e.printStackTrace();
				}
            }
        });
	    export.setIcon(DataGenConstant.EXPORT_ICON);
        
        HorizontalLayout bttnBar = new HorizontalLayout();
        if(dataOption.equalsIgnoreCase("sql")){
        	bttnBar.addComponent(execute);
        }
        bttnBar.addComponent(export);
        bttnBar.addComponent(copy);
        bttnBar.addComponent(close);
        layout.addComponent(bttnBar);
        layout.setComponentAlignment(bttnBar, Alignment.MIDDLE_CENTER);
        log.debug("ResultView constructor end");
    }
    
}