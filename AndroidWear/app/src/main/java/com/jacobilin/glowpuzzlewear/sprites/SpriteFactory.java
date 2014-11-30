package com.jacobilin.glowpuzzlewear.sprites;

import android.graphics.Bitmap;

import com.jacobilin.glowpuzzlewear.util.ResourcesManager;

/**
 * GlowPuzzle Wear
 * Created by Jacob Ilin 23/11/14 16:48.
 * Copyright © 2008 - 2014 JacobIlin.com. All rights reserved.
 */
public class SpriteFactory {

    public static Sprite createDot(float x, float y) {
        return createSprite(x, y, ResourcesManager.DOT);
    }

    public static Sprite createDotCompleted(float x, float y) {
        return createSprite(x, y, ResourcesManager.DOT_COMPLETED);
    }

    public static Sprite createDotMistaken(float x, float y) {
        return createSprite(x, y, ResourcesManager.DOT_MISTAKEN);
    }

    public static Sprite createVertex(float x, float y) {
        return createSprite(x, y, ResourcesManager.VERTEX);
    }

    public static Sprite createVertexSelected(float x, float y) {
        return createSprite(x, y, ResourcesManager.VERTEX_SELECTED);
    }

    public static Sprite createVertexMistaken(float x, float y) {
        return createSprite(x, y, ResourcesManager.VERTEX_MISTAKEN);
    }

    private static Sprite createSprite(float x, float y, String key) {
        Sprite sprite = null;
        Bitmap bitmap = ResourcesManager.getBitmap(key);
        if (bitmap != null) {
            sprite = new Sprite();
            sprite.setBitmap(bitmap);
            sprite.setX(x);
            sprite.setY(y);
        }
        return sprite;
    }
}
