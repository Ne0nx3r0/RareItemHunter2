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
        if(!player.isSprinting()){
            int currentFood = player.getFoodLevel();
            
            if(currentFood == 20){
                float newSaturation = player.getSaturation() + level;

                if(newSaturation > currentFood){
                    newSaturation = currentFood;
                }
                
                player.setSaturation(newSaturation);
            }
            else {
                int newFood = currentFood + level;
                
                if(newFood >= 20){                
                    newFood = 20;
                }
                
                if(newFood != currentFood){
                    player.setFoodLevel(newFood);
                }
            }
        }
    } 
}