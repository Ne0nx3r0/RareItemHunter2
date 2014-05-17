package com.ne0nx3r0.rih.property;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.properties.Fertilize;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

class PropertiesYmlLoader {
    private final RareItemHunterPlugin plugin;
    private final Map<String,RareItemProperty> allProperties;

    PropertiesYmlLoader(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
        this.allProperties = new HashMap<>();
        
        this.addAvailableProperty(new Fertilize());
    }
    
    public final void addAvailableProperty(RareItemProperty rip){
        this.allProperties.put(rip.getName().toLowerCase(), rip);
    }

    List<RareItemProperty> loadProperties() {
        List<RareItemProperty> availableProperties = new ArrayList<>();

        File propertiesFile = new File(plugin.getDataFolder(),"properties.yml");

        if(!propertiesFile.exists()){
            plugin.copy(plugin.getResource("properties.yml"),propertiesFile);
        }
        
        FileConfiguration propertiesYml = YamlConfiguration.loadConfiguration(propertiesFile);

        for(String propertyName : propertiesYml.getKeys(false)){
            RareItemProperty rip = this.allProperties.get(propertyName.toLowerCase());
            
            if(rip == null){
                plugin.getLogger().log(Level.WARNING, "Invalid property name: {0}", new Object[]{propertyName});
                    
                continue;
            }
            
            ConfigurationSection propertySection = propertiesYml.getConfigurationSection(propertyName);
      
            if(!propertySection.getBoolean("enabled",false)){
                plugin.getLogger().log(Level.WARNING, "Skipping property: {0} (disabled)", new Object[]{propertyName});
                
                continue;
            }
            
            String sCostType = propertySection.getString("costType");
            RareItemPropertyCostType costType;
            
            try{
                costType = RareItemPropertyCostType.valueOf(sCostType);
            }
            catch(Exception ex){
                plugin.getLogger().log(Level.WARNING, "Ignoring {0} because of invalid cost type: {1}", new Object[]{propertyName, sCostType});
                
                continue;
            }
            
            double cost;
            
            if(costType.equals(RareItemPropertyCostType.COOLDOWN)){
                cost = propertySection.getDouble("duration",-1);
                
                if(cost == -1){
                    plugin.getLogger().log(Level.WARNING, "Ignoring {0} because duration was not specified.", new Object[]{propertyName});
                    
                    continue;
                }
            }
            else{
                cost = propertySection.getDouble("cost",-1); 
                
                if(cost == -1){
                    plugin.getLogger().log(Level.WARNING, "Ignoring {0} because cost was not specified.", new Object[]{propertyName});
                    
                    continue;
                }
            }
            
            rip.setCostType(costType);
            rip.setCost(cost);
            System.out.println(rip.getName()+" added");
            availableProperties.add(rip);
        }
        
        return availableProperties;
    }
}
