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

class CommandDelSP extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandDelSP(RareItemHunterPlugin plugin) {
        super(
            "delsp",
            "<name>",
            "delete a spawn point",
            "rih.admin.delsp"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs, "Not from console.");
            
            return true;
        }
        
        if(args.length < 2){
            this.send(cs,this.getUsage());
            
            return true;
        }
        
        String spName = args[1];
        
        if(!plugin.getSpawnPointManager().delSpawnPoint(spName)){
            this.sendError(cs, spName+" is not a valid spawn point.");
            
            return true;
        }
        
        this.send(cs, spName+" was deleted!");
        
        return true;
    }
}
