package com.jacobilin.glowpuzzlewear.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jacobilin.glowpuzzle.impl.Engine;
/**
 * GlowPuzzle Wear
 * Created by Jacob Ilin 23/11/14 17:45.
 * Copyright © 2008 - 2014 JacobIlin.com. All rights reserved.
 */
public class GameStatus {

    public static final int EDGE_WIDTH = 10;
    public static final int VERTEX_RADIUS = 20;
    private static GameStatus ourInstance = new GameStatus();

    public static GameStatus getInstance() {
        return ourInstance;
    }

    private GameStatus() {
    }

    public int currentLevel = 1;
    public int openedLevels = 1;
    public int totalLevels = 10;
    public boolean soundOn = true;

    private int gameWidth;
    private int gameHeight;
    private float edgeWidth;
    private float vertexRadius;
    private Engine logicEngine = null;
    private float scaleX = 1f;
    private float scaleY = 1f;
    private float puzzleSeekX = 0;
    private float puzzleSeekY = 0;
    private int launchCounter = 0;
    private Context applicationContext;

    public void initialize(final int width, final int height) {
        if (applicationContext != null) {
            gameWidth = width;
            gameHeight = height;
            edgeWidth = EDGE_WIDTH;
            vertexRadius = VERTEX_RADIUS;

            scaleX = (float) gameWidth / 320f;
            scaleY = (float) gameHeight / 480f;

            scaleX = scaleY;

            edgeWidth *= scaleX;
            vertexRadius *= scaleX;

            loadPreferences();
            ++launchCounter;
            saveLaunchCounter();

            logicEngine = new Engine();
        }
    }

    public void loadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        launchCounter = settings.getInt("launchCounter", 1);
        currentLevel = settings.getInt("currentLevel", 1);
        openedLevels = settings.getInt("openLevel", 1);
        soundOn = settings.getBoolean("soundOn", true);
    }

    private void saveLaunchCounter() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("launchCounter", launchCounter);
        editor.commit();
    }

    public void savePreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentLevel", currentLevel);
        editor.putInt("openLevel", openedLevels);
        editor.putBoolean("soundOn", soundOn);
        editor.commit();
    }

    public void load(int level) {
        if (logicEngine != null) {
            logicEngine.load(applicationContext, level, puzzleSeekX, puzzleSeekY, scaleX);
        }
    }

    public void loadCurrentLevel() {
        load(currentLevel);
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(final Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setLogicEngine(final Engine engine) {
        this.logicEngine = engine;
    }

    public Engine getLogicEngine() {
        return logicEngine;
    }

    public float getVertexRadius() {
        return vertexRadius;
    }

    public float getEdgeWidth() {
        return edgeWidth;
    }
}
