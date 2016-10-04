package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.foids.FishEco;
import com.foids.Food;

import java.util.Random;

/**
 * A fish that swims with the flow field, detects food, goes in groups and avoids sharks.
 * Created by Cedric on 2016-09-21.
 */
public class Fish{

    private Vector2 desired;
    private Vector2 force;
    private Vector2 velocity;
    private Vector2 location;

    private float sight;

    private Food foodTarget;

    private int width;
    private int height;

    private int originRelativeToFishX;
    private int originRelativeToFishY;

    private float maxSpeed;
    private int color;

    private FishEco game;

    private byte[] fishTexture;
    private Texture texture;

    private TextureRegion textureRegion;

	private float dir;
    private float mass;

    private static int idCounter = 0;
    private int id;

    private float belly;
    private float energyLossSpeed;

    private boolean dead;

    public Fish(int x, int y, int width, int height, FishEco game, byte[] texture)
    {
        this.id = idCounter++;
        Random randomizer = new Random();
        this.maxSpeed = 0.25f +  (randomizer.nextFloat()*0.75f);

        this.belly = 1f;
        this.energyLossSpeed = 1/3000f;

        color = Color.rgba8888(maxSpeed, 1f, 1f, belly);

        this.game = game;

        this.width = width;
        this.height = height;

        this.sight = 30;

        this.originRelativeToFishX = width/2;
        this.originRelativeToFishY = height/2;

        this.foodTarget = null;

        this.fishTexture = texture;
        createFishTexture();
        this.textureRegion = new TextureRegion(this.texture);

        this.mass = 1f;

        location = new Vector2(x,y);
        velocity = new Vector2(0,0);
        force = new Vector2(0,0);
        desired = new Vector2(0.25f, 0);

        dead = false;

    }

    public void update()
    {
        applyForce();
        desired.x = 0.25f;
        desired.y = 0;

        //If theres no food target
        if(foodTarget == null)
        {
            for(Food food : game.getFoodList())
            {
                //And theres a food close, assign it as a new target
                if(Math.sqrt(Math.pow(getOriginX() - food.getX(), 2) + Math.pow(getOriginY() - food.getY(), 2)) <= sight)
                {
                    foodTarget = food;
                    break;
                }
            }
        //if the current food target is too far
        }else if(Math.sqrt(Math.pow(getOriginX() - foodTarget.getOriginX(), 2) + Math.pow(getOriginY() - foodTarget.getOriginY(), 2)) > sight)
        {
            foodTarget = null;
        //if the current food target is close enough to it
        }else if(foodTarget.contains(new Vector2(getOriginX(), getOriginY())))
        {

            belly += 0.25f;

            if(belly > 1f)
                belly = 1f;

            game.getFoodList().remove(foodTarget);
            game.getFoodList().add(new Food(game));
            foodTarget = null;
        }else{
            //Make sure the food still exist (If another fish hasnt ate it yet)
            boolean stopChasing = true;
            for(Food food : game.getFoodList())
            {
                if(food == foodTarget)
                {
                    stopChasing = false;
                    break;
                }
            }

            if(stopChasing)
            {
                game.getFoodList().remove(foodTarget);
                foodTarget = null;
            }

        }

        belly -= energyLossSpeed;

        if(belly <= 0.10f)
        {
            dead = true;
            System.out.println("Fish " + getId() + " has died of hunger.");
        }

        color = Color.rgba8888(maxSpeed, 1f, 1f, belly);
        createFishTexture();
        textureRegion = new TextureRegion(texture);

    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Creates the texture for the fish with the supplied array of bytes.
     */
    private void createFishTexture()
    {
        Gdx2DPixmap pxMap2D = new Gdx2DPixmap(width, height, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);

        for(int i = 0; i < fishTexture.length; i++)
        {
            if(fishTexture[i] == 1)
                pxMap2D.setPixel(i%width, (i/width), Color.rgba8888(0, 0, 0, 1f));
            else if (fishTexture[i] == 2)
                pxMap2D.setPixel(i%width, (i/width), color);
        }

        Pixmap pxMap = new Pixmap(pxMap2D);
        texture = new Texture(pxMap, Pixmap.Format.RGBA8888, false);
    }

    private void applyForce()
    {
        velocity.scl(0);
        force.scl(0);
        force.add(vectorFromField());
        force.scl(0.75f);
        velocity.add(force);
        if(foodTarget == null)
        {
            velocity.add(desired);
            dir = getDirectionDegrees(velocity);
        }
        else
        {
            desired.x = foodTarget.getOriginX() - getOriginX();
            desired.y = foodTarget.getOriginY() - getOriginY();
            desired.nor();
            desired.scl(maxSpeed);
            dir = getDirectionDegrees(desired);
            velocity.add(desired);
        }

        location.add(velocity);

        if(getOriginY() > Gdx.graphics.getHeight() - 1)
            setOriginY(1);

        if(getOriginY() < 1)
            setOriginY(Gdx.graphics.getHeight() - 1);

        if(getOriginX() > Gdx.graphics.getWidth() - 1)
            setOriginX(1);

        if(getOriginX() < 1)
            setOriginX(Gdx.graphics.getWidth() - 1);

    }


    public float getX()
    {
        return location.x;
    }

    public float getY()
    {
        return location.y;
    }

    public int getOriginRelativeToFishX(){return originRelativeToFishX;}

    public int getOriginRelativeToFishY(){return originRelativeToFishY;}

    public TextureRegion getTextureRegion()
    {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion)
    {
        this.textureRegion = textureRegion;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    /**
     * Gets the vector in the flow field that the Fish's origin is on
     * @return A vector in a flow field
     */
    private Vector2 vectorFromField()
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

    public float getOriginX()
    {
        return getX() + originRelativeToFishX;
    }

    public float getOriginY()
    {
        return getY() + originRelativeToFishY;
    }

    public void setOriginX(float x)
    {
        location.x -= getOriginX() - x;
    }

    public void setOriginY(float y)
    {
        location.y -= getOriginY() - y;
    }

    public float getDir() {
        return dir;
    }

    public void draw()
    {
        game.getBatch().draw(getTextureRegion(), getX(), getY(), getOriginRelativeToFishX(), getOriginRelativeToFishY(), getTexture().getWidth(), getTexture().getHeight(), 1,1, getDir());
    }

    /**
     * Will return the degree that the fish is facing in radian
     * @return degree that the fish is facing in radian
     */
    private float getDirection(Vector2 vec)
    {
        return MathUtils.atan2(vec.y,vec.x) - (float)Math.PI/2;
    }

    /**
     * Will get the degree that the fish is facing in degrees
     * @param vec Vector we want to check tan(y/x)to degrees
     * @return inclination degree
     */
    private float getDirectionDegrees(Vector2 vec)
    {
        return (float)Math.toDegrees(getDirection(vec));
    }

    public Food getFoodTarget() {
        return foodTarget;
    }

    public int getId() {
        return id;
    }

    public boolean isDead() {
        return dead;
    }

    public float getSight() {
        return sight;
    }
}
