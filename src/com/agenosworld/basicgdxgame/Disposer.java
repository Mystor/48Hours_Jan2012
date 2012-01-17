package com.agenosworld.basicgdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;

public class Disposer {

	private static ArrayList<Disposable> toBeDisposed = new ArrayList<Disposable>();

	public static void queueForDisposal(Disposable d) {
		toBeDisposed.add(d);
	}

	public static void unqueueForDisposal(Disposable d) {
		toBeDisposed.remove(d);
	}

	public static void isToBeDisposed(Disposable d) {
		toBeDisposed.contains(d);
	}

	public static void update() {
		if (toBeDisposed.size() <= 0)
			return;
		for (Disposable d : toBeDisposed) {
			d.dispose();
		}
		toBeDisposed = new ArrayList<Disposable>();
	}

}