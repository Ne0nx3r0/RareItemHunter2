package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.Entity;

public class BossSkillTemplate
{    
    String name;
    
    public BossSkillTemplate(String name)
    {
        this.name = name;
    }
    
    public boolean activateOnHitSkill(Boss boss, Entity target, int level, int damageTaken){
        return this.activateSkill(boss, target, level);
    }
    
    public boolean activateSkill(Boss boss, Entity target, int level)
    {
        return false;
    }
    
    public boolean activateSkill(Boss boss, int level)
    {
        return false;
    }

    public String getName()
    {
        return this.name;
    }
}
