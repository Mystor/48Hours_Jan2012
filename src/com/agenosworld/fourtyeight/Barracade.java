package com.agenosworld.fourtyeight;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Barracade {
	
	// Structure commands
	private BodyDef def;
	private PolygonShape shape;
	private FixtureDef fixture;
	
	// Final body
	private Body body;
	
	public Barracade(float x, float y, World world) {
		// Define the box's shape
		shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.5f);
		
		// Define the body's Definition
		def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(x+.5f, y+.5f);
		
		// Define the body's fixture
		fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.friction = .5f;
		
		if (world != null)
			addToWorld(world);
		
	}
	
	public void addToWorld(World world) {
		body = world.createBody(def);
		body.createFixture(fixture);
	}

}
