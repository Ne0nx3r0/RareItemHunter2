package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CallLightning extends RareItemProperty
{
    public CallLightning()
    {        
        super(
            PropertyType.CALL_LIGHTNING.ordinal(),
            "Call Lightning",
            "10% chance to strike an opponent with lightning per level",
            PropertyCostType.FOOD,
            4,
            5
        );
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(!e.getCause().equals(DamageCause.LIGHTNING)
        && new Random().nextInt(100) > level * 10
        && e.getEntity() instanceof LivingEntity)
        {
            int maxTargets = level * 2;
            int hitTargets = 0;
            
            Location l = e.getEntity().getLocation();

            l.getWorld().strikeLightningEffect(l);
            
            int maxIterations = 100;
            
            for(Entity ent : e.getEntity().getNearbyEntities(5, 5, 5))
            {
                maxIterations--;
                
                if(maxIterations == 0){
                    return true;
                }
                
                if(hitTargets >= maxTargets) {
                    break;
                }   
                
                if(ent == p)
                {
                    continue;
                }
                
                if(ent instanceof LivingEntity)
                {
                    hitTargets++;
                    
                    LivingEntity lent = (LivingEntity) ent;
                    
                    //Emulate damaged by lightning
                    lent.damage(level*2, p);
                }
            }
                    
            return true;
        }
        return false;
    }
}