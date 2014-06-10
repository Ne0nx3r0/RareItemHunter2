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

public class Blinding extends RareItemProperty
{
    public Blinding()
    {
        super(
            PropertyType.BLINDING.ordinal(),
            "Blinding",
            "25% chance to blind a target onhit (3s / level)",
            PropertyCostType.FOOD,
            2,
            5
        );
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(new Random().nextInt(4) == 0
        && e.getEntity() instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) e.getEntity();
            
            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,3*level,2));
            
            p.sendMessage("Blinded!");

            if(e.getEntity() instanceof Player)
            {
                ((Player) e.getEntity()).sendMessage("You are blinded!");
            }
            
            return true;
        }
        
        return false;
    }
}