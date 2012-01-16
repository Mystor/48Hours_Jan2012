package com.agenosworld.basicgdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class Updater {
	
	//private static int lastFrameTime;
	//private static int frameTime;
	
//	private static ArrayList<Updatable> toAdd = new ArrayList<Updatable>();
	private static ArrayList<Updatable> updatables = new ArrayList<Updatable>();
//	private static ArrayList<Updatable> toRemove = new ArrayList<Updatable>();
	
	public static void addUpdatable(Updatable u) {
		updatables.add(u);
	}
	
	public static void removeUpdatable(Updatable u) {
		updatables.remove(u);
	}
	
	public static boolean isUpdating(Updatable u) {
		return updatables.contains(u);
	}

	public static void update() {
		float delta = Gdx.graphics.getDeltaTime();
		for (int i=0; i<updatables.size(); i++) {
//		for (Updatable u : updatables) {
			updatables.get(i).update(delta);
		}
	}

}
