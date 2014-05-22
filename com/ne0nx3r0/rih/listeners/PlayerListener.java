package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.gui.GuiManager;
import com.ne0nx3r0.rih.property.PropertyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private final BossManager bossManager;
    private final GuiManager guiManager;
    private final PropertyManager propertymanager;

    public PlayerListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
        this.guiManager = plugin.getGuiManager();
        this.propertymanager = plugin.getPropertymanager();
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
    public void onPlayerDeath(PlayerDeathEvent e){
        System.out.println("PDE last entity:");
        System.out.println(e.getEntity().getLastDamageCause().getEntity());
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerClickShrine(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) 
        && this.guiManager.isLegendaryShrineBlock(e.getClickedBlock())){
            this.guiManager.shrineClick(e);
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent e){
        if(e.getSlotType() == InventoryType.SlotType.ARMOR)
        {
            if(e.getCursor() != null 
            && e.getCursor().getType() != Material.AIR)//equipped item
            {
                this.propertymanager.onEquip(e.getCursor());
            }
            if(e.getCurrentItem() != null 
            && e.getCurrentItem().getType() != Material.AIR)//unequipped item
            { 
                this.propertymanager.onUnequip(e.getCurrentItem());
            }
        }
        else if(this.guiManager.isRecipeEditor(e.getInventory())){
            this.guiManager.recipeEditorAction(e);
        }
        else if(this.guiManager.isLegendaryShrineScreen(e.getInventory())){
            this.guiManager.legendaryShrineAction(e);
        }
    }
}
