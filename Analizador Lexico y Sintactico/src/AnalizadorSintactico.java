/*tokens de  palabras reservadas 
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
    <bloque> ≡ <definiciones> |  <bloque de enunciados>,
    <definiciones> ≡ var <lista identificador> : <tipo> ; | <definiciones> var <lista identificador> : <tipo> ; ,<tipo> ≡ integer,
    <bloque de enunciados> ≡ begin <lista enunciados> end ,
    <lista enunciados> ≡ <Operaciones> | <Asignaciones> | <lista-enunciados> | <Alternativas> | <Bucles> ;}
    <Operaciones> ≡ <Identificador><Operador relacional><Identificador | Digito><Operador aritmetico><Identificador | Digito>;
    <Asignaciones> ≡ <Identificador><Operador relacional><Identificador | Digito>;
    <Alternativas> ≡ <Alternativa Simple> | <Alternativa Doble>
    <Alternativa Simple> ≡ <if><Condicion><then><bloque de enunciados> 
    <Alternativa Doble> ≡ <if><Condicion><then><bloque de enunciados><else><bloque de enunciados>
    <Condicion> ≡ <Identificador | Digito><Operador relacional><Identificador | Digito>;
    <Bucles> ≡ <While><Condicion > <Do><bloque>; */

public class AnalizadorSintactico {
    private static Nodo NodoIni;
    private static Nodo Auxiliar;
    private static boolean StatusError;

    // El metodo ListaIdentificadores se entrega en , siempre
    private static void ListaIdentificadores() {
        do {
            if (Auxiliar == null) {
                // Si Auxiliar es null, se rompe el bucle
                System.out.println("\n" + " Error se esperaba un identificador ");
                StatusError = true;
                break;
            }

            Auxiliar = Auxiliar.NodoSig;

            if (Auxiliar == null) {
                // Si Auxiliar es null después de moverse al siguiente nodo
                System.out.println("\n" + " Error se esperaba un identificador despues de , ");
                StatusError = true;
                break;
            }

            // Captura el error donde después de la coma no hay identificador
            if (!"100".equals(Auxiliar.Campo2)) {
                System.out.println("\n" + " Error se esperaba un identificador despues de ,");
                StatusError = true;
                break;
            }

            if ("100".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;

                if (Auxiliar == null || !"110".equals(Auxiliar.Campo2)) {
                    // Si Auxiliar es null después de moverse al siguiente nodo o no es "110"
                    break;
                }
            }
        } while (!StatusError);
    }

    
    private static void Bloque() {
        //verifica si es Definicion Al inicia con la plaba reservada var =201
        if ("201".equals(Auxiliar.Campo2)) {
           definicion(); 
        }
        //verifica si es bloque de enunciados Al inicia con la plaba reservada begin = 206
        if ("206".equals(Auxiliar.Campo2)) {
            BloqueEnunciados(); 
         }
    }
   
    private static void definicion(){}
    
    private static void BloqueEnunciados(){}

    public static void main(String[] args) {
        NodoIni = AnalizadorLexico.ObtenerNodoIniLexemas();
        AnalizadorLexico.RecorrerTokens();
        Auxiliar = NodoIni;
        // verificar si se empieza con program
        if (Auxiliar.Campo2 != "200") {
            System.out.println("\n" + " Error se esperar la palabra reservada Program");
            StatusError = true;
        }

        if (Auxiliar.Campo2 == "200") {
            Auxiliar = Auxiliar.NodoSig;
            // verifica que se iniciialize un identificador
            if (Auxiliar.Campo2 != "100") {
                System.out.println("\n" + " Error se esperar un identificador o lista de identificadores");
                StatusError = true;
            }

            if (Auxiliar.Campo2 == "100") {
                Auxiliar = Auxiliar.NodoSig;
                // Verifica que empiece una lista de identificadores con ,=110 o se termine la
                // oración con ;=111
                if (Auxiliar == null || (!Auxiliar.Campo2.equals("110") && !Auxiliar.Campo2.equals("111"))) {
                    System.out.println("\n" + "Error: Se esperaba lista de identificadores o final de oración con ;");
                    StatusError = true;
                }
                // verifica si es una lista de idetificadores al recibir ,
                if (Auxiliar.Campo2 == "110" && StatusError == false) {
                    ListaIdentificadores();

                }

                // Captura error de terminar de manera incorrecta a orracion
                if (Auxiliar == null || !"111".equals(Auxiliar.Campo2)) {
                    System.out.println("\n" + " Error se esperarba lista de identificadores o final de oracion con ;");
                    StatusError = true;
                }
                // verifica si termina la oracio con ;
                if ("111".equals(Auxiliar.Campo2)) {
                    Auxiliar = Auxiliar.NodoSig;
                    // capturar error al no terminar d emanera exitosa el programa
                    // que no sea bloque(para que sea bloque inicia con la palabra reservada
                    // begin=206 o var=201)
                    // que no sea .=109 que sea auxiliar nulo
                    if (Auxiliar == null || !Auxiliar.Campo2.equals("201") && !Auxiliar.Campo2.equals("206")
                            && !Auxiliar.Campo2.equals("109")) {
                        System.out.println("\n" + "Error: No se terminó de manera correcta el código");
                        StatusError = true;
                    }
                    // Verificar si le sigue un bloque
                    if (Auxiliar != null && (Auxiliar.Campo2.equals("201") || Auxiliar.Campo2.equals("206"))) {
                        Bloque();
                    }
                    // verificar si se termina de manera correcta el codigo
                    if (!Auxiliar.Campo2.equals("109")) {
                        System.out.println("\n" + "Error: No se terminó de manera correcta el código");
                        StatusError = true;
                    }
                }

            }
        }

    }
}
