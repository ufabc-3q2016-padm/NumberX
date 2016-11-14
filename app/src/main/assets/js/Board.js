
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

//Term constructor
function Term(value, variable, index, degree, context) {
    this.coeficient = new Symbol(value, context);
    this.variable = new Symbol(variable, context);
    this.index = new Symbol(index, context);
    this.degree = new Symbol(degree, context);
}

Term.prototype.toString = function() {
    var label;
    if (this.degree.value > 0) {
        label = this.coeficient.value + this.variable.value + "_" + this.index.value; //+ "^" + this.degree.value;
    } else {
        label = this.coeficient.value;
    }
    return label;
};

//Equation constructor
function Equation() {
    var equation = this;
    this.terms = {};
    this.current = Object.keys(this.terms).map(function(key) {return this.terms[key];});
    this.sortable;
    this.symbols = [];
    this.spans = [];
    this.addTerm = function(term) { this.terms.push(term); };
    //Recebe um ul, e constroi uma equação drag and drop com base nos termos passados na construção
    this.setup = function(equationList) {
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
            }
            element.style.display = "inline";
            equationList.appendChild(element);
        });
        //Transforma-os em uma lista reordenável
        this.sortable = new Sortable.create(equationList, {
            animation: 100,
            oposite: null,
            //Se passar pela igualdade, deve inverter o sinal
            onSort: function(evt) {
                if (this.oposite != null) {
                    this.oposite.coeficient.value = -1 * this.oposite.coeficient.value;
                    this.oposite = null;
                }
                equation.update(equationList);
            },
            onMove: function(evt) {
                if (evt.related.id == "equality") {
                    this.oposite = equation.getTermById(evt.dragged.id);
                } if (evt.dragged.id == "equality") {
                    this.oposite = equation.getTermById(evt.related.id);
                }
            },
        });
        console.log("Equation list elements: " + equationList.childElementCount);
        equation.update(equationList);
    };
    this.update = function(equationList) {
        var currentString = equation.sortable.toArray();
        equation.current = currentString.map(equation.getTermById);
        console.log("Current equation TeX: " + equation.current);
        Symbol.spans = [];
        var element;
        //Gerar código TeX para cada termo, e renderizá-lo usando KaTex
        for (var t in equation.current) {
            element = equationList.children.namedItem(currentString[t]);
            //console.log(element);
            var TeX = equation.current[t].toString();
            if (equation.current[t] instanceof Term) {
                if (t > 0 && equation.current[t-1] instanceof Term && equation.current[t].coeficient.value > 0)
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
    this.equations = equations;
    this.sortable;
    this.add = function (equation) { this.equations.push(equation); };
    //Faz multiplas configuraçẽs para mostrar as equações da forma correta
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
            
            element.appendChild(equationList);
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

//Dados de teste:

var eq = new Equation();
var eq2 = new Equation();
var eq3 = new Equation();

var terms1 = {"0": new Term(8, "x", 0, 1, eq), "1": new Term(12, "x", 1, 1, eq), "equality": "=","2": new Term(2, "x", 2, 1, eq)};
var terms2 = {"0": new Term(3, "x", 0, 1, eq2),"1": new Term(7,"x", 1, 1, eq2),"equality": "="};
var terms3 = {"0": new Term(55, "x", 0, 1, eq3),"1": new Term(9,"x", 2, 1, eq3),"equality": "="};

eq.terms = terms1;
eq2.terms = terms2;
eq3.terms = terms3;

var sy = new LinearSystem([eq, eq2, eq3]);

sy.setup();