package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.entity.Player;

public class RainbowFuryFX extends ItemPropertyRepeatingEffect
{
    public RainbowFuryFX()
    {
        super(
            PropertyType.RAINBOW_FURY_FX.ordinal(),
            "Rainbow Fury FX",
            "While worn as armor or a helmet shows a smattering of colored explosions",
            PropertyCostType.AUTOMATIC,
            3,
            1
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {	
        ParticleEffect.RED_DUST.display(
            player.getLocation().add(0,1,0), //center
            0.6F, //offsetX
            0.25F, //offsetY
            0.6F, //offsetZ
            100F, //speed
            12 // amount of particles
        );
    }
}
