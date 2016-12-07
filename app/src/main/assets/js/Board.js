
var Symbol = katex.Symbol;

//Long press logic (adapted from: http://jsfiddle.net/kelunik/pkjze6e6/42/)
var longpress = false;
var presstimer = null;
var longtarget = null;
var sortMoving = false;

var cancel = function(e) {
    if (presstimer !== null) {
        clearTimeout(presstimer);
        presstimer = null;
    }
    
    if (this.classList != null)
        this.classList.remove("longpress");
    sortMoving = false;
    
};

var finish = function(e) {
    cancel(e);
    
    trash(false);
};

var click = function(e) {
    if (presstimer !== null) {
        clearTimeout(presstimer);
        presstimer = null;
    }
    
    this.classList.remove("longpress");
    
    if (longpress) {
        return false;
    }
    
    alert("press");
};

var start = function(e) {
    //console.log(e);
    
    if (e.type === "click" && e.button !== 0) {
        return;
    }
    
    longpress = false;
    
    this.classList.add("longpress");
    
    presstimer = setTimeout(function() {
        onLongPress(e);
        longpress = true;
    }, 1000);
    
    return false;
};

var onMoveCancel =  function(e) {
    //console.log(sortMoving);
    if (sortMoving) {
        cancel(e);
    }
}
//End of long press code

//Trash icon
function trash(show) {
    var container = document.getElementById("trash-container");
    if (show) {
        var trash = document.createElement("img");
        trash.setAttribute("id","trash");
        trash.setAttribute("src","ic_delete_black_48dp_1x.png");
        trash.setAttribute("ondragover","allowDrop(event)");
        trash.setAttribute("ondrop","dropObject(event)");
        trash.style.width = '42px';
        trash.style.height = '42px';
        trash.style.paddingLeft = "150px";
        trash.style.float = "left";
        container.appendChild(trash);
    } else {
        if (container.children.length > 0)
            container.removeChild(container.children[0]);
    }
}

function decPlaces(num) {
  return (num.toString().split('.')[1] || []).length;
}

function resetSpans(equation) {
    for (var i = 0; i < equation.symbols.length; i++) {
        for (var j = 0; j < equation.spans.length; j++) {
            if (equation.spans[j].getAttribute('data-id') == equation.symbols[i].value.toString().charAt(0))
                equation.symbols[i].has_span = false;
        }
    }
}

//Term constructor
function Term(value, variable, index, degree, context) {
    var term = this;
    this.id;
    this.equation = context;
    this.coeficient = new Symbol(value, this);
    this.variable = new Symbol(variable, this);
    this.index = new Symbol(index, this);
    this.degree = new Symbol(degree, this);
    this.element;
    this.beforeEquality = true;
    this.isSimilar = function(term2) {
        if (term2 instanceof Term && term2 !== term)
            return (term.variable.value === term2.variable.value && term.index.value === term2.index.value && term.degree.value === term2.degree.value);
    }
    this.copy = function(newContext) {
        var copy = new Term(term.coeficient.value, term.variable.value, term.index.value, term.degree.value, newContext);
        copy.beforeEquality = term.beforeEquality;
        return copy;
    }
}

Term.prototype.toString = function() {
    var label = "";
    if (this.degree.value > 0) {
        if (this.coeficient.value != 1) { //For coeficient 1, do not show it
            //Format: do not show more than 3 decimals, and show it only when needed
            if (decPlaces(this.coeficient.value) < 4)
                label = this.coeficient.value;
            else
                label = this.coeficient.value.toPrecision(3);
        }
        label += this.variable.value + "_" + this.index.value; //+ "^" + this.degree.value;
    } else {
        label = this.coeficient.value.toString();
    }
    return label;
};

//Equality constructor
function Equality(context) {
    this.context = context;
    this.value = "=";
    this.element;
    context.equality = this;
}

Equality.prototype.toString = function() {
    return this.value;
}

