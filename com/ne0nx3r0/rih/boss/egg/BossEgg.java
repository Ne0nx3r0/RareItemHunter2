package com.ne0nx3r0.rih.boss.egg;

import org.bukkit.Location;

public class BossEgg {
    private final String bossName;
    private final boolean autoSpawn;
    private final Location location;
            
    public BossEgg(String bossName,Location location,boolean autoSpawn) {
        this.bossName = bossName;
        this.location = location;
        this.autoSpawn = autoSpawn;
    }
    
    public String getName() {
        return this.bossName;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public boolean getAutoSpawn() {
        return this.autoSpawn;
    }
    
    
}
