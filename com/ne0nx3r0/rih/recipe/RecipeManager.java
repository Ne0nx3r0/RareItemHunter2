package com.ne0nx3r0.rih.recipe;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.ItemStackConvertor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeManager {
    private final String ESSENCE_NAME = ChatColor.GREEN+"Rare Essence";
    
    private final String ESSENCE_DESCRIPTION_0 = ChatColor.DARK_GRAY.toString()+ChatColor.DARK_GRAY+ChatColor.DARK_GRAY+"The essence of a fallen boss";
    private final String ESSENCE_DESCRIPTION_1 = ChatColor.DARK_GRAY+"View recipes with: "+ChatColor.GRAY+"/ri wi";
    
    private final RareItemHunterPlugin plugin;
    private final Map<Integer,RareItemProperty> essenceRecipes;

    public RecipeManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
        this.essenceRecipes = new HashMap<>();
    }

    public boolean isBlankRareEssence(ItemStack is) {
        if(is.getType().equals(Material.MAGMA_CREAM)){
            if(is.hasItemMeta()){
                ItemMeta meta = is.getItemMeta();

                if(meta.hasLore()){
                    List<String> lore = meta.getLore();

                    if(lore.size() == 2
                    && lore.get(0).equals(ESSENCE_DESCRIPTION_0)
                    && lore.get(1).equals(ESSENCE_DESCRIPTION_1)){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    public ItemStack getResultOf(ItemStack[] contents) {
        // If there's a blank rare essence, check for an essence recipe
        for(int i=0;i<9;i++){            
            if(this.isBlankRareEssence(contents[i])){
                int hashCode = this.getRecipeHashCode(contents);

                RareItemProperty rip = this.essenceRecipes.get(hashCode);
                
                if(rip != null){
                    return this.generateRareEssence(rip);
                }
                
                return null;
            }
        }
        
        ItemStack isAddPropertiesTo = null;
        List<RareItemProperty> propertiesToAdd = new ArrayList<>();
        
        // allow one itemstack to add properties to
        // and rare essences of a specific type
        // otherwise it's an invalid recipe
        for(ItemStack is : contents) {
            if(is != null && !is.getType().equals(Material.AIR)){
                if(is.getType().equals(Material.MAGMA_CREAM)){
                    RareItemProperty rip = this.getPropertyFromRareEssence(is);
                    
                    if(rip == null){
                        propertiesToAdd.add(rip);
                    }
                    else {
                        return null;
                    }
                }
                else if(isAddPropertiesTo == null){
                    isAddPropertiesTo = is;
                }
                else {
                    return null;
                }
            }
        }
        
        if(isAddPropertiesTo != null && !propertiesToAdd.isEmpty()){
            
        }
        
        return null;
    }

    public int getRecipeHashCode(ItemStack[] contents){
        StringBuilder sb = new StringBuilder();
        
        for(int i=1;i<10;i++){
            if(i > contents.length){
                sb.append(ItemStackConvertor.fromItemStack(new ItemStack(Material.AIR),false));
            }
            else{
                sb.append(ItemStackConvertor.fromItemStack(contents[i],false));
            }
        }
        
        return sb.toString().hashCode();
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

        ItemMeta meta = essence.getItemMeta();
        
        meta.setDisplayName(essenceName);
        
        List<String> lore = new ArrayList<>();

        lore.add(essenceDescription0);
        lore.add(essenceDescription1);
        
        meta.setLore(lore);
        
        essence.setItemMeta(meta);
        
        return essence;
    }
    
    public ItemStack generateRareEssence(){
        ItemStack essence = new ItemStack(Material.MAGMA_CREAM);

        ItemMeta meta = essence.getItemMeta();
        
        meta.setDisplayName(ESSENCE_NAME);
        
        List<String> lore = new ArrayList<>();
        
        lore.add(ESSENCE_DESCRIPTION_0);
        lore.add(ESSENCE_DESCRIPTION_1);
        
        
        meta.setLore(lore);
        
        essence.setItemMeta(meta);
        
        return essence;
    }

    public boolean updateRecipe(RareItemProperty rip, ItemStack[] contents) {
        String[] recipe = new String[9];
        String sRecipe = "";
        
        for(int i=0;i<9;i++){
            if(i > contents.length || contents[i] == null){
                recipe[i] = ItemStackConvertor.fromItemStack(new ItemStack(Material.AIR), false);
            }
            else{
                recipe[i] = ItemStackConvertor.fromItemStack(contents[i], false);
            }
            sRecipe += recipe[i];
        }

        File propertiesFile = new File(plugin.getDataFolder(),"properties.yml");

        if(!propertiesFile.exists()){
            plugin.copy(plugin.getResource("properties.yml"),propertiesFile);
        }
        
        FileConfiguration propertiesYml = YamlConfiguration.loadConfiguration(propertiesFile);
        
        propertiesYml.set(rip.getName()+".recipe", recipe);
        
        try {
            propertiesYml.save(propertiesFile);
        } 
        catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
            
            return false;
        }

        this.essenceRecipes.put(sRecipe.hashCode(), rip);
        
        return true;
    }

    public RareItemProperty getPropertyFromRareEssence(ItemStack is) {
        if(is.hasItemMeta()){
            ItemMeta meta = is.getItemMeta();
            
            if(meta.hasLore()){
                List<String> lore = meta.getLore();
                
                String sIDLine = lore.get(0);
                
                String startsWith = String.format(this.ESSENCE_PROPERTY_DESCRIPTION_1,new Object[]{""});
                
                if(sIDLine.startsWith(startsWith)){
                    String sId = sIDLine.substring(startsWith.length());
                    
                    System.out.println(sId);
                    
                    int id;
                    
                    try{
                        id = Integer.parseInt(sId);
                    }
                    catch(NumberFormatException ex){
                        return null;
                    }
                    
                    return this.plugin.getPropertymanager().getProperty(id);
                } 
            }
        }
        
        return null;
    }
}
