package com.esspl.datagen.common;

import org.apache.log4j.Logger;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 * @author Tapas
 * 
 * FieldFactory for customizing the fields and adding validators
 */
public class SettingFieldFactory extends DefaultFieldFactory {

	private static final Logger log = Logger.getLogger(SettingFieldFactory.class);
	
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field f;
        if ("password".equals(propertyId)) {
            // Create a password field so the password is not shown
            f = createPasswordField(propertyId);
        }else {
            // Use the super class to create a suitable field base on the property type.
            f = super.createField(item, propertyId, uiContext);
        }

        if ("name".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setRequired(true);
            tf.setRequiredError("Please enter a Name for the profile");
            tf.setWidth("90%");
        }else if("driver".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setRequired(true);
            tf.setRequiredError("Please enter the database driver class name");
            tf.setWidth("90%");
        }else if("url".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setRequired(true);
            tf.setRequiredError("Please enter the database url");
            tf.setWidth("90%");
        }else if("user".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setRequired(true);
            tf.setRequiredError("Please enter the database user name");
            tf.setWidth("90%");
        }else if("password".equals(propertyId)) {
            PasswordField pf = (PasswordField) f;
            pf.setWidth("90%");
        }

        return f;
    }
    
    private PasswordField createPasswordField(Object propertyId) {
        PasswordField pf = new PasswordField();
        pf.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
        return pf;
    }
}