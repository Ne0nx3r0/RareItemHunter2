package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.skills.*;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import com.ne0nx3r0.rih.boss.entities.BossEntityType;
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
    private final RareItemHunterPlugin plugin;
    
    // we load the skills here so that only the ones which are used are kept in memory
    // Small gain I guess, but we don't need unused ones around and we only use once instance of each skill
    private final Map<String,BossSkillTemplate> availableSkills;

    public BossesYmlLoader(RareItemHunterPlugin plugin){
        this.plugin = plugin;
        
        availableSkills = new HashMap<>();
        
        this.addSkill(new Blink());
        this.addSkill(new Burst());
        this.addSkill(new Disarm());
        this.addSkill(new Disorient());
        this.addSkill(new FakeWeb());
        this.addSkill(new Freeze());
        this.addSkill(new GreaterBurst());
        this.addSkill(new JumpAttack());
        this.addSkill(new LightningBolt());
        this.addSkill(new LightningStorm());
        this.addSkill(new PoisonDart());
        this.addSkill(new Pull());
        this.addSkill(new ShootArrow());
        this.addSkill(new ShootFireball());
        this.addSkill(new SpawnCaveSpider());
        this.addSkill(new SpawnCreeper());
        this.addSkill(new SpawnSilverfish());
        this.addSkill(new SpawnSkeleton());
        this.addSkill(new SpawnSpider());
        this.addSkill(new SpawnZombie());
        this.addSkill(new Shiver());
    }
    
    public final void addSkill(BossSkillTemplate skill){
        this.availableSkills.put(skill.getName(),skill);        
    }
    
    public List<BossTemplate> loadBosses(){

        List<BossTemplate> bossTemplates = new ArrayList<>();
        
        File bossesFile = new File(plugin.getDataFolder(),"bosses.yml");

        if(!bossesFile.exists()){
            plugin.copy(plugin.getResource("bosses.yml"),bossesFile);
        }
        
        FileConfiguration bossesYml = YamlConfiguration.loadConfiguration(bossesFile);
        
        for(Iterator<String> it = bossesYml.getKeys(false).iterator(); it.hasNext();){
            String bossName = it.next();
            
            ConfigurationSection bossSection = bossesYml.getConfigurationSection(bossName);
            
            String sType = bossSection.getString("type");
            
            BossEntityType bossEntityType;
            
            try{
                bossEntityType = BossEntityType.valueOf(sType.toUpperCase());
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
            
            if(bossSection.isSet("weapon"))
            {
                // Method will return null if invalid, and handle notification of error
                weapon = this.getItemStackFromEquipmentString(bossName,bossSection.getString("weapon"));
            }
            
            BossTemplate bt = new BossTemplate(
                bossName,
                bossEntityType,
                hp,
                attackPower,
                difficulty,
                equipment,
                weapon
            );
            
            bossTemplates.add(bt);
            
// Load onHit skills
            for(String skillString : bossSection.getStringList("onHit")){
                // - 30% chance Disorient level 5

                String sSkillName = skillString.substring(skillString.indexOf("chance ")+7, skillString.indexOf(" level"));
                
                BossSkillTemplate bst = this.availableSkills.get(sSkillName);
                
                if(bst == null){
                    plugin.getLogger().log(Level.WARNING, "{0} is not a valid skill on boss {1}", new Object[]{sSkillName, bossName});
                    
                    continue;
                }
                
                String sLevel = skillString.substring(skillString.lastIndexOf(" ")+1);
                int level;
                
                try{
                    level = Integer.parseInt(sLevel);
                }
                catch(NumberFormatException ex){
                    plugin.getLogger().log(Level.WARNING, "{0} is not a valid level for {1} on boss {2}", new Object[]{sLevel, sSkillName, bossName});
                    
                    continue;
                }
                
                String sChance = skillString.substring(0, skillString.indexOf("%"));
                int chance;
                
                try{
                    chance = Integer.parseInt(sChance);
                }
                catch(NumberFormatException ex){
                    plugin.getLogger().log(Level.WARNING, "{0} is not a valid chance % for {1} on boss {2}", new Object[]{sLevel, sSkillName, bossName});
                    
                    continue;
                }
                
                bt.addOnHitSkill(null, level, chance);
            }
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
