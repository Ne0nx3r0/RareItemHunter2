package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

class CommandWhatIs extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandWhatIs(RareItemHunterPlugin plugin) {
        super(
            "wi",
            "<property's name w/ spaces >",
            "View the recipe and information about a property",
            "rih.wi"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }

        if(args.length < 2){
            StringBuilder properties = new StringBuilder();
            
            for(RareItemProperty rip : this.plugin.getPropertymanager().getAllProperties()){
                properties.append(ChatColor.GRAY).append(", ").append(ChatColor.WHITE).append(rip.getName());
            }
            
            if(properties.length() == 0){
                this.sendError(cs, "No available properties!");
                
                return true;
            }
            
            this.send(
                cs, 
                ChatColor.GRAY+"Available properties: "+properties.substring(4)
            );
            
            return true;
        }
        
        String propertyName = "";
        
        for(int i=1;i<args.length;i++){
            propertyName += " "+args[i];
        }
        
        propertyName = propertyName.substring(1);
        
        RareItemProperty rip = plugin.getPropertymanager().getProperty(propertyName);
        
        if(rip == null){
            this.sendError(cs,"Invalid property name");
            
            return true;
        }

        Player p = (Player) cs;
        
        Inventory invEditor = this.plugin.getGuiManager().createPropertyViewer(p, rip);
        
        p.openInventory(invEditor);
        
        return true;
    }
}
