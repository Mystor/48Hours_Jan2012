package com.agenosworld.fourtyeight;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.basicgdxgame.InputManager;
import com.agenosworld.basicgdxgame.Updater;
import com.agenosworld.fourtyeight.game.GameWorld;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;

public class MainLoop implements ApplicationListener {
	
	private GameWorld world;
	
	private SpriteBatch uiBatch;
	private BitmapFont fpsFont;
	
	public MainLoop() { }
	
	@Override
	public void create() {
		// Load the Game Sprites
		SpriteManager.loadSprites(Gdx.files.internal("res/sprites/pack"));
		
		// Register the InputManager
		new InputManager(true);
		
		// Create a game world
		world = new GameWorld();
		Updater.addUpdatable(world);
		
		// Create the UI SpriteBatch
		uiBatch = new SpriteBatch();
		fpsFont = new BitmapFont();
	}

	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
	}

	@Override
	public void render() {
		// Dispose of things which must be disposed of
		Disposer.update();
		
		// Update all updatables
		Updater.update();
		
		// Clear the screen
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Render the world
		world.render();
		
		// Render the Framerate
		uiBatch.begin();
		fpsFont.draw(uiBatch, ""+Gdx.graphics.getFramesPerSecond(), 5, 20);
		uiBatch.end();
	}

	@Override
	public void pause() {
		// Will be called just before game ending
	}

	@Override
	public void resume() {
		// NEVER WILL BE CALLED
	}

	@Override
	public void dispose() {
		// Dispose of the world
		world.dispose();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Pack any sprites in the res/sprites/raw directory
		Settings settings = new Settings();
        settings.padding = 2;
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        settings.minHeight = 16;
        settings.minWidth = 16;
        settings.defaultFilterMag = Texture.TextureFilter.Linear;
        settings.defaultFilterMin = Texture.TextureFilter.Linear;
        settings.incremental = true;
        settings.stripWhitespace = false;
        TexturePacker.process(settings, "res/sprites/raw", "res/sprites");
		
        // Create the LwjglFrame and prevent it from resizing
		try {
			LwjglFrame lwjglFrame = new LwjglFrame(new MainLoop(), "48 Hours", 640*2, 480*2, false);
			lwjglFrame.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
