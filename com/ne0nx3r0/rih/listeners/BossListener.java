package com.ne0nx3r0.rih.listeners;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.boss.BossTemplate;
import com.ne0nx3r0.rih.boss.skills.BossSkillInstance;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import com.ne0nx3r0.util.FireworkVisualEffect;
import com.ne0nx3r0.util.RandomCollection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
import org.bukkit.inventory.ItemStack;

public class BossListener implements Listener {
    private final BossManager bossManager;
    private final Essentials essentials;
    private RareItemHunterPlugin plugin;

    public BossListener(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
        this.bossManager = plugin.getBossManager();
        
        this.essentials = plugin.getEssentials();
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
    public void onAttemptToTameBoss(EntityTameEvent e)
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

        this.handleBossDamage((LivingEntity) e.getEntity(),boss, null, e.getDamage());

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
        
        this.handleBossDamage((LivingEntity) e.getEntity(),boss, pAttacker, e.getDamage());
        
        e.setDamage(0d);
        
        LivingEntity lent = (LivingEntity) e.getEntity();
        
        lent.setHealth(lent.getMaxHealth());
    }
    
    public void handleBossDamage(LivingEntity eBoss,Boss boss,Player attacker,double damageAmount){
        
        if(attacker != null){
            boss.addPlayerDamage(attacker, (int) damageAmount);
        
            // Disable GM if they have turned it on. Suckers. :D
            if(attacker.getGameMode().equals(GameMode.CREATIVE)) {
                attacker.setGameMode(GameMode.SURVIVAL);
            }

            // and flying
            if(attacker.isFlying()){
                attacker.setFlying(false);
            }
            
            // and essentials god mode
            if(this.essentials != null) {
                User user = essentials.getUser(attacker);

                if(user.isGodModeEnabled()) {
                    user.setGodModeEnabled(false);
                }
            }
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
            
            eBoss.damage(eBoss.getHealth()+100);
            
// Prepare a list of players close enough to get an essence
            RandomCollection<Player> randomPlayer = new RandomCollection();
            int maxDistanceFromBossToGetReward = 50^2;
            Location lBoss = eBoss.getLocation();
            
            Iterator<Map.Entry<String, Integer>> iter = boss.getPlayersDamageDone().entrySet().iterator();
            
            while(iter.hasNext()){
                Map.Entry<String, Integer> entry = iter.next();
                
                Player p = plugin.getServer().getPlayer(entry.getKey());
                
                if(p != null){
                    if(p.getLocation().getWorld().equals(lBoss.getWorld())
                    && lBoss.distanceSquared(p.getLocation()) < maxDistanceFromBossToGetReward){
                        randomPlayer.add(entry.getValue(), p);
                    }
                }
            }
            
            Player pLucky = randomPlayer.next();
            
            ItemStack essence = this.plugin.getRecipeManager().generateRareEssence();
            
            if(!pLucky.getInventory().addItem(essence).isEmpty()) {
                pLucky.getWorld().dropItemNaturally(pLucky.getLocation(), essence);
            }
            
            plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN+boss.getTemplate().getName()+ChatColor.RESET+"'s essence captured by "+pLucky.getDisplayName()+ChatColor.RESET+"!");
            
            StringBuilder playerDamages = new StringBuilder();
            
            for(Map.Entry<String, Integer> entry : boss.getPlayersDamageDone().entrySet()){
                playerDamages.append(", ").append(entry.getKey()).append("(").append(entry.getValue()).append(")");
            }

            plugin.getServer().broadcastMessage("Player damage to boss: "+playerDamages.substring(2));
            
            return;
        }

        // attempt to use a random onhit skill
        Random random = new Random();
        BossTemplate bossTemplate = boss.getTemplate();

        for(BossSkillInstance skill : bossTemplate.getOnHitSkills()){
            if(random.nextInt() < skill.getChance()){
                BossSkillTemplate skillTemplate = skill.getSkillTemplate();

                if(attacker != null){
                    if(skillTemplate.activateOnHitSkill(eBoss, boss, attacker, remainingHealth, (int) damageAmount
                    )){
                        attacker.sendMessage(bossTemplate.getName()+" used "+skillTemplate.getName()+"!");

                        break;
                    }
                }
                else {
                    LivingEntity target = null;
                    // target nearest player
                    double currentMinDistance = 15^2;
                    Location lBoss = eBoss.getLocation();

                    for(Player p : eBoss.getWorld().getPlayers()){
                        double tempMinDistance = lBoss.distanceSquared(p.getLocation());

                        if(tempMinDistance < currentMinDistance){
                            target = p;
                            currentMinDistance = tempMinDistance;
                        }
                    }

                    if(target != null){
                        if(skillTemplate.activateOnHitSkill(eBoss, boss, target, remainingHealth, (int) damageAmount)){

                            break;
                        }
                    }
                }

            }
        }
    }
}
