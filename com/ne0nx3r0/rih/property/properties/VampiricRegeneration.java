package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class VampiricRegeneration extends RareItemProperty
{
    public VampiricRegeneration()
    {
        super(
            PropertyType.VAMPIRIC_REGENERATION.ordinal(),
            "Vampiric Regeneration",
            "3% chance/level to steal 5HP from an enemy",
            PropertyCostType.FOOD,
            1,
            6
        );
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(new Random().nextInt(100) < 3 * level
        && e.getEntity() instanceof LivingEntity && e.getDamager() instanceof LivingEntity)
        {
            LivingEntity attacked = (LivingEntity) e.getEntity();
            LivingEntity attacker = (LivingEntity) e.getDamager();
                    
            int iStolenHP = new Random().nextInt(3 * level)+1;

            double iNewAttackerHP = attacked.getHealth() - iStolenHP;
            
            if(iNewAttackerHP > 20)
            {
                iNewAttackerHP = 20;
            }
            
            double iNewAttackedHP = attacker.getHealth() + iStolenHP;
            
            if(iNewAttackedHP < 1)
            {
                iNewAttackerHP = 1;
            }
            
            attacked.setHealth(iNewAttackedHP);
            attacker.setHealth(iNewAttackerHP);
            
            p.sendMessage(ChatColor.RED+"You stole "+iStolenHP+"HP!");
            
            if(attacked instanceof Player)
            {
                Player pAttacked = (Player) attacked;
                pAttacked.sendMessage(ChatColor.RED+p.getName()+" stole "+iStolenHP+"HP from you!");
            }
            
            return true;
        }
        
        return false;
    }
}