package com.jacobilin.glowpuzzlewear.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jacobilin.glowpuzzlewear.R;

import java.util.HashMap;
import java.util.Map;

/**
 * GlowPuzzle
 * Created by Jacob Ilin on 11/23/14 1:23 AM.
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */
public class ResourcesManager {

    public static final String DOT = "dot";
    public static final String DOT_COMPLETED = "dotCompleted";
    public static final String DOT_MISTAKEN = "dotMistaken";
    public static final String VERTEX = "vertex";
    public static final String VERTEX_SELECTED = "vertexSelected";
    public static final String VERTEX_MISTAKEN = "vertexMistaken";

    static protected Bitmap dot = null;
    static protected Bitmap dotCompleted = null;
    static protected Bitmap dotMistaken = null;
    static protected Bitmap vertex = null;
    static protected Bitmap vertexSelected = null;
    static protected Bitmap vertexMistaken = null;

    private static Map<String, Bitmap> bitmapMap = null;

    public static void loadRecources(Context context) {
        dot = BitmapFactory.decodeResource(context.getResources(), R.drawable.dotnormal);
        dotCompleted = BitmapFactory.decodeResource(context.getResources(), R.drawable.dotcompleted);
        dotMistaken = BitmapFactory.decodeResource(context.getResources(), R.drawable.dotmistaken);
        vertex = BitmapFactory.decodeResource(context.getResources(), R.drawable.vertex_green);
        vertexSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.vertex_selected);
        vertexMistaken = BitmapFactory.decodeResource(context.getResources(), R.drawable.vertex_red);
    }

    private static void prepareBitmapMap() {
        if (prepared()) {
            bitmapMap = new HashMap<String, Bitmap>();
            bitmapMap.put(DOT, dot);
            bitmapMap.put(DOT_COMPLETED, dotCompleted);
            bitmapMap.put(DOT_MISTAKEN, dotMistaken);
            bitmapMap.put(VERTEX, vertex);
            bitmapMap.put(VERTEX_SELECTED, vertexSelected);
            bitmapMap.put(VERTEX_MISTAKEN, vertexMistaken);
        }
    }

    public static boolean prepared() {
        return dot != null;
    }

    public static Bitmap getBitmap(String key) {
        if (bitmapMap == null) {
            prepareBitmapMap();
        }

        return bitmapMap != null && bitmapMap.containsKey(key) ? bitmapMap.get(key) : null;
    }
}
