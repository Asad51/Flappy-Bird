package io.github.asad51.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	private final String TAG = "FLAPPY_BIRD";

	private  SpriteBatch batch;
	private Texture background;
	private Texture[] birds;

	private int flapState;
	private float velocity;
	private int gameState;
	private float gravity;

	private float width, height;
	private float birdX, birdY;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		flapState = 0;
		velocity = 0;
		gameState = 0;
		gravity = 2;

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		birdX = width / 2 - birds[0].getWidth() / 2;
		birdY = height / 2 - birds[0].getHeight() / 2;
		Gdx.app.log(TAG, "Width: " + width + ", Height: " + height + ", BirdX: " + birdX + ", BirdY: " + birdY);
	}

	@Override
	public void render () {
		if(Gdx.input.justTouched()){
			gameState = 1;
			velocity -= 30;
		}

		if(gameState != 0){
			if((birdY > 0 || velocity < 0) && birdY < height) {
				velocity += gravity;
				birdY -= velocity;
				birdY = Math.min(birdY, height - birds[0].getHeight());
				birdY = Math.max(birdY, 0);
			}
		}

		flapState = (flapState == 1) ? 0: 1;

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		batch.draw(birds[flapState], birdX, birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
