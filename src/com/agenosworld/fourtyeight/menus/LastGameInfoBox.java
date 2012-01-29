package com.agenosworld.fourtyeight.menus;

import com.agenosworld.basicgdxgame.Drawable;
import com.agenosworld.fourtyeight.MainLoop;
import com.agenosworld.fourtyeight.ScoreManager;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class LastGameInfoBox implements Drawable {
	
	// Images to be rendered
	private AtlasRegion background;
	private AtlasRegion victoryLossText;
	
	// Strings to be rendered
	private String timeToRender;
	private String enemiesKilled;
	
	public LastGameInfoBox() {
		
		// Load the images required in order to render
		background = SpriteManager.getRegion("lastGame");
		
		if (ScoreManager.lastGame == ScoreManager.GAME_VICTORY) {
			victoryLossText = SpriteManager.getRegion("victoryText");
		} else {
			victoryLossText = SpriteManager.getRegion("defeatText");
		}
		
		// Determine the text for timeToRender
		timeToRender = String.valueOf(Math.round(ScoreManager.getCurrentGameTime()*100f)/100f) + " Second(s)";
		
		// Determine the text for enemiesKilledToRender;
		enemiesKilled = String.valueOf(ScoreManager.enemyKills);
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// Render the background
		batch.draw(background, 404, 207, 218, 240);
		
		// Render the Victory/loss text
		batch.draw(victoryLossText, 449, 227, 128, 33);
		
		// Render the Time text
		TextBounds timeBounds = MainLoop.myriadPro22.getBounds(timeToRender);
		MainLoop.myriadPro22.draw(batch, timeToRender, 510-timeBounds.width/2, 370);
		
		// Render the Enemies Killed text
		TextBounds enemiesBounds = MainLoop.myriadPro22.getBounds(enemiesKilled);
		MainLoop.myriadPro22.draw(batch, enemiesKilled, 510-enemiesBounds.width/2, 300);
	}

}
