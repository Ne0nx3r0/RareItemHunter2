
package com.ne0nx3r0.rih.boss.spawning;

import org.bukkit.Location;

public class SpawnPoint {
    private final int radius;
    private final Location location;
    private final String name;
    
    public SpawnPoint(String name,Location location,int radius){
        this.name = name;
        this.location = location;
        this.radius = radius;
    }
    
    public Location getLocation(){
        return this.location;
    }
    
    public int getRadius(){
        return this.radius;
    }
    
    public String getName(){
        return this.name;
    }
}
