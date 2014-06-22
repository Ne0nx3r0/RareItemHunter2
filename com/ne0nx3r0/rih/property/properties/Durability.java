package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Durability extends ItemPropertyRepeatingEffect
{
    
    public Durability()
    {
        super(
            PropertyType.DURABILITY.ordinal(),
            "Durability",
            "Automagically repairs equipped armor and held weapons by 2% per level every 10 seconds",
            PropertyCostType.AUTOMATIC,
            10,
            8
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {
        ItemStack[] armor = player.getInventory().getArmorContents();
        
        for(int i = 0; i < armor.length; i++)
        {
            this.repairItem(armor[i], level);
        }
        
        ItemStack heldItem = player.getItemInHand();
        
        if(heldItem != null){
            this.repairItem(heldItem, level);
        }
    }
    
    public void repairItem(ItemStack is,int level){
        // excludes stuff like dyes
        if(is.getType().getMaxDurability() > 20 && is.getDurability() > 0){
            int maxDurability = is.getType().getMaxDurability();
            
            // 5% per level
            int repairAmount = maxDurability / 100 * level * 2;
            
            int newDurability = is.getDurability() - repairAmount;
            
            if(newDurability < 0){
                newDurability = 0;
            }
            
            is.setDurability((short) newDurability);
        }
    }
}