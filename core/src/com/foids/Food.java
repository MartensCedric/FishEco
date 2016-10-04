package com.foids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Fish eat food to survive.
 * The bigger the food the bigger the attraction.
 *
 * Created by Cedric Martens on 2016-10-03.
 */
public class Food {

    private FishEco game;

    private float quantity;
    private Rectangle hurtbox;

    private int x;
    private int y;

    private int width;
    private int height;

    private Texture texture;
    private int foodColor;

    public Food(FishEco game)
    {
        this.game = game;

        Random randomizer = new Random();

        //TODO dont spawn food near borders

        this.x = randomizer.nextInt(Gdx.graphics.getWidth());
        this.y = randomizer.nextInt(Gdx.graphics.getHeight());
        this.quantity = 0.5f + randomizer.nextFloat();

        this.foodColor = Color.rgba8888(102f/255f, 51f/255f, 0, 1f);
        generateTexture();
    }

    private void generateTexture()
    {
        width = 5;
        height = 5;

        Gdx2DPixmap px2d = new Gdx2DPixmap(width,height,Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                if(i == 0 || i == width - 1 || j == 0 || j == height - 1)
                    px2d.setPixel(i,j, Color.rgba8888(0,0,0,1f));
                else
                   px2d.setPixel(i,j, foodColor);
            }
        }

        texture = new Texture(new Pixmap(px2d));

        hurtbox = new Rectangle(x,y, width, height);
    }

    public boolean contains(Rectangle rectangle)
    {
        return hurtbox.contains(rectangle);
    }

    public boolean contains(Vector2 vec)
    {
        return hurtbox.contains(vec);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw()
    {
        game.getBatch().draw(texture, x, y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOriginX()
    {
        return  x + width/2;
    }

    public int getOriginY()
    {
        return y + height/2;
    }
}
