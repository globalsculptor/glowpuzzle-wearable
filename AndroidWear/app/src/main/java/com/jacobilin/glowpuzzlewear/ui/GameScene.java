package com.jacobilin.glowpuzzlewear.ui;

import android.graphics.Canvas;

import com.jacobilin.glowpuzzle.Edge;
import com.jacobilin.glowpuzzle.Engine;
import com.jacobilin.glowpuzzle.Vertex;
import com.jacobilin.glowpuzzlewear.util.GameStatus;
import com.jacobilin.glowpuzzlewear.util.ResourcesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * GlowPuzzle Wear
 * Created by Jacob Ilin 23/11/14 15:48.
 * Copyright © 2008 - 2014 JacobIlin.com. All rights reserved.
 */
public class GameScene {

    public static final int SELECTED_NOTHING = -1;
    protected List<VertexGameObject> vertexes;
    protected List<EdgeGameObject> edges;
    protected int selectedVertexIndex = SELECTED_NOTHING;

    boolean hasSelected() {
        return selectedVertexIndex != SELECTED_NOTHING;
    }

    void enterPressed() {
        Engine engine = GameStatus.getInstance().getLogicEngine();

        if (engine.hasMistake()) {
            refresh();
            return;
        }

        if (selectedVertexIndex == SELECTED_NOTHING) {
            selectedVertexIndex = 0;
            return;
        }

        engine.press(vertexes.get(selectedVertexIndex).getVertex());

        if (engine.getMistakenEdge() != null) {
            EdgeGameObject edgeGameObject = new EdgeGameObject(engine.getMistakenEdge());
            connectEdgeAndVertexesGameObjects(edgeGameObject);
            edges.add(edgeGameObject);
        }

        updateAllGameObjects();
    }

    void updateAllGameObjects() {
        updateVertexGameObjects();
        updateEdgeGameObjects();
    }

    void updateVertexGameObjects() {
        for(VertexGameObject vertexGameObject : vertexes) {
            vertexGameObject.proceedSpitesWithModel();
        }
    }

    void updateEdgeGameObjects() {
        for(EdgeGameObject edgeGameObject : edges) {
            edgeGameObject.proceedSpitesWithModel();
        }
    }

    void moveSelectionLeft() {
        if (selectedVertexIndex == SELECTED_NOTHING) {
            selectedVertexIndex = 0;
        } else if (selectedVertexIndex == 0) {
            selectedVertexIndex = vertexes.size() - 1;
        } else {
            --selectedVertexIndex;
        }
    }

    void moveSelectionRight() {
        if (selectedVertexIndex == SELECTED_NOTHING) {
            selectedVertexIndex = 0;
        } else if (selectedVertexIndex == vertexes.size() - 1) {
            selectedVertexIndex = 0;
        } else {
            ++selectedVertexIndex;
        }
    }

    protected void createVertexes() {
        if (ResourcesManager.prepared()) {
            vertexes = new ArrayList<VertexGameObject>();

            for (Vertex v : GameStatus.getInstance().getLogicEngine().graph().vertexes()) {
                VertexGameObject vertexGameObject = new VertexGameObject(v);
                vertexes.add(vertexGameObject);
            }
        }
    }

    public void createEdgeGameObject(Edge edge) {
        if (ResourcesManager.prepared()) {
            EdgeGameObject edgeGameObject = new EdgeGameObject(edge);
            edges.add(edgeGameObject);
        }
    }


    protected void createEdges() {
        if (ResourcesManager.prepared()) {
            edges = new ArrayList<EdgeGameObject>();

            for (Edge e : GameStatus.getInstance().getLogicEngine().graph().edges()) {
                createEdgeGameObject(e);
            }
        }
    }

    protected void connectEdgeAndVertexesGameObjects(EdgeGameObject edgeGameObject) {
        for (VertexGameObject v : vertexes) {
            if (edgeGameObject.getEdge().firstVertex() == v.getVertex()) {
                edgeGameObject.firstVertexGameObject = v;
            }

            if (edgeGameObject.getEdge().lastVertex() == v.getVertex()) {
                edgeGameObject.lastVertexGameObject = v;
            }
        }
    }

    protected void connectEdgeAndVertexesGameObjects() {
        for(EdgeGameObject edgeGameObject : edges) {
            connectEdgeAndVertexesGameObjects(edgeGameObject);
        }
    }

    protected void clearGameObjects() {
        if (vertexes != null) {
            vertexes.clear();
        }

        if (edges != null) {
            edges.clear();
        }
    }

    public void refresh() {
        clearGameObjects();
        GameStatus.getInstance().loadCurrentLevel();

        createEdges();
        createVertexes();
        connectEdgeAndVertexesGameObjects();

        selectedVertexIndex = -1;
    }

    public void draw(Canvas canvas) {
        for (EdgeGameObject edgeGameObject : edges) {
            edgeGameObject.draw(canvas);
        }

        for(VertexGameObject vertexGameObject : vertexes) {
            vertexGameObject.draw(canvas);
        }

        if (selectedVertexIndex > SELECTED_NOTHING && selectedVertexIndex < vertexes.size()) {
            vertexes.get(selectedVertexIndex).drawCursor(canvas);
        }
    }

    public void select(Vertex vertex) {
        for(VertexGameObject vertexGameObject : vertexes) {
            if (vertex == vertexGameObject.getVertex()) {
                selectedVertexIndex = vertexes.indexOf(vertexGameObject);
                enterPressed();
            }
        }
    }
}
