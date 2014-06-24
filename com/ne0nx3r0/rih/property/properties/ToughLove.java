package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;

public class ToughLove extends RareItemProperty
{
    public ToughLove()
    {
        super(PropertyType.TOUGH_LOVE.ordinal(),"Tough Love","While worn as armor or a helmet causes a heart effect when harmed",PropertyCostType.PASSIVE,0,1);
    }
}
