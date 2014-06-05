package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandCraft extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandCraft(RareItemHunterPlugin plugin) {
        super(
            "craft",
            "[vintage] <propertyName> <level>",
            "Add a rare item property to an item",
            "rih.admin.craft"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }

        if(args.length < 3){
            this.send(cs, this.getUsage());
            
            return true;
        }
        
        Player player = (Player) cs;
        
        ItemStack is = player.getItemInHand();
        
        if(is == null || is.getType().equals(Material.AIR)){
            this.sendError(cs,"You must be holding an item to put the property on!");
            
            return true;
        }
        
        // last arg
        String sLevel = args[args.length-1];
        
        int level = 0;
        
        try{
            level = Integer.parseInt(sLevel);
        }
        catch(NumberFormatException ex){
            this.sendError(cs,sLevel+" is not a valid number!");
            
            return true;
        }
        
        String propertyName = "";
        
        boolean vintage = false;
                
        if(args[1].equalsIgnoreCase("vintage")){
            vintage = true;
            
            for(int i=2;i<args.length;i++){
                propertyName += " "+args[i];
            }
        }
        else {
            for(int i=1;i<args.length;i++){
                propertyName += " "+args[i];
            }
        }
        
        propertyName = propertyName.substring(1);
        
        RareItemProperty rip = this.plugin.getPropertymanager().getProperty(propertyName);
        
        if(rip == null){
            this.sendError(cs,propertyName+" is not a valid property type!");
            
            return true;
        }
        else if(level > rip.getMaxLevel()){
            this.sendError(cs,"Max level for "+rip.getName()+" is "+rip.getMaxLevel()+"!");
            
            return true;
        }
        
        this.plugin.getRecipeManager().addPropertyTo(is,rip,level,vintage);
        
        return true;
    }
}
