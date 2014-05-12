package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LightningStorm extends BossSkillTemplate
{
    public LightningStorm()
    {
        super("Lightning Storm");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {     
        if(!(eAttacker instanceof Player))
        {
            return false;
        }
        
        int count = 0;
        
        for(Entity ent : e.getEntity().getNearbyEntities(20, 20, 20))
        {
            if(count < level)
            {
                if(ent instanceof Player)
                {
                    ent.getWorld().strikeLightning(ent.getLocation());
                    
                    count++;
                }
            }
            else
            {
                break;
            }
        }
        
        return true;
    }
}
