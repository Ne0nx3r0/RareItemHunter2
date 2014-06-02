package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.boss.BossTemplate;
import com.ne0nx3r0.rih.boss.egg.BossEgg;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandEgg extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;

    public CommandEgg(RareItemHunterPlugin plugin) {
        super(
            "egg",
            "<boss> <spawnpoint|here>",
            "Spawn a boss egg",
            "rih.admin.egg"
        );
        
        this.plugin = plugin;
    }
    
    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(args.length < 3){
            StringBuilder sb = new StringBuilder();
            
            for(BossTemplate bt : this.plugin.getBossManager().getAllBossTemplates()){
                sb.append(", ").append(bt.getName());
            }
            
            String sBosses = sb.length() > 2 ? sb.substring(2) : "";

            this.send(cs, new String[]{
                "/ri "+this.getName()+" "+this.getUsageArguments(),
                "",
                "Here are the available bosses:",
                sBosses
            });
            
            return true;
        }
        
        if(!(cs instanceof Player) && args[2].equalsIgnoreCase("here")){
            this.sendError(cs,"\"here\" cannot be used from the console.");
            
            return true;
        }
        
        String bossName = args[1].replace("_"," ");
        String sSpawnAt = args[2];
        
        BossManager bm = this.plugin.getBossManager();
        
        if(!bm.isValidBossName(bossName)){
            this.sendError(cs,bossName+" is not a valid boss!");
            
            return true;
        }
        
        Location lSpawnAt;
        
        if(sSpawnAt.equalsIgnoreCase("here")){
            lSpawnAt = ((Player) cs).getLocation();
        }
        else{
            if(!this.plugin.getSpawnPointManager().isValidSpawnPoint(sSpawnAt)){
                this.sendError(cs,bossName+" is not a valid boss!");

                return true;
            }
            
            lSpawnAt = this.plugin.getSpawnPointManager().getRandomLocationAt(sSpawnAt);
            
            if(lSpawnAt == null){
                this.sendError(cs, "Unable to find a random location to use!");
                
                return true;
            } 
        }
        
        BossEgg egg = this.plugin.getBossManager().spawnBossEggAt(bossName,lSpawnAt,true);
        
        this.send(cs,String.format("Spawned a %s egg at %s %s %s!",new Object[]{
            bossName,
            lSpawnAt.getBlockX(),
            lSpawnAt.getBlockY(),
            lSpawnAt.getBlockZ()
        }));
        
        return true;
    }
}