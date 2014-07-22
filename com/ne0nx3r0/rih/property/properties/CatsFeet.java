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

public class CatsFeet extends RareItemProperty
{
    public CatsFeet()
    {
        super(
            PropertyType.CATS_FEET.ordinal(),
            "Cat's Feet",
            "Lets you or a clicked target jump higher for 60 seconds per level",
            PropertyCostType.LEVEL,
            1,
            8
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,20*60*level,level));
        
        e.getPlayer().sendMessage("You can jump higher!");
        
        return true;
    }    
    
    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent e, int level)
    {
        if(e.getRightClicked() instanceof LivingEntity)
        {
            int duration = 20*60*level;
        
            LivingEntity le = (LivingEntity) e.getRightClicked();

            le.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,duration,level));
                    
            if(le instanceof Player)
            {
                e.getPlayer().sendMessage("You cast Cat's Feet on "+((Player) le).getName()+"!");
                ((Player) le).sendMessage(e.getPlayer().getName()+" cast Cat's Feet on you!");
            }
            else
            {
                e.getPlayer().sendMessage("You cast Cat's Feet on that thing!");
            }
            
            return true;
        }
        return false;
    }
}