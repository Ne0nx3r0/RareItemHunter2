package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Bukkit;

public class Rage extends RareItemProperty
{
    public Rage()
    {
        super(
            PropertyType.RAGE.ordinal(),
            "Rage",
            "Damage is decreased at full health, but heavily increased at lower health levels. Higher levels modify damage even more",
            PropertyCostType.PASSIVE,
            0,
            8
        );
    }
    
    public double getModifiedDamage(double level,double hp,double damage){
        if(hp == 10.0){
            return damage;
        }
        
        double levelModifier = 0.15D + level * 0.7D;
        
        double hpModifier = (hp - 10.0D) * -1D;
        
        double newDamage = damage + levelModifier * hpModifier;
        
        if(newDamage < 1){
            newDamage = 1;
        }

        Bukkit.getPlayer("Ne0nx3r0").sendMessage(String.format("level: %s hp: %s damage: %s newDamage: %s", new Object[]{
            level,
            hp,
            damage,
            newDamage
        }));
        
        return newDamage;
    }
}
