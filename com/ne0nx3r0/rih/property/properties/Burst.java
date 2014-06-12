package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.FireworkVisualEffect;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Burst extends RareItemProperty
{
    private final FireworkVisualEffect fireworks;
    
    public Burst()
    {
        super(
            PropertyType.DURABILITY.ordinal(),
            "Burst",
            "Pushes a clicked target away",
            PropertyCostType.FOOD,
            5,
            3
        );
        
        this.fireworks = new FireworkVisualEffect();
    }
    
    @Override
    public boolean onDamageOther(EntityDamageByEntityEvent e, Player p, int level)
    {
        if(e.getEntity() instanceof LivingEntity)
        {        
            LivingEntity le = (LivingEntity) e.getEntity();
            
            try
            {
                fireworks.playFirework(
                    le.getWorld(), le.getLocation(),
                    FireworkEffect
                        .builder()
                        .with(Type.BURST)
                        .withColor(Color.WHITE)
                        .build()
                );
            }
            catch (Exception ex)
            {
                Logger.getLogger(Burst.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Vector unitVector = le.getLocation().toVector().subtract(e.getDamager().getLocation().toVector()).normalize();
            
            unitVector.setY(0.55/level);
            
            le.setVelocity(unitVector.multiply(level*2));
            
            e.setCancelled(true);
            
            return true;
        }
        return false;
    }
}