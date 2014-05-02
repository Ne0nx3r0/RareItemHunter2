package com.ne0nx3r0.rih.boss;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossSkillTemplate
{    
    String name;
    
    public BossSkillTemplate(String name)
    {
        this.name = name;
    }
    
    public boolean activateSkill(Boss boss, EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {
        return false;
    }

    public String getName()
    {
        return this.name;
    }
}
