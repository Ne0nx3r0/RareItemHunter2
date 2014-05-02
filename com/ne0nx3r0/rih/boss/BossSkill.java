
package com.ne0nx3r0.rih.boss;

public class BossSkill
{
    int chance;
    int level;
    BossSkillTemplate bossSkill;
    
    public BossSkill(BossSkillTemplate bossSkill,int level,int chance)
    {
        this.bossSkill = bossSkill;
        this.level = level;
        this.chance = chance;
    }
}
