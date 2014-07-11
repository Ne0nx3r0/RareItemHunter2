package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.ParticleEffect;
import org.bukkit.entity.Player;

public class ToughLove extends RareItemProperty
{
    public ToughLove()
    {
        super(PropertyType.TOUGH_LOVE.ordinal(),"Tough Love","While worn as armor or a helmet causes a heart effect when harmed",PropertyCostType.PASSIVE,0,1);
    }
    
    @Override
    public void applyEffectToPlayer(Player p, int level){
        ParticleEffect.HEART.display(
            p.getLocation().add(0,1.5,0), //center
            0.4F, //offsetX
            0.75F, //offsetY
            0.4F, //offsetZ
            0.1F, //speed
            4 // amount of particles
        );
    }
}
