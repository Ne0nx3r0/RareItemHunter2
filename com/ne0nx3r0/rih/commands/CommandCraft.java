package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandCraft extends RareItemHunterCommand{
    
    public CommandCraft(RareItemHunterPlugin plugin) {
        super(
            "craft",
            "",
            "open a RI crafting screen",
            "rih.tester"
        );
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }

        Player p = (Player) cs;
        
        p.openWorkbench(p.getLocation(), true);
        
        return true;
    }
}
