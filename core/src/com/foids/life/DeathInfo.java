package com.foids.life;

/**
 * This is used to save info to a file in order to get stats.
 * Created by Cedric Martens on 2016-10-08.
 */
public class DeathInfo {

    private long birthTime, deathTime;
    private int id, parentId;
    private float sight, maxSpeed;
    private int foodAte;

    public DeathInfo(long birthTime, long deathTime, int id, int parentId, float sight, float maxSpeed, int foodAte) {
        this.birthTime = birthTime;
        this.deathTime = deathTime;
        this.id = id;
        this.parentId = parentId;
        this.sight = sight;
        this.maxSpeed = maxSpeed;
        this.foodAte = foodAte;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public long getDeathTime() {
        return deathTime;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public float getSight() {
        return sight;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public int getFoodAte() {
        return foodAte;
    }

    @Override
    public String toString()
    {
        return getId()+","+getParentId()+","+getBirthTime()+","+getDeathTime()+","+getMaxSpeed()+","+getSight()+","+getFoodAte();
    }
}
