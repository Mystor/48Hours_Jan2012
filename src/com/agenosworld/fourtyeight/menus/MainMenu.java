package com.agenosworld.fourtyeight.menus;

import com.agenosworld.basicgdxgame.GameComponent;
import com.agenosworld.basicgdxgame.InputManager;
import com.agenosworld.fourtyeight.*;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class MainMenu implements GameComponent {
	
	private CameraManager cameraManager;
	
	// Level options
	private String[] levelOptions = new String[] {"level1.tmx", "level2.tmx", "level3.tmx", "level4.tmx", "level5.tmx", "level6.tmx", "level7.tmx", "level8.tmx"};
	
	// Buttons
	private LevelButton[] levelButtons;
	private SaveQuitButton saveQuitButton;
	
	// Main Menu Background
	private AtlasRegion background;
	
	// Last Game info
	private LastGameInfoBox lastGameInfoBox;
	
	public MainMenu() {
		cameraManager = new CameraManager(480f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		background = SpriteManager.getRegion("main-menu");
		
		// Create the level buttons
		levelButtons = new LevelButton[levelOptions.length];
		
		for (int i=0; i<levelOptions.length; i++) {
			levelButtons[i] = new LevelButton(i+1, levelOptions[i], cameraManager, this);
			InputManager.addInputProcessor(levelButtons[i]);
		}
		
		// Create the SaveQuit button
		saveQuitButton = new SaveQuitButton(cameraManager);
		InputManager.addInputProcessor(saveQuitButton);
		
		// Load Score data from ScoreManager
		
		if (ScoreManager.lastGame != ScoreManager.NO_HISTORY)
			lastGameInfoBox = new LastGameInfoBox();
		
	}
	
	public void render() {
		SpriteBatch batch = cameraManager.getSpriteBatch();
		
		batch.begin();
		// Draw the background
		batch.draw(background, 0, 0, 640f, 480f);
		
		// Draw the level buttons
		for (LevelButton b : levelButtons) {
			b.draw(batch);
		}
		
		// Draw the savequit button
		saveQuitButton.draw(batch);
		
		// Draw the Last Game Info Box
		if (lastGameInfoBox != null)
			lastGameInfoBox.draw(batch);
		
		batch.end();
	}
	
	@Override
	public CameraManager getCameraManager() {
		return cameraManager;
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
		for (LevelButton b : levelButtons) {
			b.dispose();
		}
		
		saveQuitButton.dispose();
		
		// Dispose of the CameraManager
		cameraManager.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO: add resizing support nub
	}

	

}
