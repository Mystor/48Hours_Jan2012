package com.agenosworld.fourtyeight;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.basicgdxgame.GameComponent;
import com.agenosworld.basicgdxgame.InputManager;
import com.agenosworld.basicgdxgame.Updater;
import com.agenosworld.fourtyeight.game.GameWorld;
import com.agenosworld.fourtyeight.menus.MainMenu;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Disposable;

public class MainLoop implements ApplicationListener, Disposable {
	
	// Is the game in DEBUG mode?
	public static boolean debug = false;
	
	// The LwjglFrame
	private static LwjglFrame lwjglFrame;
	
	// The current game loop
	public static MainLoop gameLoop;
	
	// The tutorial
	public static Tutorial tutorial;
	
	// Load map on next update
	public static String nextMap = null;
	
	// The currently active GameComponent
	private GameComponent current;
	
	// Ubiquitous graphics objects
	private SpriteBatch uiBatch;
	private BitmapFont fpsFont;
	
	// Myriad Pro BitmapFont
	public static BitmapFont myriadPro22;
	
	// Constructor
	public MainLoop() { }
	
	// Load the map represented by the string mapFile
	public void loadMap(String mapFile) {
		if (mapFile == "level1.tmx") {
			tutorial.showTutorial();
			InputManager.addInputProcessor(tutorial);
		}
		
		FileHandle mapHandle = Gdx.files.internal("res/maps/"+mapFile);
		FileHandle atlasHandle = Gdx.files.internal("res/maps/");
		
		TiledMap map = TiledLoader.createMap(mapHandle);
		TileAtlas atlas = new TileAtlas(map, atlasHandle);
		
		current = new GameWorld(map, atlas);
		ScoreManager.registerNewGame();
		Updater.addUpdatable(current);
	}
	
	// display the main menu
	public void displayMainMenu() {
		current = new MainMenu();
	}
	
	// Save and Quit
	public static void saveQuit() {
		lwjglFrame.dispose();
		System.exit(0);
	}
	
	@Override
	public void create() {
		// Load the Game Sprites
		SpriteManager.loadSprites(Gdx.files.internal("res/sprites/pack"));
		
		// Load the BitmapFonts
		myriadPro22 = new BitmapFont(Gdx.files.internal("res/fonts/myriadPro22.fnt"), Gdx.files.internal("res/fonts/myriadPro22.png"), false);
		
		// Register the InputManager
		new InputManager(true);
		
		// Create the Tutorial object
		tutorial = new Tutorial();
		
		// Create the UI SpriteBatch
		uiBatch = new SpriteBatch();
		fpsFont = new BitmapFont();
		
		// Load the Main Menu
		displayMainMenu();
	}

	@Override
	public void resize(int width, int height) {
		current.resize(width, height);
	}

	@Override
	public void render() {
		// Update the ScoreManager
		ScoreManager.update();
		
		// Update all updatables
		Updater.update();
		
		// Dispose of things which must be disposed of
		Disposer.update();
		
		// Check if a map needs to be loaded
		if (nextMap != null) {
			if (nextMap.equalsIgnoreCase("MAIN_MENU")) {
				displayMainMenu();
			} else {
				loadMap(nextMap);
			}
			nextMap = null;
		}
		
		// Clear the screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Render the currently active Game Component
		if (current != null)
			current.render();
		

		uiBatch.begin();
		// Render the Framerate
		if (debug)
			fpsFont.draw(uiBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 5, 20);
		
		//Render the Tutorial
		tutorial.draw(uiBatch);
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
		// Dispose of the SpriteManager
		SpriteManager.dispose();
		
		// Dispose of the world
		if (current != null) {
			current.dispose();
			current = null;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// ints representing the width and height of the game windo
		int width = 640;
		int height = 480;
		
		// Analyze args
		for (String s : args) {
			if (s == "debug")
				MainLoop.debug = true;
			
			if (s.contains("dim:")) {
				String[] dims = s.split(":");
				try {
					int dimW = Integer.parseInt(dims[1]);
					int dimH = Integer.parseInt(dims[2]);
					width = dimW;
					height = dimH;
				} catch (Exception e) { }
			}
			
		}
		
		// Mac friendliness stuff
		if (System.getProperty("os.name").contains("Mac")) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Rocket Infiltration");
		}
		
		BufferedImage image = null;
		// Set icon image
		try {
			image = ImageIO.read(new File("res/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Pack any sprites in the res/sprites/raw directory
		// TODO: Remove this code for release
//		Settings settings = new Settings();
//        settings.padding = 2;
//        settings.maxWidth = 1024;
//        settings.maxHeight = 1024;
//        settings.minHeight = 16;
//        settings.minWidth = 16;
//        settings.rotate = false;
//        settings.defaultFilterMag = Texture.TextureFilter.Linear;
//        settings.defaultFilterMin = Texture.TextureFilter.Linear;
//        settings.incremental = true;
//        settings.stripWhitespace = false;
//        TexturePacker.process(settings, "res/sprites/raw", "res/sprites");
		
        // Create the LwjglFrame and prevent it from resizing
		try {
			MainLoop.gameLoop = new MainLoop();
			lwjglFrame = new LwjglFrame(MainLoop.gameLoop, "Rocket Infiltration", width, height, false);
			lwjglFrame.setResizable(false);
			if (image != null)
				lwjglFrame.setIconImage(image);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
