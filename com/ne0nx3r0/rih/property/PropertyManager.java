package com.ne0nx3r0.rih.property;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R3.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PropertyManager {
    private final RareItemHunterPlugin plugin;
    private final List<RareItemProperty> properties;
    private final Map<UUID,Map<RareItemProperty,Integer>> activeEffects;
    private final Map<UUID,Map<RareItemProperty,Long>> cooldowns;
    private final RecipeManager recipeManager;
    private final Economy economy;

    public PropertyManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        this.recipeManager = plugin.getRecipeManager();
        
        PropertiesYmlLoader loader = new PropertiesYmlLoader(plugin);
        
        this.properties = loader.loadProperties();
        
        this.activeEffects = new HashMap<>();
        
        this.economy = plugin.getEconomy();
        
        this.cooldowns = new HashMap<>();
        
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                
                Iterator<Entry<UUID, Map<RareItemProperty, Long>>> allPlayersIter = cooldowns.entrySet().iterator();
                
                while(allPlayersIter.hasNext()){                    
                    Map<RareItemProperty, Long> playerCooldowns = allPlayersIter.next().getValue();
                    
                    Iterator<Map.Entry<RareItemProperty,Long>> iter = playerCooldowns.entrySet().iterator();
                    
                    while (iter.hasNext()) {
                        Map.Entry<RareItemProperty,Long> entry = iter.next();
                        
                        if(currentTime > entry.getValue()){
                            iter.remove();
                        }
                    }
                    
                    if(playerCooldowns.isEmpty()){
                        allPlayersIter.remove();
                    }
                }
            }
        }, 20, 20);
    }

    public RareItemProperty getProperty(String propertyName) {
        propertyName = propertyName.toLowerCase();
        
        for(RareItemProperty rip : this.properties){
            if(rip.getName().toLowerCase().equals(propertyName)){
                return rip;
            }
        }
        return null;
    }

    public RareItemProperty getProperty(int id) {
        for(RareItemProperty rip : this.properties){
            if(rip.getID() == id){
                return rip;
            }
        }
        return null;
    }

    public void onEquip(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        
        Map<RareItemProperty, Integer> itemProperties = this.recipeManager.getProperties(e.getCursor());
        
        if(itemProperties != null && !itemProperties.isEmpty()){
            Map<RareItemProperty,Integer> activeProperties = this.activeEffects.get(p.getUniqueId());

            if(activeProperties == null){
                activeProperties = new HashMap<>();
                
                this.activeEffects.put(p.getUniqueId(), activeProperties);
            }
            
            activeProperties.putAll(activeProperties);
        }
    }

    public void onUnequip(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        
        Map<RareItemProperty, Integer> itemProperties = this.recipeManager.getProperties(e.getCurrentItem());
        
        if(itemProperties != null && !itemProperties.isEmpty()){
            Map<RareItemProperty,Integer> activeProperties = this.activeEffects.get(p.getUniqueId());

            if(activeProperties != null){
                for(RareItemProperty rip : activeProperties.keySet()){
                    activeProperties.remove(rip);
                }
            }
        }
    }

    public void onUse(PlayerInteractEvent e) {
        if(e.hasItem()){
            Map<RareItemProperty, Integer> propertyLevels = this.recipeManager.getProperties(e.getItem());
            
            if(propertyLevels != null && !propertyLevels.isEmpty()){
                for(Entry<RareItemProperty,Integer> propertyLevel : propertyLevels.entrySet()){
                    RareItemProperty rip = propertyLevel.getKey();
                
                    if(this.hasCost(e.getPlayer(),rip)){                 
                        if(rip.onInteract(e, propertyLevel.getValue())){
                            this.used(e.getPlayer(),rip);
                        }
                    }
                    else {
                        String thingNeeded = "";
                        
                        switch(rip.getCostType()){
                            case FOOD:
                                thingNeeded = rip.getCost()+" food";
                                break;
                            case LEVEL: 
                                thingNeeded = rip.getCost()+" levels";
                                break;
                            case HEALTH: 
                                thingNeeded = rip.getCost()+" health";
                                break;
                            case MONEY: 
                                thingNeeded = this.economy.format(rip.getCost());
                                break;
                            case COOLDOWN: 
                                int seconds = (int) ((this.cooldowns.get(e.getPlayer().getUniqueId()).get(rip) - System.currentTimeMillis()) / 1000);
                                
                                if(seconds < 1){
                                    seconds = 1;
                                }
                                
                                thingNeeded = "to wait "+seconds+" seconds";
                                break;
                            case AUTOMATIC: 
                                return;
                            case PASSIVE: 
                                return;
                        }
                        
                        e.getPlayer().sendMessage(ChatColor.RED+"You need "+thingNeeded+" to use "+rip.getName()+"!");
                    }
                }
            }
        }
    }

    public void onUseEntity(PlayerInteractEntityEvent e) {
        ItemStack is = e.getPlayer().getItemInHand();
        
        if(is != null && !is.getType().equals(Material.AIR)){
            Map<RareItemProperty, Integer> propertyLevels = this.recipeManager.getProperties(is);

            if(propertyLevels != null && !propertyLevels.isEmpty()){
                for(Entry<RareItemProperty,Integer> propertyLevel : propertyLevels.entrySet()){
                    propertyLevel.getKey().onInteractEntity(e, propertyLevel.getValue());
                }
            }
        }
    }

    public void onAttackEntity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            
            ItemStack isAttackedWith = p.getItemInHand();
            
            if(isAttackedWith != null && !isAttackedWith.getType().equals(Material.AIR)){
                Map<RareItemProperty, Integer> propertyLevels = this.recipeManager.getProperties(isAttackedWith);

                if(propertyLevels != null && !propertyLevels.isEmpty()){
                    for(Entry<RareItemProperty,Integer> propertyLevel : propertyLevels.entrySet()){
                        propertyLevel.getKey().onDamageOther(e, p, propertyLevel.getValue());
                    }
                }
            }
        }
    }

    private boolean hasCost(Player player, RareItemProperty rip) {
        switch(rip.getCostType()){
            case LEVEL:
                return player.getLevel() < rip.getCost();
                
            case FOOD:
                return player.getFoodLevel() < rip.getCost();
                
            case COOLDOWN:
                Map<RareItemProperty, Long> playerCooldowns = this.cooldowns.get(player.getUniqueId());
                
                if(playerCooldowns != null){
                    Long cooldown = playerCooldowns.get(rip);
                    
                    if(cooldown != null){
                        return cooldown < System.currentTimeMillis();
                    }
                    
                    return true;
                }
                
                return true;
            case HEALTH:
                return player.getHealth() > rip.getCost();
                
            case MONEY:
                return economy.has(player.getName(), rip.getCost());
        }

        return false;
    }

    private void used(Player player, RareItemProperty rip) {
        switch(rip.getCostType()){
            case LEVEL:
                player.setLevel((int) (player.getLevel()-rip.getCost()));
                break;
                
            case FOOD:
                player.setFoodLevel((int) (player.getFoodLevel()-rip.getCost()));
                break;
                
            case COOLDOWN:
                this.setCooldown(player, rip);
                break;
                
            case HEALTH:
                player.setHealth(player.getHealth() - rip.getCost());
                break;
                
            case MONEY:
                economy.withdrawPlayer(player.getName(), rip.getCost());
                break;
        }
    }
    
    public void setCooldown(Player player,RareItemProperty rip){
        Map<RareItemProperty, Long> playerCooldowns = this.cooldowns.get(player.getUniqueId());
                
        if(playerCooldowns == null){
            playerCooldowns = new HashMap<>();
            
            this.cooldowns.put(player.getUniqueId(), playerCooldowns);
        }

        playerCooldowns.put(rip,System.currentTimeMillis() + (long) (rip.getCost() * 1000));
    }
}
