package com.agenosworld.fourtyeight;

import java.util.ArrayList;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.basicgdxgame.Updatable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Unit implements Updatable, Drawable, BulletEmitter {
	
	// Shape values
	protected float width;
	protected float height;

	// Box2d Definition values
	protected BodyDef def;
	protected Shape shape;
	protected FixtureDef fixtureDef;

	// Box2d values
	protected Body body;
	protected Fixture fixture;
	protected World world;

	// Visual properties
	protected AtlasRegion visual_main;

	// Location and facing properties
	protected Vector2 position;
	protected Vector2 facing;

	// Bullet management variables
	protected GameWorld gameWorld;
	protected ArrayList<Bullet> activeBullets = new ArrayList<Bullet> ();
	
	// Constructor
	public Unit(float x, float y, GameWorld world) {
		// Get the b2dWorld and CameraManager
		gameWorld = world;
		this.world = gameWorld.getB2dWorld();
		
		// Define position + facing
		position = new Vector2(x, y);
		facing = new Vector2(1, 0);
		
		// Create b2d Definitions
		createB2DDefinitions();
		
		// Create the body in the world
		body = this.world.createBody(def);
		fixture = body.createFixture(fixtureDef);
		
		// Obtain a visual representation for the player
		visual_main = getMainVisual();
	}
	
	protected void fireBullet() {
		Bullet newBullet = new Bullet(new Vector2(position), facing.angle(), this, world);
		ContactManager.addContactListener(newBullet);
		activeBullets.add(newBullet);
	}
	
	// Abstract functions
	protected abstract AtlasRegion getMainVisual();
	protected abstract void createB2DDefinitions();

	@Override
	public void draw(SpriteBatch batch) {
		// Draw all active bullets
		for (Bullet b : activeBullets) {
			b.draw(batch);
		}

		// Draw the main visual onto the screen
		float drawX = position.x-width/2f;
		float drawY = position.y-height/2f;
		float originX = width/2f;
		float originY = height/2f;
		float angle = facing.angle();

		batch.draw(visual_main, drawX, drawY, originX, originY, width, height, 1, 1, angle, true);
	}

	@Override
	public void update(float delta) {
		position.set(body.getPosition());
	}

	@Override
	public void destroyBullet(Bullet b) {
		activeBullets.remove(b);
	}

	@Override
	public GameWorld getGameWorld() {
		return gameWorld;
	}
	
	@Override
	public Fixture getFixture() {
		return fixture;
	}
	
}
