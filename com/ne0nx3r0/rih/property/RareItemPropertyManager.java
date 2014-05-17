package com.ne0nx3r0.rih.property;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import java.util.List;

public class RareItemPropertyManager {
    private final RareItemHunterPlugin plugin;
    private final List<RareItemProperty> properties;

    public RareItemPropertyManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
        PropertiesYmlLoader loader = new PropertiesYmlLoader(plugin);
        
        properties = loader.loadProperties();
    }

    public RareItemProperty getProperty(String propertyName) {
        propertyName = propertyName.toLowerCase();
        
        for(RareItemProperty rip : this.properties){
            System.out.println(rip.getName().toLowerCase()+" "+propertyName);
            if(rip.getName().toLowerCase().equals(propertyName)){
                return rip;
            }
        }
        return null;
    }

    public RareItemProperty getProperty(int id) {
        for(RareItemProperty rip : this.properties){
            if(rip.getID() == id){
                return rip;
            }
        }
        return null;
    }
    
}
