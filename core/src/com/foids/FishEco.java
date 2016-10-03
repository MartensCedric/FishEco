package com.foids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.foids.commands.CommandManager;
import com.foids.commands.InputManager;
import com.foids.life.Fish;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Cedric on 2016-09-21.
 */
public class FishEco extends ApplicationAdapter {

	private SpriteBatch batch;
	private LinkedList<Fish> fishList;
	private final int START_FOID_COUNT = 50;


	private Texture background;
	private byte[] fishTexture;

	private final int TRANS = 0;
	private final int BLACK = 1;
	private final int COLOR = 2;

	private CommandManager commandManager;
	private InputManager inputManager;

	private int foidWidth;
	private int foidHeight;

	private FlowField field;
	private byte updateCounter;

	
	@Override
	public void create () {
		setTextures();
		batch = new SpriteBatch();
		GdxNativesLoader.load();
		field = new FlowField();

		foidWidth = 5;
		foidHeight = 9;


		updateCounter = 0;


		spawnFish();

		commandManager = new CommandManager(this);

		inputManager = new InputManager(commandManager);
		Gdx.input.setInputProcessor(inputManager);

	}

	@Override
	public void render () {

		update();

		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		//Drawing background
		batch.draw(background, 0, 0);

		for(Fish fish : fishList)
			batch.draw(fish.getTextureRegion(), fish.getX(), fish.getY(), fish.getOriginRelativeToFishX(), fish.getOriginRelativeToFishY(), fish.getTexture().getWidth(), fish.getTexture().getHeight(), 1,1, 0);

		commandManager.draw();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	private void update()
	{
		for(Fish fish : fishList)
			fish.update();

		if(updateCounter >= 30)
		{
			//field.createField();
			updateCounter = 0;
		}


		updateCounter++;
	}

	private void setTextures()
	{
		fishTexture = new byte[]{
				TRANS,BLACK,BLACK,BLACK,TRANS,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				TRANS,BLACK,COLOR,BLACK,TRANS,
				TRANS,TRANS,COLOR,TRANS,TRANS,
				TRANS,BLACK,BLACK,BLACK,TRANS
		};

		Gdx2DPixmap pxBg = new Gdx2DPixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);

		OpenSimplexNoise noise = new OpenSimplexNoise();
		System.out.println("Background seed : " + noise.getSeed());
		float offsetI = 0;
		float offsetJ = 0;
		for(int i = 0; i < pxBg.getWidth(); i++)
		{
			offsetI+= 0.2;
			for(int j = 0; j < pxBg.getHeight(); j++)
			{
				offsetJ+=0.2;

				int blue = (int)(noise.eval(offsetI,offsetJ)*110 + 87)/2;
				pxBg.setPixel(i,j, Color.rgba8888(30f/255f,80f/255f,((float)blue+155f)/255f,1f));
			}
		}
		background = new Texture(new Pixmap(pxBg));
	}

	private void spawnFish()
	{
		fishList = new LinkedList<Fish>();

		for (int i = 0; i < START_FOID_COUNT; i++)
		{
			Random randomizer = new Random();
			fishList.add(new Fish(randomizer.nextInt(1280), randomizer.nextInt(720), foidWidth, foidHeight, this,fishTexture));
		}
	}

    public LinkedList<Fish> getFishList() {
        return fishList;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

	public int getFoidWidth() {
		return foidWidth;
	}

	public int getFoidHeight() {
		return foidHeight;
	}

	public FlowField getField() {
		return field;
	}
}
