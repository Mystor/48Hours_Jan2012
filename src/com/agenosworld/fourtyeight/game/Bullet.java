package com.agenosworld.fourtyeight.game;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.game.particles.EffectManager;
import com.agenosworld.fourtyeight.game.particles.Explosion;
import com.agenosworld.fourtyeight.game.particles.Smoke;
import com.agenosworld.fourtyeight.game.units.Unit;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class Bullet implements ContactListener, Drawable, Disposable {
	
	// Static values
	private final float BULLET_SPEED = 15f;
	private final float BULLET_DAMAGE = 10f;
	
	private final float height = 3f*CameraManager.RATIO;
	private final float width = 11f*CameraManager.RATIO;
	private final float renderHeight = 5f*CameraManager.RATIO;
	private final float renderWidth = width;

	
	// Body definition values
	private BodyDef def;
	private PolygonShape shape;
	private FixtureDef fixtureDef;
	
	// B2d Values
	private Body body;
	private Fixture fixture;
	private World world;
	
	// Positioning values
	private Vector2 position;
	private float angle;
	
	// Visual definition values
	private AtlasRegion main_appearance;
	
	// Activity values
	private BulletEmitter owner;
	
	// Smoke emitting values
	private float timeSinceLastSmoke = 0;
	private float smokeRate = 0.01f;
	
	public Bullet(Vector2 origin, float firingAngle, BulletEmitter emitter, World world) {
		position = new Vector2(origin);
		angle = firingAngle;
		owner = emitter;
		this.world = world;
		
		// Define the physics properties of the bullet
		def = new BodyDef();
		def.angle = firingAngle*MathUtils.degreesToRadians;
		def.position.set(position);
		def.bullet = true;
		def.type = BodyDef.BodyType.DynamicBody;
		
		// Determine the velocity of the bullet
		Vector2 bulletVelocity = new Vector2(BULLET_SPEED, 0);
		bulletVelocity.rotate(firingAngle);
		def.linearVelocity.set(bulletVelocity);
		
		
		// Define the shape
		shape = new PolygonShape();
		shape.setAsBox(width/2, height/2);
		
		// Define the fixture
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.isSensor = true;
		
		// Add the bullet to the world
		body = world.createBody(def);
		fixture = body.createFixture(fixtureDef);
		
		// Load the visuals for the bullet
		main_appearance = SpriteManager.getRegion("rocket");
		
	}
	
	/**
	 * 
	 * @param contact
	 * @param fixtureA whether or not the object which is hit is fixture A
	 */
	private void bulletHit(Contact contact, boolean fixtureA) {
		// Get the hit fixture
		Fixture hit;
		if (fixtureA) {
			hit = contact.getFixtureA();
		} else {
			hit = contact.getFixtureB();
		}
		
		PhysicsProp foundProp = null;
		// CHeck if its a PhysicsProp
		for (PhysicsProp p : owner.getGameWorld().physicsProps) {
			if (p.getFixture() == hit)
				foundProp = p;
		}
		if (foundProp != null) {
			// Apply Impulse
			// Find the direction between the two objects
			Vector2 forceDirection = new Vector2(foundProp.getBody().getPosition());
			forceDirection.add(-position.x, -position.y);
			
			// Create a force magnitude and rotate it to align with the force direction
			Vector2 forceMagnitude = new Vector2(foundProp.getBody().getMass()*BULLET_SPEED*.3f, 0f);
			forceMagnitude.rotate(forceDirection.angle());
			
			// Apply the force location at the bullet's position
			Vector2 forceLocation = new Vector2(position);
			
			// Apply the Linear Impulse
			foundProp.getBody().applyLinearImpulse(forceMagnitude, forceLocation);
		}
		
		// Check if it is a Unit
		Unit foundUnit = null;
		for (Unit u : owner.getGameWorld().units) {
			if (u.getFixture() == hit)
				foundUnit = u;
		}
		if (foundUnit != null) {
			// Apply Impulse
			// Find the direction between the two objects
			Vector2 forceDirection = new Vector2(foundUnit.getBody().getPosition());
			forceDirection.add(-position.x, -position.y);

			// Create a force magnitude and rotate it to align with the force direction
			Vector2 forceMagnitude = new Vector2(foundUnit.getBody().getMass()*BULLET_SPEED*.3f, 0f);
			forceMagnitude.rotate(forceDirection.angle());

			// Apply the force location at the bullet's position
			Vector2 forceLocation = new Vector2(position);

			// Apply the Linear Impulse
			foundUnit.getBody().applyLinearImpulse(forceMagnitude, forceLocation);
			
			// Deal damage
			foundUnit.damage(BULLET_DAMAGE);
		}
		
		EffectManager.addEffect(new Explosion(position.x, position.y));
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// smoke stuff
		timeSinceLastSmoke += Gdx.graphics.getDeltaTime();
		if (timeSinceLastSmoke >= smokeRate) {
			EffectManager.addEffect(new Smoke(this.position.x, this.position.y));
			timeSinceLastSmoke = 0;
		}
		
		// Update the position of the bullet as well as its orientation
		position.set(body.getPosition());
		angle = body.getAngle()*MathUtils.radiansToDegrees;
		
		// Draw the main visual onto the screen
		float drawX = position.x-renderWidth/2f;
		float drawY = position.y-renderHeight/2f/*+1*CameraManager.RATIO*/;
		float originX = renderWidth/2f;
		float originY = renderHeight/2f/* - 1*CameraManager.RATIO*/;
		float angle = this.angle;

		batch.draw(main_appearance, drawX, drawY, originX, originY, renderWidth, renderHeight, 1, 1, angle, true);
	}
	
	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA() == this.fixture) {
			if (contact.getFixtureB() != owner.getFixture()) {
				bulletHit(contact, false);
				Disposer.queueForDisposal(this);
			}
		} else if (contact.getFixtureB() == this.fixture) {
			if (contact.getFixtureA() != owner.getFixture()) {
				bulletHit(contact, true);
				Disposer.queueForDisposal(this);
			}
		}
		return;
	}

	private int disposeCount = 0;
	
	@Override
	public void dispose() {
		disposeCount++;
		if (disposeCount > 1)
			return;
		ContactManager.remContactListener(this);
		world.destroyBody(body);
		owner.getGameWorld().bullets.remove(this);
		//owner.destroyBullet(this);
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
