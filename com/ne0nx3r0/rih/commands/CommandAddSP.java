package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.spawning.SpawnPoint;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandAddSP extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandAddSP(RareItemHunterPlugin plugin) {
        super(
            "addsp",
            "<name> <radius>",
            "Add a new spawn point at your current location",
            "rih.admin.addsp"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs, "Not from console.");
            
            return true;
        }
        
        if(args.length < 3){
            this.send(cs,this.getUsage());
            
            return true;
        }
        
        String spName = args[1];
        String sRadius = args[2];
        int radius;
        
        try{
            radius = Integer.parseInt(sRadius);
        }
        catch(NumberFormatException ex){
            this.sendError(cs, "Invalid radius: "+sRadius);
            
            return true;
        }
        
        SpawnPoint sp = plugin.getSpawnPointManager().getSpawnPoint(spName);
        
        if(sp != null){
            this.sendError(cs, spName+" exists already!");
            
            return true;
        }
        
        Location spl = ((Player) cs).getLocation();
        
        plugin.getSpawnPointManager().setSpawnPoint(spName,spl,radius);
        
        this.send(cs, String.format("Added spawn point %s at X=%s Z=%s Radius=%s", new Object[]{
            spName,
            spl.getBlockX(),
            spl.getBlockZ(),
            radius
        }));
        
        return true;
    }
}
