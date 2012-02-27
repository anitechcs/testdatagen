package com.esspl.datagen.ui;

import org.apache.log4j.Logger;

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
public class DetailsPanel extends CustomComponent {

	private static final Logger log = Logger.getLogger(DetailsPanel.class);
    private final SettingForm form;
    private ListSelect profileListSelect;
    private DataGenApplication dataGenApplication;

    public DetailsPanel(ConnectionProfile profile, ListSelect list, DataGenApplication application) {
    	log.debug("DetailsPanel constructor start");
        profileListSelect = list;
        form = new SettingForm(profile);
        setCompositionRoot(form);
        dataGenApplication = application;
    }

    private class SettingForm extends Form {

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
            
            footer.addComponent(new Button("Test", new Button.ClickListener() {
            	
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
