package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class PaintWool extends RareItemProperty
{
    public PaintWool()
    {
        super(PropertyType.PAINT_WOOL.ordinal(),"Paint Wool","Changes the color of a clicked wool block",PropertyCostType.COOLDOWN,0.5,1);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null)
        {
            if(e.getClickedBlock().getType() == Material.WOOL)
            {
                Block woolBlock = e.getClickedBlock();
                byte woolData = woolBlock.getData();
                
                if(woolData == 0x15)
                {
                    woolData = 0x0;
                }
                else
                {
                    woolData++;
                }
                
                woolBlock.setData(woolData);
                
                return true;
            }
        }
        return false;
    }
}