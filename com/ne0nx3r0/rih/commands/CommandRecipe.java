package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandRecipe extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandRecipe(RareItemHunterPlugin plugin) {
        super(
            "recipe",
            "<property's name w/ spaces>",
            "modify the recipe for a rare item property",
            "rih.tester"
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
            this.send(cs, this.getUsage());
            
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
        
        p.openWorkbench(p.getLocation(), true);
        
        p.getOpenInventory().setItem(0, plugin.getRecipeManager().generateSaveResultItem(rip));
        
        p.updateInventory();
        
        return true;
    }
}
