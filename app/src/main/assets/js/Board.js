
var Symbol = katex.Symbol;

function giveIds(equation) {
    //Procurar por elementos compostos
    var digits;
    for (var i = 0; i < equation.spans.length; i++) {
        for (var j = 0; j < equation.symbols.length; j++) {
            digits = equation.symbols[j].value.toString().length;
            if (digits > 1) {
                if (equation.symbols[j].has_span == false && equation.spans[i].getAttribute('data-id') == equation.symbols[j].value.toString().charAt(0)) {
                    equation.symbols[j].has_span = true;
                    for (k = 0; k < digits; k++) {
                        var m = i + k;
                        equation.spans[m].setAttribute('id', equation.symbols[j].id);
                        equation.spans[m].setAttribute('data-id', equation.spans[i].getAttribute('data-id') + " from " + equation.symbols[j].value);
                    }
                }
            }
        }
    }
    //Procurar demais elementos
    for (var i = 0; i < equation.spans.length; i++) {
        for (var j = 0; j < equation.symbols.length; j++) {
            if (equation.symbols[j].has_span == false && equation.spans[i].getAttribute('data-id') == equation.symbols[j].value) {
                equation.symbols[j].has_span = true;
                equation.spans[i].setAttribute('id', equation.symbols[j].id);
                break;
            }
        }
    }
    resetSpans(equation);
}

function resetSpans(equation) {
    for (var i = 0; i < equation.symbols.length; i++) {
        for (var j = 0; j < equation.spans.length; j++) {
            if (equation.spans[j].getAttribute('data-id') == equation.symbols[i].value.toString().charAt(0))
                equation.symbols[i].has_span = false;
        }
    }
}

function getEquationBySymbolId(id) {
    if (Symbol.symbols[id] != undefined)
        return Symbol.symbols[id].term.equation;
    else
        return null;
}

function getTermBySymbolId(id) {
    if (Symbol.symbols[id] != undefined)
        return Symbol.symbols[id].term;
    else
        return null;
}

//Term constructor
function Term(value, variable, index, degree, context) {
    this.equation = context;
    this.coeficient = new Symbol(value, this);
    this.variable = new Symbol(variable, this);
    this.index = new Symbol(index, this);
    this.degree = new Symbol(degree, this);
    this.beforeEquality = true;
}

Term.prototype.toString = function() {
    var label;
    if (this.degree.value > 0) {
        label = this.coeficient.value + this.variable.value + "_" + this.index.value; //+ "^" + this.degree.value;
    } else {
        label = this.coeficient.value.toString();
    }
    return label;
};

