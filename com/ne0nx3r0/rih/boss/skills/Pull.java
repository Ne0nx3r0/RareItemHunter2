package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Pull extends BossSkillTemplate
{
    public Pull()
    {
        super("Pull");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        if(e.getEntity() instanceof LivingEntity)
        {        
            LivingEntity le = (LivingEntity) eAttacker;

            eAttacker.teleport(e.getEntity());

            return true;
        }
        return false;
    }
}
