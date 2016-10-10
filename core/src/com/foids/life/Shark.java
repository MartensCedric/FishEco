package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.foids.FishEco;

import java.util.Random;

/**
 * Created by Cedric Martens on 2016-10-08.
 */
public class Shark {

    private FishEco game;

    private Vector2 desired;
    private Vector2 force;
    private Vector2 velocity;
    private Vector2 location;
    private Vector2 originVector;

    private float sight;

    private Food foodTarget;
    private int foodAte;

    private int width;
    private int height;

    private int originRelativeToSharkX;
    private int originRelativeToSharkY;

    private float maxSpeed;

    private float dir;
    private float mass;

    private static int idCounter = 0;
    private int id;

    private TextureRegion textureRegion;

    public Shark(FishEco game) {
        Random randomizer = new Random();
        location = new Vector2(randomizer.nextInt(Gdx.graphics.getWidth()), randomizer.nextInt(Gdx.graphics.getHeight()));

        this.mass = 3.5f;
        this.game = game;

        this.foodTarget = null;
        this.foodAte = 0;

        createTexture();

        this.width = textureRegion.getRegionWidth();
        this.height = textureRegion.getRegionHeight();

        this.originRelativeToSharkX = width / 2;
        this.originRelativeToSharkY = height / 2;

        this.force = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.desired = new Vector2(0,0);

        this.maxSpeed = 1;

    }

    private void createTexture() {
        Texture texture = new Texture("core/assets/shark.png");
        textureRegion = new TextureRegion(texture);
    }

    private void applyForce()
    {
        velocity.scl(0);
        force.scl(0);

        force.add(vectorFromField());
        force.scl(maxSpeed/mass); //THIS IS NO GOOD NEED CHANGE
        velocity.add(force);
        velocity.add(desired);
        dir = getDirectionDegrees(velocity);

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

    public void update()
    {
        applyForce();
    }

    public void draw()
    {
        game.getBatch().draw(textureRegion, getX(), getY(), originRelativeToSharkX, originRelativeToSharkY, width, height, 1,1, dir);
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

    public float getOriginX()
    {
        return getX() + originRelativeToSharkX;
    }

    public float getOriginY()
    {
        return getY() + originRelativeToSharkY;
    }

    public void setOriginX(float x)
    {
        location.x -= getOriginX() - x;
    }

    public void setOriginY(float y)
    {
        location.y -= getOriginY() - y;
    }

    public float getX()
    {
        return location.x;
    }

    public float getY()
    {
        return location.y;
    }


}
