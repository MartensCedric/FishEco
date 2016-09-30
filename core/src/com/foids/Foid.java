package com.foids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Cedric on 2016-09-21.
 */
public class Foid {

    private Vector2 desired;
    private Vector2 pushingForce;
    private Vector2 acceleration;
    private Vector2 location;

    private int width;
    private int height;

    private int originX;
    private int originY;

    private float maxSpeed;
    private int color;

    private byte[] fishTexture;
    private Texture texture;
    private TextureRegion textureRegion;

	private float dir;

    public Foid(int x, int y, byte[] texture)
    {
        Random randomizer = new Random();
        color = Color.rgba8888(0, 57/255f, 235/255f, 1f);

        this.width = 5;
        this.height = 9;

        this.originX = width/2;
        this.originY = height/2;

        this.fishTexture = texture;
        createFishTexture();
        this.textureRegion = new TextureRegion(this.texture);

        location = new Vector2(x,y);
        acceleration = new Vector2(0, 0.05f);
        pushingForce = new Vector2(0,0);


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

    private void createFishTexture()
    {
        Gdx2DPixmap pxMap2D = new Gdx2DPixmap(width, height, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888 );

        for(int i = 0; i < fishTexture.length; i++)
        {
            if(fishTexture[i] == 1)
                pxMap2D.setPixel(i%5, (i/5), Color.rgba8888(0, 0, 0, 1f));
            else if (fishTexture[i] == 2)
                pxMap2D.setPixel(i%5, (i/5), color);
        }

        Pixmap pxMap = new Pixmap(pxMap2D);
        texture = new Texture(pxMap, Pixmap.Format.RGBA8888, false);
    }

    private void applyForce()
    {
        pushingForce = pushingForce.add(acceleration);
        pushingForce.limit(maxSpeed);
        location = location.add(pushingForce);

        if(location.y > Gdx.graphics.getHeight() + height)
            location.y = location.y - Gdx.graphics.getHeight() - height*2;

    }


    public float getX()
    {
        return location.x;
    }

    public float getY()
    {
        return location.y;
    }

    public int getOriginX(){return originX;}

    public int getOriginY(){return originY;}

    public TextureRegion getTextureRegion()
    {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion)
    {
        this.textureRegion = textureRegion;
    }
}
