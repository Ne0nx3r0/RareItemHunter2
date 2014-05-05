package com.ne0nx3r0.rih;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;

public class RareItemHunterBossListener implements Listener {
    private BossManager bossManager;

    public RareItemHunterBossListener(RareItemHunterPlugin plugin) {
        this.bossManager = plugin.getBossManager();
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onBossCombust(EntityCombustEvent e)
    {
        //TODO: fire immunity, rather than every boss being immune
        if(bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onBossAttemptTame(EntityTameEvent e)
    {
        if(bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onBossDeath(EntityDeathEvent e)
    {
        if(bossManager.isBoss(e.getEntity()))
        {
            e.setDroppedExp(0);
            e.getEntity().getEquipment().clear();
        }
    }
    
    
    // Priority is primarily meant to let other plugins adjust damage
    @EventHandler(priority=EventPriority.MONITOR,ignoreCancelled = true)
    public void onBossDamaged(EntityDamageEvent e)
    {
        Boss boss = this.bossManager.getBoss(e.getEntity());
        
        if(boss == null){
            System.out.println("------------EDE (not boss)------------");
            
            return;
        }
        
        e.setCancelled(true);
                    
        System.out.println("------------EDE------------");
        System.out.println(e.getEntity());
        System.out.println(e.getCause());
        System.out.println("");
        
        if (e.getEntity() != null && this.bossManager.isBoss(e.getEntity()))
        {
            switch(e.getCause()){
                case SUFFOCATION:
                case CONTACT:
                case FIRE:
                case FIRE_TICK:
                case MELTING:
                case LAVA:
                case FALL:
                case DROWNING:
                case FALLING_BLOCK:
                    return;
            }
        }
        
        System.out.println("sent to bossdamage");
        System.out.println("");

        this.bossDamaged(boss, null, e.getDamage());
        
        e.setDamage(1d);
        
        LivingEntity lent = (LivingEntity) e.getEntity();
        
        lent.setHealth(lent.getMaxHealth());
    }
    
    // Priority is primarily meant to let other plugins adjust damage
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onBossDamagedByEntity(EntityDamageByEntityEvent e)
    {
        Boss boss = this.bossManager.getBoss(e.getEntity());
        
        if(boss == null){
            System.out.println("------------EDBE (not boss)------------");
            
            return;
        }
        
        e.setCancelled(true);
        
        Entity eAttacker = e.getDamager();
        Player pAttacker = null;
        
        if(eAttacker instanceof Player){
           pAttacker = (Player) eAttacker;
        }
        else if(eAttacker instanceof Projectile){
            LivingEntity leShooter = ((Projectile) eAttacker).getShooter();
            
            if(leShooter instanceof Player){
                pAttacker = (Player) leShooter;
            }
        }
        
        this.bossDamaged(boss, pAttacker, e.getDamage());
        
        e.setDamage(1d);
        
        LivingEntity lent = (LivingEntity) e.getEntity();
        
        lent.setHealth(lent.getMaxHealth());
        
        System.out.println("------------EDBE------------");
        System.out.println(e.getEntity());
        System.out.println(e.getDamager());
        System.out.println(e.getDamager().getClass());
        System.out.println("");
    }
    
    public void bossDamaged(Boss boss,Player attacker,double damageAmount){
        
        if(attacker != null){
            boss.addPlayerDamage(attacker, (int) damageAmount);
        }
        
        int remainingHealth = boss.getHealth();
        
        remainingHealth -= damageAmount;
        
        // Still alive
        if(remainingHealth > 0){
            boss.setHealth(remainingHealth);
        }
        // Dead
        else {
            
        }
    }
}
