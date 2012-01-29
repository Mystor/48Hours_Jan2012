package com.agenosworld.fourtyeight.menus;

import com.agenosworld.basicgdxgame.Disposer;
import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.MainLoop;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.math.Vector2;

public class LevelButton extends Button {
	
	public String levelFile;
	
	private MainMenu mainMenu;
	
	public LevelButton(int levelNumber, String levelLocation, CameraManager cameraManager, MainMenu mainMenu) {
		super(cameraManager);
		// Store the Main Menu
		this.mainMenu = mainMenu;
		
		// Calculate the x and y position from the levelNumber
		float y = 310;
		int horzNumber = levelNumber;
		if (horzNumber > 4) {
			horzNumber -= 4;
			y -= 60;
		}
		float x = horzNumber*60 + 50;		
		
		// Set the position of the levelbutton
		position = new Vector2(x, y);
		
		// get the images for the levelbutton
		visual = SpriteManager.getRegion("level-" + levelNumber);
		visualHover = SpriteManager.getRegion("sel-" + levelNumber);
		width = 44;
		height = 45;
		
		// Save the levelFile location
		levelFile = levelLocation;
	}

	@Override
	protected void onClick() {
		Disposer.queueForDisposal(mainMenu);
		MainLoop.nextMap = levelFile;
	}

}
