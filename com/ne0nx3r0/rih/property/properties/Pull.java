package com.ne0nx3r0.rih.property.properties;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Pull extends RareItemProperty
{
    public Pull()
    {
        super(PropertyType.PULL.ordinal(),"Pull","Pulls a clicked/hit with an arrow target towards you",PropertyCostType.FOOD,1,1);
    }
    
    @Override
    public boolean onDamageOther(EntityDamageByEntityEvent e, Player attacker, int level){
        if(e.getEntity() instanceof LivingEntity){
            if(e.getEntity() instanceof Player){
                Player attacked = (Player) e.getEntity();
                
                attacked.sendMessage(attacker.getDisplayName()+ChatColor.RESET+"Pulled you towards them!");
            }
            
            e.getEntity().teleport(attacker);
            
            return true;
        }
        
        return false;
    }

}