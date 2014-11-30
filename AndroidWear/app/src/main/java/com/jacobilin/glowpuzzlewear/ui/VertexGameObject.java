package com.jacobilin.glowpuzzlewear.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jacobilin.glowpuzzle.Engine;
import com.jacobilin.glowpuzzle.Graph;
import com.jacobilin.glowpuzzle.Vertex;
import com.jacobilin.glowpuzzlewear.sprites.Sprite;
import com.jacobilin.glowpuzzlewear.sprites.SpriteFactory;
import com.jacobilin.glowpuzzlewear.util.GameStatus;
import com.jacobilin.glowpuzzlewear.util.ResourcesManager;

/**
 * �� 2014 Jacob Ilin
 * �� 2008 - 2015 JacobIlin.com. All rights reserved.
 *
 * @author Jacob Ilin
 * @since 10:18 PM - 11/22/14
 */

public class VertexGameObject extends GameObject {

    private Vertex vertex;
    private Sprite vertexSprite;
    private Sprite vertexSpriteSelected;
    private Sprite vertexSpriteMistaken;
    private Sprite dotSprite;
    private Sprite dotSpriteCompleted;
    private Sprite dotSpriteMistaken;
    private float alpha;

    public VertexGameObject(Vertex vertex) {
        if (ResourcesManager.prepared()) {
            this.vertex = vertex;

            this.dotSprite = SpriteFactory.createDot(vertex.x(), vertex.y());
            this.dotSpriteCompleted = SpriteFactory.createDotCompleted(vertex.x(), vertex.y());
            this.dotSpriteMistaken = SpriteFactory.createDotMistaken(vertex.x(), vertex.y());

            this.vertexSprite = SpriteFactory.createVertex(vertex.x(), vertex.y());
            this.vertexSpriteSelected = SpriteFactory.createVertexSelected(vertex.x(), vertex.y());
            this.vertexSpriteMistaken = SpriteFactory.createVertexMistaken(vertex.x(), vertex.y());

            proceedSpitesWithModel();
        }
    }

    private static void moveSprite(Sprite sprite, float x, float y) {
        sprite.setX(sprite.getX() + x);
        sprite.setY(sprite.getY() + y);
    }

    public void move(float x, float y) {
        moveSprite(dotSprite, x, y);
        moveSprite(dotSpriteCompleted, x, y);
        moveSprite(dotSpriteMistaken, x, y);

        moveSprite(vertexSprite, x, y);
        moveSprite(vertexSpriteSelected, x, y);
        moveSprite(vertexSpriteMistaken, x, y);
    }

    public void proceedSpitesWithModel(Graph.Element.State state) {
        dotSprite.setVisible(state == Graph.Element.State.Normal || state == Graph.Element.State.Selected);
        dotSpriteCompleted.setVisible(state == Graph.Element.State.Completed);
        dotSpriteMistaken.setVisible(state == Graph.Element.State.Mistaken);
    }

    public void proceedSpitesWithModel() {
        vertexSprite.setVisible(vertex.state() == Graph.Element.State.Normal);
        vertexSpriteSelected.setVisible(vertex.state() == Graph.Element.State.Selected);
        vertexSpriteMistaken.setVisible(vertex.state() == Graph.Element.State.Mistaken);

        proceedSpitesWithModel(vertex.state());
    }

    public void touched(final Engine engine) {
        if (getTouchEnabled()) {
            if (engine.press(vertex)) {
                if (engine.getMistakenEdge() != null) {
                    // TODO: create mistaken edge
                    //ResourcesManager.getInstance().getActivity().getGameScene().createEdgeGameObject(engine.getMistakenEdge());
                }

                // TODO: proceedAllSpritesWithModel
                //ResourcesManager.getInstance().getActivity().getGameScene().proceedAllSpritesWithModel();
            }
        }
    }

    public Sprite getDotSprite() {
        return dotSprite;
    }

    public Sprite getDotSpriteCompleted() {
        return dotSpriteCompleted;
    }

    public Sprite getDotSpriteMistaken() {
        return dotSpriteMistaken;
    }

    public Sprite getVertexSprite() {
        return vertexSprite;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;

        if (vertexSprite != null) {
            vertexSprite.setAlpha(alpha);
            getVertexSpriteSelected().setAlpha(alpha);
            getVertexSpriteMistaken().setAlpha(alpha);
        }
    }

    @Override
    public boolean spritesReady() {
        return vertexSprite != null;
    }

    public Sprite getVertexSpriteSelected() {
        return vertexSpriteSelected;
    }

    public Sprite getVertexSpriteMistaken() {
        return vertexSpriteMistaken;
    }

    public void draw(Canvas canvas) {
        if (dotSprite.isVisible()) {
            drawBitmap(canvas, dotSprite.getBitmap());
        }

        if (vertexSprite.isVisible()) {
            drawBitmap(canvas, vertexSprite.getBitmap());
        }

        if (dotSpriteMistaken.isVisible()) {
            drawBitmap(canvas, dotSpriteMistaken.getBitmap());
        }

        if (vertexSpriteMistaken.isVisible()) {
            drawBitmap(canvas, vertexSpriteMistaken.getBitmap());
        }

        if (dotSpriteCompleted.isVisible()) {
            drawBitmap(canvas, dotSpriteCompleted.getBitmap());
        }

        if (vertexSpriteSelected.isVisible()) {
            drawBitmap(canvas, vertexSpriteSelected.getBitmap());
        }
    }

    private void drawBitmap(final Canvas canvas, final Bitmap bitmap) {
        final int radius = (int)GameStatus.getInstance().getVertexRadius();
        int left = (int)vertex.x() - radius;
        int top = (int)vertex.y() - radius;
        int right = (int)vertex.x() + radius;
        int bottom = (int)vertex.y() + radius;
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(left, top, right, bottom), null);
    }

    void drawCursor(final Canvas canvas) {
        final int radius = (int)GameStatus.getInstance().getVertexRadius();
        int left = (int)vertex.x() - radius - 4;
        int top = (int)vertex.y() - radius - 4;
        int right = (int)vertex.x() + radius + 4;
        int bottom = (int)vertex.y() + radius + 4;
        final Bitmap bitmap = vertexSpriteSelected.getBitmap();
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(left, top, right, bottom), null);
    }
}

