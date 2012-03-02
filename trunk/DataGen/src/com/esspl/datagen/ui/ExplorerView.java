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
