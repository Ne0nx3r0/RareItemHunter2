package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandEssence extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandEssence(RareItemHunterPlugin plugin) {
        super(
            "essence",
            "[essenceType|blank] [playerName]",
            "Give yourself or another player a rare essence",
            "rih.admin.essence"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }

        if(args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("blank"))){
            this.send(cs, "Giving you a rare essence!");

            Player p = (Player) cs;

            ItemStack essence = plugin.getRecipeManager().generateRareEssence();

            if(!p.getInventory().addItem(essence).isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), essence);
            }
            
            return true;
        }
        else if(args.length == 2){
            String propertyName = args[1];
            
            RareItemProperty rip = this.plugin.getPropertymanager().getProperty(propertyName);
            
            if(rip == null){
                this.sendError(cs, propertyName+" is not a valid essence type!");
                
                return true;
            }
            
            Player p = (Player) cs;
            
            ItemStack essence = plugin.getRecipeManager().generateRareEssence(rip);

            if(!p.getInventory().addItem(essence).isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), essence);
            }
            
            this.send(cs, "Giving you a "+rip.getName()+" essence!");
            
            return true;
        }        
        else if(args.length == 3){
            String propertyName = args[1];
            String playerName = args[2];
            
            Player p = this.plugin.getServer().getPlayer(playerName);
            
            if(p == null){
                this.sendError(cs, "Invalid player: "+playerName);
                
                return true;
            }
            
            if(propertyName.equalsIgnoreCase("blank")){
                ItemStack essence = plugin.getRecipeManager().generateRareEssence();

                if(!p.getInventory().addItem(essence).isEmpty()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), essence);
                }

                this.send(cs, "Giving you a rare essence!");
                
                return true;
            }
            
            RareItemProperty rip = this.plugin.getPropertymanager().getProperty(propertyName);
            
            if(rip == null){
                this.sendError(cs, propertyName+" is not a valid essence type!");
                
                return true;
            }
            
            ItemStack essence = plugin.getRecipeManager().generateRareEssence(rip);

            if(!p.getInventory().addItem(essence).isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), essence);
            }
            
            this.send(cs, "Giving "+p.getName()+" a "+rip.getName()+" essence!");
            
            p.sendMessage("You were given a "+rip.getName()+" essence!");
            
            return true;
        }
        
        return false;
    }
}
