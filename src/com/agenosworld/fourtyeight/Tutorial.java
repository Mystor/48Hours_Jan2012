package com.agenosworld.fourtyeight;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Tutorial implements Drawable, InputProcessor {
	
	// Indicates whether or not the tutorial is active
	private boolean tutorialActive;
	
	// The tutorial image
	private AtlasRegion tutorialImage;
	
	public Tutorial() {
		tutorialImage = SpriteManager.getRegion("tutorial");
		tutorialActive = false;
	}
	
	public void hideTutorial() {
		tutorialActive = false;
	}
	
	public void showTutorial() {
		tutorialActive = true;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (tutorialActive)
			batch.draw(tutorialImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.SPACE)
			hideTutorial();
		return true;
	}
	
	// Unused functions
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
