package com.agenosworld.fourtyeight.game;

import java.util.ArrayList;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.basicgdxgame.GameComponent;
import com.agenosworld.basicgdxgame.InputManager;
import com.agenosworld.basicgdxgame.Updater;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.MainLoop;
import com.agenosworld.fourtyeight.game.particles.EffectManager;
import com.agenosworld.fourtyeight.game.units.Player;
import com.agenosworld.fourtyeight.game.units.Turret;
import com.agenosworld.fourtyeight.game.units.Unit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameWorld implements GameComponent {
	
	// Box2d Variables
	private World b2dWorld;
	private Box2DDebugRenderer b2dDebug;
	
	// Camera Manager
	private CameraManager manager;
	
	// TILED map variables
	private TiledMap map;
	private TileAtlas mapAtlas;
	private TileMapRenderer mapRenderer;
	
	// Player
	private Player player;
	
	// PhysicsProp ArrayList
	public ArrayList<PhysicsProp> physicsProps;
	
	// Units ArrayList
	public ArrayList<Unit> units;
	
	// Bullets ArrayList
	public ArrayList<Bullet> bullets;
	
	// Goal object
	private Goal goal;
	
	public GameWorld(TiledMap tiledMap, TileAtlas atlas) {
		// Set the map and mapAtlas fields
		map = tiledMap;
		mapAtlas = atlas;
		
		// Create the camera manager
		manager = new CameraManager(30.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Setup the tiled map
		setupTiledMap();
		
		//Setup the Box2d world based on the data loaded by the TiledMap
		setupB2DWorld();
		
		// Instantiate the bullets ArrayList
		bullets = new ArrayList<Bullet>();
	}
	
	public World getB2dWorld() {
		return b2dWorld;
	}
	
	@Override
	public CameraManager getCameraManager() {
		return manager;
	}

	private void setupTiledMap() {
//		map = TiledLoader.createMap(Gdx.files.internal("res/maps/map1.tmx"));
//		mapAtlas = new TileAtlas(map, Gdx.files.internal("res/maps/"));
		mapRenderer = new TileMapRenderer(map, mapAtlas, 16, 16, 1, 1);
	}
	
	private void setupB2DWorld() {
		// Create the b2dWorld
		Vector2 gravity = new Vector2(0,0);
		b2dWorld = new World(gravity, true);
		
		// Register the ContactManager
		ContactManager.registerManager(new ContactManager(), b2dWorld);
		
		// Define the Units array
		units = new ArrayList<Unit>();
		
		// Create the player
		player = new Player(4.5f, 4.5f, this);
		Updater.addUpdatable(player);
		InputManager.addInputProcessor(player);
		units.add(player);
		
		// Create outer borders to prevent players from exiting map
		generateWorldBorder(map.width, map.height, 1, b2dWorld);
		
		// Populate the world with fixed barriers
		int[][] fixedBarriers = map.layers.get(1).tiles;
		
		for (int mapY=0; mapY<fixedBarriers.length; mapY++) {
			
			// Find the y position where the map tile will ACTUALLY be rendered
			int y = fixedBarriers.length-mapY-1;
			
			for (int x=0; x<fixedBarriers[mapY].length; x++) {
				int currID= fixedBarriers[mapY][x];
				
				if (currID != 0)
					new Barricade(x, y, b2dWorld);
			}
		}
		
		// Add dynamic, mobile barriers
		int[][] mobileBarriers = map.layers.get(3).tiles;
		
		// Define the PhysicsProps array
		physicsProps = new ArrayList<PhysicsProp>();
		
		// Populate the world with PhysicsProps
		for (int mapY=0; mapY<mobileBarriers.length; mapY++) {
			
			// Find the y position where the map tile will ACTUALLY be rendered
			int y = mobileBarriers.length-mapY-1;
			
			for (int x=0; x<mobileBarriers[mapY].length; x++) {
				int currID = mobileBarriers[mapY][x];
				
				if (currID != 0) {
					// TODO: Fix the barricadeType loading
					PhysicsProp prop = new PhysicsProp(x, y, b2dWorld, /*map.getTileProperty(currID, "barricadeType")*/ "0");
					physicsProps.add(prop);
				}
			}
		}
		
		// Add enemy turrets
		int[][] enemies = map.layers.get(2).tiles;
		
		// Populate the world with PhysicsProps
		for (int mapY=0; mapY<enemies.length; mapY++) {
			// Find the y position where the map tile will ACTUALLY be rendered
			int y = enemies.length-mapY-1;

			for (int x=0; x<enemies[mapY].length; x++) {
				int currID = enemies[mapY][x];

				if (currID != 0) {
					Turret turret = new Turret(x+.5f, y+.5f, this, player);
					Updater.addUpdatable(turret);
					units.add(turret);
				}
			}
		}
		
		// Locate the goal
		int[][] destinations = map.layers.get(4).tiles;

		for (int mapY=0; mapY<destinations.length; mapY++) {
			// Find the y position where the map tile will ACTUALLY be rendered
			int y = destinations.length-mapY-1;

			for (int x=0; x<destinations[mapY].length; x++) {
				int currID = destinations[mapY][x];

				if (currID != 0) {
					goal = new Goal(x, y, this);
					ContactManager.addContactListener(goal);
				}
			}
		}
		
		// Debug code for viewing Box2d viewpoints
		if (MainLoop.debug)
			b2dDebug = new Box2DDebugRenderer(true, true, false);
	}
	
	private void generateWorldBorder(float worldWidth, float worldHeight, float borderWidth, World world) {
		// Define the BodyDef
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;

		// Define the horizontal borders
		PolygonShape horzShape = new PolygonShape();
		horzShape.setAsBox(worldWidth/2, borderWidth/2);
		
		// Bottom
		def.position.set(worldWidth/2, -borderWidth/2);
		Body bodyBot = world.createBody(def);
		bodyBot.createFixture(horzShape, 0);
		
		// Top
		def.position.set(worldWidth/2, borderWidth/2 + worldHeight);
		Body bodyTop = world.createBody(def);
		bodyTop.createFixture(horzShape, 0);
		
		// Define the vertical borders
		PolygonShape vertShape = new PolygonShape();
		vertShape.setAsBox(borderWidth/2, worldHeight/2);
		
		// Left
		def.position.set(-borderWidth/2, worldHeight/2);
		Body bodyLeft = world.createBody(def);
		bodyLeft.createFixture(vertShape, 0);

		// Right
		def.position.set(borderWidth/2 + worldWidth, worldHeight/2);
		Body bodyRight = world.createBody(def);
		bodyRight.createFixture(vertShape, 0);
	}
	
	@Override
	public void update(float delta) {
		b2dWorld.step(delta, 3, 3);
		EffectManager.update(delta);
	}
	
	@Override
	public void render() {
		// Render the map
		OrthographicCamera camera = manager.getCamera();
		mapRenderer.render(camera, new int[] {0, 1});
		
		// Begin rendering other sprites on the screen
		SpriteBatch batch = manager.getSpriteBatch();
		batch.begin();
		// Draw the goal
		goal.draw(batch);
		
		// Draw the bullets
		for (Bullet b : bullets) {
			b.draw(batch);
		}
		
		// Draw the units
		for (Unit u : units) {
			u.draw(batch);
		}
		
		// Draw the physics props
		for (PhysicsProp p : physicsProps) {
			p.draw(batch);
		}
		
		// Draw the Particle Effects
		EffectManager.render(batch);
		batch.end();
		
		// Render the debug for the B2Dworld
		if (b2dDebug != null )
			b2dDebug.render(b2dWorld, camera.combined);
		
	}
	
	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {
		// Clear the updatable list
		Updater.clearUpdatables();
		
		// Clear Particle effects
		EffectManager.clearEffects();
		
		// Dispose of all units
		for (Unit u : units) {
			Disposer.queueForDisposal(u);
		}
		
		// Dispose of all bullets
		for (Bullet b : bullets) {
			Disposer.queueForDisposal(b);
		}
		
		// Dispose of the map atlas
		mapAtlas.dispose();
		
		// Dispose of the map renderer
		mapRenderer.dispose();
		
		// Dispose of the b2dWorld
		Disposer.queueForDisposal(b2dWorld);
		if (b2dDebug != null)
			b2dDebug.dispose();
		
		// Dispose of the cameramanager
		manager.dispose();
		
		// Close Tutorial if it is still active
		MainLoop.tutorial.hideTutorial();
	}

	public Player getPlayer() {
		return player;
	}

}
