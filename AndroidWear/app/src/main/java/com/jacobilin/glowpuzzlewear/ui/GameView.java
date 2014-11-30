package com.jacobilin.glowpuzzlewear.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.jacobilin.glowpuzzle.Vertex;
import com.jacobilin.glowpuzzlewear.util.GameStatus;
import com.jacobilin.glowpuzzlewear.util.ResourcesManager;

import java.util.Iterator;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 11/22/14 8:17 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */
public class GameView extends View {

    private static final int SOLUTION_NEXT_ITEM_DELAY_MILLIS = 500;
    private Iterator<Vertex> solutionIterator = null;
    private GameScene gameScene;
    private boolean solutionInProgress = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ResourcesManager.loadRecources(getContext());
    }

    public boolean isSolutionInProgress() {
        return solutionInProgress;
    }

    public boolean hasSelected() {
        return gameScene.hasSelected();
    }

    public void pressLeft() {
        gameScene.moveSelectionLeft();
    }

    public void pressRight() {
        gameScene.moveSelectionRight();
    }

    public void pressEnter() {
        gameScene.enterPressed();
    }

    public void createScene() {
        gameScene = new GameScene();
        gameScene.refresh();
    }

    public void restartGame() {
        gameScene.refresh();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (gameScene != null) {
            gameScene.draw(canvas);
        }
    }

    public void runSolution() {
        if (!solutionInProgress) {
            solutionInProgress = true;
            restartGame();

            solutionIterator = GameStatus.getInstance().getLogicEngine().graph().solution().iterator();
            nextSolutionItem();
        }
    }

    public void stopSolution() {
        solutionInProgress = false;
        restartGame();
        gameScene.selectedVertexIndex = 0;
    }

    private void nextSolutionItem() {
        gameScene.select(solutionIterator.next());
        postInvalidate();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (solutionInProgress) {
                    if (solutionIterator.hasNext()) {
                        nextSolutionItem();
                    } else {
                        restartGame();
                        solutionInProgress = false;
                    }
                }
            }
        }, SOLUTION_NEXT_ITEM_DELAY_MILLIS);
    }
}
