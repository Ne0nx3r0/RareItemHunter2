package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MermaidsGift extends ItemPropertyRepeatingEffect{ 
    public MermaidsGift(){
        super(
            PropertyType.MERMAIDS_GIFT.ordinal(),
            "Mermaid's Gift",
            "Allows for better control and faster movement while underwater",
            PropertyCostType.AUTOMATIC,
            1,
            1
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {
        Location pLoc = player.getLocation();
        
        boolean isInWater = pLoc.clone().getBlock().getType().equals(Material.STATIONARY_WATER)
        && pLoc.clone().add(0,1,0).getBlock().getType().equals(Material.STATIONARY_WATER)
        && pLoc.clone().add(0,-1,0).getBlock().getType().equals(Material.STATIONARY_WATER)
        && pLoc.clone().add(-1,0,1).getBlock().getType().equals(Material.STATIONARY_WATER)
        && pLoc.clone().add(1,0,-1).getBlock().getType().equals(Material.STATIONARY_WATER);
        
        if(isInWater){
            if(!player.isFlying()){
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setFlySpeed((float) 0.1);
            }

            if(!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20*60*60,2));
            }
        }
        else if(player.isFlying()){
            player.setAllowFlight(false);

            if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        }
    }
    
    @Override
    public void removeEffectFromPlayer(Player player){
        player.setAllowFlight(false);

        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }
}