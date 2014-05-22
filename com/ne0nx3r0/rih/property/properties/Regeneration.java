package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import org.bukkit.entity.Player;

public class Regeneration extends ItemPropertyRepeatingEffect
{    
    public Regeneration()
    {
        super(PropertyType.REGENERATION.ordinal(),"Regeneration","Regenerate 1 HP / lvl / 5 seconds",PropertyCostType.AUTOMATIC,5,5);
    }

    @Override
    public void applyEffectToPlayer(Player p,int level)
    {
        if(p.getHealth() < 20)
        {
            double iNewHP = p.getHealth() + level * 1;
            
            if(iNewHP > 20)
            {
                iNewHP = 20;
            }
            
            p.setHealth(iNewHP);
        }
    }
}
