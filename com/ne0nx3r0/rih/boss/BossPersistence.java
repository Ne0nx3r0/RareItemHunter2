package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.egg.BossEgg;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;

public class BossPersistence {
    private final RareItemHunterPlugin plugin;
    private final BossManager bossManager;
    private List<Boss> activeBosses;
    private List<BossEgg> activeEggs;

    BossPersistence(RareItemHunterPlugin plugin, BossManager bm) {
        this.plugin = plugin;
        this.bossManager = bm;
    }

    void loadBossesAndEggs() {
        File ymlFile = new File(plugin.getDataFolder(),"save.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);
        
        this.activeEggs = new ArrayList<>();
        this.activeBosses = new ArrayList<>();
        
        if(config.isSet("eggs")){
            List<Map<?, ?>> eggsSave = config.getMapList("eggs");
            
            for(Map<?, ?> eggSave : eggsSave){
                String sTemplate = eggSave.get("template").toString();
                BossTemplate template = this.bossManager.getBossTemplate(sTemplate);

                if(template == null){
                    plugin.getLogger().log(Level.WARNING, "Skipping egg because of invalid template: {1}", new Object[]{sTemplate});

                    continue;
                }
                
                boolean autoSpawn = eggSave.get("autospawn").toString().equals("true");
                
                int x = Integer.parseInt(eggSave.get("x").toString());
                int y = Integer.parseInt(eggSave.get("y").toString());
                int z = Integer.parseInt(eggSave.get("z").toString());
                
                String worldName = eggSave.get("world").toString();
                
                Location l = new Location(this.plugin.getServer().getWorld(worldName),x,y,z);
                
                this.activeEggs.add(new BossEgg(template,l,autoSpawn));
            }
        }
        
        if(config.isSet("bosses")){
            Map<UUID,Boss> tempActiveBosses = new HashMap<>();
            
            ConfigurationSection configBosses = config.getConfigurationSection("bosses");
            
            for(String sUuid : configBosses.getKeys(false)){
                ConfigurationSection configBoss = configBosses.getConfigurationSection(sUuid);

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

                tempActiveBosses.put(UUID.fromString(sUuid),
                    new Boss(
                        null,// Don't like this? It doesn't like you either.
                        template,
                        currentHealth,
                        kills,
                        playerDamage
                    )
                );
            }
            
            // set the entities for each boss
            for(World world : Bukkit.getServer().getWorlds()){
                for(LivingEntity lent : world.getLivingEntities()){
                    Iterator<Entry<UUID, Boss>> iterator = tempActiveBosses.entrySet().iterator();
                    
                    while(iterator.hasNext()){
                        Entry<UUID, Boss> entry = iterator.next();
                        
                        if(lent.getUniqueId().equals(entry.getKey())){
                            entry.getValue().setEntity(lent);
                        }
                    }
                }
            }
            
            // remove any bosses who don't appear to have an entity anymore (dead?)
            Iterator<Entry<UUID, Boss>> iterator = tempActiveBosses.entrySet().iterator();

            while(iterator.hasNext()){
                Entry<UUID, Boss> entry = iterator.next();

                if(entry.getValue().getEntity() == null){
                    iterator.remove();
                }
            }
            
            this.activeBosses.addAll(tempActiveBosses.values());
        }
    }

    List<BossEgg> getEggs() {
        return this.activeEggs;
    }

    List<Boss> getActiveBosses() {
        return this.activeBosses;
    }
    
    public void startSaving(int saveInterval){
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable(){

            @Override
            public void run() {
                File ymlFile = new File(plugin.getDataFolder(),"save.yml");
                
                YamlConfiguration ymlConfig = YamlConfiguration.loadConfiguration(ymlFile);
                
                List<Map<String,Object>> eggs = new ArrayList<>();
                
                for(BossEgg egg : plugin.getBossManager().getAllActiveEggs()){
                    Map<String,Object> tempEgg = new HashMap<>();
                    
                    tempEgg.put("template", egg.getTemplate().getName());
                    tempEgg.put("autospawn", egg.getAutoSpawn());
                    tempEgg.put("x", egg.getLocation().getBlockX());
                    tempEgg.put("y", egg.getLocation().getBlockY());
                    tempEgg.put("z", egg.getLocation().getBlockZ());
                    tempEgg.put("world", egg.getLocation().getWorld().getName());
                    
                    eggs.add(tempEgg);
                }
                
                ymlConfig.set("eggs", eggs);
                
                for(Boss boss : plugin.getBossManager().getAllActiveBosses()){
                    String sUuid = boss.getEntity().toString();
                    
                    ymlConfig.set(sUuid+".template", boss.getTemplate().getName());
                    ymlConfig.set(sUuid+".currentHealth", boss.getHealth());
                    ymlConfig.set(sUuid+".kills", boss.getKills());
                    ymlConfig.set(sUuid+".playerDamage", boss.getPlayersDamageDone());
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
