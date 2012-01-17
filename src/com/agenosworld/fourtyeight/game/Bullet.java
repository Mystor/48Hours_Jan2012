package com.agenosworld.fourtyeight.game;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
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
	private final float height = 3f*CameraManager.RATIO;
	private final float width = 11f*CameraManager.RATIO;
	private final float renderHeight = 5f*CameraManager.RATIO;
	private final float renderWidth = width;
	private final float bulletSpeed = 15f;
	
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
//		def.angularVelocity = bulletSpeed;
		
		// Determine the velocity of the bullet
		Vector2 bulletVelocity = new Vector2(bulletSpeed, 0);
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
			System.out.println("applyin da forces");
			// Apply Impulse
			Vector2 fpPosition = foundProp.getBody().getPosition();
			
			Vector2 forceDirection = new Vector2(foundProp.getBody().getPosition());
			forceDirection.add(-position.x, -position.y);
			
			Vector2 forceMagnitude = new Vector2(foundProp.getBody().getMass()*bulletSpeed*.1f, 0f);
			forceMagnitude.rotate(forceDirection.angle());
			
			Vector2 forceLocation = new Vector2((position.x + fpPosition.x)/2, (position.y + fpPosition.y)/2);
			
			System.out.println(forceMagnitude + " " + forceLocation);
			
			foundProp.getBody().applyLinearImpulse(forceMagnitude, forceLocation);
		}
		
	}

	@Override
	public void draw(SpriteBatch batch) {
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
		owner.destroyBullet(this);
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
