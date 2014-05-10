package com.ne0nx3r0.rih.listeners;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.boss.BossTemplate;
import com.ne0nx3r0.rih.boss.skills.BossSkill;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import com.ne0nx3r0.util.FireworkVisualEffect;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
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
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;
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
            return;
        }
        
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
                    e.setCancelled(true);
                    return;
                // caught already by damagedbyentity
                case ENTITY_ATTACK:
                case PROJECTILE:
                case MAGIC:
                    return;
            }
        }

        this.bossDamaged(e.getEntity(),boss, null, e.getDamage());

        e.setDamage(1d);

        LivingEntity lent = (LivingEntity) e.getEntity();

        lent.setHealth(lent.getMaxHealth());
    }
    
    // Priority is primarily meant to let other plugins adjust damage
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onBossDamagedByEntity(EntityDamageByEntityEvent e)
    {
        if(e.getCause() != ENTITY_ATTACK 
        && e.getCause() != PROJECTILE 
        && e.getCause() != MAGIC){
            return;
        }
        
        Boss boss = this.bossManager.getBoss(e.getEntity());
        
        if(boss == null){
            return;
        }
        
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
        
        this.bossDamaged(e.getEntity(),boss, pAttacker, e.getDamage());
        
        e.setDamage(1d);
        
        LivingEntity lent = (LivingEntity) e.getEntity();
        
        lent.setHealth(lent.getMaxHealth());
    }
    
    public void bossDamaged(Entity eBoss,Boss boss,Player attacker,double damageAmount){
        
        if(attacker != null){
            boss.addPlayerDamage(attacker, (int) damageAmount);
        }
        
        int remainingHealth = boss.getHealth();
        
        remainingHealth -= damageAmount;
        
        // Still alive
        if(remainingHealth > 0){
            boss.setHealth(remainingHealth);
            
            int maxHealth = boss.getTemplate().getMaxHealth();
            String bossName = boss.getTemplate().getName();
            
            ((LivingEntity) eBoss).setCustomName(String.format("%s %sHP / %sHP",new Object[]{
                bossName,
                remainingHealth,
                maxHealth
            }));
                
            if(attacker != null){
                attacker.sendMessage(bossName+" HP: "+remainingHealth+"/"+maxHealth);
            }
        }
        // Dead
        else {
            try
            {
                new FireworkVisualEffect().playFirework(
                    eBoss.getWorld(), eBoss.getLocation(),
                    FireworkEffect
                        .builder()
                        .with(FireworkEffect.Type.CREEPER)
                        .withColor(Color.RED)
                        .build()
                );
            }
            catch (Exception ex)
            {
                // fireworks not supported
            }

            if(attacker instanceof Player)
            {
                Player pAttacker = (Player) attacker;

                Bukkit.getServer().broadcastMessage(pAttacker.getName()+ChatColor.DARK_GREEN+" has defeated legendary boss "+ChatColor.WHITE+boss.getTemplate().getName()+ChatColor.GREEN+"!");
            }
            else
            {
                Bukkit.getServer().broadcastMessage("A legendary boss has been defeated!");
            }

            this.bossManager.removeBoss(boss);
        }
    
        if(attacker != null){
            Random random = new Random();
            BossTemplate bossTemplate = boss.getTemplate();
            
            for(BossSkill skill : bossTemplate.getOnHitSkills()){
                if(random.nextInt() < skill.getChance()){
                    
                    BossSkillTemplate skillTemplate = skill.getSkillTemplate();
                    
                    if(attacker != null){
                        if(skillTemplate.activateSkill(boss, attacker, remainingHealth)){
                            attacker.sendMessage(bossTemplate.getName()+" used "+skillTemplate.getName()+"!");
                            
                            break;
                        }
                    }
                    else {
                        if(skillTemplate.activateSkill(boss, remainingHealth)){
                            
                            break;
                        }
                    }
                    
                }
            }
        }
    }
}
