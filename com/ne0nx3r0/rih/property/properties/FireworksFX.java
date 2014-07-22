package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.entity.Player;

public class FireworksFX extends RareItemProperty
{
    public FireworksFX()
    {
        super(
            PropertyType.FIREWORKS_FX.ordinal(),
            "Fireworks FX",
            "While worn as armor or a helmet shows a visual fireworks effect around you",
            PropertyCostType.AUTOMATIC,
            1,
            1
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {	
        ParticleEffect.FIREWORKS_SPARK.display(
            player.getLocation().add(0,1.5,0), //center
            0.6F, //offsetX
            0.6F, //offsetY
            0.6F, //offsetZ
            0.1F, //speed
            5 // amount of particles
        );
    }
}
