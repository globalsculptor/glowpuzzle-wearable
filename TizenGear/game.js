var currentLevel = 1;
var totalLevels = 120;
var openedLevels = 1;

var isSolution = false;

var game = new Phaser.Game(
    320, 320, Phaser.AUTO, 'glowpuzzle'
);

var scale = 0.40;

var engine;
var selectedVertexIndex = -1;
var cursorVertex = null;
var edgeGameObjects = [];
var vertexGameObjects = [];

function updateNavigationButtons() {
    buttonLeft.visible = currentLevel > 1;
    buttonRight.visible = currentLevel < openedLevels;
}

function updateVertexGameObjects() {
    for (i in vertexGameObjects) {
        var vertexGameObject = vertexGameObjects[i];
        vertexGameObject.proceedSpitesWithModel();
    }
}

function updateEdgeGameObjects() {
    for (i in edgeGameObjects) {
        var edgeGameObject = edgeGameObjects[i];
        edgeGameObject.proceedSpitesWithModel();
    }
}

function updateAllGameObjects() {
    updateVertexGameObjects();
    updateEdgeGameObjects();
}

function clearSelection() {
    if (selectedVertexIndex > -1 && selectedVertexIndex < engine.graph.vertexes.length) {
        vertexGameObjects[selectedVertexIndex].vertex.state = State.Normal;
        vertexGameObjects[selectedVertexIndex].proceedSpitesWithModel();
    }
}

function adjustCursor() {
    if (selectedVertexIndex > -1 && selectedVertexIndex < engine.graph.vertexes.length) {
        cursorVertex.x = vertexGameObjects[selectedVertexIndex].vertex.x - 4;
        cursorVertex.y = vertexGameObjects[selectedVertexIndex].vertex.y - 4;
    }
    
    cursorVertex.visible = selectedVertexIndex != -1;
}

function moveSelectionLeft() {
    if (selectedVertexIndex == -1) {
        selectedVertexIndex = 0;
    } else if (selectedVertexIndex == 0) {
        selectedVertexIndex = engine.graph.vertexes.length - 1;
    } else {
        --selectedVertexIndex;
    }
    
    adjustCursor();
}

function moveSelectionRight() {
    if (selectedVertexIndex == -1) {
        selectedVertexIndex = 0;
    } else if (selectedVertexIndex == engine.graph.vertexes.length - 1) {
        selectedVertexIndex = 0;
    } else {
        ++selectedVertexIndex;
    }
    
    adjustCursor();
}

var textField;

var buttonLeft;
var buttonRight;
var buttonEnter;

function createButton(name, mode) {
    if (mode == 0) {
        var scale = 0.7;
        var sprite = game.add.sprite(2, 240, name);
        sprite.scale.setTo(scale, scale);
        sprite.inputEnabled = true;
        buttonLeft = sprite;
    } else if (mode == 1) {
        var scale = 0.7;
        var sprite = game.add.sprite(235, 230, name);
        sprite.scale.setTo(scale, scale);
        sprite.inputEnabled = true;
        buttonRight = sprite;
    } else if (mode == 2) {
        var scale = 0.9;
        var sprite = game.add.sprite(106, 220, name);
        sprite.scale.setTo(scale, scale);
        sprite.inputEnabled = true;
        buttonEnter = sprite;
    }
    
}

function gotoMainMenu() {
    selectedVertexIndex = -1;
    cursorVertex = null;
    edgeGameObjects = [];
    vertexGameObjects = [];
    game.state.start('mainMenu');
}

function restartGame() {
    selectedVertexIndex = -1;
    cursorVertex = null;
    edgeGameObjects = [];
    vertexGameObjects = [];
    game.state.start('main');
}

function leftButtonPressed() {
    if (engine.won() || engine.hasMistake()) {
        mainMenu_leftButtonPressed();
        return;
    }
    
	moveSelectionLeft();
    //playSound("clickarrows");

}

function rightButtonPressed() {
    if (engine.won() || engine.hasMistake()) {
        mainMenu_rightButtonPressed();
        return;
    }
    
	moveSelectionRight();
    //playSound("clickarrows");
}

