package com.foids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.foids.commands.CommandManager;
import com.foids.commands.InputManager;
import com.foids.life.Fish;

import java.util.LinkedList;
import java.util.Random;

public class FishEco extends ApplicationAdapter {

	private SpriteBatch batch;
	private LinkedList<Fish> fishList;
	private final int START_FOID_COUNT = 50;

	private byte[] fishTexture;

	private final int TRANS = 0;
	private final int BLACK = 1;
	private final int COLOR = 2;

	private CommandManager commandManager;
	private InputManager inputManager;

	private int foidWidth;
	private int foidHeight;

	private FlowField field;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GdxNativesLoader.load();
		field = new FlowField();

		foidWidth = 5;
		foidHeight = 9;

		setTextures();
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
		for(Fish fish : fishList)
			batch.draw(fish.getTextureRegion(), fish.getX(), fish.getY(), fish.getOriginX(), fish.getOriginY(), fish.getTexture().getWidth(), fish.getTexture().getHeight(), 1,1, 0);

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
	}

	private void spawnFish()
	{
		fishList = new LinkedList<Fish>();

		for (int i = 0; i < START_FOID_COUNT; i++)
		{
			Random randomizer = new Random();
			fishList.add(new Fish(randomizer.nextInt(1280), randomizer.nextInt(720), foidWidth, foidHeight, fishTexture));
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
