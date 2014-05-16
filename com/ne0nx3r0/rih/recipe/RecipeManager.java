package com.ne0nx3r0.rih.recipe;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.properties.RareItemProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeManager {
    private final String ESSENCE_NAME = ChatColor.GREEN.toString()+ChatColor.GREEN+ChatColor.GREEN+"Rare Essence";
    
    private final String ESSENCE_DESCRIPTION_0 = ChatColor.DARK_GRAY+"The essence of a fallen boss";
    private final String ESSENCE_DESCRIPTION_1 = ChatColor.DARK_GRAY+"View recipes with: "+ChatColor.GRAY+"/ri wi";
    
    private final RareItemHunterPlugin plugin;
    private final Map<Integer,RareItemProperty> essenceRecipes;

    public RecipeManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
        this.essenceRecipes = new HashMap<>();
    }

    public boolean isRareEssence(ItemStack is) {
        if(is.getType().equals(Material.MAGMA_CREAM)){
            if(!is.hasItemMeta()){
                ItemMeta meta = is.getItemMeta();

                if(!meta.hasLore()){
                    List<String> lore = meta.getLore();

                    if(lore.get(0).equals(ESSENCE_NAME)){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    public boolean isBlankRareEssence(ItemStack is) {
        if(is.getType().equals(Material.MAGMA_CREAM)){
            if(!is.hasItemMeta()){
                ItemMeta meta = is.getItemMeta();

                if(!meta.hasLore()){
                    List<String> lore = meta.getLore();

                    if(lore.size() == 3
                    && lore.get(0).equals(ESSENCE_NAME)
                    && lore.get(1).equals(ESSENCE_DESCRIPTION_0)
                    && lore.get(2).equals(ESSENCE_DESCRIPTION_1)){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    public ItemStack getResultOf(ItemStack[] contents) {
        for(int i = 0;i<contents.length;i++){
            System.out.println(i+" "+contents[i].getType()+" "+contents[i]);
        }
        
        // if there's a blank rare essence, check for an essence recipe
        for(int i=0;i<9;i++){
            if(this.isBlankRareEssence(contents[i])){
                int hashCode = this.getRecipeHashCode(contents);
               
                RareItemProperty rip = this.essenceRecipes.get(hashCode);
                
                if(rip != null){
                    return this.generateRareEssence(rip);
                }
            }
        }
        
        return null;
    }

    public int getRecipeHashCode(ItemStack[] contents){
        return this.getHashString(contents).hashCode();
    }
    
    // The difference between this and the above method
    // Is that this one returns something that can be edited by hand
    // Which is great for recipe files, etc...
    //
    // Also this protects the recipes if the hash type has to be changed
    public String getHashString(ItemStack[] contents) {
        StringBuilder sb = new StringBuilder();
        
        for(int i=0;i<9;i++){
            sb.append("|");
            
            if(contents.length < i || contents[i].getType().equals(Material.AIR)){
                sb.append("AIR");
            }
            else {
                ItemStack is = contents[i];
                
                sb.append(is.getAmount())
                .append(is.getData().getData())
                .append(is.getDurability());
                
                for(Entry<Enchantment,Integer> enchant : is.getEnchantments().entrySet()){
                    sb.append(enchant.getKey().getName())
                    .append(enchant.getValue());
                }
                
                if(is.hasItemMeta()){
                    ItemMeta meta = is.getItemMeta();
                    
                    if(meta.hasLore()){
                        for(String loreLine : meta.getLore()){
                            sb.append(loreLine);
                        }
                    }
                    
                    // Skipping name on purpose
                    // This allows players to use renamed items
                }
                
                sb.append(is.getType().name());
            }
        }
        return sb.toString();
    }

    private final String ESSENCE_PROPERTY_NAME = ChatColor.DARK_GRAY+"Essence of "+ChatColor.GREEN+"%s";
    private final String ESSENCE_PROPERTY_DESCRIPTION_0 = ChatColor.GRAY+"/ri wi %s"+ChatColor.DARK_GRAY+" for more info";
    private final String ESSENCE_PROPERTY_DESCRIPTION_1 = ChatColor.DARK_GRAY.toString()+ChatColor.DARK_GRAY+ChatColor.DARK_GRAY+"PID: %s";
    
    public ItemStack generateRareEssence(RareItemProperty rip) {
        ItemStack essence = new ItemStack(Material.MAGMA_CREAM);
        
        String essenceName = String.format(ESSENCE_PROPERTY_NAME,new Object[]{
            rip.getName()
        });
        
        String essenceDescription0 = String.format(ESSENCE_PROPERTY_DESCRIPTION_0,new Object[]{
            rip.getName()
        });
        
        String essenceDescription1 = String.format(ESSENCE_PROPERTY_DESCRIPTION_1,new Object[]{
            rip.getID()
        });
        
        essence.getItemMeta().setDisplayName(essenceName);
        
        List<String> lore = new ArrayList<>();
        
        lore.add(essenceName);
        lore.add(essenceDescription0);
        lore.add(essenceDescription1);
        
        ItemMeta meta = essence.getItemMeta();
        
        meta.setLore(lore);
        
        essence.setItemMeta(meta);
        
        return essence;
    }
    
    public ItemStack generateRareEssence(){
        ItemStack essence = new ItemStack(Material.MAGMA_CREAM);

        essence.getItemMeta().setDisplayName(ESSENCE_NAME);
        
        List<String> lore = new ArrayList<>();
        
        lore.add(ESSENCE_NAME);
        lore.add(ESSENCE_DESCRIPTION_0);
        lore.add(ESSENCE_DESCRIPTION_1);
        
        ItemMeta meta = essence.getItemMeta();
        
        meta.setLore(lore);
        
        essence.setItemMeta(meta);
        
        return essence;
    }
}
