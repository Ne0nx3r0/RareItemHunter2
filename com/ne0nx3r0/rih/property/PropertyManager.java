package com.ne0nx3r0.rih.property;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import net.minecraft.server.v1_7_R3.Material;
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
    private final RecipeManager recipeManager;

    public PropertyManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        this.recipeManager = plugin.getRecipeManager();
        
        PropertiesYmlLoader loader = new PropertiesYmlLoader(plugin);
        
        this.properties = loader.loadProperties();
        
        this.activeEffects = new HashMap<>();
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
                
                    rip.onInteract(e, propertyLevel.getValue());
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
}
