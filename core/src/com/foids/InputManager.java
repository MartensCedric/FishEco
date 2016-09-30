package com.foids;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by 1544256 on 2016-09-30.
 */
public class InputManager implements InputProcessor {

    private CommandManager commandManager;

    public InputManager(CommandManager cm)
    {
        this.commandManager = cm;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode)
        {
            case Input.Keys.H :
                commandManager.toggleHitbox();
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
        return false;
    }
}
