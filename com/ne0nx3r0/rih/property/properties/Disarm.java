package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Disarm extends RareItemProperty
{
    private List<Material> disarmables;
    
    public Disarm()
    {
        super(
            PropertyType.DISARM.ordinal(),
            "Disarm",
            "2% chance/level on hit to cause a target to exchange their held weapon for a random one from their inventory",
            PropertyCostType.FOOD,
            2,
            8
        );

        disarmables = new ArrayList<Material>(){};
        disarmables.add(Material.WOOD_SWORD);
        disarmables.add(Material.STONE_SWORD);
        disarmables.add(Material.IRON_SWORD);
        disarmables.add(Material.GOLD_SWORD);
        disarmables.add(Material.DIAMOND_SWORD);
        disarmables.add(Material.STONE_AXE);
        disarmables.add(Material.IRON_AXE);
        disarmables.add(Material.GOLD_AXE);
        disarmables.add(Material.DIAMOND_AXE);
        disarmables.add(Material.BOW);
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(new Random().nextInt(100) < level * 2
        && e.getEntity() instanceof Player)
        {
            Player pAttacked = (Player) e.getEntity();
            
            if(pAttacked.getOpenInventory() == null
            && pAttacked.getItemInHand() != null
            && disarmables.contains(pAttacked.getItemInHand().getType()))
            {
                int iRandomSlot = (new Random()).nextInt(44)+9;

                ItemStack swapOut = pAttacked.getInventory().getItem(pAttacked.getInventory().getHeldItemSlot());
                ItemStack swapIn = pAttacked.getInventory().getItem(iRandomSlot);
                
                pAttacked.getInventory().setItem(pAttacked.getInventory().getHeldItemSlot(), swapIn);
                pAttacked.getInventory().setItem(iRandomSlot, swapOut);
                
                p.sendMessage("Disarmed "+pAttacked.getName()+"!");
                
                pAttacked.sendMessage("You have been disarmed!");

                return true;
            }
        }
        
        return false;
    }
}