package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.boss.entities.*;
import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.egg.BossEgg;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_7_R3.Entity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class BossManager {
    private final List<BossTemplate> bossTemplates;
    private final List<Boss> activeBosses;
    private final List<BossEgg> bossEggs;
    private final RareItemHunterPlugin plugin;

    public BossManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
// load boss templates
        BossTemplateLoader loader = new BossTemplateLoader(plugin);
        
        this.bossTemplates = loader.loadBosses();
        
// Load eggs                
        BossPersistence bp = new BossPersistence(plugin,this);
        
        bp.loadBossesAndEggs();
        
        this.bossEggs = bp.getEggs();
            
        this.activeBosses = bp.getActiveBosses();
        
        bp.startSaving(20*30);
        
        BossGarbageCollection bossGC = new BossGarbageCollection(plugin,this,20*60*30);
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

    
    public Boss spawnBossAt(String bossName, Location spawnAt) {
        BossTemplate template = this.getBossTemplate(bossName);
        
        if(template == null){
            return null;
        }
        
        return this.spawnBossAt(template, spawnAt);
    }
    
    public Boss spawnBossAt(BossTemplate template, Location spawnAt ){
        Entity bossEntity = this.spawnBossEntity(template.getBossEntityType(), spawnAt);

        LivingEntity lent = (LivingEntity) bossEntity.getBukkitEntity();
        
        lent.setCustomNameVisible(true);
        lent.setRemoveWhenFarAway(false);
        
        EntityEquipment lequips = lent.getEquipment();
            
        if(template.getEquipment() != null)
        {
            lequips.setArmorContents(template.getEquipment().toArray(new ItemStack[4]));

            lequips.setBootsDropChance(0f);
            lequips.setLeggingsDropChance(0f);
            lequips.setChestplateDropChance(0f);    
            lequips.setHelmetDropChance(0f);
        }
            
        if(template.getWeapon() != null)
        {
            lequips.setItemInHand(template.getWeapon());

            lequips.setItemInHandDropChance(0f);
        }
        
        Boss boss = new Boss(lent.getUniqueId(),template);
        
        this.activeBosses.add(boss);
        
        lent.setCustomName(template.getName());
        
        return boss;
    }
    
    public Entity spawnBossEntity(BossEntityType bossType,Location loc){
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
            case SNOWMAN: 
                bossEntity = new BossEntitySnowman(nmsWorld);
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

    public List<Boss> getAllActiveBosses() {
        return this.activeBosses;
    }

    public Iterable<BossTemplate> getAllBossTemplates() {
        return this.bossTemplates;
    }

    public BossEgg spawnBossEggAt(String bossName, Location lSpawnAt, boolean autoHatch) {        
        BossTemplate bt = this.getBossTemplate(bossName);
        
        if(bt == null){
            return null;
        }
        
        int blockX = lSpawnAt.getBlockX();
        int blockZ = lSpawnAt.getBlockZ();
        
        for(BossEgg egg : this.bossEggs) {
            // egg in this column already
            if(egg.getLocation().getBlockX() == blockX || egg.getLocation().getBlockZ() == blockZ)
            {
                return null;
            }
        }
        
        Block block = lSpawnAt.getBlock();
        
        block.getRelative(BlockFace.DOWN).setType(Material.BEDROCK);

        block.setType(Material.DRAGON_EGG);

        block.setMetadata("isBossEgg", new FixedMetadataValue(this.plugin,true));

        BossEgg newEgg = new BossEgg(
            bt,
            block.getLocation(),
            autoHatch
        );

        this.bossEggs.add(newEgg);

        return newEgg;
    }

    public Iterable<BossEgg> getAllActiveEggs() {
        return this.bossEggs;
    }

    public void hatchEggIfBoss(Block block) {
        if(block.getType().equals(Material.DRAGON_EGG)){
            Location lBlock = block.getLocation();

            for(BossEgg egg : this.bossEggs){
                if(lBlock.equals(egg.getLocation())){
                    BossTemplate template = egg.getTemplate();
                    
                    block.setType(Material.AIR);
                    block.getRelative(BlockFace.DOWN).setType(Material.AIR);
                    
                    lBlock.getWorld().strikeLightningEffect(lBlock);
                    
                    this.spawnBossAt(template, block.getLocation());
                    
                    return;
                }
            }
        }
    }

    public boolean removeBossEgg(BossEgg egg) {
        return this.bossEggs.remove(egg);
    }
}
