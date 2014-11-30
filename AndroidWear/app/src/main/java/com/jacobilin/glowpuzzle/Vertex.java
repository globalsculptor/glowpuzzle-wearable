package com.jacobilin.glowpuzzle;

import java.util.Collection;

/**
 * GlowPuzzle
 * Created by Jacob Ilin 5/18/13 9:17 PM
 * Copyright © 2008 - 2015 JacobIlin.com. All rights reserved.
 */

public interface Vertex extends Graph.Element {
    public void setEdges(Collection<Edge> edges);

    public Collection<Edge> edges();

    public float x();

    public float y();

    public void move(float x, float y);
    
    public float getOriginalX();
    
    public void setOriginalX(float originalX);
    
    public float getOriginalY();
    
    public void setOriginalY(float originalY);
}
