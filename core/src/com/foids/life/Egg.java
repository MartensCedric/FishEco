package com.foids.life;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.foids.FishEco;

/**
 * Fish lay these eggs and they spawn additional fish
 * Created by Cedric Martens on 2016-10-04.
 */
public class Egg {

    private float hatchProgress;
    private float hatchSpeed;

    private int x;
    private int y;

    private Fish parent;

    private Texture eggTexture;

    private int width;
    private int height;

    private FishEco game;

    private boolean hatched;

    public Egg(Fish parent, FishEco game)
    {
        this.parent = parent;
        this.hatchProgress = 1f;
        this.hatchSpeed = 1/500f;

        this.game = game;

        this.x = (int)this.parent.getX();
        this.y = (int)this.parent.getY();

        this.width = 3;
        this.height = 3;

        this.hatched = false;

        createTexture();
    }

    public void update()
    {
        if(hatchProgress <= 0)
            hatched = true;
        else
            hatchProgress -= hatchSpeed;

        createTexture();
    }

    public void draw()
    {
        game.getBatch().draw(eggTexture, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getOriginX()
    {
        return x + width/2;
    }

    public int getOriginY()
    {
        return y + height/2;
    }

    private void createTexture()
    {
        Gdx2DPixmap gdx2DPixmap = new Gdx2DPixmap(width, height, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);

        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                gdx2DPixmap.setPixel(i,j, Color.rgba8888(1f, hatchProgress, hatchProgress, 1f));
            }
        }

        eggTexture = new Texture(new Pixmap(gdx2DPixmap));
    }

    public boolean isHatched() {
        return hatched;
    }
}