function enterButtonPressed() {
    if (engine.won()) {
        mainMenu_rightButtonPressed();
        return;
    }
    
    if (engine.hasMistake()) {
        restartGame();
        //playSound("clickarrows");
        return;
    }
    
    if (selectedVertexIndex == -1) {
        selectedVertexIndex = 0;
    }
    
	engine.press(engine.graph.vertexes[selectedVertexIndex]);
    
    if (engine.getMistakenEdge() != null) {
        var edgeGameObject = new EdgeGameObject();
        edgeGameObject.createSprites(engine.getMistakenEdge());
        
        game.world.sendToBack(edgeGameObject.lineSpriteMistaken);
        edgeGameObjects.push(edgeGameObject);
    }
    
    if (engine.won()) {
        textField.text = doneText;
        playSound("winsound");
        if (soundEnabled) {
            navigator.vibrate([100, 100, 200, 200, 100]);
        }
        
        if (!isSolution && openedLevels == currentLevel && openedLevels < totalLevels) {
            ++openedLevels;
            
            localStorage.setItem('openedLevels', openedLevels);
        }
    } else if (engine.hasMistake()){
        textField.text = failedText;
        playSound("failedsound");
        if (soundEnabled) {
            navigator.vibrate(500);
        }
    } else {
        //playSound("clickarrows");
    	//navigator.vibrate(100);
    }
    
    if (engine.won() || engine.hasMistake()) {
        for (j in edgeGameObjects) {
            var edgeGameObject = edgeGameObjects[j];
            edgeGameObject.currentAnimationStep = 0;
        }
    }
    
    updateAllGameObjects();
}

var handled = true;
var winsound;
var failedsound;
var clickarrows;

var mainState = {
    preload: function() {
        game.load.image('enter', 'assets/gfx/buttoncurrent.png');
        game.load.image('left', 'assets/gfx/buttonleft.png');
        game.load.image('right', 'assets/gfx/buttonright.png');
        game.load.image('refresh', 'assets/gfx/buttonrefresh.png');
        game.load.image('help', 'assets/gfx/buttonhelp.png');
    
        game.load.image('dot', 'assets/gfx/dot_green.png');
        game.load.image('vertex', 'assets/gfx/vertex_green.png');
        game.load.image('edge', 'assets/gfx/edge_green.png');
    
        game.load.image('dot_mistaken', 'assets/gfx/dotmistaken.png');
        game.load.image('vertex_mistaken', 'assets/gfx/vertex_red.png');
        game.load.image('edge_mistaken', 'assets/gfx/edge_red.png');
    
        game.load.image('dot_completed', 'assets/gfx/dotcompleted.png');
        game.load.image('edge_completed', 'assets/gfx/edge_blue.png');
    
        game.load.image('vertex_selected', 'assets/gfx/vertex_selected.png');
    
        game.load.text('level' + currentLevel, 'assets/levels/level' + currentLevel + '.txt');
    },

    create: function() {
        dark = false;
        isSolution = false;
        this.input.maxPointers = 1;
        
        engine = new Engine();
        engine.load(game.cache.getText('level' + currentLevel));
    
        for (i in engine.graph.edges) {
            var edge = engine.graph.edges[i];
        
            var edgeGameObject = new EdgeGameObject();
            edgeGameObject.createSprites(edge);
            edgeGameObjects.push(edgeGameObject);
        }
    
        for (i in engine.graph.vertexes) {
            var vertex = engine.graph.vertexes[i];
            var vertexGameObject = new VertexGameObject();
            vertexGameObject.createSprites(vertex);
        
            for (j in edgeGameObjects) {
                var edgeGameObject = edgeGameObjects[j];
                if (edgeGameObject.edge.firstVertex == vertex) {
                    edgeGameObject.firstVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                } else if (edgeGameObject.edge.lastVertex == vertex) {
                    edgeGameObject.lastVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                }
            }
        
            vertexGameObjects.push(vertexGameObject);
        }
    
        cursorVertex = game.add.sprite(0, 0, 'vertex_selected');
        cursorVertex.scale.setTo(scale + 0.1, scale + 0.1);
        cursorVertex.visible = false;
    
        createButton('left', 0);
        createButton('right', 1);
        createButton('enter', 2);
    
        var style = { font: "35px Arial", fill: "#ffffff", align: "center" };

        textField = game.add.text(30, 10, "", style);
        textField.text = "";
        
        selectedVertexIndex = 0;
        adjustCursor();
    },

    update: function() {
        for (j in edgeGameObjects) {
            var edgeGameObject = edgeGameObjects[j];
            edgeGameObject.glow();
        }
        
        /*if (engine.won() || engine.hasMistake()) {
            for (j in edgeGameObjects) {
                var edgeGameObject = edgeGameObjects[j];
                edgeGameObject.glow();
            }
        }*/
        
       /* if (game.input.pointer1.isUp && !handled){
		  		
		  if (game.input.pointer1.x < 70 && game.input.pointer1.y > 200) {
			 leftButtonPressed();
		  } else if (game.input.pointer1.x > 220 && game.input.pointer1.y > 200) {
			 rightButtonPressed();
		  } else if (game.input.pointer1.y > 200) {
			 enterButtonPressed();
		  }		
		  handled = true;
        } else if (game.input.pointer1.isDown) {
		  handled = false;
        }*/
        
        if (this.game.input.activePointer.isUp && !handled){
		  		
		  if (game.input.activePointer.x < 70 && game.input.activePointer.y > 200) {
			 leftButtonPressed();
		  } else if (game.input.activePointer.x > 220 && game.input.activePointer.y > 200) {
			 rightButtonPressed();
		  } else if (game.input.activePointer.y > 200) {
              if (engine.won()) {
                  rightButtonPressed();
              } else {
			     enterButtonPressed();
              }
          } else {
              if (engine.won()) {
                  rightButtonPressed();
              } else {
                openLevelHelp();
              }
          }
		  handled = true;
        } else if (this.game.input.activePointer.isDown) {
		  handled = false;
        }
    
        /*if (buttonLeft.input.pointerDown()) {
            leftButtonPressed();
        }
        else if (buttonRight.input.pointerDown()) {
            rightButtonPressed();
        }
        else if (buttonEnter.input.pointerDown()) {
            enterButtonPressed();
        }*/
    }
};

