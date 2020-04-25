package io.github.asad51.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;

	int flapState;
	int width, height;
	int birdWidth, birdHeight;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		flapState = 0;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		birdWidth = birds[0].getWidth();
		birdHeight = birds[0].getHeight();
	}

	@Override
	public void render () {
		flapState = (flapState == 1) ? 0: 1;

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		batch.draw(birds[flapState], width / 2 - birdWidth / 2, height / 2 - birdHeight / 2);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
