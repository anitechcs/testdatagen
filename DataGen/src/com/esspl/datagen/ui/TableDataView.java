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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.vaadin.hene.popupbutton.PopupButton;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.JdbcTable;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.JdbcUtils;
import com.vaadin.data.Item;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *@author Tapas
 *
 */
@SuppressWarnings("serial")
public class TableDataView extends CustomComponent {

	private static final Logger log = Logger.getLogger(ExplorerView.class);
	
	private DataGenApplication dataGenApplication;
    private VerticalLayout tableContainer;
    private HorizontalLayout content;
    private TextField rows;
    private ArrayList<String> columns = new ArrayList<String>();

    public TableDataView(final JdbcTable table, final Connection connection, final DataGenApplication dataApp) {
    	log.debug("TableDataView - constructor start");
        setCaption("Data");
        dataGenApplication = dataApp;
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        setCompositionRoot(vl);
        
        HorizontalLayout hBar = new HorizontalLayout();
        hBar.setWidth("98%");
        hBar.setHeight("40px");

        rows = new TextField();
        rows.setWidth("50px");
        rows.setImmediate(true);
        rows.addValidator(new IntegerValidator("Rows must be an Integer"));
        Label lbl = new Label("Generate ");

        content = new HorizontalLayout();
        content.setHeight("40px");
        content.setMargin(false, false, false, true);
        content.setSpacing(true);
        content.addComponent(lbl);
        content.setComponentAlignment(lbl, Alignment.MIDDLE_CENTER);
        content.addComponent(rows);
        content.setComponentAlignment(rows, Alignment.MIDDLE_CENTER);
        
        Button addDataButton = new Button("Row(S) Data", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	log.debug("TableDataView - Generate Data Button clicked");
                populateGenerator(table);
            }
        });
        addDataButton.addStyleName("small");
        addDataButton.setIcon(DataGenConstant.ADD_SMALL);
        content.addComponent(addDataButton);
        content.setComponentAlignment(addDataButton, Alignment.MIDDLE_CENTER);
        
        Button refreshButton = new Button("Refresh", new Button.ClickListener() {
        	
            @Override
            public void buttonClick(ClickEvent event) {
            	log.debug("TableDataView - Refresh Button clicked");
                refreshDataView(table, connection);
            }
        });
        refreshButton.addStyleName("small");
        refreshButton.setIcon(DataGenConstant.RESET);
        content.addComponent(refreshButton);
        content.setComponentAlignment(refreshButton, Alignment.MIDDLE_CENTER);
        
        //Tapas:10/08/2012 - Export feature implementation started
        HorizontalLayout expContainer = new HorizontalLayout();
        expContainer.setSpacing(true);
        
        PopupButton exportButton = new PopupButton("Export");
        exportButton.setComponent(new DataExportView());
        exportButton.addListener(new ClickListener() {
        	
			@Override
			public void buttonClick(ClickEvent event) {
				//dataApp.getMainWindow().showNotification("Export Button clicked!");
			}
        });
        exportButton.setIcon(DataGenConstant.DATAEXPORT_ICON);
        expContainer.addComponent(exportButton);
        expContainer.setComponentAlignment(exportButton, Alignment.MIDDLE_LEFT);
        
        //Tapas:10/08/2012 - Import feature implementation started
        PopupButton importButton = new PopupButton("Import");
        importButton.setComponent(new DataImportView());
        importButton.addListener(new ClickListener() {
        	
        	@Override
			public void buttonClick(ClickEvent event) {
        		//dataApp.getMainWindow().showNotification("Import Button clicked!");
			}
		});
        importButton.setIcon(DataGenConstant.DATAIMPORT_ICON);
        expContainer.addComponent(importButton);
        expContainer.setComponentAlignment(importButton, Alignment.MIDDLE_RIGHT);
        
        
        tableContainer = new VerticalLayout();
        tableContainer.setSizeFull();
        hBar.addComponent(content);
        hBar.setComponentAlignment(content, Alignment.MIDDLE_LEFT);
        hBar.addComponent(expContainer);
        hBar.setComponentAlignment(expContainer, Alignment.MIDDLE_RIGHT);
        vl.addComponent(hBar);
        vl.addComponent(tableContainer);
        vl.setExpandRatio(tableContainer, 1f);

        refreshDataView(table, connection);
        log.debug("TableDataView - constructor end");
    }

    protected void refreshDataView(JdbcTable table, Connection connection) {
    	log.debug("TableDataView - refreshDataView() called");
        Statement stmt = null;
        ResultSet rs = null;
        Component component;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery("select * from " + createTableName(table, connection));
            component = new ResultSetTable(rs);
            component.setSizeFull();
            ResultSetMetaData meta = rs.getMetaData();
            for(int i=1;i<=meta.getColumnCount();i++){
            	columns.add(meta.getColumnName(i));
            }
        } catch (SQLException ex) {
            log.warn("failed to retrieve table data", ex);
            component = new Label(ex.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        tableContainer.removeAllComponents();
        tableContainer.addComponent(component);
    }

    protected String createTableName(JdbcTable table, Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        StringBuilder sb = new StringBuilder();
        if (null != table.getCatalog() && !table.getCatalog().isEmpty() && metaData.supportsCatalogsInTableDefinitions())
            sb.append(table.getCatalog()).append(".");
        if (null != table.getSchema() && !table.getSchema().isEmpty() && metaData.supportsSchemasInTableDefinitions())
            sb.append(table.getSchema()).append(".");
        sb.append(table.getName());
        return sb.toString();
    }
    
    @SuppressWarnings("rawtypes")
    protected void populateGenerator(JdbcTable table){
    	log.debug("TableDataView - populateGenerator() called");
    	if(rows.getValue() != null){
    		dataGenApplication.resultNum.setValue(rows.getValue().toString());
    	}
    	dataGenApplication.tblName.setValue(table.getName().toUpperCase());
    	dataGenApplication.createQuery.setValue(false);
    	dataGenApplication.tabSheet.setSelectedTab(dataGenApplication.generator);
    	int generatorLength = dataGenApplication.listing.getItemIds().size();
    	//If there are more column than the present generated row, we need to add new rows first
    	if(columns.size() > generatorLength){
    		dataGenApplication.addRow(columns.size() - generatorLength);
    	}
    	
		Iterator iter = dataGenApplication.listing.getItemIds().iterator();
    	while(iter.hasNext()){
    		int row = (Integer)iter.next();
    		Item item = dataGenApplication.listing.getItem(row);
    		TextField txtField = (TextField)(item.getItemProperty("Column Name").getValue());
    		if(columns.size() > 0){
    			txtField.setValue(columns.get(0));
    			columns.remove(0);
    		}
    	}
    }
}
