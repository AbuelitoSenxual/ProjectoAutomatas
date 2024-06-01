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

import java.util.Arrays;
import java.util.Set;

public class AnalizadorSintactico {
    private static Nodo NodoIni;
    private static Nodo Auxiliar;
    private static boolean StatusError;

    // El metodo ListaIdentificadores se entrega en , siempre lo entrega en el
    // suigiente elemento
    private static void ListaIdentificadores() {
        do {
            if (Auxiliar == null) {
                // Si Auxiliar es null, se rompe el bucle
                System.out.println("\n" + " Error se esperaba un identificador ");
                System.out.println("linea  " + Auxiliar.NumLinea);
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
                System.out.println("linea  " + Auxiliar.NumLinea);
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
        Nodo Auxiliar2;
        // verifica si es Definicion Al inicia con la plaba reservada var =201
        if ("201".equals(Auxiliar.Campo2)) {
            definicion();
            Auxiliar2 = Auxiliar.NodoSig;
            if ("201".equals(Auxiliar2.Campo2)||"206".equals(Auxiliar2.Campo2)) {
                Auxiliar = Auxiliar2;
                Bloque();
            }
            
        }
        else{
            // bloque de enunciados Al inicia con la plaba reservada begin =
            // 206
            BloqueEnunciados();
            Auxiliar2 = Auxiliar.NodoSig;
            if ("201".equals(Auxiliar2.Campo2)||"206".equals(Auxiliar2.Campo2)) {
                Auxiliar = Auxiliar2;
                Bloque();
            }
        }
        

    }

    private static void definicion() {
        // lee el siguiente lexema y verifica si es nesesario analizar una lista analiza
        // su lista
        Auxiliar = Auxiliar.NodoSig;
        if (Auxiliar == null || !"100".equals(Auxiliar.Campo2)) {
            System.out.println("\n" + " Error se esperaba una lista de identificadores");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("100".equals(Auxiliar.Campo2)) {
            Auxiliar = Auxiliar.NodoSig;
            // verifica que el siguiente elemento sea , y asi inicilisar una lista
            if ("110".equals(Auxiliar.Campo2)) {
                ListaIdentificadores();
            }
            // verifica que el siguente elemento sea : si no captura el erro
            if (Auxiliar == null || !"112".equals(Auxiliar.Campo2)) {
                System.out.println("\n" + " Error se esperaba :");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if ("112".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;
                // verifica el siguiente elemento sea tipo de datos validos
                if (Auxiliar == null || !EstaTioDato(Auxiliar)) {
                    System.out.println("\n" + " Error se esperaba tipo valido");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }

                if (EstaTioDato(Auxiliar)) {
                    // en el caso que el tipo de dato sea integer verifica que se cierre de manera
                    // correcta con ;
                    Auxiliar = Auxiliar.NodoSig;
                    if (!"111".equals(Auxiliar.Campo2)) {
                        System.out.println("\n" + " Error se esperaba ;");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                    if ("111".equals(Auxiliar.Campo2)) {
                        // en el caso que se cierre bien se verifica si es necesario volver a llamar al
                        // metodo
                        Nodo Auxiliar2 = Auxiliar;
                        Auxiliar2 = Auxiliar.NodoSig;
                        if ("201".equals(Auxiliar2.Campo2)) {
                            Auxiliar = Auxiliar2;
                            definicion();

                        }
                        
                    }

                }
            }

        }

    }

    // se recive el metodo bloque Enunciado con la palabra reservada begin = 206
    private static void BloqueEnunciados() {
        // Para que insializar una lista enunciados debe de empezar por un
        // identificador= 100 (para operacion o asignacion)
        // o palabra reservada if = 210 (para alternativa simple y doble) o while=213
        // (para bucle)
        Auxiliar = Auxiliar.NodoSig;
        // captura que no se inicialice bien
        if (!"100".equals(Auxiliar.Campo2) && !"210".equals(Auxiliar.Campo2) && !"213".equals(Auxiliar.Campo2)
                && Auxiliar == null) {
            System.out.println("\n" + " Error no se inicializo correctamente la lista de enunciados ");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        } else {
            // se insilizo bien la lista de enunciados
            ListaEnunciado();
            Auxiliar = Auxiliar.NodoSig;
            // captura el error generado al no cerrar con end
            if (!"207".equals(Auxiliar.Campo2) || Auxiliar == null) {
                System.out.println("\n" + " Error se esperaba un end");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if ("207".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;
            }
        }

    }

    // se recive el metodo ListaEnunciado con identificador= 100 (para operacion)
    // o palabra reservada if = 210 (para alternativa simple y doble) o
    // while=213para(Bucles)
    private static void ListaEnunciado() {
        // captura operaciones
        if ("100".equals(Auxiliar.Campo2)) {
            operaciones();
        }
        // Captura alternativas simples y doble
        if ("210".equals(Auxiliar.Campo2)) {
            AlternativaSimple();
        }
        // Captura bucles
        if ("213".equals(Auxiliar.Campo2)) {
            bucle();
        }

        // Captura el error donde no se cierra de manera correcta la lista de Enunciado
        // con ; = 111
        Auxiliar = Auxiliar.NodoSig;
        if (!"111".equals(Auxiliar.Campo2) || Auxiliar == null) {
            System.out.println("\n" + " Error no se  cierra de manera correcta la lista de Enunciado con ; ");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }

    }

    // verifica alternativas simples y manda a metodo alternativas doble si es
    // necesario (se entrega con if
    private static void AlternativaSimple() {
        Auxiliar = Auxiliar.NodoSig;
        // verifica error no se inicializa correctamente una condicion (tiene que ser
        // digito(101 NE,102 ND) o identificador(100))
        if (!"101".equals(Auxiliar.Campo2) && !"102".equals(Auxiliar.Campo2) && !"100".equals(Auxiliar.Campo2)
                && Auxiliar == null) {
            System.out.println("\n" + " Error no se  inicializo correctamente la condicion ");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("101".equals(Auxiliar.Campo2) || "102".equals(Auxiliar.Campo2) || "100".equals(Auxiliar.Campo2)) {
            condicion();
            Auxiliar = Auxiliar.NodoSig;
            // verifica que le siga la palabra reservada then 221
            if (!"221".equals(Auxiliar.Campo2) || Auxiliar == null) {
                System.out.println("\n" + " Error se esperaba palabra reservada then");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if ("221".equals(Auxiliar.Campo2) || Auxiliar == null) {
                // verifica que se inisialie bien el bloque de enunciado con la palalbra
                // resevada begin = 206
                Auxiliar = Auxiliar.NodoSig;
                if ("206".equals(Auxiliar.Campo2) || Auxiliar == null) {
                    System.out.println("\n" + " Error se esperaba palabra reservada begin");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
                BloqueEnunciados();
                // verifica si es necesario llamar a alterantiva doble al recibir la palabra
                // reservada else =212
                Nodo Auxiliar2 = Auxiliar;
                Auxiliar2 = Auxiliar.NodoSig;
                if ("212".equals(Auxiliar.Campo2)) {
                    Auxiliar = Auxiliar2;
                    AlternativaDoble();

                }

            }
        }
    }

    // se recive el metodo con else
    private static void AlternativaDoble() {
        Auxiliar = Auxiliar.NodoSig;
        // verificar que se inicio correctamente un bloque de enunciado con palabra
        // recervada begin 206
        if (!"206".equals(Auxiliar.Campo2) || Auxiliar == null) {
            System.out.println("\n" + " Error se esperaba palabra reservada begin");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("206".equals(Auxiliar.Campo2)) {
            BloqueEnunciados();
        }

    }

    private static void bucle() {
        Auxiliar = Auxiliar.NodoSig;
        // verifica error no se inicializa correctamente una condicion (tiene que ser
        // digito(101 NE,102 ND) o identificador(100))
        if (!"101".equals(Auxiliar.Campo2) && !"102".equals(Auxiliar.Campo2) && !"100".equals(Auxiliar.Campo2)
                && Auxiliar == null) {
            System.out.println("\n" + " Error no se  inicializo correctamente la condicion ");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("101".equals(Auxiliar.Campo2) || "102".equals(Auxiliar.Campo2) || "100".equals(Auxiliar.Campo2)) {
            condicion();
            // verifica se declare la palabra reservada do despues de la condicion do=214
            Auxiliar = Auxiliar.NodoSig;
            if (!"214".equals(Auxiliar.Campo2) || Auxiliar == null) {
                System.out.println("\n" + " Error no se  inicializo la palabra reservada do");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            } else {
                Auxiliar = Auxiliar.NodoSig;
                // Verificar si le sigue un bloque
                if (Auxiliar != null && (Auxiliar.Campo2.equals("201") || Auxiliar.Campo2.equals("206"))) {
                    Bloque();
                    Auxiliar = Auxiliar.NodoSig;
                    // verifica que se termine de manera correcta la el bucle con ;
                    if (!"111".equals(Auxiliar.Campo2) || Auxiliar == null) {
                        System.out.println("\n" + " Error se esperaba un ;");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }

                } else {
                    System.out.println("\n" + " Error se esoeraba la pablra reservada var o begin");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
            }
        }

    }

    // Se recive el metodo con un identificador 100
    private static void operaciones() {
        // Verificar que el primer token sea un identificador (campo2 == "100")
        if (!"100".equals(Auxiliar.Campo2)) {
            System.out.println("\n" + "Error: Se esperaba un identificador al inicio de la operación.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un operador relacional (campo2 == "112"
        // | "113" | "114" | "115" | "116" | "117")
        if (Auxiliar == null || !Arrays.asList("112", "113", "114", "115", "116", "117").contains(Auxiliar.Campo2)) {
            System.out.println("\n" + "Error: Se esperaba un operador relacional.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un identificador (campo2 == "100") o un
        // dígito (campo2 == "101" o "102")
        if (Auxiliar == null || (!"100".equals(Auxiliar.Campo2) && !"101".equals(Auxiliar.Campo2)
                && !"102".equals(Auxiliar.Campo2))) {
            System.out.println("\n" + "Error: Se esperaba un identificador o un dígito.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un operador aritmético (campo2 == "+"
        // 103| "-" 104| "*" 105| "/"106)
        if (Auxiliar == null || !Arrays.asList("103", "104", "106", "106").contains(Auxiliar.Campo2)) {
            System.out.println("\n" + "Error: Se esperaba un operador aritmético.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un identificador (campo2 == "100") o un
        // dígito (campo2 == "101" o "102")
        if (Auxiliar == null || (!"100".equals(Auxiliar.Campo2) && !"101".equals(Auxiliar.Campo2)
                && !"102".equals(Auxiliar.Campo2))) {
            System.out.println("\n" + "Error: Se esperaba un identificador o un dígito.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el último token sea un punto y coma (campo2 == "111")
        if (Auxiliar == null || !"111".equals(Auxiliar.Campo2)) {
            System.out.println("\n" + "Error: Se esperaba un punto y coma ';' al final de la operación.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }


    }

    private static boolean EstaTioDato(Nodo Auxiliar) {
        // Regresa true si Auxiliar.Campo2 contiene string 202, 203, o 205
        return "202".equals(Auxiliar.Campo2) || "203".equals(Auxiliar.Campo2) || "205".equals(Auxiliar.Campo2);
    }

    // se entrega con el operador el id o digito <Operador relacional>
    // ≡<|<=|>|>=|==|<>,
    private static void condicion() {
        Auxiliar = Auxiliar.NodoSig;
        // Captura error al no resivir un operador relacional
        if (Auxiliar == null ||
                (!"112".equals(Auxiliar.Campo2) &&
                        !"113".equals(Auxiliar.Campo2) &&
                        !"114".equals(Auxiliar.Campo2) &&
                        !"115".equals(Auxiliar.Campo2) &&
                        !"116".equals(Auxiliar.Campo2) &&
                        !"117".equals(Auxiliar.Campo2))) {

            System.out.println("\n" + " Error se esperaba un Operador relacional");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        } else {
            Auxiliar = Auxiliar.NodoSig;
            // verificar si le sigue un identificador o digito
            if (!"101".equals(Auxiliar.Campo2) && !"102".equals(Auxiliar.Campo2) && !"100".equals(Auxiliar.Campo2)
                    && Auxiliar == null) {
                System.out.println("\n" + " Error no se  esperaba un identificador o digito ");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            } else {
                Auxiliar = Auxiliar.NodoSig;
                // verifica que se termine de manera correcta la coindicion con ;
                if (!"111".equals(Auxiliar.Campo2) || Auxiliar == null) {
                    System.out.println("\n" + " Error se esperaba un ;");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        NodoIni = AnalizadorLexico.ObtenerNodoIniLexemas();
        AnalizadorLexico.RecorrerTokens();
        Auxiliar = NodoIni;
        // verificar si se empieza con program
        if (Auxiliar.Campo2 != "200") {
            System.out.println("\n" + " Error se esperar la palabra reservada Program");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }

        if (Auxiliar.Campo2 == "200") {
            Auxiliar = Auxiliar.NodoSig;
            // verifica que se iniciialize un identificador
            if (Auxiliar.Campo2 != "100") {
                System.out.println("\n" + " Error se esperar un identificador o lista de identificadores");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if (Auxiliar.Campo2 == "100") {
                Auxiliar = Auxiliar.NodoSig;
                // Verifica que empiece una lista de identificadores con ,=110 o se termine la
                // oración con ;=111
                if (Auxiliar == null || (!Auxiliar.Campo2.equals("110") && !Auxiliar.Campo2.equals("111"))) {
                    System.out.println("\n" + "Error: Se esperaba lista de identificadores o final de oración con ;");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
                // verifica si es una lista de idetificadores al recibir ,
                if (Auxiliar.Campo2 == "110" && StatusError == false) {
                    ListaIdentificadores();

                }
                // Captura error de terminar de manera incorrecta a orracion
                if (Auxiliar == null || !"111".equals(Auxiliar.Campo2)) {
                    System.out.println("\n" + " Error se esperarba lista de identificadores o final de oracion con ;");
                    System.out.println("linea  " + Auxiliar.NumLinea);
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
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                    // Verificar si le sigue un bloque
                    if (Auxiliar != null && (Auxiliar.Campo2.equals("201") || Auxiliar.Campo2.equals("206"))) {
                        Bloque();
                    }
                    // verificar si se termina de manera correcta el codigo
                    if (!Auxiliar.Campo2.equals("109") && StatusError == false) {
                        System.out.println("\n" + "Error: No se terminó de manera correcta el código o hay un error en la contuinidad de la gramatica");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                    if (Auxiliar.Campo2.equals("109") && StatusError == false) {
                        System.out.println("\n" + "Ejecucion exitosa");
                    }

                }

            }
        }

    }
}
