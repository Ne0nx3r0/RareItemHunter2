package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RepairItem extends RareItemProperty
{
    public RepairItem()
    {
        super(
            PropertyType.REPAIR_ITEM.ordinal(),
            "Repair Item",
            "Repairs the #1 hotbar slot item",
            PropertyCostType.LEVEL,
            1,
            5
        );
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        ItemStack isSlotOne = e.getPlayer().getInventory().getItem(0);
        
        if(isSlotOne != null && isSlotOne.getType().getMaxDurability() > 20 && isSlotOne.getDurability() > 0)
        {
            int iRepairAmount = isSlotOne.getType().getMaxDurability() / 5 * level - (isSlotOne.getType().getMaxDurability()/10);
            
            short sDurability = (short) (isSlotOne.getDurability() - iRepairAmount);
            
            if(sDurability < 0)
            {
                sDurability = 0;
            }
            
            isSlotOne.setDurability(sDurability);
            
            e.getPlayer().sendMessage("Item repaired by "+(level*20)+"%!");
            
            return true;
        }
        else
        {
            e.getPlayer().sendMessage("Item in slot #1 is not repairable!");
        }
        
        return false;
    }
    
    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent e, int level)
    {
        if(e.getRightClicked() instanceof Player)
        {
            Player pClicked = (Player) e.getRightClicked();
            ItemStack isSlotOne = pClicked.getItemInHand();

            if(isSlotOne.getType().getMaxDurability() > 20)
            {
                int iRepairAmount = isSlotOne.getType().getMaxDurability() / 5 * level - (isSlotOne.getType().getMaxDurability()/10);

                short sDurability = (short) (isSlotOne.getDurability() - iRepairAmount);

                if(sDurability < 0)
                {
                    sDurability = 0;
                }

                isSlotOne.setDurability(sDurability);

                e.getPlayer().sendMessage("Item in "+pClicked.getName()+"'s hand repaired by "+(level*20)+"%!");

                return true;
            }
            else
            {
                e.getPlayer().sendMessage("Item in "+pClicked.getName()+"'s hand is not repairable!");
            }
            
            return true;
        }
        return false;
    }
}