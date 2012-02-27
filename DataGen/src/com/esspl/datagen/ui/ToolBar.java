package com.esspl.datagen.ui;

import java.util.List;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.SettingsManager;
import com.esspl.datagen.config.ConnectionProfile;
import com.esspl.datagen.util.DataGenConstant;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

/**
 * Aplication Toolbar.
 *
 * @author Tapas
 *
 */
public class ToolBar extends HorizontalLayout {
	
	private static final Logger log = Logger.getLogger(DataGenApplication.class);
	private Button connect;
	private Button disConnect;
	private Select profiles;
	
	public ToolBar(final DataGenApplication dataGenApplication){
		log.debug("ToolBar constructor start");
		List<ConnectionProfile> profileList = SettingsManager.get().getConfiguration().getProfiles();
		profiles = new Select(null, new BeanItemContainer<ConnectionProfile>(ConnectionProfile.class, profileList));
		profiles.setImmediate(true);
        profiles.setNewItemsAllowed(false);
        profiles.setNullSelectionAllowed(false);
		addComponent(profiles);
        setComponentAlignment(profiles, Alignment.MIDDLE_RIGHT);
        
        //Connect button
        connect = new Button("Connect", new ClickListener() {
        	
        	@Override
            public void buttonClick(ClickEvent event) {
            	log.debug("ToolBar - Connect button clicked");
            	ConnectionProfile cp = (ConnectionProfile)profiles.getValue();
            	if(cp == null){
            		getApplication().getMainWindow().showNotification("Select a Connection profile to connect.", Notification.TYPE_ERROR_MESSAGE);
                    return;
            	}
                try {
                	dataGenApplication.databaseSessionManager.connect(cp);
                } catch (Exception ex) {
                    getApplication().getMainWindow().showNotification("Failed to connect<br/>", ex.getMessage(), Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                profiles.setVisible(false);
            	connect.setVisible(false);
            	disConnect.setVisible(true);
            	dataGenApplication.connectedString.setVisible(true);
            	dataGenApplication.connectedString.setValue("Connected to - "+cp.getName());
            	boolean isExplorerSelected = dataGenApplication.tabSheet.getSelectedTab() instanceof ExplorerView;
            	dataGenApplication.tabSheet.removeComponent(dataGenApplication.explorer);
            	dataGenApplication.explorer  = new ExplorerView(dataGenApplication, dataGenApplication.databaseSessionManager);
            	dataGenApplication.tabSheet.addTab(dataGenApplication.explorer, "Explorer", DataGenConstant.EXPLORER_ICON, 2);
            	if(isExplorerSelected){
            		dataGenApplication.tabSheet.setSelectedTab(dataGenApplication.explorer);
            	}
            }
        });
        connect.setIcon(DataGenConstant.DB_CONNECT_ICON);
        addComponent(connect);
        setComponentAlignment(connect, Alignment.MIDDLE_RIGHT);
        
        //Disconnect button
        disConnect = new Button("Disconnect", new ClickListener() {
        	
        	@Override
            public void buttonClick(ClickEvent event) {
            	log.debug("ToolBar - Disconnect button clicked");
            	dataGenApplication.databaseSessionManager.disconnect();
            	profiles.setVisible(true);
            	connect.setVisible(true);
            	disConnect.setVisible(false);
            	dataGenApplication.connectedString.setVisible(false);
            	dataGenApplication.connectedString.setValue("");
            	boolean isExplorerSelected = dataGenApplication.tabSheet.getSelectedTab() instanceof ExplorerView;
            	dataGenApplication.tabSheet.removeComponent(dataGenApplication.explorer);
            	dataGenApplication.explorer = new ExplorerView(dataGenApplication, dataGenApplication.databaseSessionManager);
            	dataGenApplication.tabSheet.addTab(dataGenApplication.explorer, "Explorer", DataGenConstant.EXPLORER_ICON, 2);
            	if(isExplorerSelected){
            		dataGenApplication.tabSheet.setSelectedTab(dataGenApplication.explorer);
            	}
            }
        });
        disConnect.setIcon(DataGenConstant.DB_DISCONNECT_ICON);
        disConnect.setVisible(false);
        addComponent(disConnect);
        setComponentAlignment(disConnect, Alignment.MIDDLE_RIGHT);
        
        //Settings button
        Button settings = new Button("Settings", new ClickListener() {
        	
        	@Override
            public void buttonClick(ClickEvent event) {
            	log.debug("ToolBar - Settings button clicked");
            	SettingsView settingsManagerView = new SettingsView(dataGenApplication);
            	getWindow().addWindow(settingsManagerView);
            }
        });
        settings.setIcon(DataGenConstant.DB_SETTING_ICON);
        addComponent(settings);
        setComponentAlignment(settings, Alignment.MIDDLE_RIGHT);
        
        //Reload button
        Button reset = new Button("Reset", new ClickListener() {
        	
        	@Override
            public void buttonClick(ClickEvent event) {
            	log.debug("ToolBar - Reset button clicked");
            	dataGenApplication.getMainWindow().getApplication().close();
            }
        });
        reset.setIcon(DataGenConstant.RESET);
        addComponent(reset);
        setComponentAlignment(reset, Alignment.MIDDLE_RIGHT);
        log.debug("ToolBar constructor end");
	}
	
	/**
	 * Reloads the Connection profile dropdown values from xml
	 */
	public void reloadConnectionProfile(){
		log.debug("ToolBar - reloadConnectionProfile() called");
		List<ConnectionProfile> profileList = SettingsManager.get().getConfiguration().getProfiles();
		profiles.setContainerDataSource(new BeanItemContainer<ConnectionProfile>(ConnectionProfile.class, profileList));
	}
}
