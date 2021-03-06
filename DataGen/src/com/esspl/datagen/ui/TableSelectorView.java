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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.DatabaseSessionManager;
import com.esspl.datagen.common.DetailsListener;
import com.esspl.datagen.common.JdbcTable;
import com.esspl.datagen.util.JdbcUtils;
import com.esspl.datagen.util.MetadataRetriever;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

/**
 *
 */
@SuppressWarnings("serial")
public class TableSelectorView extends VerticalLayout {

	private static final Logger log = Logger.getLogger(TableSelectorView.class);
    private static final String VALUE_PROPERTY = "value";
   
    private Connection connection;
    private MetadataRetriever metadataRetriever;
    private IndexedContainer tableListContainer;
    private DetailsListener detailsListener;
    private DataGenApplication dataGenApplication;

    public TableSelectorView(DatabaseSessionManager sessionManager, DataGenApplication dataApp) {
    	dataGenApplication = dataApp;
        try {
            connection = sessionManager.getConnection();
            if(connection != null){
            	metadataRetriever = new MetadataRetriever(connection);
            }

            setSizeFull();
            Component selectors = createSelectors();

            VerticalLayout vl = new VerticalLayout();
            vl.setSizeFull();

            tableListContainer = createObjecListContainer();
            Table objectList = new Table(null, tableListContainer);
            objectList.setSizeFull();
            objectList.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
            objectList.setSelectable(true);
            objectList.addListener(new ItemClickListener() {

                @Override
                public void itemClick(ItemClickEvent event) {
                    if (null == detailsListener)
                        return;

                    JdbcTable item = (JdbcTable) event.getItemId();
                    if (null == item)
                        return;
                    detailsListener.showDetails(createDetails(item));
                }
            });

            vl.addComponent(selectors);
            vl.addComponent(objectList);
            vl.setExpandRatio(objectList, 1f);
            addComponent(vl);
        } catch (SQLException ex) {
        	log.error("failed to create table selector view", ex);
            JdbcUtils.close(connection);
        }
    }

    @Override
    public void detach() {
        JdbcUtils.close(connection);
        super.detach();
    }

    protected Component createSelectors() throws SQLException {
        FormLayout l = new FormLayout();
        l.setWidth("100%");
        l.setSpacing(false);
        l.setMargin(true, false, true, false);

        String catalogTerm = (metadataRetriever != null)?metadataRetriever.getCatalogTerm():"DataBase";
        catalogTerm = catalogTerm.substring(0, 1).toUpperCase() + catalogTerm.substring(1, catalogTerm.length());
        List<String> catalogNames = (metadataRetriever != null)?metadataRetriever.getCatalogs():new ArrayList<String>();
        final ComboBox catalogs = new ComboBox(catalogTerm + ":", catalogNames);
        catalogs.setWidth("100%");
        catalogs.setNullSelectionAllowed(false);
        catalogs.setImmediate(true);
        catalogs.setVisible(!catalogNames.isEmpty());

        String schemaTerm = (metadataRetriever != null)?metadataRetriever.getSchemaTerm().toLowerCase():"DataBase";
        schemaTerm = schemaTerm.substring(0, 1).toUpperCase() + schemaTerm.substring(1, schemaTerm.length());

        List<String> schemaNames = (metadataRetriever != null)?metadataRetriever.getSchemas():new ArrayList<String>();
        final ComboBox schemas = new ComboBox(schemaTerm + ":", schemaNames);
        schemas.setWidth("100%");
        schemas.setNullSelectionAllowed(false);
        schemas.setImmediate(true);
        schemas.setVisible(!schemaNames.isEmpty());

        List<String> tableTypesList = (metadataRetriever != null)?metadataRetriever.getTableTypes():new ArrayList<String>();
        final ComboBox tableTypes = new ComboBox("Object:", tableTypesList);
        if (tableTypesList.contains("TABLE"))
            tableTypes.select("TABLE");
        tableTypes.setWidth("100%");
        tableTypes.setNullSelectionAllowed(false);
        tableTypes.setImmediate(true);

        ValueChangeListener valueChangeListener = new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                updateTableList(catalogs, schemas, tableTypes);
            }
        };

        catalogs.addListener(valueChangeListener);
        schemas.addListener(valueChangeListener);
        tableTypes.addListener(valueChangeListener);

        l.addComponent(catalogs);
        l.addComponent(schemas);
        l.addComponent(tableTypes);
        return l;
    }

    protected IndexedContainer createObjecListContainer() {
        IndexedContainer c = new IndexedContainer();
        c.addContainerProperty(VALUE_PROPERTY, String.class, null);
        return c;
    }

    protected void updateTableList(Property catalogProperty, Property schemaProperty, Property tableTypeProperty) {
        tableListContainer.removeAllItems();

        String catalog = null, schema = null, tableType = null;
        if (null != catalogProperty.getValue())
            catalog = catalogProperty.getValue().toString();
        if (null != schemaProperty.getValue())
            schema = schemaProperty.getValue().toString();
        if (null != tableTypeProperty.getValue())
            tableType = tableTypeProperty.getValue().toString();

        List<JdbcTable> tables;
        try {
            tables = metadataRetriever.getTables(catalog, schema, tableType);
        } catch (Exception ex) {
            getApplication().getMainWindow().showNotification("Error<br/>", ex.getMessage(), Notification.TYPE_ERROR_MESSAGE);
            return;
        }

        for (JdbcTable t : tables)
            tableListContainer.addItem(t).getItemProperty(VALUE_PROPERTY).setValue(t.getName());
    }

    protected Component createDetails(JdbcTable table) {
        TableDetailsView dv = new TableDetailsView(table, connection, dataGenApplication);
        return dv;
    }

    public void setDetailsListener(DetailsListener detailsListener) {
        this.detailsListener = detailsListener;
    }
}
