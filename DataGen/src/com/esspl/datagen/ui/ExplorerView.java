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

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.DatabaseSessionManager;
import com.esspl.datagen.common.DetailsListener;
import com.esspl.datagen.config.ConnectionProfile;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;

/**
 * @author Tapas
 * 
 */
public class ExplorerView extends CustomComponent {
	
	private static final Logger log = Logger.getLogger(ExplorerView.class);
	private DataGenApplication dataGenApplication;
	
	public ExplorerView(final DataGenApplication dataApp, DatabaseSessionManager databaseSessionManager){
		log.debug("ExplorerView - constructor start");
		dataGenApplication = dataApp;
       
		setCaption("Tables");
        setSizeFull();
        final HorizontalSplitPanel sp = new HorizontalSplitPanel();
        sp.setSizeFull();
        sp.setLocked(true);
        
        TableSelectorView tableSelectorView = new TableSelectorView(databaseSessionManager, dataApp);
        tableSelectorView.setDetailsListener(new DetailsListener() {

            @Override
            public void showDetails(Component component) {
                component.setSizeFull();
                sp.setSecondComponent(component);
            }
        });

        sp.setFirstComponent(tableSelectorView);
        sp.setSplitPosition(210, UNITS_PIXELS);
        setCompositionRoot(sp);
	}
}
