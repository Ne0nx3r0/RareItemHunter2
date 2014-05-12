package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.util.FireworkVisualEffect;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class GreaterBurst extends BossSkillTemplate
{
    public GreaterBurst()
    {
        super("Greater Burst");
    }
    
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){  
        List<Entity> nearbyEntities = bossEntity.getNearbyEntities(8, 8, 8);
        
        if(!nearbyEntities.isEmpty())
        {
            boolean showFx = false;
            Vector vBoss = bossEntity.getVelocity();
            int targetsLeft = 8;
            
            for(Entity ent : nearbyEntities)
            {
                if(ent instanceof LivingEntity && targetsLeft > 0)
                {
                    Vector unitVector = ent.getLocation().toVector().subtract(vBoss).normalize();

                    unitVector.setY(0.55/level);

                    ent.setVelocity(unitVector.multiply(level*2));
                    
                    showFx = true;
                    
                    targetsLeft--;
                }
            }
            
            if(showFx)
            {
                try
                {
                    new FireworkVisualEffect().playFirework(
                        bossEntity.getWorld(), bossEntity.getLocation(),
                        FireworkEffect
                            .builder()
                            .with(FireworkEffect.Type.BALL_LARGE)
                            .withColor(Color.WHITE)
                            .build()
                    );
                }
                catch (Exception ex){}
            }
            else
            {
                return false;
            }
            
            return true;
        }
        
        return false;
    }
}
