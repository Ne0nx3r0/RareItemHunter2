package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.entity.Player;

public class MageFX extends RareItemProperty
{
    public MageFX()
    {
        super(
            PropertyType.MAGE_FX.ordinal(),
            "Mage FX",
            "While worn as armor or a helmet shows a visual enchantment effect around you",
            PropertyCostType.AUTOMATIC,
            3,
            1
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {	
        ParticleEffect.ENCHANTMENT_TABLE.display(
            player.getLocation().add(0,2,0), //center
            0.25F, //offsetX
            0.25F, //offsetY
            0.25F, //offsetZ
            4F, //speed
            16 // amount of particles
        );
    }
}
