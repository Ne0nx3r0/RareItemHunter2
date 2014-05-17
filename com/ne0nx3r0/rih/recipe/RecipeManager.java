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
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
        for(int i=1;i<10;i++){
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
        StringBuilder sb = new StringBuilder();
        
        for(int i=1;i<10;i++){
            if(contents.length < i){
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

    private final String SAVE_RESULT_ITEM_NAME = ChatColor.GREEN+"Save Recipe";
    private final String SAVE_RESULT_ITEM_DESCRIPTION_0 = ChatColor.DARK_GRAY+"PID: "+ChatColor.GRAY+"%s";
    private final String SAVE_RESULT_ITEM_DESCRIPTION_1 = ChatColor.DARK_GRAY+"Property name: "+ChatColor.GREEN+"%s";
    
    public ItemStack generateSaveResultItem(RareItemProperty rip) {
        ItemStack is = new ItemStack(Material.BOOK);
        
        ItemMeta meta = is.getItemMeta();
        
        meta.setDisplayName(SAVE_RESULT_ITEM_NAME);
        
        List<String> lore = new ArrayList<>();
        
        lore.add(String.format(SAVE_RESULT_ITEM_DESCRIPTION_0,new Object[]{
            rip.getID()
        }));
        
        lore.add(String.format(SAVE_RESULT_ITEM_DESCRIPTION_1,new Object[]{
            rip.getName()
        }));
        
        meta.setLore(lore);
        
        is.setItemMeta(meta);
        
        return is;
    }
    
    public RareItemProperty getPropertyFromResultItem(ItemStack is) {
        if(is.getType().equals(Material.BOOK)){
            if(is.hasItemMeta()){
                ItemMeta meta = is.getItemMeta();
                
                if(meta.hasDisplayName() 
                        && meta.hasLore() 
                        && meta.getDisplayName().equals(SAVE_RESULT_ITEM_NAME)){
                    List<String> lore = meta.getLore();
                    
                    String sID = lore.get(0).replace(String.format(SAVE_RESULT_ITEM_DESCRIPTION_0,new Object[]{""}),"");
                    
                    int id;
                    
                    try{
                        id = Integer.parseInt(sID);
                    }
                    catch(NumberFormatException ex){
                        return null;
                    }
                    
                    return plugin.getPropertymanager().getProperty(id);
                }
            }
        }
        
        return null;
    }

    public boolean updateRecipe(RareItemProperty rip, ItemStack[] contents) {
        String[] recipe = new String[9];
        
        for(int i=1;i<10;i++){
            if(i > contents.length){
                recipe[i] = ItemStackConvertor.fromItemStack(new ItemStack(Material.AIR), false);
            }
            else{
                recipe[i] = ItemStackConvertor.fromItemStack(contents[i], false);
            }
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
        
        return true;
    }
}
