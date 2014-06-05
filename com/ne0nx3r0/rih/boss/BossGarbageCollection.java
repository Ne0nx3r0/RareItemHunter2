package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.egg.BossEgg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import net.minecraft.server.v1_7_R3.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

class BossGarbageCollection {
    BossGarbageCollection(final RareItemHunterPlugin plugin, final BossManager bm,long timer) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
            @Override
            public void run() {
                List<Boss> activeBosses = bm.getAllActiveBosses();
                
                if(!activeBosses.isEmpty()){
                    Map<UUID,Boss> bossesFound = new HashMap<>();
                    
                    for(Boss boss : bm.getAllActiveBosses()){
                        bossesFound.put(boss.getUniqueID(), boss);
                    }
                    
                    // Yikes. 
                    // I just don't know how else to detect 
                    // all possible ways to remove an entity
                    //
                    // Note: some bosses can teleport to new worlds
                    for(World world : plugin.getServer().getWorlds()){
                        for(LivingEntity le : world.getLivingEntities()){
                            for(Entry<UUID,Boss> entry : bossesFound.entrySet()){
                                if(entry.getKey().equals(le.getUniqueId())){
                                    // found
                                    entry.setValue(null);
                                }
                            }
                        }
                    }
                    
                    for(Entry<UUID,Boss> entry : bossesFound.entrySet()){
                        // wasn't found, remove entry
                        if(entry.getValue() != null){
                            activeBosses.remove(entry.getValue());
                        }
                    }
                }
                
                // Clean up the eggs, much cleaner
                List<BossEgg> removeThese = new ArrayList<>();
                
                for(BossEgg egg : bm.getAllActiveEggs()){
                    if(!egg.getLocation().getBlock().getType().equals(Material.DRAGON_EGG)){
                        removeThese.add(egg);
                    }
                }
                
                for(BossEgg egg : removeThese){
                    bm.removeBossEgg(egg);
                }
            }
        }, timer, timer);
    }
}
