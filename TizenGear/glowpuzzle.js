function Enum(constantsList) {
    for (var i in constantsList) {
        this[constantsList[i]] = i;
    }
}

var State = new Enum(['Normal', 'Selected', 'Mistaken', 'Completed']);

var GraphElement = klass({
    state: State.Normal,
    initialize: function() {
        this.state = State.Normal
    },
    getState: function() {
        return this.state
    },
    changeState: function(s) {
        this.state = s
        return this.state
    }
})

var Vertex = GraphElement.extend({
    edges: [],
    x: 0,
    y: 0,
    originalX: 0,
    originalY: 0,
    move: function(x, y) {
        this.x = x;
        this.y = y;
    }
})

var Edge = GraphElement.extend({
    firstVertex: new Vertex(),
    lastVertex: new Vertex(),
    initialize: function() {
    },
    distance: function() {
        return this.calcDistance(this.firstVertex, this.lastVertex);
    },
    calcDistance: function(firstVertex, lastVertex) {
        return Math.sqrt(Math.pow((lastVertex.x - firstVertex.x), 2) + Math.pow((lastVertex.y - firstVertex.y), 2));
    },
    angle: function() {  
        return this.calcAngle(this.firstVertex, this.lastVertex);
    },
    calcAngle: function(v1, v2) {
        if (v2.x >= v1.x) {
            var len = this.calcDistance(v1, v2);
            var dy = v1.y - v2.y;
            var angleInRadians = v2.y > v1.y ? Math.asin(Math.abs(dy) / len) : -1 * Math.asin(Math.abs(dy) / len);

            return angleInRadians * (180 / Math.PI);
        } else
            return this.calcAngle(v2, v1) - 180;
    }
})

var Point = klass({
    x: 0,
    y: 0
})

var Graph = klass({
    vertexes: [],
    edges: [],
    solution: [],
    solutionPath: []
})

var LevelLoader = klass({
    lines: [],
    cachedVertexes: [],
    cachedEdges: [],
    cachedSolution: [],
    cachedSolutionPath: [],
    
    parseVertex: function(fields) {
        var vertex = null;

        if (fields != null && fields.length == 3) {
            var originalX = parseFloat(fields[1]);
            var originalY = parseFloat(fields[2]);

            vertex = new Vertex();
            vertex.originalX = originalX;
            vertex.originalY = originalY;
            var scale = 1.0;
            vertex.move(originalX*scale, originalY*scale);
        }

        return vertex;
    },
    
    processVertex: function(fields) {
        var vertex = this.parseVertex(fields);
        if (vertex != null) {
            this.cachedVertexes.push(vertex);
        }

        return vertex;
    },
    
    processEdge: function(fields) {
        var edge = null;

        var index0 = parseInt(fields[0]) - 1;
        var index1 = parseInt(fields[1]) - 1;

        if (index0 > -1 && index0 < this.cachedVertexes.length && index1 > -1 && index1 < this.cachedVertexes.length) {
            edge = new Edge();
            edge.firstVertex = this.cachedVertexes[index0];
            edge.lastVertex = this.cachedVertexes[index1];
            this.cachedEdges.push(edge);
        }

        return edge;
    },
    
    processSolution: function(line) {
        var fields = line.replace("\r", "").split(' ');

        if (fields.length > 0) {
            for (i in fields) {
                var field = fields[i];
                var index = parseInt(field) - 1;
                
                if (index > -1 && index < this.cachedVertexes.length) {
                    this.cachedSolution.push(this.cachedVertexes[index]);
                }
            }
        }
    },
    
    findAllEdgesForEveryVertex: function() {
        for (i in this.cachedVertexes) {
            var vertex = this.cachedVertexes[i];
            var es = [];

            for (j in this.cachedEdges) {
                var edge = this.cachedEdges[j];
                if (edge.firstVertex == vertex || edge.lastVertex == vertex) {
                    es.push(edge);
                }
            }

            vertex.edges = es;
        }
    },
    
    processLines: function() {
        var graph = null;

        if (this.lines != null && this.lines.length > 0) {

            this.cachedVertexes = [];
            this.cachedEdges = [];
            this.cachedSolution = [];
            this.cachedSolutionPath = [];

            for (index in this.lines) {
                var line = this.lines[index];
                if (this.lines.indexOf(line) == 0) {
                    // do nothing, the line reserved
                    continue;
                } else if (this.lines.indexOf(line) == this.lines.length - 1) {
                    this.processSolution(line);
                } else {
                    var fields = line.replace("\r", "").split(' ');
                    if (fields.length == 3) {
                        this.processVertex(fields);
                    } else if (fields.length == 2) {
                        this.processEdge(fields);
                    } else if (fields.length > 3) {
                    	this.processSolution(line);
                    }
                }
            }

            this.findAllEdgesForEveryVertex();

            graph = new Graph();
            graph.vertexes = this.cachedVertexes;
            graph.edges = this.cachedEdges;
            graph.solution = this.cachedSolution;
            graph.solutionPath = this.cachedSolutionPath;
        }

        return  graph;
    },
    
    load: function(text) {
        this.lines = text.split('\n');
        return this.processLines();
    }
})

