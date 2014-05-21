package com.ne0nx3r0.rih.property.properties;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BuildersWand extends RareItemProperty
{
    public BuildersWand()
    {
        super(PropertyType.BUILDERS_WAND.ordinal(),"Builder's Wand","Clones block shapes using items from your inventory",PropertyCostType.COOLDOWN,1,2);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        int maxCopied = level * 5;
        
        if(e.getClickedBlock() != null)
        {
            String sPlayerName = e.getPlayer().getName();
            String sWorldName = e.getPlayer().getLocation().getWorld().getName();
            
            Block clickedBlock = e.getClickedBlock();
            BlockFace baseFace = e.getBlockFace();
            Material type = clickedBlock.getType();
            
            ArrayList<Block> blocksToBuildOn = this.addBlocksToBuildOn(new ArrayList<Block>(), clickedBlock, baseFace,maxCopied);
            
            if(!blocksToBuildOn.contains(clickedBlock) && clickedBlock.getRelative(baseFace).getType().equals(Material.AIR)) {
                blocksToBuildOn.add(clickedBlock);
            }
            
            PlayerInventory inventory = e.getPlayer().getInventory();
            
            int changedBlocks = 0;

            for(Block b : blocksToBuildOn) {
                ItemStack is = new ItemStack(type,1,(short) 0,b.getData());
                FlagPermissions perms = Residence.getPermsByLocForPlayer(b.getLocation(), e.getPlayer());

                if(perms.playerHas(sPlayerName, sWorldName, "build", false)) {
                    HashMap<Integer, ItemStack> leftovers = inventory.removeItem(is);

                    if(leftovers.isEmpty()) {
                        Block bFace = b.getRelative(baseFace);

                        bFace.setType(is.getType());
                        bFace.setData(is.getData().getData());

                        changedBlocks++;
                    }        
                    else {
                        break;
                    }
                }
            }
            
            e.getPlayer().updateInventory();
            
            return changedBlocks > 0;
        }
        
        return false;
    }
    
    public ArrayList<Block> addBlocksToBuildOn(ArrayList<Block> blocksToChange,Block startingPoint,BlockFace baseFace,int maxCopied) {
        BlockFace[] affectedFaces = this.getAffectedBlockFaces(baseFace);
        Material baseMaterial = startingPoint.getType();
        
        for(BlockFace face : affectedFaces) {
            if(blocksToChange.size() >= maxCopied) {
                break;
            }
            
            Block b = startingPoint.getRelative(face);
            
            if(!blocksToChange.contains(b) 
             && b.getType().equals(baseMaterial)
             && b.getRelative(baseFace).getType().equals(Material.AIR)) {
                blocksToChange.add(b);
                
                blocksToChange = this.addBlocksToBuildOn(blocksToChange, b, baseFace, maxCopied);
            }
        }
        
        return blocksToChange;
    }
    
    
    
    public BlockFace[] getAffectedBlockFaces(BlockFace bf) {
        switch(bf) {
            case DOWN:
            case UP:
                return new BlockFace[]{BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST};
            case NORTH:
            case SOUTH:
                return new BlockFace[]{BlockFace.UP,BlockFace.EAST,BlockFace.DOWN,BlockFace.WEST};
            case EAST:
            case WEST:
                return new BlockFace[]{BlockFace.UP,BlockFace.NORTH,BlockFace.DOWN,BlockFace.SOUTH};
        }
        return null;
    }
}
