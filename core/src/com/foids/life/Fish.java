package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.foids.FishEco;

import java.util.Random;

/**
 * A fish that swims with the flow field, detects food, goes in groups and avoids sharks.
 * Created by Cedric on 2016-09-21.
 */
public class Fish extends Creature{

    private final float MIN_SIGHT_VALUE = 10f;
    private final float MAX_SIGHT_VALUE = 50;

    private int color;

    private byte[] fishTexture;

    private static int idCounter = 0;
    private int id;

    private float energyLossSpeed;

    private boolean dead;

    private int parentID;

    private long textureTimer;
    private Fish parent;

    private Food foodTarget;

    private long birthTime;

    private Random randomizer;

    public Fish(int x, int y, int width, int height, byte[] texture, FishEco game)
    {
        this(x,y,width,height,texture,null, game);
    }

    public Fish(int x, int y, int width, int height, byte[] fishTexture, Fish father, FishEco game)
    {
        this.id = idCounter++;

        this.parent = father;

        if(parent == null)
            parentID = -1;
        else
            parentID = parent.getId();

        setFoodAte(0);
        this.randomizer = new Random();

        if(parent != null)
            if(parent.getParent() != null)
                parent.getParent().setParent(null);


        setBelly(1f);
        this.energyLossSpeed = 1/3000f;

        if(parent == null)
        {
            setMaxSpeed(0.25f +  (randomizer.nextFloat()*0.75f));
            setSight(10 + randomizer.nextInt((int)(MAX_SIGHT_VALUE - MIN_SIGHT_VALUE)));
        }else{
            setMaxSpeed(parent.getMaxSpeed() + MathUtils.randomTriangular()/MIN_SIGHT_VALUE);
            setSight(parent.getSight() + MathUtils.randomTriangular()*5);


            if(getMaxSpeed() < 0)
                setMaxSpeed(0);
            else if(getMaxSpeed() > 1)
                setMaxSpeed(1);

            if(getSightNormalized() < 0)
                setSight(MIN_SIGHT_VALUE);
            else if (getSightNormalized() > 1)
                setSight(MAX_SIGHT_VALUE);
        }

        textureTimer = TimeUtils.millis();
        birthTime = game.getNumberOfFrames();

        color = Color.rgba8888(getMaxSpeed(), getSightNormalized(), 1f, getBelly());

        setGame(game);

        setWidth(width);
        setHeight(height);

        setFoodTarget(null);

        this.fishTexture = fishTexture;
        createFishTexture();
        setTextureRegion(new TextureRegion(getTexture()));

        setMass(1.25f);

        setLocation(new Vector2(x,y));
        setVelocity(new Vector2(0,0));
        setForce(new Vector2(0,0));
        setDesired(new Vector2(0.25f, 0));
        setOriginVector(new Vector2(getLocation().x + getOriginRelativeToX(), getLocation().y + getOriginRelativeToY()));

        dead = false;

        System.out.println("Welcome to the world fish " + getId());
    }

