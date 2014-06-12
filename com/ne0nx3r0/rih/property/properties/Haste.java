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

public class Haste extends RareItemProperty
{    
    public Haste()
    {
        super(
            PropertyType.HASTE.ordinal(),
            "Haste",
            "Allows you to run faster for 30 seconds / level",
            PropertyCostType.LEVEL,
            1,
            8
        ); 
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*30*level,level));
        
        e.getPlayer().sendMessage("You cast "+this.getName().toLowerCase()+" on yourself!");
        
        return true;
    }
    
    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent e, int level)
    {
        if(e.getRightClicked() instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) e.getRightClicked();

            le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*30*level,level));
                    
            if(le instanceof Player)
            {
                e.getPlayer().sendMessage("You cast "+this.getName().toLowerCase()+" on "+((Player) le).getName().toLowerCase()+"!");
                ((Player) le).sendMessage(e.getPlayer().getName()+" cast "+this.getName()+" on you!");
            }
            else
            {
                e.getPlayer().sendMessage("You cast "+this.getName().toLowerCase()+" on that thing!");
            }
            
            return true;
        }
        return false;
    }
}