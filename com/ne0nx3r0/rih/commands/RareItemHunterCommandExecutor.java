package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RareItemHunterCommandExecutor implements CommandExecutor {
    private final HashMap<String, RareItemHunterCommand> commands;
    
    public RareItemHunterCommandExecutor(RareItemHunterPlugin plugin) {
        this.commands = new HashMap<>();
        
        this.registerCommand(new CommandCompass(plugin));
        this.registerCommand(new CommandEssence(plugin));
        
        this.registerCommand(new CommandBoss(plugin));
        this.registerCommand(new CommandEgg(plugin));
        
        this.registerCommand(new CommandRecipe(plugin));
        this.registerCommand(new CommandCraft(plugin));
        
        this.registerCommand(new CommandSP(plugin));
        this.registerCommand(new CommandAddSP(plugin));
        this.registerCommand(new CommandDelSP(plugin));
        
        this.registerCommand(new CommandWhatIs(plugin));
        this.registerCommand(new CommandConvert(plugin));
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        if(args.length == 0 || args[0].equals("?")) {
            this.sendUsage(cs);
            
            return true;
        }

        RareItemHunterCommand command = this.commands.get(args[0]);
        
        if(command != null) {
            if(cs.hasPermission(command.getPermissionNode())) {
                return command.execute(cs,args);
            }
            else {
                command.send(cs, 
                    ChatColor.RED+"You do not have permission to "+command.getAction(),
                    ChatColor.RED+"Required node: "+ChatColor.WHITE+command.getPermissionNode()
                );
            }
        }
        
        return false;
    }
    
    private void sendUsage(CommandSender cs) {
        cs.sendMessage(ChatColor.GRAY+"---"+ChatColor.DARK_GREEN+" RareItemHunter2 "+ChatColor.GRAY+"---");
        cs.sendMessage("Here are the commands you have access to:");
        
        for(RareItemHunterCommand lc : this.commands.values()) {
            if(cs.hasPermission(lc.getPermissionNode())) {
                cs.sendMessage(lc.getUsage());
            }
        }
    }
    
    private void registerCommand(RareItemHunterCommand command) {
        this.commands.put(command.getName(), command);
    }
}
