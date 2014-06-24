package com.ne0nx3r0.rih.property;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.properties.*;
import java.io.File;
import java.io.IOException;
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

        this.addPropertyToAllProperties(new Backstab());
        this.addPropertyToAllProperties(new Blinding());
        this.addPropertyToAllProperties(new BuildersWand());
        this.addPropertyToAllProperties(new Burst());
        this.addPropertyToAllProperties(new CallLightning());
        this.addPropertyToAllProperties(new CatsFeet());
        this.addPropertyToAllProperties(new Confuse());
        this.addPropertyToAllProperties(new CraftItem());
        this.addPropertyToAllProperties(new Disarm());
        this.addPropertyToAllProperties(new Durability());
        this.addPropertyToAllProperties(new Fertilize());
        this.addPropertyToAllProperties(new FireHandling());
        this.addPropertyToAllProperties(new FireResistance());
        this.addPropertyToAllProperties(new FlameFX());
        this.addPropertyToAllProperties(new GreaterBurst());
        this.addPropertyToAllProperties(new GrowTree());
        this.addPropertyToAllProperties(new HalfBakedIdea());
        this.addPropertyToAllProperties(new Hardy());
        this.addPropertyToAllProperties(new Haste());
        this.addPropertyToAllProperties(new HeartFX());
        this.addPropertyToAllProperties(new Invisibility());
        this.addPropertyToAllProperties(new MagicBag());
        this.addPropertyToAllProperties(new MeltObsidian());
        this.addPropertyToAllProperties(new MermaidsGift());
        this.addPropertyToAllProperties(new PaintWool());
        this.addPropertyToAllProperties(new Poison());
        this.addPropertyToAllProperties(new Pull());
        this.addPropertyToAllProperties(new Rage());
        this.addPropertyToAllProperties(new RainbowFuryFX());
        this.addPropertyToAllProperties(new Regeneration());
        this.addPropertyToAllProperties(new RepairItem());
        this.addPropertyToAllProperties(new Slow());
        this.addPropertyToAllProperties(new Smelt());
        this.addPropertyToAllProperties(new Spore());
        this.addPropertyToAllProperties(new Strength());
        this.addPropertyToAllProperties(new SummonBat());
        this.addPropertyToAllProperties(new SummonChicken());
        this.addPropertyToAllProperties(new SummonCow());
        this.addPropertyToAllProperties(new SummonMooshroom());
        this.addPropertyToAllProperties(new SummonOcelot());
        this.addPropertyToAllProperties(new SummonPig());
        this.addPropertyToAllProperties(new SummonSheep());
        this.addPropertyToAllProperties(new SummonSlime());
        this.addPropertyToAllProperties(new ToughLove());
        this.addPropertyToAllProperties(new VampiricRegeneration());
        this.addPropertyToAllProperties(new WaterBreathing());
        this.addPropertyToAllProperties(new Weaken());
    }
    
    public final void addPropertyToAllProperties(RareItemProperty rip){
        this.allProperties.put(rip.getName().toLowerCase(), rip);
    }

    List<RareItemProperty> loadProperties() {
        List<RareItemProperty> availableProperties = new ArrayList<>();

        File propertiesFile = new File(plugin.getDataFolder(),"properties.yml");

        if(!propertiesFile.exists()){
            plugin.copy(plugin.getResource("properties.yml"),propertiesFile);
        }
        
        FileConfiguration yml = YamlConfiguration.loadConfiguration(propertiesFile);
        
        for(RareItemProperty rip : this.allProperties.values()){
            String sID = String.valueOf(rip.getID());
            
            if(!yml.isSet(sID)){
                yml.set(sID+".name", rip.getName());
                yml.set(sID+".enabled", true);
                yml.set(sID+".costType", rip.getCostType().name());
                yml.set(sID+".costOrDuration", rip.getCost());
                
                availableProperties.add(rip);
            }
            else {
                ConfigurationSection propertySection = yml.getConfigurationSection(sID);

                if(propertySection.getBoolean("enabled",false)){
                    String sCostType = propertySection.getString("costType");
                    PropertyCostType costType;

                    try{
                        costType = PropertyCostType.valueOf(sCostType);
                    }
                    catch(Exception ex){
                        plugin.getLogger().log(Level.WARNING, "Disabling property {0} because of invalid cost type: {1}", new Object[]{rip.getName(), sCostType});

                        continue;
                    }

                    double cost = propertySection.getDouble("costOrDuration",-1);

                    if(cost == -1){
                        plugin.getLogger().log(Level.WARNING, "Disabling property {0} because costOrDuration was not specified.", new Object[]{rip.getName()});

                        continue;
                    }

                    rip.setCostType(costType);
                    
                    rip.setCost(cost);

                    availableProperties.add(rip);
                }
                else {
                    plugin.getLogger().log(Level.WARNING, "Skipping property: {0} (disabled)", new Object[]{rip.getName()});
                }
            }
            
            if(yml.isSet(sID+".recipe")){
                List<String> recipe = yml.getStringList(sID+".recipe");
                
                rip.setRecipe(recipe);
                
                this.plugin.getRecipeManager().loadRecipe(rip,recipe);
            }
        }
        try {
            yml.save(propertiesFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Unable to save changes to properties.yml!");
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
        
        return availableProperties;
    }
}
