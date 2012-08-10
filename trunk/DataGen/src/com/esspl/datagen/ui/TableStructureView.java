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

	private static final long serialVersionUID = 1L;
	
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
        	log.error("Exception Occoured at TableStructureView.");
            setCompositionRoot(new Label(ex.getMessage()));
        }
    }
}
