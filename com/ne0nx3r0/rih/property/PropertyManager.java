package com.ne0nx3r0.rih.property;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PropertyManager {
    private final RareItemHunterPlugin plugin;
    private final List<RareItemProperty> properties;
    private final Map<UUID,List<Integer>> activeEffects;

    public PropertyManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
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
        
        List<Integer> activeProperties = this.activeEffects.get(p.getUniqueId());
        
        
    }

    public void onUnequip(InventoryClickEvent e) {
        
    }

    public void onUse(PlayerInteractEvent e) {
        
    }

    public void onUseEntity(PlayerInteractEntityEvent e) {
        
    }
}
