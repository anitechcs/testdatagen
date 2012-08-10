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
package com.esspl.datagen.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.esspl.datagen.config.Configuration;
import com.esspl.datagen.config.ConnectionProfile;
import com.google.common.io.Files;

/**
 *@author Tapas
 *
 */
public class SettingsManager {

	private static final Logger log = Logger.getLogger(SettingsManager.class);
	
    private static final String FILE_PATH = "DataGen" + File.separator + "config";
    private static final File SETTINGS_DIR = new File(System.getProperty("user.home") + File.separator + FILE_PATH);
    private static final File SETTINGS_FILE = new File(SETTINGS_DIR, "datagen-settings.xml");

    private Configuration configuration;
    private static final SettingsManager INSTANCE = new SettingsManager();

    public static SettingsManager get() {
        return INSTANCE;
    }

    public synchronized Configuration getConfiguration() {
        if (null == configuration) {
            if (!SETTINGS_FILE.exists())
                configuration = createDefaultConfiguration();
            else{
            	try {
					JAXBContext jc = JAXBContext.newInstance(Configuration.class);
					Unmarshaller um = jc.createUnmarshaller();
					configuration = (Configuration)um.unmarshal(SETTINGS_FILE);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
            }
        }
        return configuration;
    }

    public synchronized void persistConfiguration() {
        try {
            Files.createParentDirs(SETTINGS_FILE);
            OutputStream out = new FileOutputStream(SETTINGS_FILE);
            JAXBContext jc = JAXBContext.newInstance(Configuration.class);
            Marshaller m = jc.createMarshaller();
            m.marshal( configuration, out );
            out.close();
        } catch (Exception ex) {
        	log.error("failed to create settings file", ex);
        }
    }

    private Configuration createDefaultConfiguration() {
        Configuration conf = new Configuration();
        conf.addProfile(new ConnectionProfile("H2 in memory", "org.h2.Driver", "jdbc:h2:mem:db", "sa", ""));
        return conf;
    }

}
