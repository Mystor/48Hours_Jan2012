package com.agenosworld.fourtyeight.game.particles;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.basicgdxgame.Updatable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class ParticleEffect implements Updatable, Drawable, Disposable {
	
	// Position
	protected Vector2 position;
	
	// Shape variables
	protected float width;
	protected float height;
	
	// The current frame to be rendered
	protected TextureRegion currentFrame;

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(currentFrame, position.x-width/2, position.y-height/2, width, height);
	}
	
	@Override
	public void dispose() {
		EffectManager.removeEffect(this);
	}

	public abstract boolean isFinished();
}
