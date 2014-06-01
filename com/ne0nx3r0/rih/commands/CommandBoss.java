package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.boss.BossTemplate;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandBoss extends RareItemHunterCommand{
    private final BossManager bossManager;

    public CommandBoss(RareItemHunterPlugin plugin) {
        super(
            "boss",
            "<bossName> <spawnpoint|here>",
            "Testing command",
            "rih.admin.boss"
        );
        
        this.bossManager = plugin.getBossManager();
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(args.length < 3){
            StringBuilder sb = new StringBuilder();
            
            for(BossTemplate bt : bossManager.getAllBossTemplates()){
                sb.append(", ").append(bt.getName());
            }
            
            sb.substring(2);
            
            this.send(cs, new String[]{
                "/ri "+this.getName()+" "+this.getUsageArguments(),
                "",
                "Here are the available bosses:",
                sb.toString()
            });
            
            return true;
        }
        
        if(!(cs instanceof Player) && args[2].equalsIgnoreCase("here")){
            this.sendError(cs,"\"here\" cannot be used from the console.");
            
            return true;
        }
        
        String bossName = args[1].replace("_"," ");
        String sSpawnAt = args[2];
        
        if(!this.bossManager.isValidBossName(bossName)){
            this.sendError(cs,bossName+" is not a valid boss!");
            
            return true;
        }
        
        Location lSpawnAt;
        
        if(sSpawnAt.equalsIgnoreCase("here")){
            lSpawnAt = ((Player) cs).getLocation();
        }
        else{
            this.sendError(cs, "Spawn points not implemented yet");
            
            return true;
            //lSpawnAt = this.spawnPointManager.getRandomLocationAt(sSpawnAt);
        }
        
        Boss boss = this.bossManager.spawnBossAt(bossName,lSpawnAt);
        
        this.send(cs,String.format("Spawned a %s at %s %s %s!",new Object[]{
            boss.getTemplate().getName(),
            lSpawnAt.getBlockX(),
            lSpawnAt.getBlockY(),
            lSpawnAt.getBlockZ()
        }));
        
        return true;
    }
}
