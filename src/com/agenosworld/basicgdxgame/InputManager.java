package com.agenosworld.basicgdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {
	
	private static ArrayList<InputProcessor> processors = new ArrayList<InputProcessor>();
	
	private static InputManager manager;
	
	public static void addInputProcessor(InputProcessor p) {
		processors.add(p);
	}
	
	public static void remInputProcessor(InputProcessor p) {
		processors.remove(p);
	}
	
	public static void isProcessing(InputProcessor p) {
		processors.contains(p);
	}
	
	public static void registerManager(InputManager manager) {
		Gdx.input.setInputProcessor(manager);
		
		InputManager.manager = manager;
	}
	public static InputManager getManager() {
		return manager;
	}
	
	public InputManager(boolean register) {
		if (register)
			InputManager.registerManager(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (InputProcessor p : InputManager.processors) {
			p.keyDown(keycode);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (InputProcessor p : InputManager.processors) {
			p.keyUp(keycode);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		for (InputProcessor p : InputManager.processors) {
			p.keyTyped(character);
		}
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		for (InputProcessor p : InputManager.processors) {
			p.touchDown(x, y, pointer, button);
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		for (InputProcessor p : InputManager.processors) {
			p.touchUp(x, y, pointer, button);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		for (InputProcessor p : InputManager.processors) {
			p.touchDragged(x, y, pointer);
		}
		return true;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		for (InputProcessor p : InputManager.processors) {
			p.touchMoved(x, y);
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		for (InputProcessor p : InputManager.processors) {
			p.scrolled(amount);
		}
		return true;
	}

}
