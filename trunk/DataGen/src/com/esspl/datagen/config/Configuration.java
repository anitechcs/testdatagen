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
package com.esspl.datagen.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *@author Tapas
 *
 */
@XmlRootElement(name = "datagen-config")
@XmlAccessorType(XmlAccessType.NONE)
public class Configuration {

    @XmlElementWrapper(name = "profiles")
    @XmlElement(name = "profile")
    private List<ConnectionProfile> profiles;

    public void addProfile(ConnectionProfile profile) {
        if (profiles == null){
            profiles = new ArrayList<ConnectionProfile>();
        }
        profiles.add(profile);
    }

    public List<ConnectionProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ConnectionProfile> profiles) {
        this.profiles = profiles;
    }

    public void removeProfile(ConnectionProfile profile) {
        profiles.remove(profile);
    }
}
