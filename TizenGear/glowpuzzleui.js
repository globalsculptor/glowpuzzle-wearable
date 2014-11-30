var GameObject = klass({
    touchEnabled: true,
    tag: null 
})

var dark = false;
var darkness = 2;
var alphaVertex = 1.0;
var alphaDot = 0.95;
var alphaEdge = 0.55;
var alphaEdgeCompleted = 0.85;
var alphaEdgeMistaken = 0.85;

var glowStepUp = 0.05;
var glowStepDown = 0.01;
var animationSteps = 30;

var EdgeGameObject = GameObject.extend({
    currentAnimationStep: 0,
    edge: null,
    lineSprite: null,
    lineSpriteCompleted: null,
    lineSpriteMistaken: null,
    alpha: alphaEdge,
    alphaCompleted: alphaEdgeCompleted,
    alphaMistaken: alphaEdgeMistaken,
    firstVertexGameObject: null,
    lastVertexGameObject: null,
    createSprites: function(edge) {
        if (dark) {
            this.alpha = this.alpha / darkness;
            this.alphaCompleted = this.alphaCompleted / darkness;
            this.alphaMistaken = this.alphaMistaken / darkness;
        }
        
        this.edge = edge;
        
        this.lineSprite = this.createSprite('edge', this.alpha);
        this.lineSpriteCompleted = this.createSprite('edge_completed', this.alphaCompleted);
        this.lineSpriteMistaken = this.createSprite('edge_mistaken', this.alphaMistaken);
        
        this.proceedSpitesWithModel();
    },
    createSprite: function(name, alpha) {
        var sprite = game.add.sprite(this.edge.firstVertex.x + 40*scale, this.edge.firstVertex.y + 40*scale, name);
        sprite.alpha = alpha;
        sprite.scale.setTo(scale, scale);
        sprite.anchor.setTo(0, 0.5);
        sprite.angle = this.edge.angle();
        sprite.width = this.edge.distance();
        return sprite;
    },
    proceedSpitesWithModel: function() {
        this.lineSprite.visible = this.edge.state == State.Normal;
        this.lineSpriteMistaken.visible = this.edge.state == State.Mistaken;
        this.lineSpriteCompleted.visible = this.edge.state == State.Completed;
        
        if (this.firstVertexGameObject != null) {
            this.firstVertexGameObject.proceedSpritesWithState(this.edge.state);
        }
        
        if (this.lastVertexGameObject != null) {
            this.lastVertexGameObject.proceedSpritesWithState(this.edge.state);
        }
    },
    glowUp: function() {
        var limit = dark ? 1 / darkness : 1;
        
        if (this.lineSprite.alpha + glowStepUp < limit) {
            this.lineSprite.alpha += glowStepUp;
        } else {
            this.lineSprite.alpha = limit;
        }
        
        if (this.lineSpriteCompleted.alpha + glowStepUp < limit) {
            this.lineSpriteCompleted.alpha += glowStepUp;
        } else {
            this.lineSpriteCompleted.alpha = limit;
        }
        
        if (this.lineSpriteMistaken.alpha + glowStepUp < limit) {
            this.lineSpriteMistaken.alpha += glowStepUp;
        } else {
            this.lineSpriteMistaken.alpha = limit;
        }
    },
    glowDown: function() {
        if (this.lineSprite.alpha - glowStepDown > 0) {
            this.lineSprite.alpha -= glowStepDown;
        } else {
            this.lineSprite.alpha = 0;
        }
        
        if (this.lineSpriteCompleted.alpha - glowStepDown > 0) {
            this.lineSpriteCompleted.alpha -= glowStepDown;
        } else {
            this.lineSpriteCompleted.alpha = 0;
        }
        
        if (this.lineSpriteMistaken.alpha - glowStepDown > 0) {
            this.lineSpriteMistaken.alpha -= glowStepDown;
        } else {
            this.lineSpriteMistaken.alpha = 0;
        }
    },
    glowRestore: function() {
        this.lineSprite.alpha = this.alpha;
        this.lineSpriteCompleted.alpha = this.alphaCompleted;
        this.lineSpriteMistaken.alpha = this.alphaMistaken;
    },
    glow: function() {
        if (this.currentAnimationStep > animationSteps) {
            return;
        }
        
        if (this.currentAnimationStep < animationSteps - 10) {
            this.glowUp();
        } else if (this.currentAnimationStep < animationSteps) {
            this.glowUp();
        } else {
            this.glowRestore();
        }
        
        this.currentAnimationStep++;
    }
})

var VertexGameObject = GameObject.extend({
    vertex: null,
    vertexSprite: null,
    vertexSpriteSelected: null,
    vertexSpriteMistaken: null,
    dotSprite: null,
    dotSpriteCompleted: null,
    dotSpriteMistaken: null,
    alpha: alphaVertex,
    alphaDot: alphaDot,
    createSprites: function(vertex) {
        if (dark) {
            this.alpha = this.alpha / darkness;
            //this.alphaDot = this.alphaDot / darkness;
        }
        
        this.vertex = vertex;
        
        this.vertexSprite = this.createSprite('vertex', this.alpha);
        this.vertexSpriteSelected = this.createSprite('vertex_selected', this.alpha);
        this.vertexSpriteMistaken = this.createSprite('vertex_mistaken', this.alpha);
        
        this.dotSprite = this.createSprite('dot', this.alphaDot);
        this.dotSpriteCompleted = this.createSprite('dot_completed', this.alphaDot);
        this.dotSpriteMistaken = this.createSprite('dot_mistaken', this.alphaDot);
        
        this.proceedSpitesWithModel();
    },
    
    createSprite: function(name, alpha) {
        var sprite = game.add.sprite(this.vertex.x, this.vertex.y, name);
        sprite.scale.setTo(scale, scale);
        sprite.alpha = alpha;
        return sprite;
    },
    
    proceedSpitesWithModel: function() {
        this.vertexSprite.visible = this.vertex.state == State.Normal || this.vertex.state == State.Completed;
        this.vertexSpriteSelected.visible = this.vertex.state == State.Selected;
        this.vertexSpriteMistaken.visible = this.vertex.state == State.Mistaken;
        
        this.proceedSpritesWithState(this.vertex.state);
    },
    
    proceedSpritesWithState: function(state) {
        this.dotSprite.visible = state == State.Normal || state == State.Selected;
        this.dotSpriteCompleted.visible = state == State.Completed;
        this.dotSpriteMistaken.visible = state == State.Mistaken;
    }
})