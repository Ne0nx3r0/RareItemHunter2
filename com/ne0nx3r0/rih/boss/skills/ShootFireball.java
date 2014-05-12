package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ShootFireball extends BossSkillTemplate
{
    public ShootFireball()
    {
        super("Shoot Fireball");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        LivingEntity leBoss = (LivingEntity) e.getEntity();   
        
        leBoss.launchProjectile(Fireball.class);
        
        return true;
    }
}
