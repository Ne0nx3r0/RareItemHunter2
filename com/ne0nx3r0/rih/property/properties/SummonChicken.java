package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonChicken extends RareItemProperty
{
    public SummonChicken()
    {
        super(
            PropertyType.SUMMON_CHICKEN.ordinal(),
            "Summon Chicken",
            "Creates one chicken / level",
            PropertyCostType.LEVEL,
            4,
            8
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.CHICKEN);
        }
        
        return true;
    }
}