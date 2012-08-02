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

import java.sql.Connection;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.DataGenSqlExecutor;
import com.esspl.datagen.util.JdbcUtils;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;

/**
 * @author Tapas
 *
 */
public class ExecutorView extends CustomComponent {

	private static final Logger log = Logger.getLogger(ExecutorView.class);
	private DataGenApplication dataGenApplication;
	public Connection connection;
	public VerticalSplitPanel splitPanel;
    public ComboBox maxRowsBox;
    public TabSheet resultSheet;
    public Tab resultTab;
    public Label logText;
    public TextArea sqlScript;

    public ExecutorView(DataGenApplication application) {
    	log.debug("ExecutorView - constructor start");
    	dataGenApplication = application;
        setSizeFull();

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        setCompositionRoot(vl);

        splitPanel = new VerticalSplitPanel();
        splitPanel.setSizeFull();

        //Script TextArea
        sqlScript = new TextArea();
        sqlScript.setSizeFull();
        sqlScript.setWordwrap(false);
        sqlScript.setStyleName("noResizeTextArea");

        HorizontalLayout queryOptions = new HorizontalLayout();
        queryOptions.setMargin(false, false, false, true);
        queryOptions.setSpacing(true);
        queryOptions.setWidth("100%");
        queryOptions.setHeight("40px");
        
        Button executeButton = new Button("Execute", new ClickListener() {
            @Override
        	public void buttonClick(ClickEvent event) {
            	log.info("ExecutorView - Execute Button clicked");
            	executeQuery(sqlScript.getValue().toString());
            }
        });
        executeButton.addStyleName("small");
        executeButton.setIcon(DataGenConstant.EXECUTOR_ICON);
        executeButton.setDescription("Press Ctrl+Enter to execute the query");
        
        Button clearQueryButton = new Button("Clear SQL", new ClickListener() {
            @Override
        	public void buttonClick(ClickEvent event) {
            	log.info("ExecutorView - Clear SQL Button clicked");
            	sqlScript.setValue("");
            }
        });
        clearQueryButton.addStyleName("small");
        clearQueryButton.setIcon(DataGenConstant.CLEAR_SMALL);
        clearQueryButton.setDescription("Clears the Sql");
        
        Button clearConsoleButton = new Button("Clear Console", new ClickListener() {
            @Override
        	public void buttonClick(ClickEvent event) {
            	log.info("ExecutorView - Clear Console Button clicked");
            	logText.setValue("");
            }
        });
        clearConsoleButton.addStyleName("small");
        clearConsoleButton.setIcon(DataGenConstant.CLEAR_SMALL);
        clearConsoleButton.setDescription("Clears the Console");
        
        maxRowsBox = new ComboBox(null, Arrays.asList(10, 100, 500, 1000, 5000, 10000));
        maxRowsBox.setDescription("Max number of rows to retrieve");
        maxRowsBox.setWidth(100, UNITS_PIXELS);
        maxRowsBox.setNewItemsAllowed(false);
        maxRowsBox.setNullSelectionAllowed(false);
        maxRowsBox.setValue(100);
        
        //Bottom section components
        resultSheet = new TabSheet();
        resultSheet.setSizeFull();
        resultSheet.addStyleName(Runo.TABSHEET_SMALL);

        logText = new Label();
        logText.setContentMode(Label.CONTENT_XHTML);
        resultSheet.addTab(logText, "Console");
        resultTab = resultSheet.addTab(new Label(), "Results");
        
        queryOptions.addComponent(executeButton);
        queryOptions.setComponentAlignment(executeButton, Alignment.MIDDLE_LEFT);
        queryOptions.addComponent(clearQueryButton);
        queryOptions.setComponentAlignment(clearQueryButton, Alignment.MIDDLE_LEFT);
        queryOptions.addComponent(clearConsoleButton);
        queryOptions.setComponentAlignment(clearConsoleButton, Alignment.MIDDLE_LEFT);
        queryOptions.setExpandRatio(clearConsoleButton, 1);
        queryOptions.addComponent(maxRowsBox);
        queryOptions.setComponentAlignment(maxRowsBox, Alignment.MIDDLE_RIGHT);

        splitPanel.setFirstComponent(sqlScript);
        splitPanel.setSecondComponent(resultSheet);

        vl.addComponent(queryOptions);
        vl.addComponent(splitPanel);
        vl.setExpandRatio(splitPanel, 1);

        sqlScript.addShortcutListener(new ShortcutListener("Execute Script", null, ShortcutAction.KeyCode.ENTER, ShortcutAction.ModifierKey.CTRL) {

            @Override
            public void handleAction(Object sender, Object target) {
                executeQuery(sqlScript.getValue().toString());
            }
        });
        log.debug("ExecutorView - constructor end");
    }

    /**
     * Executes the sql statements
     * 
     * @param query
     */
    protected void executeQuery(String query) {
    	log.debug("ExecutorView - executeQuery() start");
    	//If query area is blank show popup message to user
    	if(query.equals("")){
    		getWindow().showNotification("You must provide some SQL to Execute!", Notification.TYPE_ERROR_MESSAGE);
    		return;
    	}
    	if((query.indexOf("select") > -1 || query.indexOf("Select") > -1 || query.indexOf("SELECT") > -1) && (query.indexOf("insert") > -1 ||query.indexOf("Insert") > -1 ||query.indexOf("INSERT") > -1)){
    		getWindow().showNotification("SELECT and INSERT statements can not be executed simultaneously!", Notification.TYPE_ERROR_MESSAGE);
    		return;
    	}
    	
        long start = System.currentTimeMillis();
        try {
            connection = dataGenApplication.databaseSessionManager.getConnection();
        } catch (Exception ex) {
            log.debug("connection failed", ex);
            addComponent(new Label(ex.getMessage()));
            return;
        }
        
        //If user is not connected to database, show popup message to user
        if(connection == null){
        	getWindow().showNotification("You are not connected to any Database!", Notification.TYPE_ERROR_MESSAGE);
        	return;
        }
        
        DataGenSqlExecutor executor = new DataGenSqlExecutor();
        String statisticsMsg = executor.executeScript(this, query, dataGenApplication.database.getValue().toString());
        JdbcUtils.close(connection);

        long end = System.currentTimeMillis();
        getApplication().getMainWindow().showNotification("Query Stats<br/>", "exec time: " + (end - start) / 1000.0 + " ms<br/>" + statisticsMsg, Notification.TYPE_TRAY_NOTIFICATION);
        log.debug("ExecutorView - executeQuery() end");
    }

    /**
     * sets script in the Query text area
     * 
     * @param script
     */
    public void setScript(String script) {
    	log.debug("ExecutorView - setScript() called");
    	sqlScript.setValue(script);
    }
}
