package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;

public class ToughLove extends ItemPropertyRepeatingEffect
{
    public ToughLove()
    {
        super(PropertyType.TOUGH_LOVE.ordinal(),"Tough Love","(VFX) Hearts when harmed",PropertyCostType.PASSIVE,0,1);
    }
}
