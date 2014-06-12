package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagicBag extends RareItemProperty
{
    public MagicBag()
    {
        super(
            PropertyType.MAGIC_BAG.ordinal(),
            "Magic Bag",
            "Repairs the #1 hotbar slot item",
            PropertyCostType.COOLDOWN,
            4,
            1
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().openInventory(e.getPlayer().getEnderChest());
        
        return true;
    }
}