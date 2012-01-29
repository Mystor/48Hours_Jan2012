package com.agenosworld.fourtyeight;

import com.badlogic.gdx.Gdx;

public class ScoreManager {

	public static final int GAME_VICTORY = 1;
	public static final int GAME_LOSS = -1;
	public static final int NO_HISTORY = 0;
	
	public static int lastGame = 0;
	
	public static int enemyKills = 0;
	
	private static boolean countingTime = false;
	private static float currentGameTime = 0;
	
	public static void registerVictory() {
		lastGame = GAME_VICTORY;
		countingTime = false;
	}
	
	public static void registerLoss() {
		lastGame = GAME_LOSS;
		countingTime = false;
	}
	
	public static void registerEnemyKill() {
		enemyKills += 1;
	}
	
	public static void registerNewGame() {
		lastGame = 0;
		enemyKills = 0;
		currentGameTime = 0;
		countingTime = true;
	}
	
	public static float getCurrentGameTime() {
		return currentGameTime;
	}
	
	public static void update() {
		if (countingTime) {
			currentGameTime += Gdx.graphics.getDeltaTime();
		}
	}
	
}
