package com.agenosworld.fourtyeight;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainLoop implements ApplicationListener {
	
	private GameWorld world;
	
	private SpriteBatch uiBatch;
	private BitmapFont fpsFont;
	
	public MainLoop() { }
	
	@Override
	public void create() {
		// Create a game world
		world = new GameWorld();
		
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
		try {
			LwjglFrame lwjglFrame = new LwjglFrame(new MainLoop(), "48 Hours", 640, 480, false);
			lwjglFrame.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
