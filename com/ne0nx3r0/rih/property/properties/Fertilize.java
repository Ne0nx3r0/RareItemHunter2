package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.RareItemPropertyCostType;
import com.ne0nx3r0.rih.property.RareItemPropertyType;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fertilize extends RareItemProperty
{
    public Fertilize()
    {
        super(RareItemPropertyType.FERTILIZE.ordinal(),"Fertilize","Turns clicked dirt blocks to grass",RareItemPropertyCostType.COOLDOWN,0.5,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.DIRT)
        {
            e.getClickedBlock().setType(Material.GRASS);

            return true;
        }
        
        return false;
    }
}
