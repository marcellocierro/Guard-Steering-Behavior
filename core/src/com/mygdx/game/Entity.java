package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity {
    private float maxVelocity;
    private Vector2 position, initialPosition;
    private Vector2 orientation, initialOrientation;
    private Vector2 velocity;
    private Sprite sprite;
    private float maxAcceleration = 0.05f, previousAngle = 0;


    public Entity(Vector2 p, Vector2 o, float maxV) {
        position = p.cpy();
        initialPosition = p.cpy();
        orientation = o.nor().cpy();
        initialOrientation = orientation.cpy();
        velocity = new Vector2(0f,0f);
        maxVelocity = maxV;
    }

    public void reinitialize() {
        position = initialPosition.cpy();
        orientation = initialOrientation.cpy();
        sprite.setRotation(orientation.angle() - 90);
        zeroVelocity();
    }

    protected void setSprite(Sprite s) {
        sprite = s;
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setPosition(position.x,position.y);
        sprite.rotate(orientation.angle() - 90);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getOrientation() {
        return orientation.cpy();
    }


    public void rotate(int direction) {
        previousAngle = orientation.angle();
        if (direction == 0)
            return;
        float degrees;
        if (direction < 0)
            degrees = -3f;
        else
            degrees = 3f;
        orientation.rotate(degrees);
        sprite.rotate(degrees);
    }

    public void rotate(float degrees) {
        previousAngle = orientation.angle();
        orientation.rotate(degrees);
        sprite.rotate(degrees);
    }

    protected void setRotation(float degrees) {
        previousAngle = orientation.angle();
        orientation.setAngle(degrees);
        sprite.setRotation(degrees - 90);
    }

    public Vector2 getPosition() {
        return position.cpy();
    }


    protected void zeroVelocity() {
        velocity.setZero();
    }

    public float getMaxVelocity(){
        return maxVelocity;
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public void reverseVelocityX() {
        velocity.set(-velocity.x, velocity.y);
    }
    public void reverseVelocityY(){
        velocity.set(velocity.x, -velocity.y);
    }

    public void accelerate() {
        velocity.add(orientation.scl(maxAcceleration));
        orientation.nor();
        if(velocity.len() > maxVelocity)
            velocity.setLength(maxVelocity);
    }

    public void accelerate(Vector2 acceleration) {
        if (acceleration.len() > maxAcceleration)
            acceleration.setLength(maxAcceleration);
        velocity.add(acceleration);
        orientation.setAngle(velocity.angle());
        sprite.setRotation(orientation.angle()-90);
        if (velocity.len() > maxVelocity)
            velocity.setLength(maxVelocity);
    }

    public void decelerate() {
        float initialV = velocity.len();
        velocity.sub(velocity.cpy().nor().scl(maxAcceleration));
        orientation.nor();
        if (initialV < velocity.len())
            velocity.setLength(0f);
    }


    public void update() {
        position.add(velocity);
        sprite.setPosition(position.x, position.y);
    }

    public Rectangle getBounds(){
        return new Rectangle(position.x, position.y, (sprite.getWidth() /5) * 4,  (sprite.getHeight()/5) * 4);

    }
}