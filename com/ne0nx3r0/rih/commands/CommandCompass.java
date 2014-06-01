package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandCompass extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandCompass(RareItemHunterPlugin plugin) {
        super(
            "compass",
            "[playerName]",
            "Give yourself or another player a legendary compass",
            "rih.admin.compass"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player) && args.length == 1){
            this.send(cs, this.getUsage());
            
            return true;
        }

        if(args.length == 1){
            this.send(cs, "Giving you a legendary compass!");

            Player p = (Player) cs;

            ItemStack compass = plugin.getRecipeManager().generateLegendaryCompass();

            if(!p.getInventory().addItem(compass).isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), compass);
            }
            
            return true;
        } 
        else if(args.length == 2){
            String playerName = args[1];
            
            Player p = this.plugin.getServer().getPlayer(playerName);
            
            if(p == null){
                this.sendError(cs, "Invalid player: "+playerName);
                
                return true;
            }
            
            ItemStack compass = plugin.getRecipeManager().generateLegendaryCompass();

            if(!p.getInventory().addItem(compass).isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), compass);
            }
            
            this.send(cs, "Giving "+p.getName()+" a legendary compass!");
            
            p.sendMessage("You were given a legendary compass!");
            
            return true;
        }
        
        return false;
    }
}
