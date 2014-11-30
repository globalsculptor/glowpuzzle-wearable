package com.jacobilin.glowpuzzlewear.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jacobilin.glowpuzzle.Edge;
import com.jacobilin.glowpuzzle.Graph;
import com.jacobilin.glowpuzzlewear.util.GameStatus;
import com.jacobilin.glowpuzzlewear.util.ResourcesManager;

/**
 * GlowPuzzle Wear
 * Created by Jacob Ilin 23/11/14 15:45.
 * Copyright © 2008 - 2014 JacobIlin.com. All rights reserved.
 */
public class EdgeGameObject extends GameObject {

    private static final float DEFAULT_EDGE_ALPHA = 0.5f;
    public static final int MISTAKEN_EDGE_ALPHA = 200;
    public static final int COMPLETED_EDGE_ALPHA = 240;
    public VertexGameObject firstVertexGameObject;
    public VertexGameObject lastVertexGameObject;

    private Edge edge;
    private Paint paint;

    private float alpha = DEFAULT_EDGE_ALPHA;
    private int alpha255 = (int)(alpha * 255f);
    private int alpha255Mistaken = MISTAKEN_EDGE_ALPHA;
    private int alpha255Completed = COMPLETED_EDGE_ALPHA;

    public EdgeGameObject(Edge edge) {
        if (ResourcesManager.prepared()) {
            this.edge = edge;
            proceedSpitesWithModel();
        }
    }

    public void proceedSpitesWithModel() {
        if (firstVertexGameObject != null) {
            firstVertexGameObject.proceedSpitesWithModel(edge.state());
        }

        if (lastVertexGameObject != null) {
            lastVertexGameObject.proceedSpitesWithModel(edge.state());
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.alpha255 = (int)(alpha * 255f);
    }

    public Edge getEdge() {
        return edge;
    }

    @Override
    public boolean spritesReady() {
        return true;
    }

    public void draw(Canvas canvas) {

        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(GameStatus.getInstance().getEdgeWidth());
            paint.setAlpha(100);
        }

        paint.setColor(edge.state() == Graph.Element.State.Completed ? Color.BLUE :
                edge.state() == Graph.Element.State.Mistaken ? Color.RED :
                        Color.GREEN);
        paint.setAlpha(edge.state() == Graph.Element.State.Completed ? alpha255Completed :
                edge.state() == Graph.Element.State.Mistaken ? alpha255Mistaken : alpha255);

        canvas.drawLine(firstVertexGameObject.getVertex().x(), firstVertexGameObject.getVertex().y(),
                lastVertexGameObject.getVertex().x(), lastVertexGameObject.getVertex().y(), paint);
    }
}

