package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.event.player.PlayerInteractEvent;

public class CraftItem extends RareItemProperty
{
    public CraftItem()
    {
        super(
            PropertyType.DURABILITY.ordinal(),
            "Craft Item",
            "Opens a crafting table",
            PropertyCostType.COOLDOWN,
            5,
            1
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().openWorkbench(null, true);
        
        return true;
    }
}