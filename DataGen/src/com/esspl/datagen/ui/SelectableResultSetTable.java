package com.esspl.datagen.ui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.esspl.datagen.util.DataGenConstant;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;

/**
 *@author Tapas
 *
 */
public class SelectableResultSetTable extends CustomComponent {

	private static final Logger log = Logger.getLogger(SelectableResultSetTable.class);
    private final Table table;
    private boolean isChecked = true;

    public SelectableResultSetTable(ResultSet resultSet) {
        this(resultSet, null);
    }

    public SelectableResultSetTable(ResultSet resultSet, List<String> columns) {
        setSizeFull();

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        setCompositionRoot(vl);

        Component content;
        try {
            content = createResultsTable(resultSet, columns);
        } catch (Exception ex) {
            content = new Label("Failed to process supplied result set: " + ex.getMessage());
        }

        vl.addComponent(content);

        if (content instanceof Table)
            table = (Table) content;
        else
            table = null;
    }

    public Collection<?> getItemIds() {
        return null != table ? table.getItemIds() : Collections.emptyList();
    }

    protected Table createResultsTable(ResultSet rs, List<String> columns) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        Container c = new IndexedContainer();
        c.addContainerProperty(" ", CheckBox.class, null);
        for (int i = 0; i < md.getColumnCount(); ++i) {
            String label = md.getColumnLabel(i + 1);
            if (null == columns || columns.contains(label)) {
                Class<?> clazz = getClassForSqlType(md.getColumnClassName(i + 1));
                c.addContainerProperty(label, clazz, null);
            }
        }

        int i = 0;
        while (rs.next()) {
            Item item = c.addItem(i++);
            int j = 0;
            for (Object propertyId : c.getContainerPropertyIds()) {
            	if(j == 0){
            		CheckBox cb = new CheckBox();
            		cb.setValue(true);
            		item.getItemProperty(propertyId).setValue(cb);
            	}else{
            		item.getItemProperty(propertyId).setValue(rs.getObject(propertyId.toString()));
            	}
            	j++;
            }
        }

        final Table t = new Table(null, c);
        t.setSizeFull();
        t.setPageLength(50);
        t.setColumnReorderingAllowed(true);
        t.setColumnCollapsingAllowed(true);
        t.setSelectable(true);
        t.setColumnIcon(" ", DataGenConstant.CHECK_ALL);
        //Handle the header checkbox selector click
        t.addListener(new HeaderClickListener() {
        	
        	@Override
	        public void headerClick(HeaderClickEvent event) {
		        String column = (String) event.getPropertyId();
		        if(column.equals(" ")){
		        	log.debug("SelectableResultSetTable - createResultsTable() checkbox states-"+isChecked);
		        	isChecked = (isChecked)?false:true;
		        	Iterator iter = t.getItemIds().iterator();
		        	while(iter.hasNext()){
		        		int row = (Integer)iter.next();
		        		Item item = t.getItem(row);
		        		CheckBox cbx = (CheckBox)(item.getItemProperty(" ").getValue());
		        		cbx.setValue(isChecked);
		        	}
		        }
	        }
        });
        return t;
    }

    protected Class<?> getClassForSqlType(String name) {
        try {
            return Class.forName(name);
        } catch (Exception ex) {
            log.error("unable to get class for name " + name, ex);
            return null;
        }
    }
}
