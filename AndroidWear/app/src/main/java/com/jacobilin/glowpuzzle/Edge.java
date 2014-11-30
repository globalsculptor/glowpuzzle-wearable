package com.jacobilin.glowpuzzle;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:17 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public interface Edge extends Graph.Element {

    public Vertex firstVertex();

    public Vertex lastVertex();

    public float distance();

    public float angle();
}