    @Override
    public void update()
    {
        applyForce();
        getDesired().x = 0.25f;
        getDesired().y = 0f;

        //If there's no food target
        if(getFoodTarget() == null)
        {
            for(Food food : getGame().getFoodList())
            {
                //And theres a food close, assign it as a new target
                if(Math.sqrt(Math.pow(getOriginX() - food.getX(), 2) + Math.pow(getOriginY() - food.getY(), 2)) <= getSight())
                {
                    setFoodTarget(food);
                    break;
                }
            }
        //if the current food target is too far
        }else if(Math.sqrt(Math.pow(getOriginX() - getFoodTarget().getOriginX(), 2) + Math.pow(getOriginY() - foodTarget.getOriginY(), 2)) > getSight())
        {
            foodTarget = null;
        //if the current food target is close enough to it
        }else if(foodTarget.contains(getOriginVector()))
        {

            ateFood(foodTarget.getQuantity());

            if(getBelly() > 1f)
                setBelly(1f);

            getGame().getFoodList().remove(foodTarget);

            if(getGame().getFoodList().size() == 19)
                getGame().getFoodList().add(new Food(getGame()));

            createFishTexture();
            foodTarget = null;
        }else{
            //Make sure the food still exist (If another fish hasn't ate it yet)
            boolean stopChasing = true;
            for(Food food : getGame().getFoodList())
            {
                if(food == foodTarget)
                {
                    stopChasing = false;
                    break;
                }
            }

            if(stopChasing)
            {
                foodTarget = null;
            }
        }

        crave(energyLossSpeed);

        if(getBelly() <= 0.10f)
        {
            dead = true;
            System.out.println("Fish " + getId() + " has died of hunger.");
        }

        if(textureTimer + 2_000 < TimeUtils.millis())
        {
            textureTimer = TimeUtils.millis();

            color = Color.rgba8888(getMaxSpeed(), getSightNormalized(), 1f, getBelly());
            createFishTexture();
            setTextureRegion(new TextureRegion(getTexture()));//This seems to bug less even if I make a new TextureRegion every time
        }


        if(this.randomizer.nextInt(15000) < 3)
        {
            getGame().getEggList().add(new Egg(this, getGame()));
        }

        getOriginVector().x = getLocation().x + getOriginRelativeToX();
        getOriginVector().y = getLocation().y + getOriginRelativeToY();
    }

    /**
     * Creates the texture for the fish with the supplied array of bytes.
     */
    private void createFishTexture()
    {
        Gdx2DPixmap pxMap2D = new Gdx2DPixmap(getWidth(), getHeight(), Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);

        for(int i = 0; i < fishTexture.length; i++)
        {
            if(fishTexture[i] == 1)
                pxMap2D.setPixel(i%getWidth(), (i/getWidth()), Color.rgba8888(0, 0, 0, 1f));
            else if (fishTexture[i] == 2)
                pxMap2D.setPixel(i%getWidth(), (i/getWidth()), color);
        }

        Pixmap pxMap = new Pixmap(pxMap2D);
        setTexture(new Texture(pxMap, Pixmap.Format.RGBA8888, false));
    }

    /**
     * The forces are applied to calculate the new location of the fish and it's rotation.
     */
    public void applyForce()
    {
        getVelocity().scl(0);
        getForce().scl(0);
        getForce().add(vectorFromField());
        getForce().scl(getMaxSpeed()/getMass()); //THIS IS NO GOOD NEED CHANGE
        getVelocity().add(getForce());

        if(foodTarget == null)
        {
            getVelocity().add(getDesired());
            setDir(getDirectionDegrees(getVelocity()));
        }
        else
        {
            getDesired().x = foodTarget.getOriginX() - getOriginX();
            getDesired().y = foodTarget.getOriginY() - getOriginY();
            getDesired().nor();
            getDesired().scl(getMaxSpeed());
            setDir(getDirectionDegrees(getDesired()));
            getVelocity().add(getDesired());
        }

        getLocation().add(getVelocity());

        if(getOriginY() > Gdx.graphics.getHeight() - 1)
            setOriginY(1);

        if(getOriginY() < 1)
            setOriginY(Gdx.graphics.getHeight() - 1);

        if(getOriginX() > Gdx.graphics.getWidth() - 1)
            setOriginX(1);

        if(getOriginX() < 1)
            setOriginX(Gdx.graphics.getWidth() - 1);

    }


    public int getId() {
        return id;
    }

    public boolean isDead() {
        return dead;
    }

    public Fish getParent() {
        return parent;
    }

    public int getParentID()
    {
        return parentID;
    }

    public void setParent(Fish parent) {
        this.parent = parent;
    }

    public long getBirthTime() {
        return birthTime;
    }


    public Food getFoodTarget() {
        return foodTarget;
    }

    public void setFoodTarget(Food foodTarget) {
        this.foodTarget = foodTarget;
    }

    /**
     * The sight normalized is the sight value scaled between 0 and 1.
     * Normally the sight value is the radius that a Creature can see and is between 10 and 50.
     * @return the sight value between 0 and one
     */
    public float getSightNormalized()
    {
        return (getSight() - MIN_SIGHT_VALUE)/MAX_SIGHT_VALUE;
    }
}
