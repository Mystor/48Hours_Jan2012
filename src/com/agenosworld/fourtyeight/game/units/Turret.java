package com.agenosworld.fourtyeight.game.units;

import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.ScoreManager;
import com.agenosworld.fourtyeight.game.GameWorld;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class Turret extends Unit implements RayCastCallback {
	
	// Player tracking values
	private Player player;
	
	// Firing values
	private float timeSinceLastShot;
	private float shotCooldown = 0.1f;

	// Aimless rotation rate
	private float rotateRate = 22.5f;
	
	// Variables for RayCasting
	private Fixture closestFixture;
	private boolean lastCall = false;
	private float timeSinceLastCall = 0;
	
	// Different turret states
	private AtlasRegion resting;
	private AtlasRegion angry;
	
	public Turret(float x, float y, GameWorld world, Player player) {
		super(x, y, world);
		this.player = player;
		
		resting = SpriteManager.getRegion("enemy-turret");
		angry = SpriteManager.getRegion("turret-angry");
		
		// Set HP and max HP
		maxHealth = 60;
		health = 60;
		
		// Generate a random starting facing in order to reduce the number of identical Turrets
		float randomAngle = (float) Math.random()*360f;
		facing.rotate(randomAngle);
		
		float randomRate = (float) (Math.random()-0.5f) * 5f + 22.5f;
		rotateRate = randomRate;
	}

	@Override
	protected AtlasRegion getMainVisual() {
		width = 16f*CameraManager.RATIO;
		height = 16f*CameraManager.RATIO;
		return SpriteManager.getRegion("enemy-turret");
	}

	@Override
	protected void createB2DDefinitions() {
		// Shape
		shape = new CircleShape();
		shape.setRadius(6f*CameraManager.RATIO);
		
		// Definition
		def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(position);
		def.fixedRotation = true;
		def.linearDamping = 3f;
		
		// Define the body's fixture
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 4.0f;
	}
	
	// Update function - purpose is to enable player tracking etc
	@Override
	public void update(float delta) {
		timeSinceLastCall += delta;
		
		// Refresh shot cooldown
		timeSinceLastShot += delta;
		
		if (canSeePlayer()) { // Player detected - try to shoot
			this.facing.set(player.position.x-position.x, player.position.y-position.y);
			if (timeSinceLastShot >= shotCooldown) {
				fireBullet();
				timeSinceLastShot = 0;
			}
			
			// Make the turret appear angry
			visual_main = angry;
		} else { // Player not detected - rotate aimlessly clockwise (I think)
			float currentAngle = facing.angle();
			currentAngle += delta*rotateRate;
			facing = new Vector2(1, 0);
			facing.rotate(currentAngle);
			
			// Make the turret appear resting
			visual_main = resting;
		}
		
		super.update(delta);
	}
	
	private boolean canSeePlayer() {
		if (timeSinceLastCall >= 0.1) {
			closestFixture = null;
			world.rayCast(this, new Vector2(position), new Vector2(player.position));
			if (closestFixture == player.getFixture()) {
				lastCall = true;
				return true;
			}
			lastCall = false;
			return false;
		} else {
			return lastCall;
		}
	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point,
			Vector2 normal, float fraction) {
		System.out.println(fixture.isSensor());
		if (!fixture.isSensor()) {
			closestFixture = fixture; // This must be the closest fixture in the line of the ray
			return fraction; // Cut the ray short at this point - we only want closer fixtures
		}
		return -1;
	}
	
	@Override
	public void die() {
		ScoreManager.registerEnemyKill();
		super.die();
	}

}
