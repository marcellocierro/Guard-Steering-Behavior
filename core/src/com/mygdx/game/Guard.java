package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Guard extends Entity {

    public enum State {
        GUARD,
        CHASE
    }

    private State state;
    private Entity target;
    private Vector2 guardPosition;
    private boolean inPosition, rotatingLeft = true, rotatingToStartPosition = false;
    private int rotationCounter;
    private float sightRange = 150f;


    public Guard(Vector2 p, Vector2 o, Entity t) {
        super(p, o, 1f);
        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("Sprites/Police.png")));
        sprite.setScale(0.5f);
        super.setSprite(sprite);
        state = State.GUARD;
        target = t;
        guardPosition = p.cpy();
        inPosition = true;
    }


    public void update() {
        if (state == State.GUARD) {
            if (targetSighted()) {
                inPosition = false;
                state = State.CHASE;
            } else {
                if (inPosition) {
                    patrolPosition();
                } else {
                    returnToPost();
                }
            }
        } else if (state == State.CHASE) {
            if (!targetSighted()) {
                state = State.GUARD;
                rotatingToStartPosition = true;
            } else {
                pursueTarget();
            }
        }
        super.update();
    }

    private void patrolPosition() {
        if (inPosition) {
            if (rotatingLeft) {
                this.rotate(0.5f);
                if (rotationCounter++ == 540)
                    rotatingLeft = false;
            } else {
                this.rotate(-0.5f);
                if (rotationCounter-- == 0)
                    rotatingLeft = true;
            }
        }
    }

    private void returnToPost() {
        Vector2 velocity = this.getVelocity();
        Vector2 position = this.getPosition();
        Vector2 target = guardPosition.cpy().sub(position);
        if (target.len() < 5f && velocity.len() < 2f) {
            this.zeroVelocity();
            inPosition = true;
        } else {
            float maxVelocity = this.getMaxVelocity();
            Vector2 desiredVelocity = target.cpy().scl(maxVelocity / target.len());
            this.accelerate(desiredVelocity.sub(velocity));
        }
    }


    private void pursueTarget() {
        Vector2 targetPursuit = target.getPosition().sub(this.getPosition()).nor().scl(this.getMaxVelocity());
        this.accelerate(targetPursuit.sub(this.getVelocity()));
    }

    private boolean targetSighted() {
        Vector2 orientation = this.getOrientation();
        Vector2 targetOffset = target.getPosition().sub(this.getPosition());
        return ((orientation.angle(targetOffset) > -60 && orientation.angle(targetOffset) < 60) && targetOffset.len() < sightRange);
    }

    public void drawView(ShapeRenderer shapeRenderer) {
        Vector2 pos = this.getPosition();
        Sprite sprite = this.getSprite();
        float startAngle = this.getOrientation().angle() - 60;
        shapeRenderer.arc(pos.x + sprite.getWidth() / 2,
                pos.y + sprite.getHeight() / 2,
                sightRange,
                startAngle,
                120
        );
    }

    public void reinitialize() {
        super.reinitialize();
        state = State.GUARD;
        inPosition = true;
        rotatingLeft = true;
        rotationCounter = 0;
    }

}