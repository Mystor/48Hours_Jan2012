package com.agenosworld.fourtyeight;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class ContactManager implements ContactListener {

	private static ArrayList<ContactListener> processors = new ArrayList<ContactListener>();

	private static ContactManager manager;

	public static void addInputProcessor(ContactListener p) {
		processors.add(p);
	}

	public static void remInputProcessor(ContactListener p) {
		processors.remove(p);
	}

	public static void isProcessing(ContactListener p) {
		processors.contains(p);
	}

	public static void registerManager(ContactManager manager, World world) {
		world.setContactListener(manager);
		ContactManager.manager = manager;
	}

	public static ContactManager getManager() {
		return manager;
	}

	public ContactManager() {

	}

	@Override
	public void beginContact(Contact contact) {
		for (ContactListener p : ContactManager.processors) {
			p.beginContact(contact);
		}
	}

	@Override
	public void endContact(Contact contact) {
		for (ContactListener p : ContactManager.processors) {
			p.endContact(contact);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		for (ContactListener p : ContactManager.processors) {
			p.preSolve(contact, oldManifold);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		for (ContactListener p : ContactManager.processors) {
			p.postSolve(contact, impulse);
		}
	}

}
