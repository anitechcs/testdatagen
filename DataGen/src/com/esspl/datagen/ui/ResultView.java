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
package com.esspl.datagen.ui;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.GeneratorBean;
import com.esspl.datagen.generator.Generator;
import com.esspl.datagen.generator.impl.CsvDataGenerator;
import com.esspl.datagen.generator.impl.SqlDataGenerator;
import com.esspl.datagen.generator.impl.XmlDataGenerator;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.DataGenStreamUtil;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Tapas
 */
@SuppressWarnings("serial")
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
        }else if(dataOption.equalsIgnoreCase("sql")){
        	genrator = new SqlDataGenerator();
        }else if(dataOption.equalsIgnoreCase("csv")){
        	genrator = new CsvDataGenerator();
        }
        
        if(genrator == null){
        	log.info("ResultView - genrator object is null");
        	dataGenApplication.getMainWindow().removeWindow(this);
        	return;
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
            	//As on Unix environment, it gives headless exception we need to handle it
            	try {
					//StringSelection stringSelection = new StringSelection(message.getValue().toString());
            		Transferable tText = new StringSelection(message.getValue().toString());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(tText, null );
				} catch (HeadlessException e) {
					dataGenApplication.getMainWindow().showNotification("Due to some problem Text could not be copied.");
					e.printStackTrace();
				}
            }
        });
	    copy.setIcon(DataGenConstant.COPY_ICON);
	    
	    Button execute = new Button("Execute", new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	log.info("ResultView - Execute Button clicked");
            	dataGenApplication.tabSheet.setSelectedTab(dataGenApplication.executor);
            	dataGenApplication.executor.setScript(message.getValue().toString());
            	dataGenApplication.getMainWindow().removeWindow(event.getButton().getWindow());
            }
        });
	    execute.setIcon(DataGenConstant.EXECUTOR_ICON);
	    
	    Button export = new Button("Export to File", new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	log.info("ResultView - Export to File Button clicked");
            	String dataOption = dataGenApplication.generateType.getValue().toString();
            	DataGenStreamUtil resource = null;
				try {
					if(dataOption.equalsIgnoreCase("xml")){
						File tempFile = File.createTempFile("tmp", ".xml");
						BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
						out.write(message.getValue().toString());
						out.close();
						resource = new DataGenStreamUtil(dataGenApplication, "data.xml", "text/xml", tempFile);
			        }else if(dataOption.equalsIgnoreCase("csv")){
			        	File tempFile = File.createTempFile("tmp", ".csv");
						BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
						out.write(message.getValue().toString());
						out.close();
						resource = new DataGenStreamUtil(dataGenApplication, "data.csv", "text/csv", tempFile);
			        }else if(dataOption.equalsIgnoreCase("sql")){
			        	File tempFile = File.createTempFile("tmp", ".sql");
						BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
						out.write(message.getValue().toString());
						out.close();
						resource = new DataGenStreamUtil(dataGenApplication, "data.sql", "text/plain", tempFile);
			        }
					getWindow().open(resource, "_self");
				}catch(FileNotFoundException e) {
					log.info("ResultView - Export to File Error - "+e.getMessage());
					e.printStackTrace();
				}catch(IOException e) {
					log.info("ResultView - Export to File Error - "+e.getMessage());
					e.printStackTrace();
				}catch(Exception e) {
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