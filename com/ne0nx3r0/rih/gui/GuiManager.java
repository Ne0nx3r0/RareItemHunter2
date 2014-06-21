package com.ne0nx3r0.rih.gui;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.ItemStackConvertorRI2;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;

public class GuiManager {
    private final RareItemHunterPlugin plugin;
    
    private final int[] ITEM_CRAFTING_SLOTS = new int[]{10,11,12,19,20,21,28,29,30};

    public GuiManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
    }
       
    private final  int INVENTORY_SIZE = 45;
    private final String EDITING_RECIPE = ChatColor.BLACK+"Recipe: "+ChatColor.DARK_GREEN+"%s";
    
    public Inventory createRecipeEditor(InventoryHolder ih,RareItemProperty rip) {
        
        String sTitle = String.format(EDITING_RECIPE,new Object[]{
            rip.getName()
        });
        
        if(sTitle.length() > 32){
            sTitle = sTitle.substring(0,28)+"...";
        }
        
        Inventory inv = Bukkit.getServer().createInventory(ih, INVENTORY_SIZE, sTitle);
        
        ItemStack isBG = this.getBlank(Material.NETHER_BRICK);
        
        for(int i=0;i<INVENTORY_SIZE;i++){
            switch(i){
                    //bg
                default:
                    inv.setItem(i, isBG);
                    break;
                case 2:
                    inv.setItem(i, this.getBlank(Material.DIAMOND_BLOCK));
                    break;
                case 18:
                    inv.setItem(i, this.getBlank(Material.GOLD_BLOCK));
                    break;
                case 22:
                    inv.setItem(i, this.getBlank(Material.EMERALD_BLOCK));
                    break;
                case 38:
                    inv.setItem(i, this.getBlank(Material.IRON_BLOCK));
                    break;                  
                case 10://crafting area
                case 11://crafting area
                case 12://crafting area
                case 19://crafting area
                case 20://crafting area
                case 21://crafting area
                case 28://crafting area
                case 29://crafting area
                case 30://result slot
                    // pretty sure the compiler will remove this
                    // but it's a good reminder
                    break;
                case 25:
                    inv.setItem(i, this.generateSaveResultItem(rip));
                    break;
            }
        }
        
        List<String> recipe = rip.getRecipe();
        
        if(recipe != null){
            for(int i=0;i<recipe.size();i++){
                inv.setItem(ITEM_CRAFTING_SLOTS[i], ItemStackConvertorRI2.fromString(recipe.get(i)));
            }
        }
        
        return inv;
    }
    
    private ItemStack getBlank(Material m){
        ItemStack is = new ItemStack(m);
        
        ItemMeta meta = is.getItemMeta();
        
        meta.setDisplayName(ChatColor.BLACK+"");
        
        is.setItemMeta(meta);
        
        return is;
    }

    public boolean isRecipeEditor(Inventory inventory) {
        return inventory.getTitle().startsWith(String.format(EDITING_RECIPE, new Object[]{""}));
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
        if(is != null && is.getType().equals(Material.BOOK)){
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

    public void recipeEditorAction(InventoryClickEvent e) {
        int slot = e.getRawSlot();
        
        switch(slot){
                // bg
            default:
                if(slot < this.INVENTORY_SIZE){
                    e.setCancelled(true);
                }
                // crafting slots, allow action
            case 10:
            case 11:
            case 12:
            case 19:
            case 20:
            case 21:
            case 28:
            case 29:
            case 30:
                return;
                // save recipe
            case 25:
                RareItemProperty rip = this.getPropertyFromResultItem(e.getInventory().getItem(25));
                
                Inventory inv = e.getInventory();
                
                ItemStack[] recipe = new ItemStack[9];
                
                for(int i=0;i<ITEM_CRAFTING_SLOTS.length;i++){
                    ItemStack is = inv.getItem(ITEM_CRAFTING_SLOTS[i]);
                    
                    if(is == null || is.getType().equals(Material.AIR)){
                        recipe[i] = new ItemStack(Material.AIR);
                    }
                    else {
                        recipe[i] = is;
                    }
                }                        
                        
                plugin.getRecipeManager().updateRecipe(rip, recipe);
                
                Player p = (Player) e.getWhoClicked();
                
                p.sendMessage("Recipe for "+rip.getName()+" updated!");
                
                p.closeInventory();
                
                break;
        }
    }

    private final String SHRINE_TITLE = ChatColor.DARK_GRAY+"Legendary Shrine";

    public Inventory createLegendaryShrineCrafter(Block block, InventoryHolder ih) {
        Inventory inv = Bukkit.getServer().createInventory(ih, INVENTORY_SIZE, SHRINE_TITLE);
        
        Material north = block.getRelative(BlockFace.NORTH).getType();
        Material east = block.getRelative(BlockFace.EAST).getType();
        Material south = block.getRelative(BlockFace.SOUTH).getType();
        Material west =  block.getRelative(BlockFace.WEST).getType();
        
        ItemStack isBG = this.getBlank(Material.NETHER_BRICK);
        
        for(int i=0;i<INVENTORY_SIZE;i++){
            switch(i){
                default://background
                    inv.setItem(i, isBG);
                    break;
                case 2://top symbol
                    inv.setItem(i, this.getBlank(north));
                    break;
                case 18://left symbol
                    inv.setItem(i, this.getBlank(west));
                    break;
                case 22://right symbol
                    inv.setItem(i, this.getBlank(east));
                    break;
                case 38://bottom symbol
                    inv.setItem(i, this.getBlank(south));
                    break;
                case 10:// crafting area
                case 11:// crafting area
                case 12:// crafting area
                case 19:// crafting area
                case 20:// crafting area center
                case 21:// crafting area
                case 28:// crafting area
                case 29:// crafting area
                case 30:// crafting area
                case 25:// result slot
                    break;
                case 8:// store shrine location in top right itemstack
                    ItemStack isLoc = new ItemStack(Material.NETHER_BRICK);
                    
                    ItemMeta meta = isLoc.getItemMeta();
                    
                    meta.setDisplayName(ChatColor.BLACK.toString()+block.getX()+" "+block.getY()+" "+block.getZ());
                    
                    isLoc.setItemMeta(meta);
                    
                    inv.setItem(8, isLoc);
                    
                    break;
            }
        }
        
        return inv;
    }
    
    public boolean isLegendaryShrineScreen(Inventory inventory) {
        return inventory.getTitle().equals(SHRINE_TITLE);
    }

    public void legendaryShrineAction(InventoryClickEvent e) {
        int slot = e.getRawSlot();
        final Inventory inv = e.getInventory();

        switch(slot){
            default:// GUI BG
                if(slot < this.INVENTORY_SIZE){
                    e.setCancelled(true);
                }
                return;
            case 10:// crafting slots, allow action
            case 11:
            case 12:
            case 19:
            case 20:
            case 21:
            case 28:
            case 29:
            case 30:
                // Run immediately after so the result is calculated instead of the current state
                this.plugin.getServer().getScheduler().runTask(plugin, new Runnable(){
                    @Override
                    public void run() {
                        ItemStack[] potentialRecipe = new ItemStack[9];

                        for(int i=0;i<9;i++){
                            ItemStack is = inv.getItem(ITEM_CRAFTING_SLOTS[i]);
                            if(is == null || is.getType().equals(Material.AIR)){
                                potentialRecipe[i] = new ItemStack(Material.AIR);
                            }
                            else {
                                potentialRecipe[i] = is;
                            }
                        }

                        ItemStack currentResult = inv.getItem(25);
                        
                        ItemStack newResult = plugin.getRecipeManager().getResultOf(potentialRecipe);

                        if(newResult != null){
                            inv.setItem(25, newResult);
                        }
                        else if(currentResult != null && !currentResult.getType().equals(Material.AIR)){
                            inv.setItem(25, new ItemStack(Material.AIR));
                        }
                    }
                });
                return;

            case 25:// craft essence
                e.setCancelled(true);
                
                if(e.getCursor() != null && e.getCursor().getType().equals(Material.AIR)){
                    ItemStack result = inv.getItem(25);
                    
                    if(result != null && !result.getType().equals(Material.AIR)){
                        for(int i=0;i<9;i++){
                            inv.setItem(ITEM_CRAFTING_SLOTS[i], new ItemStack(Material.AIR));
                        }   
                        
                        e.setCancelled(false);
                        
                        String[] sCoords = inv.getItem(8).getItemMeta().getDisplayName().substring(2).split(" ");

                        final Player p = (Player) e.getWhoClicked();
                        
                        try{
                            Block block = p.getWorld().getBlockAt(
                                    Integer.parseInt(sCoords[0]), 
                                    Integer.parseInt(sCoords[1]), 
                                    Integer.parseInt(sCoords[2])
                            );
                            
                            block.getWorld().strikeLightningEffect(block.getLocation());

                            block.getWorld().playEffect(block.getLocation(), Effect.EXPLOSION, 5);
                        }
                        catch(NumberFormatException ex){}
                        
                        plugin.getServer().getScheduler().runTask(plugin, new Runnable(){

                            @Override
                            public void run() {
                                p.updateInventory();
                            }
                        
                        });
                    }
                }                
        }
    }

    // Surrounded (in any order) by the 
    public boolean isLegendaryShrineBlock(Block block) {
        if(block.getType().equals(Material.HOPPER)){
            boolean[] hasFaces = new boolean[4];

            for(BlockFace bf : new BlockFace[]{BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST}){
                switch(block.getRelative(bf).getType()){
                    case DIAMOND_BLOCK:
                        hasFaces[0] = true;
                        break;
                        
                    case GOLD_BLOCK:
                        hasFaces[1] = true;
                        break;
                        
                    case IRON_BLOCK:
                        hasFaces[2] = true;
                        break;
                        
                    case EMERALD_BLOCK:
                        hasFaces[3] = true;
                        break;
                }
            }
            
            for(boolean hasFace : hasFaces){
                if(!hasFace){
                    return false;
                }
            }
            
            return true;
        }
        
        return false;
    }

    public void shrineClick(PlayerInteractEvent e) {
            e.setCancelled(true);
            
            Inventory invShrine = this.createLegendaryShrineCrafter(e.getClickedBlock(),e.getPlayer());

            e.getPlayer().openInventory(invShrine);
    }

    public void closeScreen(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();

        for(int i=0;i<9;i++){
            ItemStack is = inv.getItem(ITEM_CRAFTING_SLOTS[i]);
            
            if(is != null && !is.getType().equals(Material.AIR)){
                Player p = (Player) e.getPlayer();
                
                if(!p.getInventory().addItem(is).isEmpty()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), is);
                }
            }
        }   
    }

    private final String WHAT_IS_TITLE = ChatColor.DARK_GRAY+"Property Viewer";
    
    public Inventory createPropertyViewer(Player p, RareItemProperty rip) {
        Inventory inv = Bukkit.getServer().createInventory(p, INVENTORY_SIZE, WHAT_IS_TITLE);
        
        ItemStack isBG = this.getBlank(Material.NETHER_BRICK);
        
        for(int i=0;i<INVENTORY_SIZE;i++){
            switch(i){
                default://background
                    inv.setItem(i, isBG);
                    break;
                case 2://top symbol
                    inv.setItem(i, this.getBlank(Material.DIAMOND_BLOCK));
                    break;
                case 18://left symbol
                    inv.setItem(i, this.getBlank(Material.IRON_BLOCK));
                    break;
                case 22://right symbol
                    inv.setItem(i, this.getBlank(Material.EMERALD_BLOCK));
                    break;
                case 38://bottom symbol
                    inv.setItem(i, this.getBlank(Material.GOLD_BLOCK));
                    break;
                case 10:// crafting area
                case 11:// crafting area
                case 12:// crafting area
                case 19:// crafting area
                case 20:// crafting area center
                case 21:// crafting area
                case 28:// crafting area
                case 29:// crafting area
                case 30:// crafting area
                    break;
                case 25:// result slot
                    ItemStack is = new ItemStack(Material.MAGMA_CREAM);
                    
                    ItemMeta meta = is.getItemMeta();
                    
                    String thingNeeded = "";

                    switch(rip.getCostType()){
                        case FOOD:
                            thingNeeded = rip.getCost()+" food per use";
                            break;
                        case LEVEL: 
                            thingNeeded = rip.getCost()+" level"+(rip.getCost()>1?"s":"")+" per use";
                            break;
                        case HEALTH: 
                            thingNeeded = rip.getCost()+" health per use";
                            break;
                        case MONEY: 
                            thingNeeded = this.plugin.getEconomy().format(rip.getCost())+" per use";
                            break;
                        case COOLDOWN: 
                            double seconds = rip.getCost();

                            if(seconds < 1){
                                seconds = 1;
                            }

                            thingNeeded = seconds+" second cooldown";
                            break;
                        case AUTOMATIC: 
                            thingNeeded = "Automatic every "+rip.getCost()+" seconds";
                            break;
                        case PASSIVE: 
                            thingNeeded = "None / Passive";
                            break;
                    }
                    
                    List<String> lore = new ArrayList<>();
                    
                    meta.setDisplayName(ChatColor.GREEN+rip.getName());
                    
                    String[] lines = org.apache.commons.lang.WordUtils.wrap(rip.getDescription(), 30, "#!#", true).split("#!#");
                    
                    for(String line : lines){
                        lore.add(line);
                    }
                    
                    lore.add(ChatColor.GRAY+"Max Level: "+ChatColor.GREEN+rip.getMaxLevel());
                    lore.add(ChatColor.GRAY+"Cost: "+ChatColor.GREEN+thingNeeded);
                    
                    meta.setLore(lore);
                    
                    is.setItemMeta(meta);
                    
                    inv.setItem(i, is);
                    
                    break;
            }
        }
        
        List<String> recipe = rip.getRecipe();
        
        if(recipe != null){
            for(int i=0;i<recipe.size();i++){
                inv.setItem(ITEM_CRAFTING_SLOTS[i], ItemStackConvertorRI2.fromString(recipe.get(i)));
            }
        }
        
        
        return inv;
    }

    public boolean isPropertyViewer(Inventory inventory) {
        return inventory.getTitle().equals(this.WHAT_IS_TITLE);
    }
}
