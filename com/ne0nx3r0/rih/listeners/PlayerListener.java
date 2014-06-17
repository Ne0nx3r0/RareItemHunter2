package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.gui.GuiManager;
import com.ne0nx3r0.rih.property.PropertyManager;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private final BossManager bossManager;
    private final GuiManager guiManager;
    private final PropertyManager propertymanager;
    private RecipeManager recipeManager;

    public PlayerListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
        this.guiManager = plugin.getGuiManager();
        this.propertymanager = plugin.getPropertymanager();
        this.recipeManager = plugin.getRecipeManager();
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamagedByBoss(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Boss boss = this.bossManager.getBoss(e.getDamager());

            if(boss != null){
                e.setDamage(boss.getTemplate().getAttackPower());
            }
        }
    }
    /*
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamaged(EntityDamageEvent e){
        if(this.propertymanager.hasPropertyActive("Hardy")){
            e.setDamage(e.getDamage()-);
        }
    }*/
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeathByBoss(PlayerDeathEvent e){
        EntityDamageEvent lastDamageCause = e.getEntity().getLastDamageCause();
        
        if(lastDamageCause instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent eLastHit = (EntityDamageByEntityEvent) lastDamageCause;
            
            Boss boss = this.bossManager.getBoss(eLastHit.getDamager());

            if(boss != null){
                e.setDeathMessage(String.format("%s"+ChatColor.RED+" was defeated by "+ChatColor.GREEN+"%s",new Object[]{
                    e.getEntity().getDisplayName(),
                    boss.getTemplate().getName()
                }));
            }
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerClickShrineOrEgg(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) 
        && this.guiManager.isLegendaryShrineBlock(e.getClickedBlock())){
            this.guiManager.shrineClick(e);
        }
        else if(e.hasBlock()){
            this.bossManager.hatchEggIfBoss(e.getClickedBlock());
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerClickRIInventory(InventoryClickEvent e){
        if(e.getSlotType() == InventoryType.SlotType.ARMOR)
        {
            if(e.getCursor() != null 
            && e.getCursor().getType() != Material.AIR){//equipped item
                this.propertymanager.onEquip(e);
            }
            
            if(e.getCurrentItem() != null 
            && e.getCurrentItem().getType() != Material.AIR){//unequipped item
                this.propertymanager.onUnequip(e);
            }
        }
        else if(this.guiManager.isRecipeEditor(e.getInventory())){
            this.guiManager.recipeEditorAction(e);
        }
        else if(this.guiManager.isLegendaryShrineScreen(e.getInventory())){
            this.guiManager.legendaryShrineAction(e);
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCloseShrine(InventoryCloseEvent e){
        if(this.guiManager.isLegendaryShrineScreen(e.getInventory())){
            this.guiManager.closeScreen(e);
        }            
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.hasItem() && this.recipeManager.isLegendaryCompass(e.getItem())){
            Location lClosest = this.bossManager.getClosestBossOrEggTo(e.getPlayer().getLocation());
            
            if(lClosest != null){
                e.getPlayer().setCompassTarget(lClosest);
            }
        }
        
        this.propertymanager.onUse(e);
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent e){
        this.propertymanager.onUseEntity(e);
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDamageEntityWithRareItem(EntityDamageByEntityEvent e){
        this.propertymanager.onAttackEntity(e);
    }
}
