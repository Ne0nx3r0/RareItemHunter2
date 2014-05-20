package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.gui.GuiManager;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class RareItemHunterPlayerListener implements Listener {
    private final BossManager bossManager;
    private final RecipeManager recipeManager;
    private final GuiManager guiManager;

    public RareItemHunterPlayerListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
        this.recipeManager = plugin.getRecipeManager();
        this.guiManager = plugin.getGuiManager();
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
    public void onPlayerInventoryClick(PlayerInteractEvent e){
        if(this.guiManager.isLegendaryShrineBlock(e.getClickedBlock())){
            Inventory invShrine = this.guiManager.createLegendaryShrineCrafter(e.getClickedBlock(),e.getPlayer());

            e.getPlayer().openInventory(invShrine);
        }
    }
    
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent e){
        if(this.guiManager.isRecipeEditor(e.getInventory())){
            this.guiManager.recipeEditorAction(e);
        }
        else if(this.guiManager.isLegendaryShrineScreen(e.getInventory())){
            this.guiManager.legendaryShrineAction(e);
        }
    }
}