var Engine = klass({
    graph: new Graph(),
    firstVertex: null,
    lastVertex: null,
    mistakenEdge: null,
    
    applySeek: function(seekX, seekY) {
        if (seekX == 0 && seekY == 0) {
            return;
        }

        for (index in this.graph.vertexes) {
            var vertex = this.graph.vertexes[index];
            vertex.move(seekX + vertex.x, seekY + vertex.y);
        }  
    },
    
    applyScale: function(scale) {
        if (scale == 1) {
            return;
        }

        for (index in this.graph.vertexes) {
            var vertex = this.graph.vertexes[index];
            vertex.move(scale * vertex.x, scale * vertex.y);
        }
    },
    
    press: function(vertex) {
        if (this.hasMistake() || this.won()) {
            return false;
        }

        if (this.firstVertex == null) {
            this.mistakenEdge = null;
            this.firstVertex = vertex;
            this.firstVertex.changeState(State.Selected);
            return true;
        } else if (this.firstVertex != vertex) {
            this.lastVertex = vertex;
            this.lastVertex.changeState(State.Selected);
            this.firstVertex.changeState(State.Normal);

            for (index in this.graph.edges) {
                var edge = this.graph.edges[index];
                if (edge.firstVertex == this.firstVertex && edge.lastVertex == this.lastVertex ||
                        edge.firstVertex == this.lastVertex && edge.lastVertex == this.firstVertex) {
                    if (edge.getState() == State.Completed) {
                        edge.changeState(State.Mistaken);
                        this.firstVertex.changeState(State.Mistaken);
                        this.lastVertex.changeState(State.Mistaken);
                        return true;
                    } else if (edge.getState() == State.Normal) {
                        edge.changeState(State.Completed);
                        this.firstVertex = this.lastVertex;
                        this.lastVertex = null;
                        return true;
                    }
                }
            }

            this.mistakenEdge = new Edge();
            this.mistakenEdge.firstVertex = this.firstVertex;
            this.mistakenEdge.lastVertex = this.lastVertex;
            this.mistakenEdge.changeState(State.Mistaken);
            this.firstVertex.changeState(State.Mistaken);
            this.lastVertex.changeState(State.Mistaken);
            return true;
        }

        return false;
    },
    
    load: function(text) {
        this.firstVertex = null;
        this.lastVertex = null;
        this.mistakenEdge = null;
        var levelLoader = new LevelLoader();
        this.graph = levelLoader.load(text);
        
        if (this.graph != null) {
            this.applySeek(50, -50);
            this.applyScale(0.65);
        }

        return this.graph != null;
    },
    
    hasMistake: function() {
        for (index in this.graph.vertexes) {
            var vertex = this.graph.vertexes[index];
            if (vertex.getState() == State.Mistaken) {
                return true;
            }
        }

        return false;
    },
    
    won: function() {
        for (index in this.graph.edges) {
            var edge = this.graph.edges[index];
            if (edge.getState() == State.Mistaken ||
                    edge.getState() == State.Normal) {
                return false;
            }
        }

        return true;
    },
    
    getMistakenEdge: function() {
        return this.mistakenEdge;
    }
})