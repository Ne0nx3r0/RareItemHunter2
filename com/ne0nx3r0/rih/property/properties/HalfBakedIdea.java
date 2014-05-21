package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.Random;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HalfBakedIdea extends RareItemProperty
{
    public HalfBakedIdea()
    {
        super(PropertyType.HALF_BAKED_IDEA.ordinal(),"Half-Baked Idea","To the lab mouse, life is a confusing array of cheese and electricity",PropertyCostType.FOOD,2,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        Random r = new Random();
        if(r.nextBoolean())
        {
            for(int i=0;i<level;i++)
            {
                PotionEffectType[] potionEffects = PotionEffectType.values();

                e.getPlayer().addPotionEffect(new PotionEffect(potionEffects[r.nextInt(potionEffects.length)],20*60,1));

                e.getPlayer().sendMessage("Something happened!");
            }
        
            return true;
        }
        else
        {        
            e.getPlayer().sendMessage("Nothing happened?");
            
            return false;
        }
    }
}