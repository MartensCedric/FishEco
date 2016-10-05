package com.foids.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.foids.FlowField;
import com.foids.life.Fish;
import com.foids.FishEco;

import java.util.LinkedList;

/**
 * Manages the commands and makes sure to display stuff when toggled
 * Created by Cedric Martens on 2016-09-30.
 */
public class CommandManager {


    private FishEco game;

    private LinkedList<Fish> fishList;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Gdx2DPixmap hitboxPxMap2D;
    private Texture hitboxTexture;
    private TextureRegion hitboxRegion;
    private boolean hitbox;

    private Gdx2DPixmap originPxMap2D;
    private Texture originTexture;
    private int originMarkerSize;
    private boolean origin;

    private boolean direction;
    private int lineSize;

    private boolean flowField;
    private FlowField field;
    private Vector2 centerPoint;

    private boolean pause;

    private boolean meal;
    private boolean sight;

    public CommandManager(FishEco game)
    {
        this.game = game;

        this.fishList = game.getFishList();
        this.batch = game.getBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.field = game.getField();

        this.hitbox = false;
        this.origin = false;
        this.direction = false;
        this.pause = false;
        this.meal = false;
        this.sight = false;

        this.originMarkerSize = 1;
        this.lineSize = 3;

        createTextures();
    }

    /*
    Command list
    Show Vectors (Direction only) (D)
    Show Vector Length (L)
    Show Meal (M)
    Show Flow Field (F)
    Show Fish Origin (O)
    Speed (Arrows)
	Show Parents (P)
	Show Children (C)
	Checkbox mode for hotkeys?
    Show ALL (A)
    Remove ALL (R)
    */

    public void draw()
    {
        if(flowField)
            drawFlowField();

        if(hitbox)
            drawHurtboxes();

        if(origin)
            drawOrigin();

        if(meal)
            drawMealLines();

        if(sight)
            drawSight();
    }

    public void toggleHitbox()
    {
        hitbox = !hitbox;
    }

    public void toggleOrigin()
    {
        origin = !origin;
    }

    public void toggleFlowField()
    {
        flowField = !flowField;
    }

    public void togglePause()
    {
        pause = !pause;
    }

    public void toggleMeal()
    {
        meal = !meal;
    }

    public void toggleSight()
    {
        sight = !sight;
    }

    public void showAll()
    {
        hitbox = true;
        origin = true;
        flowField = true;
        meal = true;
        sight = true;
    }

    public void removeAll()
    {
        hitbox = false;
        origin = false;
        flowField = false;
        meal = false;
        sight = false;
    }

    /**
     * Draws the hurtboxes of the fish
     */
    private void drawHurtboxes()
    {
        for(Fish fish : fishList)
        {
            batch.draw(hitboxRegion, fish.getX(), fish.getY(),
                    fish.getOriginRelativeToFishX(), fish.getOriginRelativeToFishY(),
                    fish.getTexture().getWidth(), fish.getTexture().getHeight(), 1, 1, fish.getDir());
        }
    }

    private void drawOrigin()
    {
        for(Fish fish : fishList)
        {
            batch.draw(originTexture, fish.getOriginX(), fish.getOriginY());
        }
    }

    private void drawFlowField()
    {
        batch.end();
        shapeRenderer.begin();
        shapeRenderer.setProjectionMatrix(game.getCam().combined);
        //Drawing Grid
        for(int i = 0; i < field.getWidth(); i++)
        {
            shapeRenderer.line(i*field.getTileWidth(), Gdx.graphics.getHeight(), i*field.getTileWidth(), 0, Color.BLACK, Color.BLACK);
        }

        for(int i = 0; i < field.getHeight(); i++)
        {
            shapeRenderer.line(0, i*field.getTileHeight(), Gdx.graphics.getWidth(), i*field.getTileHeight(), Color.BLACK, Color.BLACK);
        }

        //Drawing Arrows
        for(int i = 0; i < field.getWidth(); i++)
        {
            for(int j = 0; j < field.getHeight(); j++)
            {
                centerPoint = new Vector2(i * field.getTileWidth() + field.getTileWidth()/2, j * field.getTileHeight() + field.getTileHeight() /2);
                shapeRenderer.line(centerPoint.x, centerPoint.y, centerPoint.x + field.getFieldData()[i][j].x * 10, centerPoint.y + field.getFieldData()[i][j].y * 10, Color.BLACK, Color.BLACK);
            }
        }
        shapeRenderer.end();
        batch.begin();
    }

    private void drawMealLines()
    {
        batch.end();
        shapeRenderer.begin();
        shapeRenderer.setProjectionMatrix(game.getCam().combined);
        for(Fish fish : game.getFishList())
        {
            if(fish.getFoodTarget() != null)
            {
                shapeRenderer.line(fish.getOriginX(), fish.getOriginY(),
                        fish.getFoodTarget().getOriginX(), fish.getFoodTarget().getOriginY(),
                        Color.BLACK, Color.BLACK);
            }
        }
        shapeRenderer.end();
        batch.begin();
    }

    private void drawSight()
    {
        batch.end();
        shapeRenderer.begin();

        for(Fish fish : game.getFishList())
        {
            shapeRenderer.circle(fish.getOriginX(), fish.getOriginY(), fish.getSight());
        }

        shapeRenderer.end();
        batch.begin();
    }

    private void createTextures()
    {
        hitboxPxMap2D = new Gdx2DPixmap(game.getFoidWidth(), game.getFoidHeight(), Gdx2DPixmap.GDX2D_FORMAT_RGBA8888 );

        for(int i = 0; i < game.getFoidWidth() * game.getFoidHeight(); i++)
        {
            hitboxPxMap2D.setPixel(i%game.getFoidWidth(), (i/game.getFoidWidth()), Color.rgba8888(1f, 144/255f, 0, 0.65f));
        }

        hitboxTexture = new Texture(new Pixmap(hitboxPxMap2D));
        hitboxRegion = new TextureRegion(hitboxTexture);


        originPxMap2D = new Gdx2DPixmap(originMarkerSize, originMarkerSize, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
        for(int i = 0; i < originMarkerSize * originMarkerSize; i++)
        {
            originPxMap2D.setPixel(i%originMarkerSize, (i/originMarkerSize), Color.rgba8888(1f, 0, 0, 1f));
        }

        originTexture = new Texture(new Pixmap(originPxMap2D));
    }
}
