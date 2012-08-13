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

import org.apache.log4j.Logger;

import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.DataGenImportUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Tapas
 *
 */
@SuppressWarnings("serial")
public class DataImportView extends VerticalLayout {

	private static final Logger log = Logger.getLogger(DataImportView.class);
	
	public DataImportView(){
		log.debug("DataExportView-> Loading Constructor.");
		
		setWidth("95px");
		Button excelButton = new Button("Excel");
		excelButton.setIcon(DataGenConstant.DATAIMPORT_EXCEL_ICON);
		excelButton.setWidth("95px");
		addComponent(excelButton);
		excelButton.addListener(ClickEvent.class, this, "excelImportButtonClick");
		
		Button sqlButton = new Button("Sql");
		sqlButton.setIcon(DataGenConstant.DATAEXPORT_SQL_ICON);
		sqlButton.setWidth("95px");
		addComponent(sqlButton);
		sqlButton.addListener(ClickEvent.class, this, "sqlImportButtonClick");
		
	}
	
	public void excelImportButtonClick(ClickEvent event) {
		this.getApplication().getMainWindow().showNotification("Excel Import Button Clicked");
		
		//Actual import logic handled through separate utility class
		DataGenImportUtil imp = new DataGenImportUtil();
		imp.doImport(event.getButton().getCaption());
	}
	
	public void sqlImportButtonClick(ClickEvent event) {
		this.getApplication().getMainWindow().showNotification("Sql Import Button Clicked");
		
		//Actual import logic handled through separate utility class
		DataGenImportUtil imp = new DataGenImportUtil();
		imp.doImport(event.getButton().getCaption());
	}
	
}
