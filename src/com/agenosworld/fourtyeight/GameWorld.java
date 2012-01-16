package com.agenosworld.fourtyeight;

import com.agenosworld.basicgdxgame.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class GameWorld implements Updatable, Disposable {
	
	// Box2d Variables
	private World b2dWorld;
	private Box2DDebugRenderer b2dDebug;
	
	// Camera Manager
	private CameraManager manager;
	
	// TILED map variables
	private TiledMap map;
	private TileAtlas mapAtlas;
	private TileMapRenderer mapRenderer;
	
	public GameWorld() {
		// Create the camera manager
		manager = new CameraManager(30.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Setup the tiled map
		setupTiledMap();
		
		//Setup the Box2d world based on the data loaded by the TiledMap
		setupB2DWorld();
		
	}

	private void setupTiledMap() {
		map = TiledLoader.createMap(Gdx.files.internal("res/maps/map1.tmx"));
		mapAtlas = new TileAtlas(map, Gdx.files.internal("res/maps/"));
		mapRenderer = new TileMapRenderer(map, mapAtlas, 16, 16, 1, 1);
	}
	
	private void setupB2DWorld() {
		// Create the b2dWorld
		Vector2 gravity = new Vector2(0,0);
		b2dWorld = new World(gravity, true);
		
		// Populate the world with fixed barriers
		int[][] fixedBarriers = map.layers.get(1).tiles;
		
		for (int mapY=0; mapY<fixedBarriers.length; mapY++) {
			// Find the y position where the map tile will ACTUALLY be rendered
			int y = fixedBarriers.length-mapY-1;
			
			for (int x=0; x<fixedBarriers[mapY].length; x++) {
				int currID= fixedBarriers[mapY][x];
				
				if (currID != 0) {
					new Barracade(x, y, b2dWorld);
					System.out.println("Created barracade at " + x + " " + y);
				}
			}
		}
		
		// TODO: Add dynamic, mobile barriers
		
		// TODO: Debug code for viewing Box2d viewpoints
		b2dDebug = new Box2DDebugRenderer(true, true, false);
	}
	
	@Override
	public void update(float delta) {
		b2dWorld.step(delta, 3, 3); // TODO: Test without ever calling step - no physics is simulated
	}
	
	public void render() {
		// Render the map
		OrthographicCamera camera = manager.getCamera();
		mapRenderer.render(camera, new int[] {0, 1});
		
		// Begin rendering other sprites on the screen
		SpriteBatch batch = manager.getSpriteBatch();
		batch.begin();
		
		batch.end();
		
		// Render the debug for the B2Dworld
		if (b2dDebug != null )
			b2dDebug.render(b2dWorld, camera.combined);
		
	}
	
	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {
		// Dispose of the cameramanager
		manager.dispose();
	}

}
