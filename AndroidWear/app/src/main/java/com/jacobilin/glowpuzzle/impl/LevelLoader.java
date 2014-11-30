package com.jacobilin.glowpuzzle.impl;

import android.content.Context;
import android.graphics.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:52 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public class LevelLoader {

    private List<String> lines;
    private List<com.jacobilin.glowpuzzle.Vertex> cachedVertexes;
    private List<com.jacobilin.glowpuzzle.Edge> cachedEdges;
    private List<com.jacobilin.glowpuzzle.Vertex> cachedSolution;
    private List<Point> cachedSolutionPath;

    private float scaleX;
    private float scaleY;

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    private Vertex parseVertex(String[] fields) {
        Vertex vertex = null;

        if (fields != null && fields.length == 3) {
            try {
                float originalX = Float.parseFloat(fields[1]);
                float originalY = Float.parseFloat(fields[2]);

                vertex = new Vertex();
                vertex.setOriginalX(originalX);
                vertex.setOriginalY(originalY);
                vertex.move(originalX * scaleX, originalY * scaleY);
            } catch (NumberFormatException e) {
                // mistake, parse error, a-a-a-a-a... impossible...
                vertex = null;
            }
        }

        return vertex;
    }

    private Vertex processVertex(String[] fields) {
        Vertex vertex = parseVertex(fields);
        if (vertex != null) {
            cachedVertexes.add(vertex);
        }

        return vertex;
    }


    private Edge processEdge(String[] fields) {
        Edge edge = null;

        try {
            int index0 = Integer.parseInt(fields[0]) - 1;
            int index1 = Integer.parseInt(fields[1]) - 1;

            if (index0 > -1 && index0 < cachedVertexes.size() && index1 > -1 && index1 < cachedVertexes.size()) {
                edge = new Edge(cachedVertexes.get(index0), cachedVertexes.get(index1));
                cachedEdges.add(edge);
            }
        } catch (NumberFormatException e) {
            // mistake, parse error, a-a-a-a-a... impossible...
            edge = null;
        }

        return edge;
    }

    private boolean processSolution(String line) {

        String[] fields = line.replace("\r", "").split(" ");

        if (fields.length > 0) {
            try {
                for (String field : fields) {

                    int index = Integer.parseInt(field) - 1;

                    if (index > -1 && index < cachedVertexes.size()) {
                        cachedSolution.add(cachedVertexes.get(index));
                    }

                }
            } catch (NumberFormatException e) {
                // mistake, parse error, a-a-a-a-a... impossible...
            }
        }

        return false;
    }

    private Graph processLines() {
        Graph graph = null;

        if (lines != null && lines.size() > 0) {

            cachedVertexes = new LinkedList<com.jacobilin.glowpuzzle.Vertex>();
            cachedEdges = new LinkedList<com.jacobilin.glowpuzzle.Edge>();
            cachedSolution = new LinkedList<com.jacobilin.glowpuzzle.Vertex>();
            cachedSolutionPath = new LinkedList<Point>();

            for (String line : lines) {
                if (lines.indexOf(line) == 0) {
                    // do nothing, the line reserved
                    continue;
                } else if (lines.indexOf(line) == lines.size() - 1) {
                    processSolution(line);
                } else {
                    String[] fields = line.replace("\r", "").split(" ");
                    if (fields.length == 3) {
                        processVertex(fields);
                    } else if (fields.length == 2) {
                        processEdge(fields);
                    } else if (fields.length > 3) {
                    	processSolution(line);
                    }
                }
            }

            findAllEdgesForEveryVertex();

            graph = new Graph(cachedVertexes, cachedEdges, cachedSolution, cachedSolutionPath);
        }

        return graph;
    }

    private void findAllEdgesForEveryVertex() {
        for (com.jacobilin.glowpuzzle.Vertex vertex : cachedVertexes) {
            Collection<com.jacobilin.glowpuzzle.Edge> es = new LinkedList<com.jacobilin.glowpuzzle.Edge>();

            for (com.jacobilin.glowpuzzle.Edge edge : cachedEdges) {
                if (edge.firstVertex() == vertex || edge.lastVertex() == vertex) {
                    es.add(edge);
                }
            }

            vertex.setEdges(es);
        }
    }

    public Graph load(Context context, int index) {

        Graph graph = null;

        try {
        	String assetsFileName = "levels/level"+ index + ".txt";
        	
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(assetsFileName), "UTF-8"));

            lines = new LinkedList<String>();
            // do reading, usually loop until end of file reading
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            reader.close();

            graph = processLines();
        } catch (IOException e) {
            //log the exception
            e.printStackTrace();
        }

        return graph;
    }
}
