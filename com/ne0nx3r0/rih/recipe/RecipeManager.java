package com.ne0nx3r0.rih.recipe;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.ItemStackConvertorRI2;
import com.ne0nx3r0.util.RomanNumeral;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    private final int MAX_PROPERTIES_PER_ITEM = 8;

    public RecipeManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
        
        // loaded by the property manager
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

    private final String PROPERTY_HEADER = ChatColor.DARK_PURPLE+"Rare Item";
    private final String PROPERTY_LINE_PREFIX = ChatColor.DARK_GRAY+"Property: "+ChatColor.GREEN;
    private final String PROPERTY_LINE = PROPERTY_LINE_PREFIX+"%s "+ChatColor.GREEN+"%s "+ChatColor.BLACK+"%s";
    
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
        Map<RareItemProperty,Integer> propertyLevels = new HashMap<>();
        
        // allow one itemstack to add properties to
        // and rare essences of a specific type
        // otherwise it's an invalid recipe
        for(ItemStack is : contents) {
            if(is != null && !is.getType().equals(Material.AIR)){
                if(is.getType().equals(Material.MAGMA_CREAM)){
                    RareItemProperty rip = this.getPropertyFromRareEssence(is);
                    
                    if(rip != null){
                        Integer currentLevel = propertyLevels.get(rip);
                        
                        if(currentLevel == null){
                            propertyLevels.put(rip,1);
                        }
                        else if(currentLevel < rip.getMaxLevel() && propertyLevels.size() < this.MAX_PROPERTIES_PER_ITEM){
                            propertyLevels.put(rip,currentLevel+1);
                        }
                    }
                    else {
                        return null;
                    }
                }
                else if(isAddPropertiesTo == null){
                    isAddPropertiesTo = is.clone();
                }
                else {
                    return null;
                }
            }
        }
        
        if(isAddPropertiesTo != null && !propertyLevels.isEmpty()){
            // strip existing properties from the item and add them to the properties to add
            ItemMeta meta = isAddPropertiesTo.getItemMeta();
            List<String> lore;
            List<String> newLore = new ArrayList<>();
            
            if(meta.hasLore()){
                lore = meta.getLore();

                for(String sLore : lore){
                    if(sLore.startsWith(PROPERTY_LINE_PREFIX)){
                        String sPID = sLore.substring(sLore.lastIndexOf(ChatColor.COLOR_CHAR)+2);
                        int itemPropertyLevel = 1;
                        
                        try{
                            itemPropertyLevel = RomanNumeral.valueOf(sLore.substring(
                                    sLore.lastIndexOf(ChatColor.GREEN.toString())+2,
                                    sLore.lastIndexOf(ChatColor.COLOR_CHAR)-1
                            ));
                        }
                        catch(IllegalArgumentException ex){
                            continue;
                        }
                        
                        int pid;
                        
                        try{
                            pid = Integer.parseInt(sPID);
                        }
                        catch(NumberFormatException ex){
                            continue;
                        }
                        
                        RareItemProperty rip = this.plugin.getPropertymanager().getProperty(pid);
                        
                        if(rip != null){
                            Integer currentLevel = propertyLevels.get(rip);
                            
                            if(currentLevel == null){
                                currentLevel = 1;
                            }
                            
                            int newLevel = currentLevel + itemPropertyLevel;

                            if(currentLevel < rip.getMaxLevel() && propertyLevels.size() < this.MAX_PROPERTIES_PER_ITEM){
                                propertyLevels.put(rip,newLevel);
                            }
                        }
                    }
                    else if(!sLore.equals(PROPERTY_HEADER)){
                        newLore.add(sLore);
                    }
                }
            }
            else{
                lore = new ArrayList<>();
            }
            
            lore = newLore;
            
            lore.add(PROPERTY_HEADER);
            
            for(Entry<RareItemProperty,Integer> entry : propertyLevels.entrySet()){
                RareItemProperty rip = entry.getKey();
                int level = entry.getValue();
                
                lore.add(String.format(PROPERTY_LINE,new Object[]{
                    rip.getName(),
                    RomanNumeral.convertToRoman(level),
                    rip.getID()
                }));
            }
            
            meta.setLore(lore);
            
            isAddPropertiesTo.setItemMeta(meta);
            
            return isAddPropertiesTo;
        }
        
        return null;
    }

    public int getRecipeHashCode(ItemStack[] contents){
        StringBuilder sb = new StringBuilder();
        
        for(int i=0;i<9;i++){
            if(i > contents.length){
                sb.append(ItemStackConvertorRI2.fromItemStack(new ItemStack(Material.AIR),false));
            }
            else{
                sb.append(ItemStackConvertorRI2.fromItemStack(contents[i],false));
            }
        }
        
        return sb.toString().hashCode();
    }

    private final String ESSENCE_PROPERTY_NAME = ChatColor.DARK_GRAY+"Essence of "+ChatColor.GREEN+"%s";
    private final String ESSENCE_PROPERTY_DESCRIPTION_0 = ""+ChatColor.DARK_GRAY+ChatColor.DARK_GRAY+ChatColor.DARK_GRAY+"PID: %s";
    private final String ESSENCE_PROPERTY_DESCRIPTION_1 = ChatColor.GRAY+"/ri wi %s"+ChatColor.DARK_GRAY+" for more info";
    
    public ItemStack generateRareEssence(RareItemProperty rip) {
        ItemStack essence = new ItemStack(Material.MAGMA_CREAM);
        
        String essenceName = String.format(ESSENCE_PROPERTY_NAME,new Object[]{
            rip.getName()
        });
        
        String essenceDescription0 = String.format(ESSENCE_PROPERTY_DESCRIPTION_0,new Object[]{
            rip.getID()
        });
        
        String essenceDescription1 = String.format(ESSENCE_PROPERTY_DESCRIPTION_1,new Object[]{
            rip.getName()
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

    public void loadRecipe(RareItemProperty rip, List<String> recipe) {
        StringBuilder sb = new StringBuilder();
        
        for(String sItem : recipe){
            sb.append(sItem);
        }
        
        this.essenceRecipes.put(sb.toString().hashCode(), rip);
    }
    
    public boolean updateRecipe(RareItemProperty rip, ItemStack[] contents) {
        String[] recipe = new String[9];
        String sRecipe = "";
        
        for(int i=0;i<9;i++){
            if(i > contents.length || contents[i] == null){
                recipe[i] = ItemStackConvertorRI2.fromItemStack(new ItemStack(Material.AIR), false);
            }
            else{
                recipe[i] = ItemStackConvertorRI2.fromItemStack(contents[i], false);
            }
            sRecipe += recipe[i];
        }

        File propertiesFile = new File(plugin.getDataFolder(),"properties.yml");

        if(!propertiesFile.exists()){
            plugin.copy(plugin.getResource("properties.yml"),propertiesFile);
        }
        
        FileConfiguration propertiesYml = YamlConfiguration.loadConfiguration(propertiesFile);
        
        propertiesYml.set(rip.getID()+".recipe", recipe);
        
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
                
                String startsWith = String.format(this.ESSENCE_PROPERTY_DESCRIPTION_0,new Object[]{""});

                if(sIDLine.startsWith(startsWith)){
                    String sId = sIDLine.substring(startsWith.length());

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
    
    public Map<RareItemProperty, Integer> getProperties(ItemStack is) {
        //TODO: Consider some form of caching
        if(is.hasItemMeta()){
            ItemMeta meta = is.getItemMeta();
            
            if(meta.hasLore()){
                List<String> lore = meta.getLore();
                Map<RareItemProperty,Integer> propertyLevels = new HashMap<>();
                
                for(String sLore : lore){
                    if(sLore.startsWith(PROPERTY_LINE_PREFIX)){
                        String sPID = sLore.substring(sLore.lastIndexOf(ChatColor.COLOR_CHAR)+2);
                        int itemPropertyLevel = 1;
                        
                        try{
                            itemPropertyLevel = RomanNumeral.valueOf(sLore.substring(
                                    sLore.lastIndexOf(ChatColor.GREEN.toString())+2,
                                    sLore.lastIndexOf(ChatColor.COLOR_CHAR)-1
                            ));
                        }
                        catch(IllegalArgumentException ex){
                            continue;
                        }
                        
                        int pid;
                        
                        try{
                            pid = Integer.parseInt(sPID);
                        }
                        catch(NumberFormatException ex){
                            continue;
                        }
                        
                        RareItemProperty rip = this.plugin.getPropertymanager().getProperty(pid);
                        
                        if(rip != null){
                            Integer currentLevel = propertyLevels.get(rip);
                            
                            if(currentLevel == null){
                                currentLevel = 1;
                            }
                            
                            int newLevel = currentLevel + itemPropertyLevel;

                            if(currentLevel < rip.getMaxLevel() && propertyLevels.size() < this.MAX_PROPERTIES_PER_ITEM){
                                propertyLevels.put(rip,newLevel);
                            }
                        }
                    }
                }

                return propertyLevels;
            }
        }
        
        return null;
    }

    public ItemStack generateLegendaryCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        
        ItemMeta itemMeta = compass.getItemMeta();
        
        itemMeta.setDisplayName(ChatColor.DARK_GREEN+"Legendary Compass");
        
        List<String> lore = new ArrayList<>();
        
        lore.add(ChatColor.DARK_GRAY+"When tapped against the ground");
        lore.add(ChatColor.DARK_GRAY+"this compass will attune itself");
        lore.add(ChatColor.DARK_GRAY+"to the nearest legendary boss egg.");
        
        itemMeta.setLore(lore);
        
        compass.setItemMeta(itemMeta);
        
        return compass;
    }
}
