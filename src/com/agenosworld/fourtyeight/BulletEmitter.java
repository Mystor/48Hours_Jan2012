package com.agenosworld.fourtyeight;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface BulletEmitter {
	
	public Fixture getFixture();
	
	public void destroyBullet(Bullet b);

}
