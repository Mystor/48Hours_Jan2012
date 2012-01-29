package com.agenosworld.fourtyeight.spritemanager;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;



public class SpriteManager {
	
	private static TextureAtlas atlas;
	
	public static void loadSprites(FileHandle packFile) {
		atlas = new TextureAtlas(packFile);
	}
	
	/**
	 * Get the region defined by name from the TextureAtlas
	 * @param name The name of the texture to retrieve
	 * @return The AtlasRegion representing the region retrieved from the TextureAtlas
	 */
	public static AtlasRegion getRegion(String name) {
		if (atlas == null)
			return null;
		
		AtlasRegion region = atlas.findRegion(name);
		return region;
	}
	
	/**
	 * Get the regions defined by name from the TextureAtlas
	 * @param name The name of the textures to retrieve
	 * @return A list of AtlasRegions representing the regions retrieved from the TextureAtlas
	 */
	public static List<AtlasRegion> getRegions(String name) {
		if (atlas == null)
			return null;
		
		List<AtlasRegion> regions = atlas.findRegions(name);
		return regions;
	}
	
	public static void dispose() {
		atlas.dispose();
	}

}
