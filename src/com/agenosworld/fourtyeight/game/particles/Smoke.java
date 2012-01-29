package com.agenosworld.fourtyeight.game.particles;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Smoke extends ParticleEffect {
	
	// The alpha to render at
	private float alpha;
	
	// The time which has elapsed since the smokeEffect was first created
	private float stateTime;
	
	// The time which it takes for Smoke to animate
	private float animationTime = 0.5f;
	
	public Smoke(float x, float y) {
		// Set the position of the smoke particle
		position = new Vector2(x, y);
		
		// Set the appearance of the smoke particle
		currentFrame = SpriteManager.getRegion("smoke");
		
		// Set the stateTime to 0
		stateTime = 0;
		
		// Set the width and height
		width = 10f*CameraManager.RATIO;
		height = 10f*CameraManager.RATIO;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		Color tmp = batch.getColor();
		Color tmp2 = new Color (tmp);
		tmp2.a = alpha;
		batch.setColor(tmp2);
		batch.draw(currentFrame, position.x-width/2, position.y-height/2, width, height);
		batch.setColor(tmp);
	}

	@Override
	public void update(float delta) {
		// Find the current alpha of the particle
		stateTime += delta;
		alpha = 0.2f - (stateTime/animationTime)*0.2f;
		
		// If the particle is completed animating
		if (alpha <= 0) {
			Disposer.queueForDisposal(this);
		}
	}

	@Override
	public boolean isFinished() {

		return false;
	}

}
