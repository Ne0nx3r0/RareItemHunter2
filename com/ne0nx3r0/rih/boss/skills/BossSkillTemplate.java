package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.LivingEntity;

public class BossSkillTemplate
{    
    String name;
    
    public BossSkillTemplate(String name)
    {
        this.name = name;
    }
    
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){
        return this.activateOnHitSkill(bossEntity, boss, target, level);
    }
    
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level){
        return this.activateSkill(bossEntity, boss, level);
    }
    
    public boolean activateSkill(LivingEntity bossEntity, Boss boss, int level){
        return false;
    }

    public String getName()
    {
        return this.name;
    }
}
