package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;

public class ShootFireball extends BossSkillTemplate
{
    public ShootFireball()
    {
        super("Shoot Fireball");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){   
        bossEntity.launchProjectile(Fireball.class);
        
        return true;
    }
}
