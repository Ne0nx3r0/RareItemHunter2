package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonCow extends RareItemProperty
{
    public SummonCow()
    {
        super(
            PropertyType.SUMMON_COW.ordinal(),
            "Summon Cow",
            "Creates one cow / level",
            PropertyCostType.LEVEL,
            6,
            8
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e, int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.COW);
        }
        
        return true;
    }
}