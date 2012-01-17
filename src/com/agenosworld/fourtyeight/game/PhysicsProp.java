package com.agenosworld.fourtyeight.game;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsProp implements Drawable {
	
	// Structure
	private BodyDef def;
	private Shape shape;
	private FixtureDef fixtureDef;
	
	// Final body and fixture
	private Body body;
	private Fixture fixture;
	
	// The world
	private World world;
	
	// Positioning values
	private Vector2 position;
	private float rotation;
	
	// Visual Values
	private float width;
	private float height;
	private AtlasRegion visual;
	
	public PhysicsProp(int x, int y, World b2dWorld, String propID) {
		if (propID == null)
			return;
		
		position = new Vector2(x, y);
		rotation = 0;
		world = b2dWorld;
		
		// Create generic defs.
		def = new BodyDef();
		def.position.set(position);
		def.angularDamping = 10f;
		def.type = BodyDef.BodyType.DynamicBody;
		
		// Prop dependent properties
		if (propID == "0") { // Barrel
			shape = new CircleShape();
			shape.setRadius(8f*CameraManager.RATIO);
			
			width = 16f*CameraManager.RATIO;
			height = 16f*CameraManager.RATIO;
			visual = SpriteManager.getRegion("barrel");
		}
		
		// Define the fixturedef
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 4f;
		fixtureDef.restitution = .1f;
		
		// Create the object in the physics world
		body = world.createBody(def);
		fixture = body.createFixture(fixtureDef);
	}
	
	public Body getBody() {
		return body;
	}
	
	public Fixture getFixture() {
		return fixture;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (visual == null)
			return;
		// Update the location to draw the sprite at
		position.set(body.getPosition());
		rotation = body.getAngle()*MathUtils.radiansToDegrees;
		
		// Draw the main visual onto the screen
		float drawX = position.x-width/2f;
		float drawY = position.y-height/2f;
		float originX = width/2f;
		float originY = height/2f;

		batch.draw(visual, drawX, drawY, originX, originY, width, height, 1, 1, rotation, true);
	}

}
