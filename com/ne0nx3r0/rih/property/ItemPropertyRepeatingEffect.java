package com.ne0nx3r0.rih.property;

import org.bukkit.entity.Player;

public class ItemPropertyRepeatingEffect extends RareItemProperty
{
    public ItemPropertyRepeatingEffect(int id, String name, String description, PropertyCostType defaultCostType, double defaultCost, int maxLevel) {
        super(id, name, description, defaultCostType, defaultCost, maxLevel);
    }
    
    public void applyEffectToPlayer(Player p,int level){}
    
    public void removeEffectFromPlayer(Player p){}
}