function mainMenu_leftButtonPressed() {
    if (currentLevel > 1) {
        --currentLevel;
        gotoMainMenu();
    }
    
    //playSound("clickmenu");
}

function mainMenu_rightButtonPressed() {
    if (currentLevel < openedLevels) {
        ++currentLevel;
        gotoMainMenu();
    }
    
    //playSound("clickmenu");
}

function mainMenu_enterButtonPressed() {
    restartGame();
    
    //playSound("clickpuzzle");
}

var soundEnabled = true;

function playSound(name) {
    if (soundEnabled) {
        var audio = document.getElementById(name);
        audio.play();
    } 
}


var mainMenuState = {
    preload: function() {
        game.load.image('enter', 'assets/gfx/buttoncurrent.png');
        game.load.image('left', 'assets/gfx/buttonleft.png');
        game.load.image('right', 'assets/gfx/buttonright.png');
        game.load.image('refresh', 'assets/gfx/buttonrefresh.png');
        game.load.image('help', 'assets/gfx/buttonhelp.png');
    
        game.load.image('dot', 'assets/gfx/dot_green.png');
        game.load.image('vertex', 'assets/gfx/vertex_green.png');
        game.load.image('edge', 'assets/gfx/edge_green.png');
    
        game.load.image('dot_mistaken', 'assets/gfx/dotmistaken.png');
        game.load.image('vertex_mistaken', 'assets/gfx/vertex_red.png');
        game.load.image('edge_mistaken', 'assets/gfx/edge_red.png');
    
        game.load.image('dot_completed', 'assets/gfx/dotcompleted.png');
        game.load.image('edge_completed', 'assets/gfx/edge_blue.png');
    
        game.load.image('vertex_selected', 'assets/gfx/vertex_selected.png');
    
        game.load.text('level' + currentLevel, 'assets/levels/level' + currentLevel + '.txt');
    },

    create: function() {
        this.input.maxPointers = 1;
        dark = true;
        
        engine = new Engine();
        engine.load(game.cache.getText('level' + currentLevel));
    
        for (i in engine.graph.edges) {
            var edge = engine.graph.edges[i];
        
            var edgeGameObject = new EdgeGameObject();
            edgeGameObject.createSprites(edge);
            edgeGameObjects.push(edgeGameObject);
        }
    
        for (i in engine.graph.vertexes) {
            var vertex = engine.graph.vertexes[i];
            var vertexGameObject = new VertexGameObject();
            vertexGameObject.createSprites(vertex);
        
            for (j in edgeGameObjects) {
                var edgeGameObject = edgeGameObjects[j];
                if (edgeGameObject.edge.firstVertex == vertex) {
                    edgeGameObject.firstVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                } else if (edgeGameObject.edge.lastVertex == vertex) {
                    edgeGameObject.lastVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                }
            }
        
            vertexGameObjects.push(vertexGameObject);
        }
    
        cursorVertex = game.add.sprite(0, 0, 'vertex_selected');
        cursorVertex.scale.setTo(scale + 0.1, scale + 0.1);
        cursorVertex.visible = false;
    
        createButton('left', 0);
        createButton('right', 1);
        createButton('enter', 2);
    
        var style = { font: "65px Arial", fill: "#ffffff", align: "center" };

        textField = game.add.text(30, 10, "", style);
        textField.text = "#" + currentLevel;
        
        updateNavigationButtons();
    },

    update: function() {
        for (j in edgeGameObjects) {
            var edgeGameObject = edgeGameObjects[j];
            edgeGameObject.glow();
        }
        
        if (this.game.input.activePointer.isUp && !handled){
		  		
		  if (game.input.activePointer.x < 70 && game.input.activePointer.y > 200) {
			 mainMenu_leftButtonPressed();
		  } else if (game.input.activePointer.x > 220 && game.input.activePointer.y > 200) {
			 mainMenu_rightButtonPressed();
		  } else {
			 mainMenu_enterButtonPressed();     
		  }		
		  handled = true;
        } else if (this.game.input.activePointer.isDown) {
		  handled = false;
        }
    }
};

