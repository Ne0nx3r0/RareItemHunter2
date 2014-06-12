package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonPig extends RareItemProperty
{
    public SummonPig()
    {
        super(
            PropertyType.SUMMON_PIG.ordinal(),
            "Summon Pig",
            "Creates one pig / level",
            PropertyCostType.LEVEL,
            5,
            8
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.PIG);
        }
        
        return true;
    }
}