package com.jacobilin.glowpuzzle.impl;

import com.jacobilin.glowpuzzle.Vertex;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:24 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public class Edge implements com.jacobilin.glowpuzzle.Edge {

    private State state = State.Normal;
    private Vertex firstVertex;
    private Vertex lastVertex;

    public Edge(Vertex firstVertex, Vertex lastVertex) {
        this.firstVertex = firstVertex;
        this.lastVertex = lastVertex;
    }

    @Override
    public void changeState(State state) {
        this.state = state;
    }

    @Override
    public Vertex firstVertex() {
        return firstVertex;
    }

    @Override
    public Vertex lastVertex() {
        return lastVertex;
    }

    static float distance(Vertex firstVertex, Vertex lastVertex) {
        return (float) Math.sqrt(Math.pow((lastVertex.x() - firstVertex.x()), 2) + Math.pow((lastVertex.y() - firstVertex.y()), 2));
    }

    @Override
    public float distance() {
        return distance(firstVertex, lastVertex);
    }

    static float angle(Vertex v1, Vertex v2) {
        if (v2.x() >= v1.x()) {
            float len = distance(v1, v2);
            float dy = v1.y() - v2.y();
            float angleInRadians = v2.y() > v1.y() ? (float) Math.asin(Math.abs(dy) / len) : -(float) Math.asin(Math.abs(dy) / len);

            return angleInRadians * (180.0f / (float) Math.PI);
        } else
            return angle(v2, v1) - 180f;
    }

    @Override
    public float angle() {
        return angle(firstVertex, lastVertex);
    }

    @Override
    public State state() {
        return state;
    }
}
