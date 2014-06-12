package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonOcelot extends RareItemProperty
{
    public SummonOcelot()
    {
        super(
            PropertyType.SUMMON_OCELOT.ordinal(),
            "Summon Ocelot",
            "Creates one ocelot / level",
            PropertyCostType.LEVEL,
            8,
            8
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.OCELOT);
        }

        return true;
    }
}