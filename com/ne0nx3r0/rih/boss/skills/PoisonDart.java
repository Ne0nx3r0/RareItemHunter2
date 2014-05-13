package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonDart extends BossSkillTemplate
{
    public PoisonDart()
    {
        super("Poison Dart");
    }
    
    @Override    
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){   
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20*10,level));

        return true;
    }
}
