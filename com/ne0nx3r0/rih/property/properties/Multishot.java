package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import java.util.Random;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Multishot extends RareItemProperty
{
    public Multishot()
    {
        super(
            PropertyType.MULTISHOT.ordinal(),
            "Multishot",
            "Fires one extra arrow per level when shot from a bow",
            PropertyCostType.FOOD,
            1,
            8
        );
    }
    
    private double randomDouble(Random rand,double a, double b) {
        double random = (rand.nextFloat()) / (double) Double.MAX_VALUE;
        double diff = b - a;
        double r = random * diff;
        return a + r;
    }

    @Override
    public boolean onLaunchProjectile(EntityShootBowEvent e, Player shooter, int level){
        Random r = new Random();
        Vector originalArrowVector = e.getProjectile().getVelocity();
        
        for(int i=0;i<level;i++){
            Vector bonusArrowVector = new Vector(
                originalArrowVector.getX()+randomDouble(r,-0.1f,0.1f),
                originalArrowVector.getY()+randomDouble(r,-0.1f,0.1f),
                originalArrowVector.getZ()+randomDouble(r,-0.1f,0.1f)
            );

            Arrow bonusArrow = shooter.getWorld().spawnArrow(e.getProjectile().getLocation(), bonusArrowVector, e.getForce()*5, 12);

            //bonusArrow.setShooter(shooter);
        }
        
        return true;
    }
}