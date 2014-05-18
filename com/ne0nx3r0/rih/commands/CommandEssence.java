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
            "[playerName]",
            "Give yourself or another player a rare essence",
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

        this.send(cs, "Giving you a rare essence!");
        
        Player p = (Player) cs;
        
        ItemStack essence = plugin.getRecipeManager().generateRareEssence();
        
        if(!p.getInventory().addItem(essence).isEmpty()) {
            p.getWorld().dropItemNaturally(p.getLocation(), essence);
        }
        
        return true;
    }
}