function openLevelHelp() {
    game.state.start('helpLevel');
}

var helpStep = 0;
var helpDelay = 30;
var currentHelpDelay = helpDelay;

var helpLevelState = {
    preload: function() {
        game.load.image('enter', 'assets/gfx/buttoncurrent.png');
        game.load.image('left', 'assets/gfx/buttonleft.png');
        game.load.image('right', 'assets/gfx/buttonright.png');
        game.load.image('refresh', 'assets/gfx/buttonrefresh.png');
        game.load.image('help', 'assets/gfx/buttonhelp.png');
    
        game.load.image('dot', 'assets/gfx/dot_green.png');
        game.load.image('vertex', 'assets/gfx/vertex_green.png');
        game.load.image('edge', 'assets/gfx/edge_green.png');
    
        game.load.image('dot_mistaken', 'assets/gfx/dotmistaken.png');
        game.load.image('vertex_mistaken', 'assets/gfx/vertex_red.png');
        game.load.image('edge_mistaken', 'assets/gfx/edge_red.png');
    
        game.load.image('dot_completed', 'assets/gfx/dotcompleted.png');
        game.load.image('edge_completed', 'assets/gfx/edge_blue.png');
    
        game.load.image('vertex_selected', 'assets/gfx/vertex_selected.png');
    
        game.load.text('level' + currentLevel, 'assets/levels/level' + currentLevel + '.txt');
    },

    create: function() {
        isSolution = true;
        helpStep = 0;
        this.input.maxPointers = 1;
        dark = false;
        
        engine = new Engine();
        engine.load(game.cache.getText('level' + currentLevel));
    
        for (i in engine.graph.edges) {
            var edge = engine.graph.edges[i];
        
            var edgeGameObject = new EdgeGameObject();
            edgeGameObject.createSprites(edge);
            edgeGameObjects.push(edgeGameObject);
        }
    
        for (i in engine.graph.vertexes) {
            var vertex = engine.graph.vertexes[i];
            var vertexGameObject = new VertexGameObject();
            vertexGameObject.createSprites(vertex);
        
            for (j in edgeGameObjects) {
                var edgeGameObject = edgeGameObjects[j];
                if (edgeGameObject.edge.firstVertex == vertex) {
                    edgeGameObject.firstVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                } else if (edgeGameObject.edge.lastVertex == vertex) {
                    edgeGameObject.lastVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                }
            }
        
            vertexGameObjects.push(vertexGameObject);
        }
    
        cursorVertex = game.add.sprite(0, 0, 'vertex_selected');
        cursorVertex.scale.setTo(scale + 0.1, scale + 0.1);
        cursorVertex.visible = false;
        
        createButton('enter', 2);
    
        var text = "- phaser -\n with a sprinkle of \n pixi dust.";
        var style = { font: "25px Arial", fill: "#ffffff", align: "center" };

        textField = game.add.text(30, 10, text, style);
        textField.text = "#" + currentLevel + solutionText;
    },

    update: function() {
        for (j in edgeGameObjects) {
            var edgeGameObject = edgeGameObjects[j];
            edgeGameObject.glow();
        }
        
        if (currentHelpDelay == 0) {
            if (helpStep >= 0 && helpStep < engine.graph.solution.length) {
                var vertex = engine.graph.solution[helpStep];
                selectedVertexIndex = engine.graph.vertexes.indexOf(vertex);
                adjustCursor();
                enterButtonPressed();
            
                ++helpStep;
            } else {
                restartGame();
            }
            currentHelpDelay = helpDelay;
        } else {
            --currentHelpDelay;
        }        
        
        if (this.game.input.activePointer.isUp && !handled){
            if (game.input.activePointer.x > 70 && game.input.activePointer.x < 220 && game.input.activePointer.y > 200) {
                restartGame();
            }
		  	
		  handled = true;
        } else if (this.game.input.activePointer.isDown) {
		  handled = false;
        }
    }
};

