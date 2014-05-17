package com.ne0nx3r0.rih.property;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class RareItemProperty {
    private final int id;
    private final String name;
    private final String description;
    private RareItemPropertyCostType costType;
    private double cost;
    private final int maxLevel;
    
    public RareItemProperty(int id,String name,String description,RareItemPropertyCostType costType,double cost,int maxLevel){
        this.id = id;
        this.name = name;
        this.description = description;
        this.costType = costType;
        this.cost = cost;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return this.name;
    }
    
    public String getDescription(){
        return this.description;
    }

    public int getID() {
        return this.id;
    }
    
    public int getMaxLevel(){
        return this.maxLevel;
    }
    
    public double getCost(){
        return this.cost;
    }

    void setCost(double cost) {
        this.cost = cost;
    }
    
    public RareItemPropertyCostType getCostType(){
        return this.costType;
    }

    void setCostType(RareItemPropertyCostType costType) {
        this.costType = costType;
    }
    
// events
    public boolean onInteract(PlayerInteractEvent e, int level){return false;}

    public boolean onInteractEntity(PlayerInteractEntityEvent e, int level){return false;}
    
    public boolean onDamageOther(EntityDamageByEntityEvent e, Player attacker, int level){return false;}

// Defaults to the same action as a melee attack, but offers optional overwritting.
    public boolean onArrowHitEntity(EntityDamageByEntityEvent e, Player shooter, int level)
    {
        return this.onDamageOther(e, shooter, level);
    }

    public boolean onArrowHitGround(ProjectileHitEvent e, Player shooter, int level){return false;}

    public void onEquip(Player p, int level){}
    
    public void onUnequip(Player p, int level){}
}
