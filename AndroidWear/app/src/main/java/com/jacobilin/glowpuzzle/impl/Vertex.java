package com.jacobilin.glowpuzzle.impl;

import com.jacobilin.glowpuzzle.Edge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:29 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public class Vertex implements com.jacobilin.glowpuzzle.Vertex {

    private float x;
    private float y;
    private State state = State.Normal;
    private Set<Edge> edges;

    private float originalX;
    private float originalY;
    
    @Override
    public void changeState(State state) {
        this.state = state;
    }

    @Override
    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setEdges(Collection<Edge> edges) {
        this.edges = new HashSet<Edge>(edges);
    }

    @Override
    public Collection<Edge> edges() {
        return edges;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public State state() {
        return state;
    }

    @Override
	public float getOriginalX() {
		return originalX;
	}

    @Override
    public void setOriginalX(float originalX) {
		this.originalX = originalX;
	}

    @Override
    public float getOriginalY() {
		return originalY;
	}

    @Override
    public void setOriginalY(float originalY) {
		this.originalY = originalY;
	}
}
