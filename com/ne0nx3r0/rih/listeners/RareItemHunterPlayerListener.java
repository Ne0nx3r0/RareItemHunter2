package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.gui.GuiManager;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

public class RareItemHunterPlayerListener implements Listener {
    private final BossManager bossManager;
    private final RecipeManager recipeManager;
    private final GuiManager guiManager;
    private final RareItemHunterPlugin plugin;

    public RareItemHunterPlayerListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
        this.recipeManager = plugin.getRecipeManager();
        this.guiManager = plugin.getGuiManager();
        this.plugin = plugin;
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
            e.setCancelled(true);

            String sKey = "rihCrafted"+e.getPlayer().getUniqueId().toString();
            
            if(e.getClickedBlock().hasMetadata(sKey)){
                ItemStack isCrafted = null;
                
                // Should only be one... but we can't concurrently remove it
                for(MetadataValue meta : e.getClickedBlock().getMetadata(sKey)){
                    if(meta.getOwningPlugin().equals(this.plugin)){
                        isCrafted = (ItemStack) meta.value();
                        
                        break;
                    }
                }
                
                if(isCrafted != null){
                    Player p = e.getPlayer();

                    e.getClickedBlock().removeMetadata(sKey, this.plugin);

                    if(!p.getInventory().addItem(isCrafted).isEmpty()) {
                        p.getWorld().dropItemNaturally(p.getLocation(), isCrafted);
                    }

                    e.getPlayer().sendMessage(ChatColor.GREEN+"You retrieved your crafted item!");

                    return;
                }
            }
            
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