//Equation constructor
function Equation(context) {
    var equation = this;
    var equationList;
    this.system = context;
    this.id;
    this.equationList;
    this.terms;
    this.current;
    this.equality;
    this.sortable;
    this.symbols = [];
    this.spans = [];
    this.addTerm = function(term) { 
        if (term.beforeEquality)
            this.current.unshift(term);
        else
            this.current.push(term);
    };
    this.setTerms = function(terms) {
        this.terms = terms;
        this.current = terms;
    }
    this.multiplyBy = function(constant) {
        for (t in this.current) {
            if (equation.current[t] instanceof Term)
                equation.current[t].coeficient.value *= constant;
        }
        equation.update();
    };
    this.sum = function(dragged, target) {
        var id = dragged.element.id;
        if (dragged.equation == target.equation) {
            if (dragged.variable.value == target.variable.value && dragged.index.value == target.index.value && dragged.degree.value == target.degree.value) {
                target.coeficient.value += dragged.coeficient.value;
                dragged.element.parentElement.removeChild(dragged.element);
                dragged.equation.current[id] =  undefined;
                //delete dragged.equation.current[dragged.equation.current.indexOf(dragged)];
                delete dragged;
                
                //Se o termo resultante for nulo, elimine-o
                /*
                if (target.coeficient.value == 0) {
                    target.element.parentElement.removeChild(target.element);
                    delete target.equation.terms[id];
                    delete target;
                }
                */
                target.equation.update();
            } else
                console.log("Termos nao podem ser somados!");
        }
    };
    this.reduce = function() {
        for (t in equation.current) {
            var term = equation.current[t];
            var similars;
            if (term instanceof Term) {
                similars = equation.current.filter(term.isSimilar);
                for (s in similars) {
                    if (term.beforeEquality !== similars[s].beforeEquality)
                        term.coeficient.value *= -1;
                    equation.sum(term, similars[s]);
                }
            }
        }
        equation.update();
    }
    //Recebe um ul, e constroi uma equação drag and drop com base nos termos passados na construção
    this.setup = function(eqList) {
        var parent = eqList.parentElement;
        var newList = document.createElement("ul");
        parent.replaceChild(newList, eqList);
        
        equation.equationList = newList;
        equationList = newList;
        
        if (equation.equality == undefined)
            equation.current.push(new Equality(equation));
        
        var element;
        //Cria list items para cada termo:
        for (t in equation.current) {
            element = document.createElement("li");
            
            if (equation.current[t] instanceof Term) {
                
                element.setAttribute("data-id", t);
                element.setAttribute("id", t);
                element.setAttribute("class","term");
                
                //Data transfer drag
                var coeficientID = equation.current[t].coeficient.id;
                element.setAttribute("ondragstart","onDrag(event)");
                element.setAttribute("ondrop","dropObject(event)");
                element.setAttribute("ondragover","allowDrop(event)");
                
                //Long press listeners
                //element.addEventListener("mousedown", start); //Needed to work on PC browsers
                element.addEventListener("touchstart", start);
                element.addEventListener("click", click);
                element.addEventListener("mouseout", cancel);
                element.addEventListener("touchend", cancel);
                element.addEventListener("touchleave", cancel);
                element.addEventListener("touchcancel", cancel);
                element.addEventListener("touchmove", onMoveCancel);
                element.addEventListener("drag", onMoveCancel);
                //element.addEventListener("dragstart", onMoveCancel);
                
                element.context = equation.current[t];
                equation.current[t].element = element;
                
            } else if (equation.current[t] instanceof Equality) {
                element.setAttribute("data-id", t);
                element.setAttribute("id", t);
                element.setAttribute("class","ignore");
                
                equation.current[t].element = element;
                element.context = equation.current[t];
            }
            element.style.display = "inline";
            equationList.appendChild(element);
        }
        
        //Transforma-os em uma lista reordenável
        this.sortable = new Sortable.create(equationList, {
            sort: true,
            //animation: 100,
            filter: ".ignore",
            //Se passar pela igualdade, deve inverter o sinal
            onSort: function(evt) {
                equation.update();
            },
            onMove: function(evt) {
                if (evt.dragged.context != null)
                    equation.oposite = evt.dragged.context;
                //Cancel long press withour event
                sortMoving = true;
            },
        });
        console.log("Equation list elements: " + equationList.childElementCount);
        equation.update();
    };
    this.update = function() {
        //Lógica de multiplicar por -1
        if (equation.oposite != null) {
            //Antes que atualize pros termos atuais
            //console.log("Previous equation TeX: " + equation.current);
            var previousPos = equation.current.indexOf(equation.oposite);
            var equalityPos = equation.current.indexOf(equation.equality); 
            //console.log(equation.equalityPos);
            if (previousPos < equalityPos)
                equation.oposite.beforeEquality = true;
            else
                equation.oposite.beforeEquality = false;
        }
        
        //Atualizar termos
        var currentString = equation.sortable.toArray();
        equation.current = [];
        var i;
        for (i = 0; i < equationList.childElementCount; i++) {
            if (equation.equationList.children[i].context != null )
                equation.current.push(equation.equationList.children[i].context);
        }
        
        console.log("Current equation TeX: " + equation.current);
        
        //Lógica de multiplicar por -1, parte 2
        if (equation.oposite != null) {
            var currentPos = equation.current.indexOf(equation.oposite);
            equalityPos = equation.current.indexOf(equation.equality);
            console.log("Previous pos: " + previousPos);
            console.log("Current pos: " + currentPos);
            if (equation.oposite.beforeEquality) {
                if (currentPos >= equalityPos)
                    equation.oposite.coeficient.value = -1 * this.oposite.coeficient.value;
            } else {
                if (currentPos <= equalityPos)
                    equation.oposite.coeficient.value = -1 * this.oposite.coeficient.value;
            }
        }
        
        Symbol.spans = [];
        var element;
        //Gerar código TeX para cada termo, e renderizá-lo usando KaTex
        for (var t in equation.current) {
            //element = equationList.children.namedItem(currentString[t]);
            //console.log(element.context);
            var TeX;
            if (equation.current[t] instanceof Term) {
                
                element = equation.current[t].element;
                
                equation.current[t].id = t;
                console.log(equation.current);
                var equalityPos = equation.current.indexOf(equation.equality);
                if (t < equalityPos)
                    equation.current[t].beforeEquality = true;
                else
                    equation.current[t].beforeEquality = false;
                
                TeX = equation.current[t].toString();
                if (t > 0 && equation.current[t-1] instanceof Term && equation.current[t].coeficient.value >= 0)
                    TeX = "+" + TeX;
            } else if (equation.current[t] instanceof Equality) {         //equality
                
                element = equation.current[t].element;
                
                if (t == 0)
                    TeX = "0=";
                else if (t == equation.current.length-1)
                    TeX = "=0";
                else
                    TeX = "=";
            }
            katex.render(TeX, element, equation.current[t]);
            
            //Create spaces
            if (element.childElementCount < 2) {
                var space = document.createElement("span");
                space.style.paddingLeft = "4px";
                space.setAttribute("id","space#" + t);
                //space.setAttribute("class", t);
                space.setAttribute("ondrop","dropObject(event)");
                space.setAttribute("ondragover","allowDrop(event)");
                space.setAttribute("ondragleave","onDragLeave(event)");
                element.appendChild(space);
                //console.log(space);
            }
        }
        equation.spans = Symbol.spans;
    };
}

