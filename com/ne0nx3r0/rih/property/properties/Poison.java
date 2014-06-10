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

public class Poison extends RareItemProperty
{
    public Poison()
    {     
        super(
            PropertyType.POISON.ordinal(),
            "Poison",
            "3% chance/level to poison an enemy onhit",
            PropertyCostType.FOOD,
            2,
            6
        );
    }

    @Override
    public boolean onDamageOther(EntityDamageByEntityEvent e, Player attacker, int level)
    {
        if(new Random().nextInt(100) < 3 * level
        && e.getEntity() instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) e.getEntity();
            
            le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 180, 1 * level));

            attacker.sendMessage("Poisoned!");
            
            if(e.getEntity() instanceof Player)
            {
                ((Player) e.getEntity()).sendMessage("You are poisoned!");
            }
            
            return true;
        }
        return false;
    }
}
