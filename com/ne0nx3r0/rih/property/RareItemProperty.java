package com.ne0nx3r0.rih.property;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class RareItemProperty {
    private final int id;
    private final String name;
    private final String description;
    private PropertyCostType costType;
    private double cost;
    private final int maxLevel;
    private List<String> recipe;

    public RareItemProperty(int id,String name,String description,PropertyCostType defaultCostType,double defaultCost,int maxLevel){
        this.id = id;
        this.name = name;
        this.description = description;
        this.costType = defaultCostType;
        this.cost = defaultCost;
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
    
    public PropertyCostType getCostType(){
        return this.costType;
    }

    void setCostType(PropertyCostType costType) {
        this.costType = costType;
    }
    
// events
    public boolean onInteract(PlayerInteractEvent e, int level){return false;}

    public boolean onInteractEntity(PlayerInteractEntityEvent e, int level){return false;}
    
    public boolean onLaunchProjectile(EntityShootBowEvent e, Player shooter, int level) {return false;}
    
    public void applyEffectToPlayer(Player p,int level){}
    
    public void removeEffectFromPlayer(Player p){}
// The reason player is specified is because arrow events are forwarded here
// and damager requires some extra work to grab from those events
    public boolean onDamageOther(EntityDamageByEntityEvent e, Player attacker, int level){return false;}

// Defaults to the same action as a melee attack, but offers optional overwritting.
    public boolean onArrowHitEntity(EntityDamageByEntityEvent e, Player shooter, int level)
    {
        return this.onDamageOther(e, shooter, level);
    }

    public boolean onArrowHitGround(ProjectileHitEvent e, Player shooter, int level){return false;}

    public void setRecipe(List<String> recipe) {
        this.recipe = recipe;
    }
    
    public List<String> getRecipe(){
        return this.recipe;
    }
}
