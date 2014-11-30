package com.jacobilin.glowpuzzle;

import android.content.Context;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:16 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public interface Engine {

    public void applyScale(float scale);

    public void applySeek(float seekX, float seekY);

    public Graph graph();

    public boolean press(Vertex vertex);

    public boolean load(Context context, int index, float puzzleSeekX, float puzzleSeekY, float puzzleScale);

    public boolean hasMistake();

    public boolean won();

    public Edge getMistakenEdge();
}
