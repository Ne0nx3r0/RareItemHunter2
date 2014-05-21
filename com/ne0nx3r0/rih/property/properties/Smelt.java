package com.ne0nx3r0.rih.property.properties;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Smelt extends RareItemProperty
{
    public Smelt()
    {
        super(PropertyType.SMELT.ordinal(),"Smelt","Turns clicked cobblestone into stone",PropertyCostType.COOLDOWN,1,1);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.COBBLESTONE)
        {
            e.getClickedBlock().setType(Material.STONE);
            
            return true;
        }
        return false;
    }
}