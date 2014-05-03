package com.ne0nx3r0.rih;

import com.ne0nx3r0.rih.boss.BossManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTameEvent;

public class RareItemHunterBossListener implements Listener {
    private BossManager bossManager;

    public RareItemHunterBossListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void Damage(EntityDamageByEntityEvent e)
    {
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e)
    {
        if (e.getEntity() != null && this.bossManager.isBoss(e.getEntity().getUniqueId()))
        {
            if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK 
            && e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE 
            && e.getCause() != EntityDamageEvent.DamageCause.MAGIC)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityCombust(EntityCombustEvent e)
    {
        //TODO: fire immunity, rather than every boss being immune
        if(bossManager.isBoss(e.getEntity().getUniqueId()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent e)
    {
        if(bossManager.isBoss(e.getEntity().getUniqueId()))
        {
            e.setCancelled(true);
        }
    }
}
