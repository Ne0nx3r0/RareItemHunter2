package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;

public class Hardy extends RareItemProperty
{
    public Hardy()
    {
        super(PropertyType.HARDY.ordinal(),"Hardy","-1 damage / lvl",PropertyCostType.PASSIVE,0,3);
    }
}
