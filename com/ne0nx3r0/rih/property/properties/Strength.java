package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;

public class Strength extends RareItemProperty
{
    public Strength()
    {
        super(
            PropertyType.STRENGTH.ordinal(),
            "Strength",
            "While worn as armor or a helmet adds 1 damage per lvl",
            PropertyCostType.PASSIVE,
            0,
            3
        );
    }
}
