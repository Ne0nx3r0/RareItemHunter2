package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.gui.GuiManager;
import com.ne0nx3r0.rih.property.PropertyManager;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.properties.Rage;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    private final BossManager bossManager;
    private final GuiManager guiManager;
    private final PropertyManager propertyManager;
    private final RecipeManager recipeManager;

    public PlayerListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
        this.guiManager = plugin.getGuiManager();
        this.propertyManager = plugin.getPropertymanager();
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

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamaged(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            Map<RareItemProperty, Integer> playerActiveEffects = this.propertyManager.getPlayerActiveEffects(p);
                  
            if(playerActiveEffects != null){
                for(Entry<RareItemProperty, Integer> entry : playerActiveEffects.entrySet()){
                    if(entry.getKey().getID() == PropertyType.WATER_BREATHING.ordinal()){
                        if(e.getCause() == DamageCause.DROWNING){

                            p.setRemainingAir(20);

                            e.setCancelled(true);

                            return;
                        }
                    }
                    else if(entry.getKey().getID() == PropertyType.HARDY.ordinal()){
                        e.setDamage(e.getDamage()-entry.getValue());
                    }
                }
            }
        }
    }
    
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
                this.propertyManager.onEquip((Player) e.getWhoClicked(),e.getCursor());
            }
            
            if(e.getCurrentItem() != null 
            && e.getCurrentItem().getType() != Material.AIR){//unequipped item
                this.propertyManager.onUnequip((Player) e.getWhoClicked(),e.getCurrentItem());
            }
        }
        else if(this.guiManager.isRecipeEditor(e.getInventory())){
            this.guiManager.recipeEditorAction(e);
        }
        else if(this.guiManager.isLegendaryShrineScreen(e.getInventory())){
            this.guiManager.legendaryShrineAction(e);
        }
        else if(this.guiManager.isPropertyViewer(e.getInventory())){
            // read only
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCloseShrine(InventoryCloseEvent e){
        if(this.guiManager.isLegendaryShrineScreen(e.getInventory())){
            this.guiManager.closeScreen(e);
        }            
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent e){
        if(this.recipeManager.isLegendaryCompass(e.getPlayer().getItemInHand())){            
            Location lClosest = this.bossManager.getClosestBossOrEggTo(e.getPlayer().getLocation());
            
            if(lClosest != null){
                e.getPlayer().setCompassTarget(lClosest);
                
                e.getPlayer().sendMessage(ChatColor.GREEN+"The compass glows and points sharply!");
            }
            else {
                e.getPlayer().setCompassTarget(e.getPlayer().getLocation().getWorld().getSpawnLocation());
                
                e.getPlayer().sendMessage(ChatColor.DARK_GRAY+"The compass glows for a moment and then fades");
            }
        }
        else if(!e.isCancelled()){
            this.propertyManager.onUse(e);
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent e){
        this.propertyManager.onUseEntity(e);
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDamageEntityWithRareItem(EntityDamageByEntityEvent e){
        this.propertyManager.onAttackEntity(e);
        
        if(e.getDamager() instanceof Player){
            Player pAttacker = (Player) e.getDamager();
            
            Map<RareItemProperty, Integer> playerActiveEffects = this.propertyManager.getPlayerActiveEffects(pAttacker);
                  
            if(playerActiveEffects != null){
                for(Entry<RareItemProperty, Integer> entry : playerActiveEffects.entrySet()){
                    if(entry.getKey().getID() == PropertyType.STRENGTH.ordinal()){
                        e.setDamage(e.getDamage()+entry.getValue());
                    }
                    else if(entry.getKey().getID() == PropertyType.RAGE.ordinal()){
                        e.setDamage(((Rage) entry.getKey()).getModifiedDamage(
                                entry.getValue(), 
                                pAttacker.getHealth(), 
                                e.getDamage()
                        ));
                    }
                }
            }
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e){
        for(ItemStack is : e.getPlayer().getInventory().getArmorContents()){
            if(is != null && is.getType() != Material.AIR){
                this.propertyManager.onEquip(e.getPlayer(), is);
            }
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent e){
        this.propertyManager.onQuit(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPlaceRareItem(BlockPlaceEvent e){
        Map<RareItemProperty, Integer> properties = this.recipeManager.getProperties(e.getItemInHand());
        
        if(properties != null && !properties.isEmpty()){
            e.setCancelled(true);
        }
    }
}