var tutorialState = {
    preload: function() {
        game.load.image('enter', 'assets/gfx/buttoncurrent.png');
        game.load.image('left', 'assets/gfx/buttonleft.png');
        game.load.image('right', 'assets/gfx/buttonright.png');
        game.load.image('refresh', 'assets/gfx/buttonrefresh.png');
        game.load.image('help', 'assets/gfx/buttonhelp.png');
    
        game.load.image('dot', 'assets/gfx/dot_green.png');
        game.load.image('vertex', 'assets/gfx/vertex_green.png');
        game.load.image('edge', 'assets/gfx/edge_green.png');
    
        game.load.image('dot_mistaken', 'assets/gfx/dotmistaken.png');
        game.load.image('vertex_mistaken', 'assets/gfx/vertex_red.png');
        game.load.image('edge_mistaken', 'assets/gfx/edge_red.png');
    
        game.load.image('dot_completed', 'assets/gfx/dotcompleted.png');
        game.load.image('edge_completed', 'assets/gfx/edge_blue.png');
    
        game.load.image('vertex_selected', 'assets/gfx/vertex_selected.png');
    
        game.load.text('level1', 'assets/levels/level1.txt');
    },

    create: function() {
        isSolution = true;
        helpStep = 0;
        this.input.maxPointers = 1;
        dark = false;
        
        selectedVertexIndex = -1;
        cursorVertex = null;
        edgeGameObjects = [];
        vertexGameObjects = [];
        
        engine = new Engine();
        engine.load(game.cache.getText('level1'));
    
        for (i in engine.graph.edges) {
            var edge = engine.graph.edges[i];
        
            var edgeGameObject = new EdgeGameObject();
            edgeGameObject.createSprites(edge);
            edgeGameObjects.push(edgeGameObject);
        }
    
        for (i in engine.graph.vertexes) {
            var vertex = engine.graph.vertexes[i];
            var vertexGameObject = new VertexGameObject();
            vertexGameObject.createSprites(vertex);
        
            for (j in edgeGameObjects) {
                var edgeGameObject = edgeGameObjects[j];
                if (edgeGameObject.edge.firstVertex == vertex) {
                    edgeGameObject.firstVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                } else if (edgeGameObject.edge.lastVertex == vertex) {
                    edgeGameObject.lastVertexGameObject = vertexGameObject;
                    edgeGameObject.proceedSpitesWithModel();
                }
            }
        
            vertexGameObjects.push(vertexGameObject);
        }
        
        createButton('enter', 2);
    
        cursorVertex = game.add.sprite(0, 0, 'vertex_selected');
        cursorVertex.scale.setTo(scale + 0.1, scale + 0.1);
        cursorVertex.visible = false;
    
        var style = { font: "25px Arial", fill: "#ffffff", align: "center" };

        textField = game.add.text(30, 10, "", style);
        textField.text = tutorialText;
        
        alert(welcomeText);
    },

    update: function() {
        for (j in edgeGameObjects) {
            var edgeGameObject = edgeGameObjects[j];
            edgeGameObject.glow();
        }
        
        if (currentHelpDelay == 0) {
            if (helpStep >= 0 && helpStep < engine.graph.solution.length) {
                var vertex = engine.graph.solution[helpStep];
                selectedVertexIndex = engine.graph.vertexes.indexOf(vertex);
                adjustCursor();
                enterButtonPressed();
            
                ++helpStep;
            } else {
                game.state.start('firstMenu');
            }
            currentHelpDelay = helpDelay;
        } else {
            --currentHelpDelay;
        }        
        
        if (this.game.input.activePointer.isUp && !handled){
            if (game.input.activePointer.x > 70 && game.input.activePointer.x < 220 && game.input.activePointer.y > 200) {
                game.state.start('firstMenu');
            }
		  	
            handled = true;
        } else if (this.game.input.activePointer.isDown) {
            handled = false;
        }
    }
};

