package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_7_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

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
        
        Entity bossEntity = this.spawnBossEntity(bt.getBossEntityType(), lSpawnAt);

        LivingEntity lent = (LivingEntity) bossEntity.getBukkitEntity();
        
        lent.setCustomNameVisible(true);
        lent.setRemoveWhenFarAway(false);
        
        EntityEquipment lequips = lent.getEquipment();
            
        if(bt.getEquipment() != null)
        {
            lequips.setArmorContents(bt.getEquipment().toArray(new ItemStack[4]));

            lequips.setBootsDropChance(0f);
            lequips.setLeggingsDropChance(0f);
            lequips.setChestplateDropChance(0f);    
            lequips.setHelmetDropChance(0f);
        }
            
        if(bt.getWeapon() != null)
        {
            lequips.setItemInHand(bt.getWeapon());

            lequips.setItemInHandDropChance(0f);
        }
        
        this.activeBosses.add(new Boss(bossEntity.getUniqueID(),bt));
        
        return new Boss(bossEntity.getUniqueID(),bt);
    }
    
    private Entity spawnBossEntity(BossEntityType bossType,Location loc){
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
            case ENDERMAN: 
                bossEntity = new BossEntityEnderman(nmsWorld);
                break;
        }
        
        bossEntity.setPosition(loc.getX(), loc.getY(), loc.getZ());
        
        nmsWorld.addEntity(bossEntity);
        
        return bossEntity;
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
