package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class Freeze extends BossSkillTemplate
{
    public Freeze()
    {
        super("Freeze");
    }

    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){   
        boolean activated = false;
        
        int range = level;
        
        if(range > 8){
            range = 8;
        }
        //TODO: add max level to constructor of bossSkillTemplate
        
        Location lStart = bossEntity.getLocation();
        
        int endX = lStart.getBlockX() + range;
        int endY = lStart.getBlockY() + 1;
        int endZ = lStart.getBlockZ() + range;
        
        World world = lStart.getWorld();
        
        for(int x = lStart.getBlockX()-range;x < endX; x++){
            for(int y = lStart.getBlockY()-range;y < endY; y++){
                for(int z = lStart.getBlockZ()-range;z < endZ; z++){
                    Block b = world.getBlockAt(x,y,z);
                    
                    if(b.getType().equals(Material.WATER)){
                        b.setType(Material.ICE);
                        
                        activated = true;
                    }
                    else if(b.getType().equals(Material.LAVA)){
                        b.setType(Material.OBSIDIAN);
                        
                        activated = true;
                    }
                }
            }
        }
        
        if(activated){
            bossEntity.teleport(lStart.add(0, 1, 0));
        }
        
        return activated;
    }
}
