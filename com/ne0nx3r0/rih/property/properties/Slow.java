package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.Random;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Slow extends RareItemProperty
{
    public Slow()
    {
        super(
            PropertyType.SLOW.ordinal(),
            "Slow",
            "3% chance/level to slow an attacked enemy",
            PropertyCostType.FOOD,
            1,
            8
        );
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(new Random().nextInt(100) < level * 3
        && e.getEntity() instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) e.getEntity();
            
            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,level));

            p.sendMessage("Slowed!");

            if(e.getEntity() instanceof Player)
            {
                ((Player) e.getEntity()).sendMessage("You are slowed!");
            } 
            
            return true;
        }
        
        return false;
    }
}