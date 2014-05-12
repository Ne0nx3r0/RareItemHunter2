package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.util.FireworkVisualEffect;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Burst extends BossSkillTemplate
{
    public Burst()
    {
        super("Burst");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){      
        try
        {
            new FireworkVisualEffect().playFirework(
                target.getWorld(),
                target.getLocation(),
                FireworkEffect
                    .builder()
                    .with(FireworkEffect.Type.BURST)
                    .withColor(Color.WHITE)
                    .build()
            );
        }
        catch (Exception ex){}

        Vector unitVector = target.getLocation().toVector().subtract(bossEntity.getLocation().toVector()).normalize();

        unitVector.setY(0.55/level);

        target.setVelocity(unitVector.multiply(2 * level));

        return true;
    }
}
