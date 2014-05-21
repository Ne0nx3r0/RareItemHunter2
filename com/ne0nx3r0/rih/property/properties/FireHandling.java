package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FireHandling extends RareItemProperty
{
    public FireHandling()
    {   
        super(PropertyType.FIRE_HANDLING.ordinal(),"Fire Handling","Clicked fire becomes a holdable item.",PropertyCostType.HEALTH,2,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null)
        {
            Block bFire = e.getClickedBlock().getRelative(BlockFace.UP);
            
            if(bFire != null && bFire.getType() == Material.FIRE)
            {
                bFire.setType(Material.AIR);

                ItemStack fire = new ItemStack(Material.FIRE);

                e.getPlayer().getWorld().dropItemNaturally(bFire.getLocation(), fire);
                
                return true;
            }
        }
        return false;
    }
}