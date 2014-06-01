package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.spawning.SpawnPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandSP extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandSP(RareItemHunterPlugin plugin) {
        super(
            "sp",
            "",
            "List available spawn points",
            "rih.admin.sp"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }

        Collection<SpawnPoint> spawnPoints = plugin.getSpawnPointManager().getAllSpawnPoints();
        
        List<String> messages = new ArrayList<>();
        
        messages.add("These are the available spawn points:");
        
        for(SpawnPoint sp : spawnPoints){
            Location l = sp.getLocation();
            
            messages.add(String.format(ChatColor.GREEN+"%s "+ChatColor.GRAY+"- R=%s, X=%s, Y=%s, Z=%s)",new Object[]{
                sp.getName(),
                sp.getRadius(),
                l.getBlockX(),
                l.getBlockY(),
                l.getBlockZ()
            }));
        }
        
        this.send(cs, messages.toArray(new String[messages.size()]));
        
        return true;
    }
}
