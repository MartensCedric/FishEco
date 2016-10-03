package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.foids.FishEco;

import java.util.Random;

/**
 * Created by Cedric on 2016-09-21.
 */
public class Fish {

    private Vector2 desired;
    private Vector2 force;
    private Vector2 velocity;
    private Vector2 location;

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

    public Fish(int x, int y, int width, int height, FishEco game, byte[] texture)
    {
        Random randomizer = new Random();
        color = Color.rgba8888(1f, 1f, 1f, 1f);

        this.game = game;

        this.width = width;
        this.height = height;

        this.originRelativeToFishX = width/2;
        this.originRelativeToFishY = height/2;

        this.fishTexture = texture;
        createFishTexture();
        this.textureRegion = new TextureRegion(this.texture);

        this.mass = 1f;

        location = new Vector2(x,y);
        velocity = new Vector2(0,0);
        force = new Vector2(0,0);
        desired = new Vector2(0, 0);


        this.maxSpeed = 0.35f +  (randomizer.nextFloat()/4);
    }

    public void update()
    {
        applyForce();
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
        force.add(desired);
        force.scl(1/mass);
        velocity.add(force);
        location.add(velocity);

        if(originY() > Gdx.graphics.getHeight() - 1)
            setOriginY(1);

        if(originY() < 1)
            setOriginY(Gdx.graphics.getHeight() - 1);

        if(originX() > Gdx.graphics.getWidth() - 1)
            setOriginX(1);

        if(originX() < 1)
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
        if(originX() >= Gdx.graphics.getWidth())
            return new Vector2(1f,0f);

        if(originX() <= 0)
            return  new Vector2(-1f, 0f);

        if(originY() >= Gdx.graphics.getHeight())
            return new Vector2(0f, 1f);

        if(originY() <= 0)
            return new Vector2(0f, -1f);

        return game.getField().getFieldData()[(int)originX() / game.getField().getTileWidth()][(int)originY() /game.getField().getTileHeight()];
    }

    public float originX()
    {
        return getX() + originRelativeToFishX;
    }

    public float originY()
    {
        return getY() + originRelativeToFishY;
    }

    public void setOriginX(float x)
    {
        location.x -= originX() - x;
    }

    public void setOriginY(float y)
    {
        location.y -= originY() - y;
    }
}