//Linear System constructor
function LinearSystem() {
    var system = this;
    this.systemList;
    this.id;
    this.equations = [];
    this.pivots = [];
    this.sortable;
    this.add = function(equation) { this.equations.push(equation); };
    this.getEquationById = function(id) { return this.equation[id]; };
    this.sum =  function(dragged, target) {
        dragged.reduce();
        target.reduce();
        console.log("dragged " + dragged.terms);
        console.log("dragged " + dragged.current);
        console.log("target " + target.current);
        
        var toSum;
        var term;
        for (t in dragged.current) {
            term = dragged.current[t];
            if (term instanceof Term) {
                toSum = target.current.filter(term.isSimilar);
                if (toSum.length == 1) {
                    console.log(toSum[0].toString() + " somar " +term.toString() + " esta " + term.beforeEquality);
                    if (toSum[0].beforeEquality != term.beforeEquality) {
                        toSum[0].coeficient.value -= term.coeficient.value;
                    } else {
                        toSum[0].coeficient.value += term.coeficient.value;
                    }
                    //TODO: Se o termo resultante for nulo, elimine-o
                } else if (toSum.length == 0) {
                    target.addTerm(term.copy(target));
                    target.setup(target.equationList);
                }
            }
        }
        
        target.update();
        dragged.update();
    }
    this.equationSort = function(state) {
        for (e in system.equations)
            system.equations[e].sortable.options.sort = state;
    };
    //Faz multiplas configurações para mostrar as equações da forma correta
    this.setup = function(equations) {
        if (equations != undefined)
            system.equations = equations;
        var systemContainer = document.createElement('div');
        
        //Mostrar sinal chave, simbolizando sistema de equações
        var sign = document.createElement('p');
        sign.appendChild(document.createTextNode("\{"));
        sign.style.fontSize = "72px";
        sign.style.lineHeight = "60px";
        sign.style.marginTop = "0px";
        sign.style.float = 'left';
        sign.style.fontStretch = "expanded";
        //systemContainer.appendChild(sign);
        
        //Cria ul que conterá todas as equações
        var systemList = document.createElement('ul');
        systemList.setAttribute('id', 'system');
        systemList.setAttribute('style',"list-style-type: none;");
        systemList.style.padding = "10px";
        systemContainer.appendChild(systemList);
        system.systemList = systemList;
        
        document.body.appendChild(systemContainer);
        var equationList;
        var element, vertList, container, divider, space;
        //Para cada equação, cria um list item, e dentro dela, um ícone e um ul, onde os termos serão inseridos no setup
        for (var e in system.equations) {
            system.equations[e].system = system;  //Passando o contexto para a equação
            
            element = document.createElement('li');
            element.setAttribute('style',"list-style-type: none;");
            element.setAttribute('data-id', e);
            element.setAttribute('id', "equation" + e);
            
            element.setAttribute("ondrop","dropObject(event)");
            element.setAttribute("ondragover","allowDrop(event)");
            
            vertList = document.createElement('table');
            element.appendChild(vertList);
            
            //Setup container for the equation and drag handler
            container = document.createElement('tr');
            //container.setAttribute('style',"list-style-type: none;");
            var icon = document.createElement('img');
            icon.setAttribute("src","ic_open_with_black_24dp_2x.png");
            icon.style.width = '24px';
            icon.style.height = '24px';
            icon.style.float = 'left';
            icon.style.fontSize = "15px";
            container.appendChild(icon);
            vertList.appendChild(container);
            
            //Setup division listener
            divider = document.createElement("li");
            //divider.textContent = " ";
            divider.style.paddingTop = "4px";
            divider.setAttribute("ondrop","dropObject(event)");
            divider.setAttribute("ondragover","allowDrop(event)");
            divider.setAttribute("ondragleave","onDragLeave(event)");
            //divider.setAttribute('style',"list-style-type: none;");
            divider.class = "divider";
            divider.context = system.equations[e];
            vertList.appendChild(divider);
            
            systemList.appendChild(element);
            
            equationList = document.createElement('ul');
            equationList.style.paddingLeft = "40px";
            
            container.appendChild(equationList);
            
            system.equations[e].id = e;
            system.equations[e].setup(equationList);
        }
        
        systemContainer.style.fontSize = "24px";
        
        //Tranforma a lista de equações em uma lista reordenável drag and drop
        this.sortable = new Sortable.create(systemList, {
            animation: 100,
        });
    };
}

