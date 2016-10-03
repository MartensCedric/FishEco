package com.foids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Cedric on 2016-10-01.
 */
public class FlowField {

    private Vector2[][] fieldData;

    private int width;
    private int height;

    private int tileWidth;
    private int tileHeight;

    private float offsetX;
    private float offsetY;

    private OpenSimplexNoise noise;

    /**
     * Will create a grid with Perlin noise's Simplex (new version of PN) random vectors.
     */
    public FlowField()
    {
        this.tileWidth = 40;
        this.tileHeight = 40;

        this.width = Gdx.graphics.getWidth()/tileWidth;
        this.height = Gdx.graphics.getHeight()/tileHeight;

        noise = new OpenSimplexNoise(432424324);
        //U 432424324

        offsetX = 0;
        offsetY = 0;
        createField();
    }

    public Vector2[][] getFieldData()
    {
        return fieldData;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void createField()
    {
        fieldData = new Vector2[width][height];

        for(int i = 0; i < width; i++)
        {
            offsetX += 0.0025;

            for(int j = 0; j < height; j++)
            {
                offsetY += 0.0025;
                fieldData[i][j] = new Vector2((float)noise.eval(offsetX, offsetY),(float)noise.eval(offsetY, offsetX));
                fieldData[i][j].nor();
            }
        }
    }
}
