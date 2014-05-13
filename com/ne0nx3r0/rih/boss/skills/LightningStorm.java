package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class LightningStorm extends BossSkillTemplate
{
    public LightningStorm()
    {
        super("Lightning Storm");
    }

    @Override
    public boolean activateOnHitSkill(LivingEntity eBoss, Boss boss, LivingEntity target, int level, int damageTaken){   
        double maxDistanceSquared = 30^2;
        
        Location lBoss = eBoss.getLocation();
        
        Random r = new Random();
        
        for(Player p : eBoss.getWorld().getPlayers()){
            Location lPlayer = p.getLocation();
            
            if(lBoss.distanceSquared(lPlayer) < maxDistanceSquared){
                for(int i = 0;i<level;i++){
                    if(r.nextBoolean()){
                        eBoss.getWorld().strikeLightning(lPlayer);
                    }
                }
            }
        }
        
        return true;
    }
}
