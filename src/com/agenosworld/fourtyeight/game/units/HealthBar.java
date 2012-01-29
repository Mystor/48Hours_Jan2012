package com.agenosworld.fourtyeight.game.units;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public class HealthBar implements Drawable {
	
	// The distance above a unit to draw the health bar
	private float disAbove = 10f*CameraManager.RATIO;
	
	// The width and height of a full health bar
	private float width = 20f*CameraManager.RATIO;
	private float height = 2f*CameraManager.RATIO;
	
	// The unit the HealthBar is tracking
	private Unit unit;
	
	// The visual properties 
	private AtlasRegion healthEmpty;
	private AtlasRegion healthFull;
	
	public HealthBar(Unit unit) {
		this.unit = unit;
		
		healthEmpty = SpriteManager.getRegion("health-empty");
		healthFull = SpriteManager.getRegion("health-full");
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (unit.health == unit.maxHealth)
			return;
		Vector2 drawPosition = new Vector2(unit.position);
		drawPosition.y += disAbove;
		
		// Draw the empty healthbar
		batch.draw(healthEmpty, drawPosition.x-width/2, drawPosition.y-height/2, width, height);
		
		float healthFullWidth = width*unit.health/unit.maxHealth;
		// Draw the full healthbar
		batch.draw(healthFull, drawPosition.x-healthFullWidth/2, drawPosition.y-height/2, healthFullWidth, height);
	}

}
