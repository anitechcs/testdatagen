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
