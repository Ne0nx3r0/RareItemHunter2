package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class Blink extends BossSkillTemplate
{
    public Blink()
    {
        super("Blink");
    }

    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){   
        Location blinkTo = bossEntity.getLocation();
        
        Random random = new Random();
        
        blinkTo.add(random.nextInt(10), 0, random.nextInt(10));
        
        bossEntity.teleport(blinkTo);
        
        return true;
    }
}
