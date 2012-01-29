package com.agenosworld.basicgdxgame;

import com.agenosworld.fourtyeight.CameraManager;
import com.badlogic.gdx.utils.Disposable;

public interface GameComponent extends Updatable, Disposable {
	
	public void render();
	
	public void resize(int width, int height);
	
	public CameraManager getCameraManager();

}
