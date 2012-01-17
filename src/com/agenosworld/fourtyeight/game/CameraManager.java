package com.agenosworld.fourtyeight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class CameraManager implements Disposable {
	
	public static final float RATIO = 1.0f/16.0f;
	
	// The camera being managed
	private OrthographicCamera camera;

	// The spritebatch to render using
	private SpriteBatch batch;
	
	// The aspect Ratio and viewHeight of the viewport
	private float aspectRatio;
	private float viewHeight;
	
	// The actual screen width and height in pixels - must be updated with resize
	private int scrnWidth;
	private int scrnHeight;
	
	// The rectangle which represents the glViewport
	private Rectangle glViewport;
	
	public CameraManager() {
		
	}
	
	public void lookAt(int x, int y) {
		camera.position.set(x, y, 0);
	}
	
	public CameraManager(float viewHeight, int width, int height) {
		this.viewHeight = viewHeight;
		camera = new OrthographicCamera(viewHeight, viewHeight);
		batch = new SpriteBatch();
		resize(width, height);
	}
	
	public void setPosition(Vector2 pos) {
		camera.position.set(pos.x, pos.y, 0);
		
		updateCamera();
	}
	public void setPosition(float x, float y) {
		camera.position.set(x, y, 0);
		
		updateCamera();
	}
	
	public void resize(int width, int height) {
		//Correct the aspect ratio to the new scaling
		aspectRatio = (float)width/(float)height;
		
		//Adjust the camera's viewport width to match the new aspect ratio
		camera.viewportWidth = viewHeight*aspectRatio;
		camera.viewportHeight = viewHeight;
		
		//Set the scrnWidth and scrnHeight attributes
		scrnWidth = width;
		scrnHeight = height;
		
		//Update the camera
		updateCamera();
	}
	
	public void updateCamera() {
		//Update its position
		camera.position.set(viewHeight*aspectRatio/2, viewHeight/2,0); // TODO: Clean this up
		
		//TODO: Understand wtf is going on here
		// get the GL10 object
		GL10 gl = Gdx.graphics.getGL10();
		
		// Update the GL10 object's viewport.
		glViewport = new Rectangle(0, 0, scrnWidth, scrnHeight);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glViewport((int) glViewport.x, (int) glViewport.y,
                        (int) glViewport.width, (int) glViewport.height);
        
        camera.update();
        camera.apply(gl);
        
        //Update the SpriteBatch's Projection Matrix
        batch.setProjectionMatrix(camera.combined);
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
