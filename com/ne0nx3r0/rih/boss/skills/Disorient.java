package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Disorient extends BossSkillTemplate
{
    public Disorient()
    {
        super("Disorient");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level)
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,20 * 5 * level,level));
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20 * 5 * level,level));

        return true;
    }
}
