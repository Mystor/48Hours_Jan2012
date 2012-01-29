package com.agenosworld.fourtyeight.menus;

import com.agenosworld.fourtyeight.CameraManager;
import com.agenosworld.fourtyeight.MainLoop;
import com.agenosworld.fourtyeight.spritemanager.SpriteManager;
import com.badlogic.gdx.math.Vector2;

public class SaveQuitButton extends Button {

	public SaveQuitButton(CameraManager cameraManager) {
		super(cameraManager);
		
		position = new Vector2(264-101, 480-390-35);
		
		// get the images for the saveQuitButton
		visual = SpriteManager.getRegion("saveQuit");
		visualHover = SpriteManager.getRegion("saveQuitSel");
		width = 202;
		height = 71;
		
	}

	@Override
	protected void onClick() {
		MainLoop.saveQuit();
	}

}
