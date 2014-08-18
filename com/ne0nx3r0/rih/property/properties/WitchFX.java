package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.entity.Player;

public class WitchFX extends RareItemProperty
{
    public WitchFX()
    {
        super(
            PropertyType.WITCH_FX.ordinal(),
            "Witch FX",
            "While worn as armor or a helmet shows purple sparkles",
            PropertyCostType.AUTOMATIC,
            1,
            1
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {	
        ParticleEffect.WITCH_MAGIC.display(
            player.getLocation().add(0,1,0), //center
            0.5F, //offsetX
            0.5F, //offsetY
            0.5F, //offsetZ
            0.25F, //speed
            10 // amount of particles
        );
    }
}
