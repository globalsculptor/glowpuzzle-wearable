package com.jacobilin.glowpuzzlewear.ui;

/**
 * © 2014 Jacob Ilin
 * © 2008 - 2015 JacobIlin.com. All rights reserved.
 *
 * @author Jacob Ilin
 * @since 1:14 AM - 11/22/14
 */

public abstract class GameObject {

    private boolean touchEnabled = true;
    private Object tag;

    public boolean getTouchEnabled() {
        return touchEnabled;
    }

    public void setTouchEnabled(boolean enabled) {
        this.touchEnabled = enabled;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public abstract boolean spritesReady();
}
