package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Sprite building;
    private ShapeRenderer shapeRenderer;
    private int screenWidth, screenHeight;
    private Entity guard, player;
    private boolean gameOver;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        shapeRenderer = new ShapeRenderer();
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        building = new Sprite(new Texture(Gdx.files.internal("Sprites/Building.png")));
//        building.setOrigin(building.getWidth() / 2, building.getHeight() / 2);
        building.setScale(.5f);
        building.setPosition(screenWidth / 2 - building.getWidth() / 2, screenHeight / 2 - building.getHeight() / 2);


        player = new Player(new Vector2(500f, 400f),
                new Vector2(0, 1f));
        guard = new Guard(new Vector2(2f * building.getX(), 3f * building.getY()),
                new Vector2(0f, 1f),
                player);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!gameOver) {
            batch.begin();
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.rotate(1);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.rotate(-1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                player.decelerate();
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                player.accelerate();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                player.reinitialize();
                guard.reinitialize();
                gameOver = false;
            }
            player.update();
            guard.update();


            guard.getSprite().draw(batch);
            player.getSprite().draw(batch);
            building.draw(batch);

            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 1, 0.3f);

            ((Guard) guard).drawView(shapeRenderer);

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);



            String collision = detectCollision(guard, building);
            if(collision != null){
                guard.reverseVelocityX();
                guard.reverseVelocityY();
            }

            collision = detectCollision(player, building);
            if(collision != null){
               // player.reverseVelocityX();
                player.reverseVelocityY();
            }

            if(detectCollision(player, guard.getSprite()) != null){
                gameOver = true;
            }

//            if (player.getBounds().overlaps(building.getBoundingRectangle())) {
//                player.zeroVelocity();
//            }


//
//            if (player.getBounds().overlaps(guard.getBounds())) {
//                gameOver = true;
//            }

//            if (guard.getBounds().overlaps(building.getBoundingRectangle())){
//                guard.reverseVelocityX();
//            }

        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                player.reinitialize();
                guard.reinitialize();
                gameOver = false;
            }
        }
    }

    private String detectCollision(Entity collidingEntity, Sprite Obstruction) {
        Rectangle r1 = collidingEntity.getSprite().getBoundingRectangle();
        Rectangle r2 = Obstruction.getBoundingRectangle();
        Rectangle intersection = new Rectangle();
        Intersector.intersectRectangles(r1, r2, intersection);
        if (intersection.x == 0 && intersection.y == 0)
            return null;
        if ((intersection.x > r1.x)  || (intersection.x + intersection.width < r1.x + r1.width)) {
            if ((r1.x + r1.width/2 > r2.x + r2.width + 2) ||
                    (r1.x + r1.width/2 < r2.x)) {
                return "x";
            }
        }
        if (intersection.y > r1.y || (intersection.y + intersection.height < r1.y + r1.height)) {
            if ((r1.y + r1.height/2 > r2.y + r2.height + 2) ||
                    (r1.y + r1.height/2 < r2.y)) {
                return "y";
            }
        }
        return null;
    }
}