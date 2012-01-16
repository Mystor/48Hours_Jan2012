package com.agenosworld.fourtyeight;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.basicgdxgame.Updatable;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player implements InputProcessor, Updatable, Drawable {
	
	// Shape values
	private float width = 16f*CameraManager.RATIO;
	private float height = 16f*CameraManager.RATIO;
	private float radius = 8f*CameraManager.RATIO;
	
	// Box2d Definition values
	private BodyDef def;
	private CircleShape shape;
	private FixtureDef fixtureDef;
	
	// Box2d values
	private Body body;
	@SuppressWarnings("unused")
	private Fixture fixture;
	private World world;
	
	// Visual properties
	private AtlasRegion visual_main;
	private CameraManager cameraManager;
	
	// Location and facing properties
	private Vector2 position;
	private Vector2 facing;
	
	// Movement variables
	private boolean movingUp = false, movingDown = false, movingLeft=false, movingRight=false;
	
	// Constructor
	public Player(float x, float y, GameWorld world) {
		// Get the b2dWorld and CameraManager
		this.world = world.getB2dWorld();
		this.cameraManager = world.getCameraManager();
		
		// Define position
		position = new Vector2(x, y);
		facing = new Vector2(1, 0);
		
		// Create the definitions for Box2d
		// Shape
		shape = new CircleShape();
		shape.setRadius(radius-(2f*CameraManager.RATIO)); // Slightly modified radius
//		shape.setPosition(position);
		
		// Definition
		def = new BodyDef();
		def.type = BodyType.DynamicBody; // Object is affected by forces
		def.position.set(position);
		def.fixedRotation = true;
		def.linearDamping = 5f;
		
		// Define the body's fixture
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.friction = 0.5f;
		fixtureDef.density = 1.0f;
		
		// Create the body in the world
		body = this.world.createBody(def);
		fixture = body.createFixture(fixtureDef);
		
		// Obtain the image representation for the player
		visual_main = SpriteManager.getRegion("player-standing");
	}
	
	// Update functions - connecting object to the b2dWorld
	@Override
	public void update(float delta) {
		// Apply Movement FOrces
		Vector2 movementForce = new Vector2();
		if (movingUp)
			movementForce.y+=15f;
		if (movingDown)
			movementForce.y-=15f;
		if (movingRight)
			movementForce.x+=15f;
		if (movingLeft)
			movementForce.x-=15f;
		
		// Prevent from accellerating if going too fast
		Vector2 velocity = body.getLinearVelocity();
		if (Math.abs(velocity.x) > 5 && velocity.x*movementForce.x > 0) {
			movementForce.x = 0;
		}
		if (Math.abs(velocity.y) > 5 && velocity.y*movementForce.y > 0) {
			movementForce.y = 0;
		}
		
		// Apply the force
		body.applyForceToCenter(movementForce);
		
		// Update the position to that of the body in the b2dWorld
		this.position.set(body.getPosition());
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		// Draw the main visual onto the screen
		float drawX = position.x-width/2f;
		float drawY = position.y-height/2f;
		float originX = width/2f;
		float originY = height/2f;
		float angle = facing.angle();
		
		batch.draw(visual_main, drawX, drawY, originX, originY, width, height, 1, 1, angle, true);
	}
	
	// Input Management Functions
	@Override
	public boolean keyDown(int keycode) {
		// Movement Code
		switch (keycode) {
			case Input.Keys.W:
				movingUp = true;
				break;
			case Input.Keys.S:
				movingDown = true;
				break;
			case Input.Keys.A:
				movingLeft = true;
				break;
			case Input.Keys.D:
				movingRight = true;
				break;
		}
		
		return true; // Input was processed
	}
	@Override
	public boolean keyUp(int keycode) {
		// Movement Code
		switch (keycode) {
			case Input.Keys.W:
				movingUp = false;
				break;
			case Input.Keys.S:
				movingDown = false;
				break;
			case Input.Keys.A:
				movingLeft = false;
				break;
			case Input.Keys.D:
				movingRight = false;
				break;
		}
		
		return true; // Input was processed
	}
	@Override
	public boolean touchMoved(int x, int y) {
		Vector3 cursorPosition = new Vector3(x, y, 0);
		OrthographicCamera camera = cameraManager.getCamera();
		camera.unproject(cursorPosition);
		Vector2 facingAngle = new Vector2(cursorPosition.x, cursorPosition.y);
		facingAngle.add(-position.x, -position.y);
		this.facing = facingAngle;
		return false;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return touchMoved(x,y); // Simply call touchMoved
	}
	
	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		return false;
	}
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		
		return false;
	}

}
