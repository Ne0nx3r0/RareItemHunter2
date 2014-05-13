package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class BossSkillTemplateSpawn extends BossSkillTemplate
{
    private final EntityType entityType;
    
    public BossSkillTemplateSpawn(String sName,EntityType e)
    {
        super(sName);
        
        this.entityType = e;
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){  
        this.spawnMobs(target.getLocation(),level);
        
        return true;
    }
    
    public void spawnMobs(Location l,int level){        
        World w = l.getWorld();
        
        for(int i=0;i<level;i++)
        {
            w.spawnEntity(l, this.entityType);
        }
    }
}
