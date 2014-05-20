package com.ne0nx3r0.rih.gui;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.property.RareItemProperty;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class GuiManager {
    private final RareItemHunterPlugin plugin;

    public GuiManager(RareItemHunterPlugin plugin) {
        this.plugin = plugin;
    }
       
    private final  int INVENTORY_SIZE = 45;
    private final String EDITING_RECIPE = ChatColor.BLACK+"Editing recipe for "+ChatColor.DARK_GREEN+"%s";
    
    public Inventory createRecipeEditor(InventoryHolder ih,RareItemProperty rip) {
        
        Inventory inv = Bukkit.getServer().createInventory(ih, INVENTORY_SIZE, String.format(EDITING_RECIPE,new Object[]{
            rip.getName()
        }));
        
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
                    //crafting area
                case 10:
                case 11:
                case 12:
                case 19:
                case 20:
                case 21:
                case 28:
                case 29:
                case 30:
                    // result slot
                    break;
                case 25:
                    inv.setItem(i, this.generateSaveResultItem(rip));
                    break;
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
                if(slot <= this.INVENTORY_SIZE){
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
                
                int[] slots = new int[]{10,11,12,19,20,21,28,29,30};
                
                ItemStack[] recipe = new ItemStack[9];
                
                for(int i=0;i<slots.length;i++){
                    ItemStack is = inv.getItem(slots[i]);
                    
                    if(is == null || is.getType().equals(Material.AIR)){
                        recipe[i] = new ItemStack(Material.AIR);
                    }
                    else {
                        recipe[i] = inv.getItem(i);
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
        Inventory inv = e.getInventory();
        
        switch(slot){
            default:// GUI BG
                if(slot <= this.INVENTORY_SIZE){
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
                ItemStack[] potentialRecipe = new ItemStack[9];
                int[] itemRows = new int[]{10,11,12,19,20,20,28,29,30};

                for(int i=0;i<9;i++){
                    ItemStack is = inv.getItem(itemRows[i]);
                    if(is == null || is.getType().equals(Material.AIR)){
                        potentialRecipe[i] = new ItemStack(Material.AIR);
                    }
                    else {
                        potentialRecipe[i] = is;
                    }
                }

                ItemStack currentResult = inv.getItem(25);

                ItemStack newResult = this.plugin.getRecipeManager().getResultOf(potentialRecipe);

                if(!currentResult.equals(newResult)){
                    inv.setItem(25, newResult);
                }
                
                return;

            case 25:// craft essence
                e.setCancelled(true);
                    
                ItemStack result = inv.getItem(25);
                
                if(result == null || result.getType().equals(Material.AIR)){
                    return;
                }
                
                final RareItemProperty rip = this.plugin.getRecipeManager().getPropertyFromRareEssence(result);
                
                if(rip == null){
                    return;
                }
                
                final Player p = (Player) e.getWhoClicked();
                
                p.sendMessage("Would craft "+rip.getName());
                
                p.closeInventory();
                
                String[] sCoords = inv.getItem(8).getItemMeta().getDisplayName().substring(2).split(" ");
                
                final Block block;
                
                try{
                    block = p.getWorld().getBlockAt(
                            Integer.parseInt(sCoords[0]), 
                            Integer.parseInt(sCoords[1]), 
                            Integer.parseInt(sCoords[2])
                    );
                }
                catch(NumberFormatException ex){
                    return;
                }
            
                if(this.isLegendaryShrineBlock(block)){
                    final Block bUp = block.getRelative(BlockFace.UP,1);
                    
                    bUp.setType(Material.AIR);
                    
                    final BlockFace[] affectedFaces = new BlockFace[]{
                        BlockFace.SELF,
                        BlockFace.NORTH,
                        BlockFace.SOUTH,
                        BlockFace.EAST,
                        BlockFace.WEST,
                        BlockFace.NORTH_EAST,
                        BlockFace.NORTH_WEST,
                        BlockFace.SOUTH_EAST,
                        BlockFace.SOUTH_WEST
                    };
                    
                    for(BlockFace bf : affectedFaces){
                        bUp.getRelative(bf).setType(Material.AIR);
                    }
                    
                    block.getWorld().strikeLightningEffect(block.getLocation());
                    
                    block.getWorld().playEffect(block.getLocation(), Effect.EXPLOSION, 5);
                    
                    bUp.getRelative(BlockFace.UP).setType(Material.LAVA);
                    
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                        @Override
                        public void run() {
                            bUp.getRelative(BlockFace.UP).setType(Material.AIR);
                            
                            block.setMetadata("rihCrafted"+p.getUniqueId().toString(), 
                                new FixedMetadataValue(plugin, plugin.getRecipeManager().generateRareEssence(rip))
                            );
                            
                            block.getWorld().playEffect(block.getLocation(), Effect.INSTANT_SPELL, 5);
                    
                            p.sendMessage(ChatColor.GREEN+"Your item has been crafted!");
                        }
                    }, 20*3);
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
}
