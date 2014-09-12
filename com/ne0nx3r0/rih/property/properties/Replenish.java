package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.Player;

public class Replenish extends RareItemProperty
{
    public Replenish()
    {
        super(
            PropertyType.REPLENISH.ordinal(),
            "Replenish",
            "Adds 1 food per level every 20 seconds",
            PropertyCostType.AUTOMATIC,
            20,
            4
        );
    }
    
    @Override
    public void applyEffectToPlayer(Player player, int level){
        double health = player.getHealthScale();
        
        health = health + level;
        
        if(health > 20){
            health = 20;
        }
        player.setHealth(health);
    } 
}