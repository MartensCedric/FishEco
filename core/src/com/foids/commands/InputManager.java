package com.foids.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.foids.FishEco;

/**
 * Calls commands when some inputs are typed
 * Created by Cedric Martens on 2016-09-30.
 */
public class InputManager implements InputProcessor {

    public final int MOVE_SPEED = 15;
    private CommandManager commandManager;
    private FishEco game;

    public InputManager(CommandManager cm, FishEco game)
    {
        this.commandManager = cm;
        this.game = game;

    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode)
        {
            case Input.Keys.H :
                commandManager.toggleHitbox();
                break;
            case Input.Keys.O :
                commandManager.toggleOrigin();
                break;
            case Input.Keys.F :
                commandManager.toggleFlowField();
                break;
            case Input.Keys.M :
                commandManager.toggleMeal();
                break;
            case Input.Keys.S :
                commandManager.toggleSight();
                break;
            case Input.Keys.A :
                commandManager.showAll();
                break;
            case Input.Keys.R :
                commandManager.removeAll();
                break;
            case Input.Keys.SPACE :
                commandManager.togglePause();
                break;
            case Input.Keys.LEFT :
                moveCamera(-MOVE_SPEED, 0);
                break;
            case Input.Keys.RIGHT :
                moveCamera(MOVE_SPEED, 0);
                break;
            case Input.Keys.UP :
                moveCamera(0, MOVE_SPEED);
                break;
            case Input.Keys.DOWN :
                moveCamera(0, -MOVE_SPEED);
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        OrthographicCamera camera = game.getCam();

        if(amount == 0)
            return false;

        if(amount > 0 && camera.zoom < 1)
        {
            camera.zoom *= 1.1f;

            if(camera.zoom > 1)
                camera.zoom = 1;
        }


        if(amount < 0 && camera.zoom > 0.25)
            camera.zoom /= 1.1f;

        return true;
    }

    public void moveCamera(int amountX, int amountY)
    {
        OrthographicCamera cam = game.getCam();
        cam.position.x += amountX*cam.zoom;
        cam.position.y += amountY*cam.zoom;

    }
}