//Constant constructor
function Constant(value) {
    var context = this;
    this.value = value;
    this.element;
    this.sortable;
    this.delete =  function() {
        this.element.parentElement.removeChild(this.element);
        delete this;
    }
    this.setup = function() {
        var container = document.createElement('ui');
        var constant = document.createElement('li');
        var value;
        
        constant.context = context;
        container.style.fontSize = "24px";
        container.style.marginLeft = "6px";
        container.style.marginRight = "6px";
        container.appendChild(constant);
        document.body.appendChild(container);
        
        if (decPlaces(this.value) < 4)
            value = this.value;
        else
            value = this.value.toPrecision(3);
        katex.render(value.toString(), constant, context);
        
        constant.style.display = "inline";
        
        this.sortable = new Sortable.create(container);
        
        container.setAttribute("draggable","true");
        container.setAttribute("ondragstart","onDrag(event)");
        
        //Long press listeners
        //container.addEventListener("mousedown", start); //Needed to work on PC browsers
        container.addEventListener("touchstart", start);
        container.addEventListener("click", click);
        container.addEventListener("mouseout", finish);
        container.addEventListener("touchend", finish);
        container.addEventListener("touchleave", finish);
        container.addEventListener("touchcancel", finish);
        container.addEventListener("touchmove", cancel);
        //container.addEventListener("drag", cancel);
        
        this.element = container;
    };
}

