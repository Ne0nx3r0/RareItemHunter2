package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

class CommandHat extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandHat(RareItemHunterPlugin plugin) {
        super(
            "hat",
            "",
            "Put held item on as a hat",
            "rih.hat"
        );
        
        this.plugin = plugin;
    }

    @Override
    boolean execute(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            this.sendError(cs,"Cannot be used from the console.");
            
            return true;
        }

        Player p = (Player) cs;
        ItemStack itemHand = p.getItemInHand();
        
        if (itemHand != null && itemHand.getType() != Material.AIR)
        {
          PlayerInventory inv = p.getInventory();
          
          ItemStack itemHead = inv.getHelmet();
          
          inv.removeItem(new ItemStack[] { itemHand });
          
          inv.setHelmet(itemHand);
          
          inv.setItemInHand(itemHead);
          
          plugin.getPropertymanager().onEquip(p,itemHand);
          
          if(itemHead != null && itemHead.getType() != Material.AIR){
            plugin.getPropertymanager().onUnequip(p,itemHead);
          }
          
          p.sendMessage(ChatColor.GREEN + "Enjoy your new hat!");
          
          return true;
        }
        
        p.sendMessage(ChatColor.RED + "You can't wear that! It would look horrible and cliche.");

        return true;
    }
}
