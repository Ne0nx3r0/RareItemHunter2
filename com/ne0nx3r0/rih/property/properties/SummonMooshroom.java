package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonMooshroom extends RareItemProperty
{
    public SummonMooshroom()
    {
        super(
            PropertyType.SUMMON_MOOSHROOM.ordinal(),
            "Summon Mooshroom",
            "Creates one mooshroom / level",
            PropertyCostType.LEVEL,
            10,
            8
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.MUSHROOM_COW);
        }

        return true;
    }
}