package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.entities.BossEntityChicken;
import com.ne0nx3r0.rih.entities.BossEntityOcelot;
import com.ne0nx3r0.rih.entities.BossEntityType;
import com.ne0nx3r0.rih.entities.BossEntityZombie;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_7_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;

public class BossManager {
    private final List<BossTemplate> bossTemplates;

    public BossManager(RareItemHunterPlugin plugin) {
        BossesYmlLoader loader = new BossesYmlLoader(plugin);
        
        this.bossTemplates = loader.loadBosses();
    }
    public BossTemplate getBossTemplate(String bossName){
        for(BossTemplate bt : this.bossTemplates){
            if(bt.getName().equalsIgnoreCase(bossName)){
                return bt;
            }
        }
        return null;
    }

    public boolean isValidBossName(String bossName) {
        return this.getBossTemplate(bossName) != null;
    }

    public Boss spawnBossAt(String bossName, Location lSpawnAt) {
        BossTemplate bt = this.getBossTemplate(bossName);
        
        if(bt == null){
            return null;
        }
        UUID uuid = this.spawnBossEntity(bt.getBossEntityType(), lSpawnAt);
        
        return new Boss(uuid,bt);
    }
    
    public UUID spawnBossEntity(BossEntityType bossType,Location loc){
        net.minecraft.server.v1_7_R3.World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        
        Entity bossEntity;
        
        switch(bossType){
            default:
            case ZOMBIE: 
                bossEntity = new BossEntityZombie(nmsWorld);
                break;
            case CHICKEN: 
                bossEntity = new BossEntityChicken(nmsWorld);
                break;
            case OCELOT: 
                bossEntity = new BossEntityOcelot(nmsWorld);
                break;
        }
        
        bossEntity.setPosition(loc.getX(), loc.getY(), loc.getZ());
        
        nmsWorld.addEntity(bossEntity);
        
        return bossEntity.getUniqueID();
    }
    
}
