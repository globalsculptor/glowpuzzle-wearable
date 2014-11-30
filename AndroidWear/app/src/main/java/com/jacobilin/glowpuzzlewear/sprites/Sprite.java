package com.jacobilin.glowpuzzlewear.sprites;

import android.graphics.Bitmap;

/**
 * GlowPuzzle
 * Created by Jacob Ilin on 11/22/14 10:23 PM.
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */
public class Sprite {

    private float x;
    private float y;
    private boolean visible;
    private float alpha;
    private Bitmap bitmap;

    public float getX() {
        return x;
    }

    public void setX(final float x) {
        this.x = x;
    }


    public float getY() {
        return y;
    }

    public void setY(final float y) {
        this.y = y;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
