package com.ne0nx3r0.rih.commands;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.RomanNumeral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class CommandConvert extends RareItemHunterCommand{
    private final RareItemHunterPlugin plugin;
    
    public CommandConvert(RareItemHunterPlugin plugin) {
        super(
            "convert",
            "",
            "Converts a held RareItemHunter1 item to RIH2",
            "rih.convert"
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
        ItemStack isHeld = p.getItemInHand();
        
        if(isHeld != null){
            if(isHeld.equals(this.getEssenceItem())){
                p.setItemInHand(plugin.getRecipeManager().generateRareEssence());

                this.send(cs,"Converted your essence!");
                
                return true;
            }
            
            RareItemProperty essenceProperty = this.getPropertyFromRi1Essence(isHeld);
            
            if(essenceProperty != null){
                p.setItemInHand(plugin.getRecipeManager().generateRareEssence(essenceProperty));
                
                this.send(cs,"Converted your essence!");
                
                return true;
            }

            if(this.convertItemStack(p, isHeld)){
                this.send(cs, "Converted your item!");
                
                return true;
            }   
        }
        
        
        this.sendError(cs, "You must be holding an RIH1 essence or item!");

        return true;
    }
    
// Legacy code from RI1
    public ItemStack getEssenceItem()
    {
        ItemStack essence = new ItemStack(Material.MAGMA_CREAM);
        
        ItemMeta itemMeta = essence.getItemMeta();
        
        itemMeta.setDisplayName(ChatColor.DARK_GREEN+"Rare Essence");
        
        List<String> lore = new ArrayList<>();
        //TODO: Better way to identify essences/compasses uniquely, allowing the name to change
        lore.add(ChatColor.DARK_GRAY+"This is the rare essence of");
        lore.add(ChatColor.DARK_GRAY+"a legendary boss. It can be used");
        lore.add(ChatColor.DARK_GRAY+"to craft Rare Items.");
        
        itemMeta.setLore(lore);

        essence.getData().setData((byte) -41);
        
        essence.setItemMeta(itemMeta);
        
        return essence;
    }
    

    public final String COMPONENT_STRING = ChatColor.DARK_PURPLE+"RareItem Component";
    public final String RAREITEM_HEADER_STRING = ChatColor.DARK_PURPLE+"RareItem";
    
    private boolean convertItemStack(Player player, ItemStack is){
        if(is == null
        || !is.hasItemMeta()
        || !is.getItemMeta().hasLore())
        {
            return false;
        }
        
        List<String> lore = is.getItemMeta().getLore();
        
        if(!lore.get(0).equals(RAREITEM_HEADER_STRING))
        {
            return false;
        }
        
        lore.remove(0);
        
        List<String> TYPE_PREFIXES = new ArrayList<>();
        TYPE_PREFIXES.add(ChatColor.GRAY+"Skill: "+ChatColor.RED);
        TYPE_PREFIXES.add(ChatColor.GRAY+"Enchantment: "+ChatColor.GREEN);
        TYPE_PREFIXES.add(ChatColor.GRAY+"Spell: "+ChatColor.LIGHT_PURPLE);
        TYPE_PREFIXES.add(ChatColor.GRAY+"Ability: "+ChatColor.GOLD);
        TYPE_PREFIXES.add(ChatColor.GRAY+"Visual: "+ChatColor.DARK_PURPLE);
        TYPE_PREFIXES.add(ChatColor.DARK_PURPLE.toString());
        
        Map<RareItemProperty,Integer> newProperties = new HashMap<>();
        
        for(String sLore : lore){
            for(String sPrefix : TYPE_PREFIXES){
                if(sLore.startsWith(sPrefix)){
                    String sPropertyString = sLore.substring(sPrefix.length());
                    
                    int level = 1;
                    
                    String sLevel = sPropertyString.substring(sPropertyString.lastIndexOf(" ")+1);

                    try{
                        level = RomanNumeral.valueOf(sLevel);
                        sPropertyString = sPropertyString.substring(0,sPropertyString.lastIndexOf(" "));
                    }
                    catch(Exception ex){}

                    RareItemProperty property = plugin.getPropertymanager().getProperty(sPropertyString.toLowerCase());
                    
                    if(property != null){
                        newProperties.put(property,level);
                    }
                } 
            }
        }
        
        if(newProperties.isEmpty()){
            return false;
        }
        
// so this is an item that needs to be converted.
// Remove the lore and append Ri2 values
        ItemMeta meta = is.getItemMeta();
        
        meta.setLore(new ArrayList<String>());
        
        is.setItemMeta(meta);
        
        for(Entry<RareItemProperty,Integer> entry :newProperties.entrySet()){
            plugin.getRecipeManager().addPropertyTo(is, entry.getKey(), entry.getValue(), true);
        }
        
        return true;
    }

    public RareItemProperty getPropertyFromRi1Essence(ItemStack is)
    {
        if(is != null
        && is.getType().equals(Material.MAGMA_CREAM)
        && is.getItemMeta() != null
        && is.getItemMeta().getLore() != null)
        {
            List<String> TYPE_PREFIXES = new ArrayList<>();
            TYPE_PREFIXES.add(ChatColor.GRAY+"Skill: "+ChatColor.RED);
            TYPE_PREFIXES.add(ChatColor.GRAY+"Enchantment: "+ChatColor.GREEN);
            TYPE_PREFIXES.add(ChatColor.GRAY+"Spell: "+ChatColor.LIGHT_PURPLE);
            TYPE_PREFIXES.add(ChatColor.GRAY+"Ability: "+ChatColor.GOLD);
            TYPE_PREFIXES.add(ChatColor.GRAY+"Visual: "+ChatColor.DARK_PURPLE);
            TYPE_PREFIXES.add(ChatColor.DARK_PURPLE.toString());
            
            String sPropertyString = is.getItemMeta().getLore().get(0);
            
            for(String sPrefix : TYPE_PREFIXES)
            {
                if(sPropertyString.startsWith(sPrefix))
                {
                    String sPropertyName = sPropertyString.substring(sPrefix.length());

                    return plugin.getPropertymanager().getProperty(sPropertyName);
                }
            }
        }
        
        return null;
    }
}
