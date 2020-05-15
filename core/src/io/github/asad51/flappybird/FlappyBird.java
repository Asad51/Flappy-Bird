package io.github.asad51.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    private final String TAG = "FLAPPY_BIRD";

    private SpriteBatch batch;
    private Texture background;
    private Texture[] birds;
    private Circle birdCircle;
    private Texture topTube, bottomTube;
    private BitmapFont font;
    private Texture gameOver;
    private Random random;

    private int flapState;
    private float velocity;
    private int gameState;
    private float gravity;
    private float distanceBetweenTwoTube, distanceBetweenTopBottom;

    private int numberOfTubes;
    private float tubeVelocity;
    private float[] tubeX, tubeOffset;
    private Rectangle[] topRectangles, bottomRectangles;

    private float width, height;
    private float birdX, birdY;

    private int score;
    private int scoringTube;

    @Override
    public void create() {
        numberOfTubes = 5;

        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        birdCircle = new Circle();
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        topRectangles = new Rectangle[numberOfTubes];
        bottomRectangles = new Rectangle[numberOfTubes];
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5);
        gameOver = new Texture("gameover.png");
        random = new Random();

        gravity = 1;
        distanceBetweenTwoTube = 500;
        distanceBetweenTopBottom = 600;

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        tubeVelocity = 4;
        tubeOffset = new float[numberOfTubes];
        tubeX = new float[numberOfTubes];

        startGame();
        Gdx.app.log(TAG, "Width: " + width + ", Height: " + height + ", BirdX: " + birdX + ", BirdY: " + birdY + " tubeWidth: " + topTube.getWidth());
    }

    private void startGame(){
        flapState = 0;
        velocity = 0;
        gameState = 0;
        score = 0;
        scoringTube = 0;
        birdX = width / 2 - birds[0].getWidth() / 2;
        birdY = height / 2 - birds[0].getHeight() / 2;
        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (random.nextFloat() * 0.5f) * (height - distanceBetweenTopBottom - distanceBetweenTopBottom / 2);
            tubeX[i] = width + i * distanceBetweenTwoTube;
            topRectangles[i] = new Rectangle();
            bottomRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, width, height);

        if(gameState == 0){
            batch.draw(birds[gameState], birdX, birdY);
            startGame();
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        else if (gameState == 1) {
            if (Gdx.input.justTouched()) {
                velocity -= 15;
            }
            if(tubeX[scoringTube] < width - birds[0].getWidth() / 2) {
                score += 1;
                scoringTube = (scoringTube + 1) % 5;
            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTwoTube;
                    tubeOffset[i] = (random.nextFloat() * 0.5f) * (height - distanceBetweenTopBottom - distanceBetweenTopBottom / 2);
                } else {
                    tubeX[i] -= tubeVelocity;
                }

                batch.draw(topTube, tubeX[i], height / 2 + distanceBetweenTopBottom / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], height / 2 - distanceBetweenTopBottom / 2 - bottomTube.getHeight() + tubeOffset[i]);
                topRectangles[i] = new Rectangle(tubeX[i], height / 2 + distanceBetweenTopBottom / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomRectangles[i] = new Rectangle(tubeX[i], height / 2 - distanceBetweenTopBottom / 2 - bottomTube.getHeight() + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            }

            if ((birdY > 0 || velocity < 0) && birdY < height) {
                velocity += gravity;
                birdY -= velocity;
                birdY = Math.min(birdY, height - birds[0].getHeight());
                birdY = Math.max(birdY, 0);
            }

            flapState = (flapState == 1) ? 0 : 1;
            batch.draw(birds[flapState], birdX, birdY);
            birdCircle.set(width / 2, birdY + birds[0].getHeight() / 2, birds[flapState].getWidth() / 2);
            for (int i = 0; i < numberOfTubes; i++) {
                if(Intersector.overlaps(birdCircle, topRectangles[i]) || Intersector.overlaps(birdCircle, bottomRectangles[i])){
                    Gdx.app.log(TAG, "Collision with: " + i);
                    gameState = 2;
                }
            }
        }
        else {
            batch.draw(birds[flapState], birdX, birdY);
            batch.draw(gameOver, width / 2 - gameOver.getWidth() / 2, height / 2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 0;
            }
        }

        font.draw(batch, "Score: " + score, 50, height - 50);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
