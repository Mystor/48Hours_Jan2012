package com.agenosworld.fourtyeight.game.units;

import com.agenosworld.basicgdxgame.InputManager;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.MainLoop;
import com.agenosworld.fourtyeight.ScoreManager;
import com.agenosworld.fourtyeight.game.GameWorld;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends Unit implements InputProcessor {

	// Camera Manager
	private CameraManager cameraManager;

	// Mouse Position storage
	private Vector2 mousePosition = new Vector2();
	
	// Movement variables
	private boolean movingUp = false, movingDown = false, movingLeft=false, movingRight=false;
	
	// Firing values
	private float timeSinceLastShot;
	private float shotCooldown = .1f;
	
	// Constructor
	public Player(float x, float y, GameWorld world) {
		super(x, y, world);
		cameraManager = world.getCameraManager();
		
		// Set Max HP
		maxHealth = 80f;
		health = 80f;
	}
	
	// Load the main visual for the player and set the width/height attributes
	@Override
	protected AtlasRegion getMainVisual() {
		width = 16f*CameraManager.RATIO;
		height = 16f*CameraManager.RATIO;
		return SpriteManager.getRegion("player-standing");
	}

	// Create the B2DDefinitions required to create the unit object in space
	@Override
	protected void createB2DDefinitions() {
		// Create the definitions for Box2d
		// Shape
		shape = new CircleShape();
		shape.setRadius(6f*CameraManager.RATIO);

		// Definition
		def = new BodyDef();
		def.type = BodyType.DynamicBody; // Object is affected by forces
		def.position.set(position);
		def.fixedRotation = true;
		def.linearDamping = 5f;

		// Define the body's fixture
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
	}
	
	// Update functions - connecting object to the b2dWorld
	@Override
	public void update(float delta) {
		// Increment time since last shot
		timeSinceLastShot += delta;
		
		// Apply Movement Forces
		Vector2 movementForce = new Vector2();
		if (movingUp)
			movementForce.y+=15f;
		if (movingDown)
			movementForce.y-=15f;
		if (movingRight)
			movementForce.x+=15f;
		if (movingLeft)
			movementForce.x-=15f;
		
		// Prevent from accelerating if going too fast
		Vector2 velocity = body.getLinearVelocity();
		if (Math.abs(velocity.x) > 5 && velocity.x*movementForce.x > 0) {
			movementForce.x = 0;
		}
		if (Math.abs(velocity.y) > 5 && velocity.y*movementForce.y > 0) {
			movementForce.y = 0;
		}
		
		// Apply the force
		body.applyForceToCenter(movementForce);
		updateFacing();
		
		// Update the position to that of the body in the b2dWorld
		super.update(delta);
	}
	
	public void updateFacing() {
		Vector2 facingAngle = new Vector2(mousePosition.x, mousePosition.y);
		facingAngle.add(-position.x, -position.y);
		this.facing = facingAngle;
	}
	
	@Override
	public void die() {
		ScoreManager.registerLoss();
		gameWorld.dispose();
		MainLoop.nextMap = "MAIN_MENU";
	}
	
	@Override
	public void dispose() {
		InputManager.remInputProcessor(this);
		super.dispose();
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
		// Update the cursor's position
		Vector3 cursorPosition = new Vector3(x, y, 0);
		OrthographicCamera camera = cameraManager.getCamera();
		camera.unproject(cursorPosition);
		mousePosition.set(cursorPosition.x, cursorPosition.y);
		
		// Update facing
		updateFacing();
		
		return false;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return touchMoved(x,y); // Simply call touchMoved
	}
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (timeSinceLastShot > shotCooldown) {
			timeSinceLastShot = 0;
			fireBullet();
		}
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		
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
