package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.LivingEntity;

public class LightningBolt extends BossSkillTemplate
{
    public LightningBolt()
    {
        super("Lightning Bolt");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){   
        target.getWorld().strikeLightning(target.getLocation());
        
        return true;
    }
}
