package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.Random;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonSlime extends RareItemProperty
{
    public SummonSlime()
    {
        super(
            PropertyType.SUMMON_SLIME.ordinal(),
            "Summon Slime",
            "Creates one Slime / level",
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
            Slime slime = (Slime)  e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.SLIME);
            slime.setSize(new Random().nextInt(5)+1);
        }

        return true;
    }
}