package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;

public class WaterBreathing extends RareItemProperty
{
    public WaterBreathing()
    {
        super(PropertyType.WATER_BREATHING.ordinal(),"Water Breathing","While worn as armor or a helmet allows underwater breathing",PropertyCostType.PASSIVE,0,1);
    }
}
