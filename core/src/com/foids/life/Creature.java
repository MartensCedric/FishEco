package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.foids.FishEco;

/**
 * Created by Cedric Martens on 2016-10-10.
 */
public abstract class Creature {

    private FishEco game;

    private Vector2 desired;
    private Vector2 force;
    private Vector2 velocity;
    private Vector2 location;
    private Vector2 originVector;

    private float sight;
    private float maxSpeed;
    private float awareness;

    private float mass;

    private int width;
    private int height;

    private Texture texture;
    private TextureRegion textureRegion;

    private float dir;

    private int foodAte;
    private float belly;

    public abstract void update();

    /**
     * Will draw the creature using its TextureRegion, using size from the texture at the position
     * of the location Vector2 with a scale of 1 and facing its direction.
     */
    public void draw()
    {
        getGame().getBatch().draw(getTextureRegion(), getLocation().x, getLocation().y,
                getOriginRelativeToX(), getOriginRelativeToY(),
                getTexture().getWidth(), getTexture().getHeight(),
                1,1, getDir());
    }

    /**
     * The sight is the distance the creature can perceive things such as food.
     * @return the sight value as a radius in pixels between 10 and 50.
     */
    public float getSight() {
        return sight;
    }

    /**
     * The sight is the distance the creature can perceive things such as food.
     * This value must be between 10 and 50.
     * @param sight
     */
    public void setSight(float sight) {
        this.sight = sight;
    }

    /**
     * The awareness is the distance that a creature reacts to predators.
     * @return the awareness value
     */
    public float getAwareness() {
        return awareness;
    }

    /**
     * The awareness is the distance that a creature reacts to predators.
     * @param awareness
     */
    public void setAwareness(float awareness) {
        this.awareness = awareness;
    }

    /**
     * The maxSpeed is the maximum speed a creature can swim.
     * @return
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Mass is how heavy a creature is. This affects speed.
     * @return Mass value
     */
    public float getMass() {
        return mass;
    }

    /**
     * Mass is how heavy a creature is. This affects speed.
     * @param mass
     */
    public void setMass(float mass) {
        this.mass = mass;
    }


    public Vector2 getDesired() {
        return desired;
    }

    public void setDesired(Vector2 desired) {
        this.desired = desired;
    }

    public Vector2 getForce() {
        return force;
    }

    public void setForce(Vector2 force) {
        this.force = force;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getOriginVector() {
        return originVector;
    }

    public void setOriginVector(Vector2 originVector) {
        this.originVector = originVector;
    }



    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public int getOriginRelativeToX() {
        return getWidth()/2;
    }

    public int getOriginRelativeToY() {
        return getHeight()/2;
    }

    public float getOriginX()
    {
        return getLocation().x + getOriginRelativeToX();
    }

    public void setOriginX(float x)
    {
        location.x -= getOriginX() - x;
    }

    public float getOriginY()
    {
        return getLocation().y + getOriginRelativeToY();
    }

    public void setOriginY(float y)
    {
        location.y -= getOriginY() - y;
    }


    public FishEco getGame() {
        return game;
    }

    public void setGame(FishEco game) {
        this.game = game;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    /**
     * The total amount of food eaten by this creature.
     * @return gets the amount of food eaten by this creature.
     */
    public int getFoodAte(){return foodAte;}

    /**
     * Sets the amount of food eaten by this creature.
     * @param foodAte
     */
    public void setFoodAte(int foodAte){this.foodAte = foodAte;}

    /**
     * The creature eats a food. the FoodAte value increments by 1
     * @param foodValue The value of the food that the creature ate. The belly will be increased by this value.
     */
    public void ateFood(float foodValue)
    {
        this.belly += foodValue;
        foodAte++;
    }

    /**
     * The belly represents how filled the stomach of the creature is. Too low will represent death.
     * @return a value between 0f and 1f representing how filled the stomach of the creature is.
     */
    public float getBelly() {
        return belly;
    }

    /**
     * Sets the value that represent how filled the stomach of the creature is.
     * @param belly a value between 0f and 1f representing how filled the stomach of the creature is.
     */
    public void setBelly(float belly) {
        this.belly = belly;
    }

    /**
     * With time the creature digests its food
     * @param amount how much the belly has shrank
     */
    public void crave(float amount)
    {
        this.belly -= amount;
    }

    public float getDir() {
        return dir;
    }

    public void setDir(float dir) {
        this.dir = dir;
    }

    /**
     * Will return the degree that the Creature is facing in radian
     * @return degree that the Creature is facing in radian
     */
    public float getDirection(Vector2 vec)
    {
        return MathUtils.atan2(vec.y,vec.x) - (float)Math.PI/2;
    }

    /**
     * Will get the degree that the Creature is facing in degrees
     * @param vec Vector we want to check tan(y/x)to degrees
     * @return inclination degree
     */
    public float getDirectionDegrees(Vector2 vec)
    {
        return (float)Math.toDegrees(getDirection(vec));
    }

    /**
     * Gets the vector in the flow field that the Creature's origin is on
     * @return A vector in a flow field
     */
    public Vector2 vectorFromField()
    {
        if(getOriginX() >= Gdx.graphics.getWidth())
            return new Vector2(1f,0f);

        if(getOriginX() <= 0)
            return  new Vector2(-1f, 0f);

        if(getOriginY() >= Gdx.graphics.getHeight())
            return new Vector2(0f, 1f);

        if(getOriginY() <= 0)
            return new Vector2(0f, -1f);

        return game.getField().getFieldData()[(int) getOriginX() / game.getField().getTileWidth()][(int) getOriginY() /game.getField().getTileHeight()];
    }


}
