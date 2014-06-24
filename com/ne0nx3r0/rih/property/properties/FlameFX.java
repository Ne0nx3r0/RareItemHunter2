package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class FlameFX extends RareItemProperty
{
    public FlameFX()
    {
        super(
            PropertyType.FLAME_FX.ordinal(),
            "Flame FX",
            "While worn as armor or a helmet shows a visual flame effect around you",
            PropertyCostType.AUTOMATIC,
            1,
            1
        );
    }

    @Override
    public void applyEffectToPlayer(Player player, int level)
    {	
        /**
	 * Displays a particle effect which is only visible for all players within a range of 20 in the world of @param center
	 * 
	 * @param center Center location of the effect
	 * @param offsetX Maximum distance particles can fly away from the center on the x-axis
	 * @param offsetY Maximum distance particles can fly away from the center on the y-axis
	 * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
	 * @param speed Display speed of the particles
	 * @param amount Amount of particles
	 * @param players Receivers of the effect
	 * @see #display(Location, double, float, float, float, float, int)
	 */
        ParticleEffect.FLAME.display(
                player.getLocation().add(0,1.5,0), //center
                0.05F, //offsetX
                0.1F, //offsetY
                0.05F, //offsetZ
                0.05F, //speed
                12 // amount of particles
        );
        /*ParticleEffect.HEART.display(player.getLocation(), .25F, .1F, .25F, .1F, 33);
        ParticleEffect.SNOW_SHOVEL.display(player.getLocation(), .15F, 0.15F, 0.15F, .1F, 10);
        ParticleEffect.LAVA.display(player.getLocation().add(0.5D, 0.5D, 0.5D), 0.0f, 0.0f, 0.0f, 1.0F, 30);
        ParticleEffect.CLOUD.display(player.getLocation(), 1.0F, 1.0F, 1.0F, 0.9F, 33);
        ParticleEffect.INSTANT_SPELL.display(player.getLocation(), .25F, .1F, .25F, .1F, 33);
        ParticleEffect.BUBBLE.display(player.getLocation(), .15F, 0.15F, 0.15F, .1F, 10);
        ParticleEffect.WAKE.display(player.getLocation().add(0.5D, 0.5D, 0.5D), 0.0f, 0.0f, 0.0f, 1.0F, 30);*/
    }
}
