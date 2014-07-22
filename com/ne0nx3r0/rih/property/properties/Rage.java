package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;

public class Rage extends RareItemProperty
{
    public Rage()
    {
        super(
            PropertyType.RAGE.ordinal(),
            "Rage",
            "While worn as armor or a helmet damage is decreased at high health, but heavily increased at low health. Higher levels modify damage even more",
            PropertyCostType.PASSIVE,
            0,
            5
        );
    }
    
    public double getModifiedDamage(double level,double hp,double damage){
        if(hp == 10.0){
            return damage;
        }
        
        double levelModifier = 0.15D + level * 0.6D;
        
        double hpModifier = (hp - 10.0D) * -1D;
        
        double newDamage = damage + levelModifier * hpModifier;
        
        if(newDamage < 1){
            newDamage = 1;
        }
        
        return newDamage;
    }
}
