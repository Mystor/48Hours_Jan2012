package com.agenosworld.fourtyeight.menus;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.basicgdxgame.InputManager;
import com.agenosworld.fourtyeight.CameraManager;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public abstract class Button implements Drawable, InputProcessor, Disposable {
	
	// Placement properties
	protected Vector2 position;
	protected int width;
	protected int height;
	
	// Visual properties
	protected AtlasRegion visual;
	protected AtlasRegion visualHover;
	
	// Hovering?
	protected boolean isHovering;
	
	// Camera Manager
	protected CameraManager cameraManager;
	
	// Event driven methods - to be overrode
	protected abstract void onClick();
	protected void onHover() {}
	protected void onUnhover() {}

	public Button(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}
	
	// Input Management
	@Override
	public boolean touchMoved(int x, int y) {
		// Convert the mouse coordinates to world coordinates
		Vector3 mousePos3D = new Vector3(x, y, 0);
		cameraManager.getCamera().unproject(mousePos3D);
		
		if ((position.x < mousePos3D.x) && (mousePos3D.x < position.x + width)) {
			if ((position.y < mousePos3D.y) && (mousePos3D.y < position.y + height)) {
				isHovering = true;
				onHover();
				return true;
			}
		}
		isHovering = false;
		onUnhover();
		return false;
	}
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (isHovering)
			onClick();
		return false;
	}
	
	// Rendering
	@Override
	public void draw(SpriteBatch batch) {
		if (visual == null || visualHover == null) {
			return;
		}
		
		if (isHovering) {
			batch.draw(visualHover, position.x, position.y, width, height);
		} else {
			batch.draw(visual, position.x, position.y, width, height);
		}
	}
	
	// Dispose
	@Override
	public void dispose() {
		InputManager.remInputProcessor(this);
	}
	
	// Unused methods
	@Override
	public boolean keyDown(int keycode) {
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
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	

}
