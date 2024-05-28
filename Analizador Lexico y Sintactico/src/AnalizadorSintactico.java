//     R={<programa> ≡ program <identificador> (<lista identificador>) ; <bloque> ., (note que el punto
//     se usa para indicar fin del programa)

//     <identificador> ≡ <letra> | <letra><resto>,
//     <resto> ≡ <letra> | <digito> | <letra> <resto> | <digito> <resto>,
//     <letra> ≡ a | b | ... | z | A | B | ... | Z,
//     <digito> ≡ 0 | 1 | 2 | ... | 9,
//     <coma> ≡ ”,”, (la coma ”,” se usa como s´ımbolo terminal)
//     <Operador relacional> ≡   <|<=|>|>=|==|<>,
//     <Operador aritmetico> ≡  +|-|*|/
//     <lista identificador> ≡ <identificador> | <lista identificador> <coma> <identificador>,
//     <bloque> ≡ <definiciones> | <funciones> | <procedimientos> | <bloque de enunciados>,
//     <definiciones> ≡ var <lista identificador> : <tipo> ; | <definiciones> var <lista identificador> : <tipo> ; ,<tipo> ≡ integer,
//     <bloque de enunciados> ≡ begin <lista enunciados> end ,
//     <lista enunciados> ≡ <Operaciones> | <Asignaciones> | <lista-enunciados> | <Alternativas> | <Bucles> ;}
//     <Operaciones> ≡ <Identificador><Operador relacional><Identificador | Digito><Operador aritmetico><Identificador | Digito>;
//     <Asignaciones> ≡ <Identificador><Operador relacional><Identificador | Digito>;
//     <Alternativas> ≡ <Alternativa Simple> | <Alternativa Doble>
//     <Alternativa Simple> ≡ <if><Condicion><then><bloque de enunciados> 
//     <Alternativa Doble> ≡ <if><Condicion><then><bloque de enunciados><else><bloque de enunciados>
//     <Condicion> ≡ <Identificador | Digito><Operador relacional><Identificador | Digito>;
//     <Bucles> ≡ <While><Condicion > <Do><bloque>;
public class AnalizadorSintactico {
    public static Nodo NodoIni;
    public static void main(String[] args) {
        NodoIni=AnalizadorLexico.ObtenerNodoIniLexemas();
        AnalizadorLexico.RecorrerTokens();
    }
}
