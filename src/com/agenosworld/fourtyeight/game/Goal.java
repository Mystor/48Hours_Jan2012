package com.agenosworld.fourtyeight.game;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.MainLoop;
import com.agenosworld.fourtyeight.ScoreManager;
import com.agenosworld.fourtyeight.game.units.Player;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class Goal implements Drawable, ContactListener {
	
	// Shape
	private float width = 32f*CameraManager.RATIO;
	private float height = 32f*CameraManager.RATIO;
	
	private float radius = 16f*CameraManager.RATIO;
	
	// b2d Definition values
	private BodyDef def;
	private CircleShape shape;
	private FixtureDef fixtureDef;
	
	// b2d values
	private Body body;
	private Fixture fixture;
	private World b2dWorld;
	
	// Visual values
	private AtlasRegion visual;
	private Vector2 position;
	
	// Player
	private Player player;
	
	// World
	GameWorld world;
	
	public Goal(float x, float y, GameWorld world) {
		this.world = world;
		b2dWorld = world.getB2dWorld();
		player = world.getPlayer();
		position = new Vector2(x+1, y+1);
		
		// Create the body definition
		def = new BodyDef();
		def.position.set(position);
		def.type = BodyDef.BodyType.StaticBody;
		
		// Create the Shape definition
		shape = new CircleShape();
		shape.setRadius(radius);
		
		// Create the Fixture Definition
		fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = shape;
		
		// Add the body to the world
		body = b2dWorld.createBody(def);
		fixture = body.createFixture(fixtureDef);
		
		// Load the visual
		visual = SpriteManager.getRegion("target");
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (visual != null)
			batch.draw(visual, position.x-width/2, position.y-height/2, width, height);
	}
	
	private void victory() {
		ScoreManager.registerVictory();
		world.dispose();
		MainLoop.nextMap = "MAIN_MENU";
	}
	
	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == this.fixture) {
			if (contact.getFixtureB() == player.getFixture()) {
				victory();
			}
		} else if (contact.getFixtureB() == this.fixture) {
			if (contact.getFixtureA() == player.getFixture()) {
				victory();
			}
		}
		return;
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
