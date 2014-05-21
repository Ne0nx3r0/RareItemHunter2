package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class MeltObsidian extends RareItemProperty
{
    public MeltObsidian()
    {
        super(PropertyType.MELT_OBSIDIAN.ordinal(),"Melt Obsidian","Turns clicked lava into obsidian",PropertyCostType.COOLDOWN,1,1);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null)
        {
            if(e.getClickedBlock().getType() == Material.OBSIDIAN)
            {
                e.getClickedBlock().setType(Material.LAVA);
                
                e.setCancelled(true);
                
                return true;
            }
        }
        return false;
    }
}