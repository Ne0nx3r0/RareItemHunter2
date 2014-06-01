package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DimensionDoor extends BossSkillTemplate
{
    public DimensionDoor()
    {
        super("Dimension Door");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){  
        String bossWorld = bossEntity.getWorld().getName();
        World warpTo;
        
        if(bossWorld.endsWith("_the_end")){
            warpTo = bossEntity.getServer().getWorld(bossWorld.substring(bossWorld.indexOf("_the_end")));
        }
        else if(bossWorld.endsWith("_nether")){
            warpTo = bossEntity.getServer().getWorld(bossWorld.substring(bossWorld.indexOf("_nether"))+"_the_end");
        }
        else {
            warpTo = bossEntity.getServer().getWorld(bossWorld+"_nether");
        }
        
        if(warpTo == null){
            return false;
        }
        
        int distance = 30^2;
        int warped = 0;
        
        Location bossLocation = bossEntity.getLocation();
        
        for(Player p : bossEntity.getWorld().getPlayers()){
            if(bossLocation.distanceSquared(p.getLocation()) < distance){
                Location pTo = p.getLocation();
                
                pTo.setWorld(warpTo);
                
                p.teleport(pTo);

                target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,30,level));
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,30,level));
                
                warped++;
            }
        }
        
        if(warped > 0){
            Location bossTo = bossEntity.getLocation();
            
            bossTo.setWorld(warpTo);
            
            bossEntity.teleport(bossTo);
            
            return true;
        }
        
        return false;
    }
}
