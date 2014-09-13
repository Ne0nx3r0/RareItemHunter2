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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class PropertyManager {
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
        
        final PropertyManager pm = this;
        
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
            private int runCount = 1;
            
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
                
                runCount++;
                
                if(runCount > 100){
                    runCount = 1;
                    
                    for(Entry<UUID, Map<RareItemProperty, Integer>> playerActiveEffects : activeEffects.entrySet()){
                        playerActiveEffects.getValue().clear();
                    }
                    
                    for(Entry<UUID, Map<RareItemProperty, Integer>> playerActiveEffects : activeEffects.entrySet()){
                        Player p = Bukkit.getServer().getPlayer(playerActiveEffects.getKey());
                        
                        
                        for(ItemStack is : p.getInventory().getArmorContents()){
                            if(is != null && is.getType() != Material.AIR){
                                pm.onEquip(p, is);
                            }
                        }
                    }
                }
                
                for(Entry<UUID, Map<RareItemProperty, Integer>> playerActiveEffects : activeEffects.entrySet()){
                    for(Entry<RareItemProperty, Integer> activeEffect : playerActiveEffects.getValue().entrySet()){
                        if(activeEffect.getKey().getCostType().equals(PropertyCostType.AUTOMATIC)){
                            Player p = Bukkit.getServer().getPlayer(playerActiveEffects.getKey());
                            
                            if(p != null){
                                if(runCount % (int) activeEffect.getKey().getCost() == 0){
                                    activeEffect.getKey().applyEffectToPlayer(p,activeEffect.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }, 20, 20);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().length > 0){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        if(p.getInventory() != null){
                            for(ItemStack is: p.getInventory().getArmorContents()){
                                if(is != null && is.getType() != Material.AIR){
                                    pm.onEquip(p, is);
                                }
                            }
                        }
                    }
                }
            }
        }, 20);
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

    public void onEquip(Player p,ItemStack is) {
        Map<RareItemProperty, Integer> itemProperties = this.recipeManager.getProperties(is);
        
        if(itemProperties == null){
            return;
        }
        
// remove any properties that are not automatic/passive
        Iterator<Entry<RareItemProperty, Integer>> iter = itemProperties.entrySet().iterator();
        
        while(iter.hasNext()){
            Entry<RareItemProperty, Integer> next = iter.next();
            PropertyCostType costType = next.getKey().getCostType();
            
            if(costType != PropertyCostType.PASSIVE && costType != PropertyCostType.AUTOMATIC){
                iter.remove();
            }
        }
        
        if(!itemProperties.isEmpty()){
            Map<RareItemProperty,Integer> activeProperties = this.activeEffects.get(p.getUniqueId());
            
            if(activeProperties == null){
                activeProperties = new HashMap<>();
                
                this.activeEffects.put(p.getUniqueId(), activeProperties);
            }
            
            activeProperties.putAll(itemProperties);
        }
    }

    public void onUnequip(Player p,ItemStack is) {
        Map<RareItemProperty, Integer> itemProperties = this.recipeManager.getProperties(is);
        
        if(itemProperties != null && !itemProperties.isEmpty()){
            Map<RareItemProperty,Integer> activeProperties = this.activeEffects.get(p.getUniqueId());

            if(activeProperties != null){
                for(RareItemProperty rip : itemProperties.keySet()){
                    if(rip.getCostType() == PropertyCostType.AUTOMATIC || rip.getCostType() == PropertyCostType.PASSIVE){
                        activeProperties.remove(rip);
                        
                        rip.removeEffectFromPlayer(p);
                    }
                }
                
                if(activeProperties.isEmpty()){
                    this.activeEffects.remove(p.getUniqueId());
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
                                thingNeeded = rip.getCost()+" level"+(rip.getCost()>1?"s":"");
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

    public void onShootBow(EntityShootBowEvent e) {
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            ItemStack bow = e.getBow();
      
            if(bow != null){
                Map<RareItemProperty, Integer> propertyLevels = this.recipeManager.getProperties(bow);

                if(propertyLevels != null && !propertyLevels.isEmpty()){
                    for(Entry<RareItemProperty,Integer> propertyLevel : propertyLevels.entrySet()){
                        RareItemProperty rip = propertyLevel.getKey();

                        if(this.hasCost(player,rip)){                 
                            if(rip.onLaunchProjectile(e, player, propertyLevel.getValue())){
                                this.used(player,rip);
                            }
                        }
                        else {
                            String thingNeeded = "";

                            switch(rip.getCostType()){
                                case FOOD:
                                    thingNeeded = rip.getCost()+" food";
                                    break;
                                case LEVEL: 
                                    thingNeeded = rip.getCost()+" level"+(rip.getCost()>1?"s":"");
                                    break;
                                case HEALTH: 
                                    thingNeeded = rip.getCost()+" health";
                                    break;
                                case MONEY: 
                                    thingNeeded = this.economy.format(rip.getCost());
                                    break;
                                case COOLDOWN: 
                                    int seconds = (int) ((this.cooldowns.get(player.getUniqueId()).get(rip) - System.currentTimeMillis()) / 1000);

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

                            player.sendMessage(ChatColor.RED+"You need "+thingNeeded+" to use "+rip.getName()+"!");
                        }
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
                    RareItemProperty rip = propertyLevel.getKey();
                    
                    if(this.hasCost(e.getPlayer(),rip)){                 
                        if(rip.onInteractEntity(e, propertyLevel.getValue())){
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
                                thingNeeded = rip.getCost()+" level"+(rip.getCost()>1?"s":"");
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
                return player.getLevel() >= rip.getCost();
                
            case FOOD:
                return player.getFoodLevel() >= rip.getCost();
                
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
                return player.getHealth() >= rip.getCost();
                
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

    public Iterable<RareItemProperty> getAllProperties() {
        return this.properties;
    }

    public Map<RareItemProperty, Integer> getPlayerActiveEffects(Player p) {
        return this.activeEffects.get(p.getUniqueId());
    }

    public void onQuit(Player player) {
        this.activeEffects.remove(player.getUniqueId());
    }
}
