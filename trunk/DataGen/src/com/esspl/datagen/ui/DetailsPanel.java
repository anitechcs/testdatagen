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
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.common.SettingFieldFactory;
import com.esspl.datagen.common.SettingsManager;
import com.esspl.datagen.config.ConnectionProfile;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * @author Tapas
 * 
 */
@SuppressWarnings("serial")
public class DetailsPanel extends CustomComponent {

	private static final Logger log = Logger.getLogger(DetailsPanel.class);
    public final SettingForm form;
    private ListSelect profileListSelect;
    private DataGenApplication dataGenApplication;

    public DetailsPanel(ConnectionProfile profile, ListSelect list, DataGenApplication application, ContextHelp contextHelp) {
    	log.debug("DetailsPanel constructor start");
        profileListSelect = list;
        form = new SettingForm(profile);
      
        //Add help text here for each field
        contextHelp.setFollowFocus(true);
        contextHelp.addHelpForComponent(form.getField("name"), "Name help text goes here...");
        contextHelp.addHelpForComponent(form.getField("driver"), "Driver help text goes here...");
        contextHelp.addHelpForComponent(form.getField("url"), "Url help text goes here...");
        contextHelp.addHelpForComponent(form.getField("user"), "User help text goes here...");
        contextHelp.addHelpForComponent(form.getField("password"), "Password help text goes here...");
        
        setCompositionRoot(form);
        dataGenApplication = application;
    }

    public class SettingForm extends Form {

        private SettingForm(ConnectionProfile profile) {
        	log.debug("SettingForm constructor start");
            setSizeFull();
            setCaption(profile.getName());
            setWriteThrough(false);
            setInvalidCommitted(false);

            setFormFieldFactory(new SettingFieldFactory());
            setItemDataSource(new BeanItem<ConnectionProfile>(profile));
            setVisibleItemProperties(new String[]{"name", "driver", "url", "user", "password"});
            
            HorizontalLayout footer = new HorizontalLayout();
            footer.setSpacing(true);
            footer.addComponent(new Button("Apply", new Button.ClickListener() {
            	
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                    	form.commit();
                    	profileListSelect.requestRepaint();
                    } catch (Exception e) {
                        if (e instanceof ErrorMessage) {
                            getWindow().showNotification("Check required values", Window.Notification.TYPE_WARNING_MESSAGE);
                        }
                    }
                }
            }));
            
            footer.addComponent(new Button("Remove", new Button.ClickListener() {
            	
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    ConnectionProfile value = (ConnectionProfile) profileListSelect.getValue();
                    if (value != null) {
                        SettingsManager.get().getConfiguration().removeProfile(value);
                        profileListSelect.getContainerDataSource().removeItem(value);
                        profileListSelect.select(null);
                    } else {
                        getWindow().showNotification("Select some profile to remove it");
                    }
                }
            }));
            
            footer.addComponent(new Button("Test Connection", new Button.ClickListener() {
            	
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                    	dataGenApplication.databaseSessionManager.test(form.getItemProperty("driver").getValue().toString(), form.getItemProperty("url").getValue().toString(), form.getItemProperty("user").getValue().toString(), form.getItemProperty("password").getValue().toString());
                        getApplication().getMainWindow().showNotification("Test successful");
                    } catch (Exception e) {
                    	log.error("connection test failed", e);
                        getApplication().getMainWindow().showNotification("Test failed<br/>", e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
                    }
                }
            }));

            setFooter(footer);
        }

    }
}
