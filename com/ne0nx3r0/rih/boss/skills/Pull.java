package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.LivingEntity;

public class Pull extends BossSkillTemplate
{
    public Pull()
    {
        super("Pull");
    }
    
    @Override    
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){
        bossEntity.teleport(target);

        return true;
    }
}
