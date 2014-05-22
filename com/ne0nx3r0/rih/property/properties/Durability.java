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
        super(PropertyType.DURABILITY.ordinal(),"Durability","Automagically repairs equipped armor over time",PropertyCostType.AUTOMATIC,5,5);
        
        //this.createRepeatingAppliedEffect(this, 20*10);
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {
        ItemStack[] armor = player.getInventory().getArmorContents();
        
        for(int i = 0; i < armor.length; i++)
        {
            if(armor[i].getDurability() > 0)
            {
                int iNewArmor = armor[i].getDurability() - 1 * level;
                
                if(iNewArmor < 0)
                {
                    iNewArmor = 0;
                }
                
                armor[i].setDurability((short) iNewArmor);
            }
        }
    }
}
