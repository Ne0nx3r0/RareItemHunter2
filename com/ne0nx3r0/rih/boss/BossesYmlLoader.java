package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.entities.BossEntityType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BossesYmlLoader {
    private RareItemHunterPlugin plugin;
    
    public BossesYmlLoader(RareItemHunterPlugin plugin){
        this.plugin = plugin;
    }
    
    public List<BossTemplate> loadBosses(){
        List<BossTemplate> bossTemplates = new ArrayList<>();
        
        File bossesFile = new File(plugin.getDataFolder(),"bosses.yml");

        if(!bossesFile.exists())
        {
            plugin.copy(plugin.getResource("bosses.yml"),bossesFile);
        }
        
        FileConfiguration bossesYml = YamlConfiguration.loadConfiguration(bossesFile);
        
        for(Iterator<String> it = bossesYml.getKeys(false).iterator(); it.hasNext();)
        {
            String bossName = it.next();
            
            ConfigurationSection bossSection = bossesYml.getConfigurationSection(bossName);
            
            String sType = bossSection.getString("type");
            
            BossEntityType bossEntityType;
            
            try{
                bossEntityType = BossEntityType.valueOf(sType);
            }
            catch(IllegalArgumentException ex){
                plugin.getLogger().log(Level.WARNING, "{0} is not a valid boss type for {1}",
                        new Object[]{sType,bossName });
                
                continue;
            }
            
            int hp = bossSection.getInt("hp");

            int attackPower = bossSection.getInt("attackPower");

            int difficulty = bossSection.getInt("difficulty");
            
// Add equipment if it has any
            List<ItemStack> equipment = new ArrayList<>();
            
            if(bossSection.isSet("armor"))
            {
                List<String> bossEquipmentStrings = (List<String>) bossSection.getList("armor");
                
                for(String sItem : bossEquipmentStrings)
                {
                    if(equipment.size() < 4)
                    {
                        ItemStack is = this.getItemStackFromEquipmentString(bossName,sItem);
                        
                        if(is != null)
                        {
                            equipment.add(is);
                        }
                    }
                    else
                    {
                        plugin.getLogger().log(Level.WARNING, "{0} has too many armor items, skipping ''{1}''",
                                new Object[]{bossName, sItem});
                    }
                }
            }
            
// Add weapon if boss has one
            ItemStack weapon = null; 
            
            if(bossesYml.isSet(bossName+".weapon"))
            {
                // Method will return null if invalid, and handle notification of error
                weapon = this.getItemStackFromEquipmentString(bossName,bossSection.getString("weapon"));
            }
            
            bossTemplates.add(new BossTemplate(
                bossName,
                bossEntityType,
                hp,
                attackPower,
                difficulty,
                equipment,
                weapon
            ));
        }
        
        return bossTemplates;
    }
    
// Misc helper methods
    private ItemStack getItemStackFromEquipmentString(String sBossName,String sItem)
    {
        String[] equipValues = sItem.split(" ");
                    
        Material equipMaterial = Material.matchMaterial(equipValues[0]);

        if(equipMaterial != null)
        {
            ItemStack is = new ItemStack(equipMaterial);

            if(equipValues.length > 1)
            {
                for(String sEnchantment : equipValues[1].split(","))
                {
                    String[] enchantmentPair = sEnchantment.split(":");

                    Enchantment en = Enchantment.getByName(enchantmentPair[0]);
                    int level = 0;

                    try
                    {
                        level = Integer.parseInt(enchantmentPair[1]);
                    }
                    catch(Exception e)
                    {
                        plugin.getLogger().log(Level.WARNING,"'"+enchantmentPair[1]+"' is not a valid enchantment level on boss '"+sBossName+"'. Skipping.");

                        return null;
                    }

                    if(en == null)
                    {
                        plugin.getLogger().log(Level.WARNING,"'"+enchantmentPair[0]+"' is not a valid enchantment name on boss '"+sBossName+"'. Skipping.");

                        return null;
                    }

                    is.addEnchantment(en, level);
                }
            }

            return is;
        }
        else
        {
            plugin.getLogger().log(Level.WARNING,"'"+equipValues[0]+"' is not a valid material on boss '"+sBossName+"'. Skipping.");
        }
        
        return null;
    }
}