var buttonSoundOn;
var buttonSoundOff;
var buttonHelp;
var buttonPlay;

var firstMenuState = {
    preload: function() {
        game.load.image('soundon', 'assets/gfx/buttonsoundon.png');
        game.load.image('soundoff', 'assets/gfx/buttonsoundoff.png');
        game.load.image('play', 'assets/gfx/buttonplay.png');
        game.load.image('refresh', 'assets/gfx/buttonrefresh.png');
        game.load.image('help', 'assets/gfx/buttonhelp.png');
    
        game.load.image('mainlogo', 'assets/gfx/mainlogo2.png');
    },

    create: function() {
        var mainLogo = game.add.sprite(0, 0, 'mainlogo');
        
        if (localStorage.getItem('openedLevels') === null) {
            openedLevels = 1;
        } else {
            openedLevels = localStorage.getItem('openedLevels');
        }
        
        if (localStorage.getItem('soundEnabled') === null) {
            soundEnabled = true;
        } else {
            soundEnabled = localStorage.getItem('soundEnabled') == 1;
        }
        
        
        //mainlogo.y = 0;
        //mainLogo.scale.setTo(0.50, 0.50);
        //mainLogo.x = 0;
        
        //mainLogo.scale.setTo(0.24, 0.24);
        //mainLogo.x = 75;
        
        buttonSoundOn = game.add.sprite(5, 225, 'soundon');
        buttonSoundOn.scale.setTo(0.7, 0.7);
        buttonSoundOff = game.add.sprite(5, 225, 'soundoff');
        buttonSoundOff.scale.setTo(0.7, 0.7);
        buttonHelp = game.add.sprite(107, 220, 'help');
        buttonHelp.scale.setTo(0.8, 0.8);
        buttonPlay = game.add.sprite(225, 225, 'play');
        buttonPlay.scale.setTo(0.7, 0.7);
        
        buttonSoundOn.visible = soundEnabled;
        buttonSoundOff.visible = !soundEnabled;
    },

    update: function() {        
        if (this.game.input.activePointer.isUp && !handled){
		  		
		  if (game.input.activePointer.x < 70 && game.input.activePointer.y > 200) {
			 soundOnOffPressed();
		  } else if (game.input.activePointer.x > 220 && game.input.activePointer.y > 200) {
			 playPressed();  
		  } else if (game.input.activePointer.y > 200) {
             helpPressed();
		  }		
		  handled = true;
        } else if (this.game.input.activePointer.isDown) {
		  handled = false;
        }
    }
};

function soundOnOffPressed() {
    soundEnabled = !soundEnabled;
    
    buttonSoundOn.visible = soundEnabled;
    buttonSoundOff.visible = !soundEnabled;
    
    localStorage.setItem('soundEnabled', soundEnabled ? 1 : 0);
}

function helpPressed() {
    game.state.start('tutorial');
}

function playPressed() {
    gotoMainMenu();
}

/*
//solution 
var moveSprite = this.game.add.sprite(startX, startY, 'spritekey');
var tween = game.add.tween(moveSprite).to({
x: [startX, firstBezierPointX, secondBezierPointX, endX],
y: [startY, firstBezierPointy, secondBezierPointY, endY],
}, 1000,Phaser.Easing.Quadratic.Out, true).interpolation(function(v, k){
    return Phaser.Math.bezierInterpolation(v, k);
});
*/

// Add and start the 'main' state to start the game
game.state.add('main', mainState);  
game.state.add('mainMenu', mainMenuState);
game.state.add('firstMenu', firstMenuState);
game.state.add('helpLevel', helpLevelState);
game.state.add('tutorial', tutorialState);
game.state.start('firstMenu');

( function () {
	window.addEventListener( 'tizenhwkey', function( ev ) {
		if( ev.keyName == "back" ) {
			if (game.state.current == 'main') {
				gotoMainMenu();
			} else if (game.state.current == 'mainMenu' || game.state.current == 'tutorial') {
				game.state.start('firstMenu');
			} else if (game.state.current == 'helpLevel') {
				restartGame();
			} else {
				tizen.application.getCurrentApplication().exit();
			}
			
		}
	} );
} () );