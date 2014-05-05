package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;

public class BossManager {
    private final List<BossTemplate> bossTemplates;
    private final List<Boss> activeBosses;

    public BossManager(RareItemHunterPlugin plugin) {
        BossesYmlLoader loader = new BossesYmlLoader(plugin);
        
        this.bossTemplates = loader.loadBosses();
        this.activeBosses = new ArrayList<>();
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
        
        this.activeBosses.add(new Boss(uuid,bt));
        
        return new Boss(uuid,bt);
    }
    
    private UUID spawnBossEntity(BossEntityType bossType,Location loc){
        net.minecraft.server.v1_7_R3.World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        
        net.minecraft.server.v1_7_R3.Entity bossEntity;
        
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
            case PIG: 
                bossEntity = new BossEntityPig(nmsWorld);
                break;
        }
        
        bossEntity.setPosition(loc.getX(), loc.getY(), loc.getZ());
        
        nmsWorld.addEntity(bossEntity);
        
        return bossEntity.getUniqueID();
    }

    public boolean isBoss(org.bukkit.entity.Entity entity) {
        UUID uuid = entity.getUniqueId();
        
        for(Boss boss : this.activeBosses){
            if(boss.getUniqueID().equals(uuid)){
                return true;
            }
        }
        
        return false;
    }

    public Boss getBoss(org.bukkit.entity.Entity entity) {
        UUID uuid = entity.getUniqueId();
        
        for(Boss boss : this.activeBosses){
            if(boss.getUniqueID().equals(uuid)){
                return boss;
            }
        }
        
        return null;
    }

    public void removeBoss(Boss boss) {
        this.activeBosses.remove(boss);
    }
}
