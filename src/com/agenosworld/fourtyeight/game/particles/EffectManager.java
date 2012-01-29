package com.agenosworld.fourtyeight.game.particles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EffectManager {
	
	public static ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	
	public static void addEffect(ParticleEffect e) {
		effects.add(e);
	}
	
	public static void removeEffect(ParticleEffect e) {
		effects.remove(e);
	}
	
	public static void clearEffects() {
		effects.clear();
	}
	
	public static void update(float delta) {
		for (ParticleEffect e : effects) {
			e.update(delta);
		}
	}
	
	public static void render(SpriteBatch batch) {
		for (ParticleEffect e : effects) {
			e.draw(batch);
		}
	}

}
