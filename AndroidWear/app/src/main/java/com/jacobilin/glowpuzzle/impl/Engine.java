package com.jacobilin.glowpuzzle.impl;

import android.content.Context;

import com.jacobilin.glowpuzzle.Edge;
import com.jacobilin.glowpuzzle.Graph;
import com.jacobilin.glowpuzzle.Vertex;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:41 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public class Engine implements com.jacobilin.glowpuzzle.Engine {

    protected Graph graph;
    private Vertex firstVertex = null;
    private Vertex lastVertex = null;
    private Edge mistakenEdge = null;

    @Override
    public void applySeek(float seekX, float seekY) {
        if (seekX == 0f && seekY == 0f) {
            return;
        }

        for (Vertex vertex : graph.vertexes()) {
            vertex.move(seekX + vertex.x(), seekY + vertex.y());
        }
    }

    @Override
    public void applyScale(float scale) {
        if (scale == 1f) {
            return;
        }

        for (Vertex vertex : graph.vertexes()) {
            vertex.move(scale * vertex.x(), scale * vertex.y());
        }
    }

    @Override
    public Graph graph() {
        return graph;
    }

    @Override
    public boolean press(Vertex vertex) {
        if (hasMistake() || won()) {
            return false;
        }

        if (firstVertex == null) {
            mistakenEdge = null;
            firstVertex = vertex;
            firstVertex.changeState(Graph.Element.State.Selected);
            return true;
        } else if (firstVertex != vertex) {
            lastVertex = vertex;
            lastVertex.changeState(Graph.Element.State.Selected);
            firstVertex.changeState(Graph.Element.State.Normal);

            for (Edge edge : graph.edges()) {
                if (edge.firstVertex() == firstVertex && edge.lastVertex() == lastVertex ||
                        edge.firstVertex() == lastVertex && edge.lastVertex() == firstVertex) {
                    if (edge.state() == Graph.Element.State.Completed) {
                        edge.changeState(Graph.Element.State.Mistaken);
                        firstVertex.changeState(Graph.Element.State.Mistaken);
                        lastVertex.changeState(Graph.Element.State.Mistaken);
                        return true;
                    } else if (edge.state() == Graph.Element.State.Normal) {
                        edge.changeState(Graph.Element.State.Completed);
                        firstVertex = lastVertex;
                        lastVertex = null;
                        return true;
                    }
                }
            }

            mistakenEdge = new com.jacobilin.glowpuzzle.impl.Edge(firstVertex, lastVertex);
            mistakenEdge.changeState(Graph.Element.State.Mistaken);
            firstVertex.changeState(Graph.Element.State.Mistaken);
            lastVertex.changeState(Graph.Element.State.Mistaken);
            return true;
        }

        return false;
    }

    @Override
    public boolean load(Context context, int index, float puzzleSeekX, float puzzleSeekY, float puzzleScale) {
        firstVertex = lastVertex = null;
        mistakenEdge = null;
        LevelLoader levelLoader = new LevelLoader();
        levelLoader.setScaleX(puzzleScale);
        levelLoader.setScaleY(puzzleScale);
        graph = levelLoader.load(context, index);
        if (graph != null) {
            applySeek(puzzleSeekX, puzzleSeekY);
            applyScale(puzzleScale);
        }

        return graph != null;
    }

    @Override
    public boolean hasMistake() {
        for (Vertex vertex : graph.vertexes()) {
            if (vertex.state() == Graph.Element.State.Mistaken) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean won() {
        for (Edge edge : graph.edges()) {
            if (edge.state() == Graph.Element.State.Mistaken ||
                    edge.state() == Graph.Element.State.Normal) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Edge getMistakenEdge() {
        return mistakenEdge;
    }
}
