package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Backstab extends RareItemProperty
{
    public Backstab()
    {
        super(
            PropertyType.BACKSTAB.ordinal(),
            "Backstab",
            "Deal extra damage if attacking an enemy from behind (damage * level)",
            PropertyCostType.FOOD,
            2,
            8
        );
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(e.getEntity().getLocation().getDirection().dot(e.getDamager().getLocation().getDirection()) > 0.0D)
        {
            e.setDamage(e.getDamage() * level);
            
            p.sendMessage("Backstab!");
            
            if(e.getEntity() instanceof Player)
            {
                ((Player) e.getEntity()).sendMessage("You were backstabbed!");
            }
            
            return true;
        }
        return false;
    }
}