package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RareItemHunterPlayerListener implements Listener {
    private final BossManager bossManager;
    private final RecipeManager recipeManager;

    public RareItemHunterPlayerListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
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
    public void onPlayerDeath(PlayerDeathEvent e){
        System.out.println("PDE last entity:");
        System.out.println(e.getEntity().getLastDamageCause().getEntity());
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent e){
        System.out.println(e.getRawSlot());
        
        if(e.getInventory().getType().equals(InventoryType.WORKBENCH)){
            Inventory inventory = e.getInventory();
            
            RareItemProperty rip = this.recipeManager.getPropertyFromResultItem(e.getCurrentItem());
            
            if(rip != null){
                
                e.setCancelled(true);
                
                Player p = (Player) e.getInventory().getHolder();
                
                if(!this.recipeManager.updateRecipe(rip,e.getInventory().getContents())){
                    p.sendMessage(ChatColor.RED+"An error occurred while trying to save the recipe!");
                    
                    return;
                }
                
                p.closeInventory();
                
                p.sendMessage("Recipe updated!");
                
                return;
            }

            // filter out non-essence crafting
            for(ItemStack is : inventory.getContents()){
                if(this.recipeManager.isRareEssence(is)){
                    // safety precaution against errors
                    e.setCancelled(true);

                    ItemStack isResult = this.recipeManager.getResultOf(inventory.getContents());

                    // set the result item accordingly
                    if(isResult != null){                            
                        inventory.setItem(0, is);
                    }
                    else{
                        inventory.setItem(0, new ItemStack(Material.AIR));
                    }

                    // If grabbing the result item destroy the components
                    if(e.getSlotType().equals(SlotType.RESULT) 
                     && !e.getCurrentItem().getType().equals(Material.AIR)){
                        for(int i=1;i<10;i++){
                            inventory.setItem(i, new ItemStack(Material.AIR));
                        }
                    }

                    // You shall... pass.
                    e.setCancelled(false);

                    break;
                }
            }
        }
    }
}
