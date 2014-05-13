package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;

public class ShootArrow extends BossSkillTemplate
{
    public ShootArrow()
    {
        super("Shoot Arrow");
    }

    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){   
        bossEntity.launchProjectile(Arrow.class);
        
        return true;
    }
}
