package com.esspl.datagen.ui;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.esspl.datagen.common.JdbcTable;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.JdbcUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
public class TableDataView extends CustomComponent {

	private static final Logger log = Logger.getLogger(ExplorerView.class);

    private VerticalLayout tableContainer;
    private HorizontalLayout content;

    public TableDataView(final JdbcTable table, final Connection connection) {
    	log.debug("TableDataView - constructor start");
        setCaption("Data");

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        setCompositionRoot(vl);

        TextField rows = new TextField();
        rows.setWidth("50px");
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
                populateGenerator();
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
        
        tableContainer = new VerticalLayout();
        tableContainer.setSizeFull();
        vl.addComponent(content);
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
    
    protected void populateGenerator(){
    	log.debug("TableDataView - populateGenerator() called");
    	//TODO: logic goes here
    }
}
