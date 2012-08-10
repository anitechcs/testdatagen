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

import java.util.List;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.SettingsManager;
import com.esspl.datagen.config.ConnectionProfile;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

/**
 * @author Tapas
 * 
 */
@SuppressWarnings("serial")
public class SettingsView extends Window {

	private static final Logger log = Logger.getLogger(SettingsView.class);
	
    private ListSelect list;
    private Panel panel;
    private DetailsPanel details;
    private DataGenApplication dataGenApplication;

    public SettingsView(DataGenApplication application) {
        setModal(true);
        setResizable(false);
        setCaption("Settings");
        setWidth("620px");
        setHeight("440px");
        dataGenApplication = application;

        GridLayout layout = createMainLayout();
        setContent(layout);

        Component bottom = createBottomBar(dataGenApplication);
        layout.addComponent(bottom, 0, 1);
        layout.setComponentAlignment(bottom, Alignment.MIDDLE_RIGHT);
        layout.setRowExpandRatio(0, 1);

        refreshDetails();
    }

    private void refreshDetails() {
    	log.debug("SettingsView->refreshDetails()-----Refreshing page.");
        panel.removeAllComponents();

        ConnectionProfile profile = (ConnectionProfile) list.getValue();
        if (profile == null) {
            return;
        }

        details = createDetails(profile);
        details.setSizeFull();
        panel.addComponent(details);
    }

    private DetailsPanel createDetails(ConnectionProfile profile) {
    	log.debug("SettingsView->createDetails()-----Creating Detail Section.");
        DetailsPanel detailsPanel = new DetailsPanel(profile, list, dataGenApplication);
        return detailsPanel;
    }

    private GridLayout createMainLayout() {
    	log.debug("SettingsView->createMainLayout()-----Creating Main Layout.");
        GridLayout layout = new GridLayout(1, 2);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addComponent(createWorkArea(), 0, 0);
        layout.setSizeFull();
        return layout;
    }

    private Component createWorkArea() {
        HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setSizeFull();
        split.setSplitPosition(30);

        List<ConnectionProfile> profiles = SettingsManager.get().getConfiguration().getProfiles();
        list = new ListSelect(null, new BeanItemContainer<ConnectionProfile>(ConnectionProfile.class, profiles));
        list.setSizeFull();
        list.setNullSelectionAllowed(false);
        list.setImmediate(true);
        if (!profiles.isEmpty()) {
            list.select(profiles.get(0));
        }

        list.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                refreshDetails();
            }
        });

        panel = new Panel();
        panel.setSizeFull();

        split.addComponent(list);
        split.addComponent(panel);

        return split;
    }

    private Component createBottomBar(final DataGenApplication dataGenApplication) {
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.setWidth("100%");
        bottom.setSpacing(true);

        Button addButton = new Button("Add", new Button.ClickListener() {
        	
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ConnectionProfile profile = new ConnectionProfile("New Profile", "", "", "", "");
                SettingsManager.get().getConfiguration().addProfile(profile);
                list.getContainerDataSource().addItem(profile);
                list.select(profile);
            }
        });

        Button saveProfilesButton = new Button("Save Profiles", new Button.ClickListener() {
        	
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SettingsManager.get().persistConfiguration();
            }
        });

        Button closeButton = new Button("Close", new Button.ClickListener() {
        	
            @Override
            public void buttonClick(Button.ClickEvent event) {
            	dataGenApplication.toolbar.reloadConnectionProfile();
                close();
            }
        });

        bottom.addComponent(addButton);
        bottom.addComponent(saveProfilesButton);
        bottom.addComponent(closeButton);

        bottom.setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);
        bottom.setExpandRatio(closeButton, 1);

        return bottom;
    }
}
