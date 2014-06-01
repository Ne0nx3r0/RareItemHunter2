package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class BossPersistence {
    private final RareItemHunterPlugin plugin;
    private BossManager bossManager;

    public BossPersistence(RareItemHunterPlugin plugin) {    
        this.plugin = plugin;
    }

    BossPersistence(RareItemHunterPlugin plugin, BossManager bm) {
        this.plugin = plugin;
        this.bossManager = bm;
    }
    
    public List<Boss> loadActiveBosses(){
        File ymlFile = new File(plugin.getDataFolder(),"save.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);
        
        List<Boss> bosses = new ArrayList<>();
        
        for(String sUuid : config.getKeys(false)){
            ConfigurationSection configBoss = config.getConfigurationSection(sUuid);
            
            String sTemplate = configBoss.getString("template");
            BossTemplate template = this.bossManager.getBossTemplate(sTemplate);

            if(template == null){
                plugin.getLogger().log(Level.WARNING, "Skipping boss with UUID {0} because of invalid template: {1}", new Object[]{sUuid, sTemplate});
            
                continue;
            }
            
            int currentHealth = configBoss.getInt("currentHealth",template.getMaxHealth());
            int kills = configBoss.getInt("template",0);
            
            ConfigurationSection configPlayerDamage = configBoss.getConfigurationSection("playerDamage");
        
            Map<String,Integer> playerDamage = new HashMap<>();
            
            for(Entry<String,Object> pd : configPlayerDamage.getValues(true).entrySet()){
                playerDamage.put(pd.getKey(), Integer.parseInt(pd.getValue().toString()));
            }
            
            bosses.add(new Boss(
                    UUID.fromString(sUuid),
                    template,
                    currentHealth,
                    kills,
                    playerDamage
            ));
        }
        
        return bosses;
    }
    
    public void startSaving(int saveInterval){
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable(){

            @Override
            public void run() {
                File ymlFile = new File(plugin.getDataFolder(),"save.yml");
                
                YamlConfiguration ymlConfig = YamlConfiguration.loadConfiguration(ymlFile);
                
                for(Boss boss : plugin.getBossManager().getAllActiveBosses()){
                    String uuid = boss.getUniqueID().toString();
                    
                    ymlConfig.set(uuid+".template", boss.getTemplate().getName());
                    ymlConfig.set(uuid+".currentHealth", boss.getHealth());
                    ymlConfig.set(uuid+".kills", boss.getKills());
                    ymlConfig.set(uuid+".playerDamage", boss.getPlayersDamageDone());
                }
                
                try {
                    ymlConfig.save(ymlFile);
                } 
                catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Unable to save bosses to {0}!", ymlFile.getName());
                    
                    plugin.getLogger().log(Level.SEVERE, null, ex);
                }
            }
        }, saveInterval, saveInterval);
    }
}
