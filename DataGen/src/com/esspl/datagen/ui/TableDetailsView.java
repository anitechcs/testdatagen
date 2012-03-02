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