//Equation constructor
function Equation() {
    var equation = this;
    var equationList;
    this.terms = {};
    this.current = Object.keys(this.terms).map(function(key) {return this.terms[key];});
    this.sortable;
    this.symbols = [];
    this.spans = [];
    this.addTerm = function(term) { this.terms.push(term); };
    this.multiplyBy = function(constant) {
        for (t in this.terms) {
            if (this.terms[t] instanceof Term)
                this.terms[t].coeficient.value *= constant;
        }
        this.update();
    };
    //Recebe um ul, e constroi uma equação drag and drop com base nos termos passados na construção
    this.setup = function(eqList) {
        equationList = eqList;
        
        if (equation.terms.equality == undefined) {
            equation.terms.equality = "=0";
        }
        var element;
        //Cria list items para cada termo:
        Object.keys(this.terms).forEach(function(t, i) {
            element = document.createElement("li");
            if (equation.terms[t] instanceof Term) {
                element.setAttribute("data-id", t);
                element.setAttribute("id", t);
            } else {
                element.setAttribute("data-id", "equality");
                element.setAttribute("id", "equality");
                element.setAttribute("class","ignore");
            }
            element.style.display = "inline";
            equationList.appendChild(element);
        });
        //Transforma-os em uma lista reordenável
        this.sortable = new Sortable.create(equationList, {
            animation: 100,
            filter: ".ignore",
            //Se passar pela igualdade, deve inverter o sinal
            onSort: function(evt) {
                equation.update();
            },
            onMove: function(evt) {
                    equation.oposite = equation.getTermById(evt.dragged.id);
                
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
            var equalityPos = equation.current.indexOf(equation.terms.equality);
            console.log(equation.equalityPos);
            if (previousPos < equalityPos)
                equation.oposite.beforeEquality = true;
            else
                equation.oposite.beforeEquality = false;
        }
        
        //Atualizar termos
        var currentString = equation.sortable.toArray();
        equation.current = currentString.map(equation.getTermById);
        console.log("Current equation TeX: " + equation.current);
        
        //Lógica de multiplicar por -1 parte 2
        if (equation.oposite != null) {
            var currentPos = equation.current.indexOf(equation.oposite);
            equalityPos = equation.current.indexOf(equation.terms.equality);
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
            element = equationList.children.namedItem(currentString[t]);
            //console.log(element);
            var TeX = equation.current[t].toString();
            if (equation.current[t] instanceof Term) {
                if (t > 0 && equation.current[t-1] instanceof Term && equation.current[t].coeficient.value >= 0)
                    TeX = "+" + TeX;
            } else {         //equality
                console.log(t);
                if (t == 0)
                    TeX = "0=";
                else if (t == currentString.length-1)
                    TeX = "=0";
                else
                    TeX = "=";
            }
            katex.render(TeX, element);
        }
        equation.spans = Symbol.spans;
        giveIds(equation);
        
    };
    this.getTermById = function(id) {
        return equation.terms[id];
    };
}

//Linear System constructor
function LinearSystem(equations) {
    this.id;
    this.equations = equations;
    this.sortable;
    this.add = function(equation) { this.equations.push(equation); };
    this.getEquationById = function(id) { return this.equation[id]; };
    //Faz multiplas configurações para mostrar as equações da forma correta
    this.setup = function() {
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
        
        document.body.appendChild(systemContainer);
        var equationList;
        var element;
        //Para cada equação, cria um list item, e dentro dela, um ícone e um ul, onde os termos serão inseridos no setup
        for (var e in equations) {
            element = document.createElement('li');
            element.setAttribute('data-id', e);
            element.setAttribute('id', "equation" + e);
            
            systemList.appendChild(element);
            
            var icon = document.createElement('img');
            icon.setAttribute("src","ic_open_with_black_24dp_2x.png");
            icon.style.width = '24px';
            icon.style.height = '24px';
            icon.style.float = 'left';
            icon.style.fontSize = "15px";
            element.appendChild(icon);
            
            equationList = document.createElement('ul');
            equationList.style.paddingLeft = "40px";
            //equationList.setAttribute('class', "ignore");
            element.setAttribute("ondrop","dropConstant(event)");
            element.setAttribute("ondragover","allowDrop(event)");
            
            element.appendChild(equationList);
            this.equations[e].id = e;
            this.equations[e].setup(equationList);
        }
        
        systemContainer.style.fontSize = "28px";
        
        //Tranforma a lista de equações em uma lista reordenável drag and drop
        this.sortable = new Sortable.create(systemList, {
            animation: 100,
            filter: "ignore",
        });
    };
}

//Constant constructor
function Constant(value) {
    this.value = value;
    this.sortable;
    this.setup = function() {
        var container = document.createElement('ui');
        var constant = document.createElement('li');
        container.style.fontSize = "28px";
        container.style.marginLeft = "6px";
        container.style.marginRight = "6px";
        container.appendChild(constant);
        document.body.appendChild(container);
        katex.render(this.value.toString(), container);
        this.sortable = new Sortable.create(container);
        container.setAttribute("ondragstart","dragConstant(event," + this.value + ")");
    };
}

function createConstant(value) {
    var constant = new Constant(value);
    constant.setup();
}

function dragConstant(ev, value) {
    ev.dataTransfer.setData("value", value);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function dropConstant(event) {
    event.preventDefault();
    var equation = getEquationBySymbolId(event.target.id);
    var constant = event.dataTransfer.getData("value");
    if (equation != null && constant != 0)
        equation.multiplyBy(constant);
        //alert("Should multiply: " + getEquationSymbolbyId(event.target.id).id + " by " + event.dataTransfer.getData("value"));
        
    
}

//Dados de teste:

var eq = new Equation();
var eq2 = new Equation();
var eq3 = new Equation(); 

eq.terms = {"0": new Term(8, "x", 0, 1, eq), "1": new Term(12, "x", 1, 1, eq), "equality": "=", "2": new Term(2, "x", 2, 0, eq)};
eq2.terms = {"0": new Term(3, "x", 0, 1, eq2),"1": new Term(7,"x", 1, 1, eq2),"equality": "="};
eq3.terms = {"0": new Term(55, "x", 0, 1, eq3),"1": new Term(9,"x", 2, 1, eq3),"equality": "="};

var sy = new LinearSystem([eq, eq2, eq3]);

sy.setup();

createConstant(2);
createConstant(7);
