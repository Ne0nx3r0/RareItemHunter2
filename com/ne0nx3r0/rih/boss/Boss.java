package com.ne0nx3r0.rih.boss;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class Boss {
    private final UUID entityUUID;
    private final BossTemplate template;
    private int currentHealth;
    private int kills = 0;
    private final Map<String,Integer> playerDamage;

    Boss(UUID entityID, BossTemplate template) {
        this.entityUUID = entityID;
        this.template = template;
        this.currentHealth = template.getMaxHealth();
        this.playerDamage = new HashMap<>();
    }

    Boss(UUID uuid, BossTemplate template, int currentHealth, int kills, Map<String, Integer> playerDamage) {
        this.entityUUID = uuid;
        this.template = template;
        this.currentHealth = currentHealth;
        this.kills = kills;
        this.playerDamage = playerDamage;
    }
    
    public BossTemplate getTemplate(){
        return this.template;
    }
    
    public UUID getUniqueID(){
        return this.entityUUID;
    }
    
    public int getHealth(){
        return this.currentHealth;
    }
    
    public void setHealth(int health){
        this.currentHealth = health;
    }
    
    public int getKills(){
        return this.kills;
    }
    
    public void setKills(int kills){
        this.kills = kills;
    }
    
    public Map<String,Integer> getPlayersDamageDone(){
        return this.playerDamage;
    }
    
    public void addPlayerDamage(Player player,int damageAmount){
        String playerName = player.getName().toLowerCase();
        
        Integer damageDone = this.playerDamage.get(playerName);
        
        if(damageDone != null){
            this.playerDamage.put(playerName,damageAmount);
        }
        else
        {
            this.playerDamage.put(playerName,damageAmount);
        }
    }
}
