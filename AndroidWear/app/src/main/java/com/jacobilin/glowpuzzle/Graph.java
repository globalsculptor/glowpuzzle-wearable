package com.jacobilin.glowpuzzle;

import java.util.Collection;

import android.graphics.Point;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:18 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public interface Graph {

    public Collection<Vertex> vertexes();

    public Collection<Edge> edges();

    public Collection<Vertex> solution();
    
    public Collection<Point> solutionPath();

    public interface Element {

        public enum State {
            Normal,
            Selected,
            Mistaken,
            Completed
        }

        public State state();

        public void changeState(State state);
    }
}