function createConstant(value) {
    var constant = new Constant(value);
    constant.setup();
}

var dragObject;
function onDrag(ev) {
    dragObject = ev.target.context;
}

function allowDrop(ev) {
    ev.preventDefault();
    var space = event.target.class;
    if (space == "divider") {
        ev.target.style.backgroundColor = "red";
        ev.target.style.paddingLeft = "15px";
    }
}

function onDragLeave(ev) {
    var space = event.target.class;
    if (space == "divider") {
        ev.target.style.backgroundColor = "";
        ev.target.style.paddingLeft = "4px";
    }
}

function dropObject(event) {
    
    if (event.stopPropagation) {
        event.stopPropagation(); // stops the browser from redirecting.
    }
    event.preventDefault();
    
    trash(false);
    
    console.log(event.target);
    
    if(event.target.context != null && event.target.context.equation != null) {
        var equation = event.target.context.equation;
        if (dragObject instanceof Constant) {  //Multiply the target equation
            var constant = dragObject.value;
            if (constant != 0) {
                    equation.multiplyBy(constant);
            }
        } else if (dragObject instanceof Term && !dragObject.equation.sortable.options.sort) { //Do the sum of the dragged and target terms
            var draggedTerm = dragObject;
            var draggedEquation = dragObject.equation;
            var targetTerm = event.target.context;
            var targetEquation = targetTerm.equation;
            if (draggedEquation == targetEquation) { //Sum of two terms
                if (targetTerm !== draggedTerm)
                    draggedEquation.sum(draggedTerm,targetTerm);
            } else { //Sum of two equations
                draggedEquation.system.sum(draggedEquation,targetEquation);
            }
            console.log(draggedTerm);
            console.log(targetTerm);
        
            //Future feature: allow using spacing for trigger permutation on bigger screens
            var space = event.target.id.split("#");
            if (space[0] == "space" && space[1] != draggedTermPos) {
                console.log(eq.equationList.childNodes[space[1]]);
            }
            draggedTerm.element.style.backgroundColor = "";
            //console.log("target " + targetTerm);
        }
        equation.system.equationSort(true);
    } else if (event.target.class === "divider") {
        if (dragObject instanceof Constant) {  //Multiply the target equation
            var constant = dragObject.value;
            if (constant != 0)
                event.target.context.multiplyBy(1/constant);
            event.target.style.backgroundColor = "";
        }
    }
    
    if (event.target.id === "trash")
            dragObject.delete();
}

function onLongPress(event) {
    presstimer = null;
    
    if (event.stopPropagation) {
        event.stopPropagation(); // stops the browser from redirecting.
    }
    if (event.target.context instanceof Term) {
        event.target.context.element.style.backgroundColor = "blue";
        var selectedEquation = event.target.context.equation;
        selectedEquation.system.equationSort(false);
    } else {
        trash(true);
    }
    
}


//Dados de teste:
var sy = new LinearSystem();

var eq = new Equation(sy);
var eq2 = new Equation(sy);
var eq3 = new Equation(sy); 

eq.setTerms([new Term(8, "x", 0, 1, eq), new Term(12, "x", 1, 1, eq), new Equality(eq), new Term(3, "x", 1, 0, eq), new Term(2, "x", 1, 0, eq)]);
eq2.setTerms([new Term(3, "x", 0, 1, eq2), new Term(7,"x", 1, 1, eq2), new Term(2,"x", 0, 1, eq2)]);
eq3.setTerms([new Term(55, "x", 0, 1, eq3), new Term(9,"x", 2, 1, eq3), new Equality(eq3)]);

sy.setup([eq, eq2, eq3]);

createConstant(1/8);
createConstant(-5);
