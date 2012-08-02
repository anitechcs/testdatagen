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

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.JdbcTable;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Runo;

/**
 *
 *
 */
public class TableDetailsView extends CustomComponent {

    private final TabSheet tabSheet;

    public TableDetailsView(JdbcTable table, Connection connection, DataGenApplication dataApp) {
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addStyleName(Runo.TABSHEET_SMALL);
        setCompositionRoot(tabSheet);

        TableStructureView tableStructureView = new TableStructureView(table, connection);
        tableStructureView.setSizeFull();

        TableDataView tableContentView = new TableDataView(table, connection, dataApp);
        tableContentView.setSizeFull();

        tabSheet.addTab(tableStructureView);
        tabSheet.addTab(tableContentView);
    }
}
