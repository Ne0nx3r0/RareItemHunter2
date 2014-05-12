package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.LivingEntity;

public class JumpAttack extends BossSkillTemplate
{
    public JumpAttack()
    {
        super("Jump Attack");
    }
    

    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){          
        bossEntity.teleport(target.getLocation());
        
        return true;
    }
}
