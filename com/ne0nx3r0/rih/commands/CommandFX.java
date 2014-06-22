package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandFX extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandFX(RareItemHunterPlugin plugin) {
        super(
            "fx",
            "<property's name w/ spaces >",
            "View the recipe and information about a property",
            "rih.fx"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }
        
        if(args.length < 7){
            this.send(cs,"Usage: ","/ri fx <Effect> <offsetX> <Y> <Z> <speed> <# particles>");
            
            return true;
        }
        
        Player p = (Player) cs;
        
        try{
            ParticleEffect fx = ParticleEffect.valueOf(args[1].toUpperCase());

            fx.display(
                p.getLocation().add(0,1.5,0), //center
                Float.parseFloat(args[2]), //offsetX
                Float.parseFloat(args[3]), //offsetY
                Float.parseFloat(args[4]), //offsetZ
                Float.parseFloat(args[5]), //speed
                Integer.parseInt(args[6]) // amount of particles
            );
        }
        catch(NumberFormatException ex){
            this.sendError(cs, "One of the numbers was invalid bra. ");
            
            return true;
        }
        catch(IllegalArgumentException ex){
            this.sendError(cs, "Invalid effect type: "+args[1].toUpperCase());
            
            return true;
        }
        
        return true;
    }
}
