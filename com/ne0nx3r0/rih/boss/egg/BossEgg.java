package com.ne0nx3r0.rih.boss.egg;

import com.ne0nx3r0.rih.boss.BossTemplate;
import org.bukkit.Location;

public class BossEgg {
    private final BossTemplate boss;
    private final boolean autoSpawn;
    private final Location location;
            
    public BossEgg(BossTemplate template,Location location,boolean autoSpawn) {
        this.boss = template;
        this.location = location;
        this.autoSpawn = autoSpawn;
    }
    
    public BossTemplate getTemplate() {
        return this.boss;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public boolean getAutoSpawn() {
        return this.autoSpawn;
    }
    
    
}
