package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Spore extends RareItemProperty
{
    public Spore()
    {
        super(PropertyType.SPORE.ordinal(),"Spore","Turns clicked cobblestone into mossy cobblestone.",PropertyCostType.COOLDOWN,1,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.COBBLESTONE)
        {
            e.getClickedBlock().setType(Material.MOSSY_COBBLESTONE);
            
            return true;
        }
        
        return false;
    }
}