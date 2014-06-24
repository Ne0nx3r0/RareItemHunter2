package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import net.minecraft.server.v1_7_R3.Entity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class DimensionDoor extends BossSkillTemplate
{
    public DimensionDoor()
    {
        super("Dimension Door");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){  
        String bossWorld = bossEntity.getWorld().getName();
        World worldTo;
        
        if(bossWorld.endsWith("_the_end")){
            worldTo = bossEntity.getServer().getWorld(bossWorld.substring(0,bossWorld.indexOf("_the_end")));
        }
        /*else if(bossWorld.endsWith("_nether")){
            worldTo = bossEntity.getServer().getWorld(bossWorld.substring(0,bossWorld.indexOf("_nether"))+"_the_end");
        }*/
        else {
            //worldTo = bossEntity.getServer().getWorld(bossWorld+"_nether");
            worldTo = bossEntity.getServer().getWorld(bossWorld+"_the_end");
        }
        
        if(worldTo == null){
            return false;
        }
        
        int distance = 60^2;
        int warped = 0;
        
        Location bossLocation = bossEntity.getLocation();
        
        for(Player p : bossEntity.getWorld().getPlayers()){
            if(bossLocation.distanceSquared(p.getLocation()) < distance){
                Location pTo = p.getLocation();
                
                pTo.setWorld(worldTo);
                
                p.teleport(pTo);
                
                warped++;
            }
        }
        
        if(warped > 0){
            Location bossTo = bossEntity.getLocation();
            
            Entity bossHandle = ((CraftEntity) bossEntity).getHandle();
            
            bossHandle.world.removeEntity(bossHandle);
            bossHandle.dead = false;
            bossHandle.world = ((CraftWorld) worldTo).getHandle();
            bossHandle.setLocation(bossTo.getX(), bossTo.getY()+1, bossTo.getZ(), bossTo.getYaw(), bossTo.getPitch());
            bossHandle.world.addEntity(bossHandle);
  
            boss.setEntity((LivingEntity) bossHandle.getBukkitEntity());
            
            return true;
        }
        
        return false;
    }
}