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
        return false;
    }

    public String getName()
    {
        return this.name;
    }
}
