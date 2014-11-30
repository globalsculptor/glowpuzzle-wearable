package com.jacobilin.glowpuzzle.impl;

import android.graphics.Point;

import com.jacobilin.glowpuzzle.Edge;
import com.jacobilin.glowpuzzle.Vertex;

import java.util.*;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:40 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public class Graph implements com.jacobilin.glowpuzzle.Graph {

    private Set<Vertex> vertexes;
    private Set<com.jacobilin.glowpuzzle.Edge> edges;
    private List<com.jacobilin.glowpuzzle.Vertex> solution;
    private List<Point> solutionPath;

    public Graph(Collection<Vertex> vertexes, Collection<Edge> edges, Collection<Vertex> solution, 
    		Collection<Point> solutionPath) {
        this.vertexes = new HashSet<Vertex>(vertexes);
        this.edges = new HashSet<Edge>(edges);
        
        if (solution != null) {
        	this.solution = new ArrayList<Vertex>(solution);
        }
        
        if (solutionPath != null) {
        	this.solutionPath = new LinkedList<Point>(solutionPath);
        }
    }

    @Override
    public Collection<Vertex> vertexes() {
        return vertexes;
    }

    @Override
    public Collection<Edge> edges() {
        return edges;
    }

    @Override
    public Collection<Vertex> solution() {
        return solution;
    }
    
    @Override
    public Collection<Point> solutionPath() {
        return solutionPath;
    }
}
