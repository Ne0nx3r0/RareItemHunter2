package com.ne0nx3r0.rih.boss;

import java.util.UUID;

public class Boss {
    private final UUID entityUUID;
    private final BossTemplate template;
    private int currentHealth;
    private int kills = 0;
    
    public Boss(UUID entityUUID,BossTemplate template){
        this.entityUUID = entityUUID;
        this.template = template;
        this.currentHealth = template.getMaxHealth();
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
}
