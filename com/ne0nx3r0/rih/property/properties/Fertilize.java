package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fertilize extends RareItemProperty
{
    public Fertilize()
    {
        super(PropertyType.FERTILIZE.ordinal(),"Fertilize","Turns clicked dirt blocks to grass",PropertyCostType.COOLDOWN,0.5,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.hasBlock() && e.getClickedBlock().getType() == Material.DIRT)
        {
            e.getClickedBlock().setType(Material.GRASS);

            return true;
        }
        
        return false;
    }
}
