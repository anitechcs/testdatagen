package com.esspl.datagen.ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.esspl.datagen.common.JdbcTable;
import com.esspl.datagen.util.JdbcUtils;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 *@author Tapas
 *
 */
public class TableStructureView extends CustomComponent {

	private static final Logger log = Logger.getLogger(TableStructureView.class);
	
    public TableStructureView(JdbcTable table, Connection connection) {
        setCaption("Structure");
        try {
            ResultSet attributes = connection.getMetaData().getColumns(table.getCatalog(), table.getSchema(), table.getName(), null);
            SelectableResultSetTable resultSetTable = new SelectableResultSetTable(attributes, Arrays.asList("CHECK", "COLUMN_NAME", "TYPE_NAME", "COLUMN_SIZE", "DECIMAL_DIGITS", "IS_NULLABLE", "IS_AUTOINCREMENT", "COLUMN_DEF", "REMARKS"));
            resultSetTable.setSizeFull();
            setCompositionRoot(resultSetTable);
            JdbcUtils.close(attributes);
        } catch (Exception ex) {
            setCompositionRoot(new Label(ex.getMessage()));
        }
    }
}
