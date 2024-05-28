/* R={<programa> ≡ program <identificador> (<lista identificador>) ; <bloque> ., (note que el punto
    se usa para indicar fin del programa)
    <identificador> ≡ <letra> | <letra><resto>,
    <resto> ≡ <letra> | <digito> | <letra> <resto> | <digito> <resto>,
    <letra> ≡ a | b | ... | z | A | B | ... | Z,
    <digito> ≡ 0 | 1 | 2 | ... | 9,
    <coma> ≡ ”,”, (la coma ”,” se usa como s´ımbolo terminal)
    <Operador relacional> ≡   <|<=|>|>=|==|<>,
    <Operador aritmetico> ≡  +|-|*|/
    <lista identificador> ≡ <identificador> | <lista identificador> <coma> <identificador>,
    <bloque> ≡ <definiciones> | <funciones> | <procedimientos> | <bloque de enunciados>,
    <definiciones> ≡ var <lista identificador> : <tipo> ; | <definiciones> var <lista identificador> : <tipo> ; ,<tipo> ≡ integer,
    <bloque de enunciados> ≡ begin <lista enunciados> end ,
    <lista enunciados> ≡ <Operaciones> | <Asignaciones> | <lista-enunciados> | <Alternativas> | <Bucles> ;}
    <Operaciones> ≡ <Identificador><Operador relacional><Identificador | Digito><Operador aritmetico><Identificador | Digito>;
    <Asignaciones> ≡ <Identificador><Operador relacional><Identificador | Digito>;
    <Alternativas> ≡ <Alternativa Simple> | <Alternativa Doble>
    <Alternativa Simple> ≡ <if><Condicion><then><bloque de enunciados> 
    <Alternativa Doble> ≡ <if><Condicion><then><bloque de enunciados><else><bloque de enunciados>
    <Condicion> ≡ <Identificador | Digito><Operador relacional><Identificador | Digito>;
    <Bucles> ≡ <While><Condicion > <Do><bloque>; 
   

  tokens de  palabras reservadas 
  program	200
var	201
string	202
integer	203
real	204
boolean	205
begin	206
end	207
read	208
write	209
if	210
then	211
else	212
while	213
do	214
or	215
and	216
not	217
   */
public class AnalizadorSintactico {
    public static Nodo NodoIni;
    static Nodo Axiliar;
    public static void main(String[] args) {
        NodoIni=AnalizadorLexico.ObtenerNodoIniLexemas();
        AnalizadorLexico.RecorrerTokens();
        Axiliar = NodoIni;

        if (Axiliar.Campo2!="200") {
            System.out.println("/n"+" Error se esperar la palabra reservada Program");
        }
        if (Axiliar.Campo2=="200") {
            Axiliar = Axiliar.NodoSig;
            if (Axiliar.Campo2 !="100") {
                System.out.println("/n"+" Error se esperar un identificador o lista de identificadores");
            }
            if (Axiliar.Campo2 =="100") {
                Axiliar = Axiliar.NodoSig;
                if (Axiliar.Campo2!="110" && Axiliar.Campo2!=";") {
                    System.out.println("/n"+" Error se esperarba un identificador o lista de identificadores");
                }
                
                
            }
        }
        



    }
}
