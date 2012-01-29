package com.agenosworld.fourtyeight.game.particles;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Explosion extends ParticleEffect {
	
	// Loaded Texture FIles
	private AtlasRegion loadedTextures;
	private TextureRegion[] texturesArray;

	// Animation
	private Animation animation;
	
	// State Time
	float stateTime = 0f;
	
	public Explosion(float x, float y) {
		position = new Vector2(x, y);
		
		width = 10f*CameraManager.RATIO;
		height = 10f*CameraManager.RATIO;
		
		loadedTextures = SpriteManager.getRegion("explosions");
		
		TextureRegion[][] tmp = loadedTextures.split(10, 10);
		texturesArray = new TextureRegion[9*9];
		
		int index = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				texturesArray[index++] = tmp[i][j];
			}
		}
		
		animation = new Animation(0.025f, texturesArray);
	}

	@Override
	public void update(float delta) {
		stateTime += delta;
		if (animation.isAnimationFinished(stateTime))
			Disposer.queueForDisposal(this);
		else	
			currentFrame = animation.getKeyFrame(stateTime, false);
		
	}

	@Override
	public boolean isFinished() {
		return animation.isAnimationFinished(stateTime);
	}

}
