package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {

    public Player(Vector2 p, Vector2 o) {
        super(p, o, 20f);
        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("Sprites/Player.png")));
        sprite.setScale(0.5f);
        super.setSprite(sprite);
    }
}