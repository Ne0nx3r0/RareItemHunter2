package com.ne0nx3r0.rih.property.properties;


import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireResistance extends RareItemProperty
{
    public FireResistance()
    {
        super(
            PropertyType.FIRE_RESISTANCE.ordinal(),
            "Fire Resistance",
            "Grants you or a clicked target 20% fire resistance per level for 60 seconds",
            PropertyCostType.LEVEL,
            2,
            5
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*60,20*level));
        
        e.getPlayer().sendMessage("Upped your fire resistance!");
        
        return true;
    }
    
    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent e, int level)
    {
        if(e.getRightClicked() instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) e.getRightClicked();

            le.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*60,20*level));
                    
            if(le instanceof Player)
            {
                e.getPlayer().sendMessage("You cast "+this.getName()+" on "+((Player) le).getName()+"!");
                ((Player) le).sendMessage(e.getPlayer().getName()+" cast "+this.getName()+" on you!");
            }
            else
            {
                e.getPlayer().sendMessage("You cast "+this.getName()+" on that thing!");
            }
            
            return true;
        }
        return false;
    }
}